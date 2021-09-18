package org.firstinspires.ftc.robotcontroller.internal.VisDebug;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets.NewConnectionPacket.ConsoleLine;
import org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets.NewConnectionPacket;
import org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets.OpmodeChangePacket;
import org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets.TelemetryPacket;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class VDConnectionController {
    private static final VDConnectionController instance = new VDConnectionController();

    public static VDConnectionController getInstance() {
        return instance;
    }

    private VDConnectionServer server;

    private final Gson gson = SimpleGson.getInstance();

    private final ArrayList<WebSocket> sockets;
    private final HashMap<WebSocket, ArrayList<ConsoleLine>> newConsoleLines;
    private final ArrayList<ConsoleLine> consoleLines;

    private final Object lock;

    public VDConnectionController(){
        newConsoleLines = new HashMap<>();
        sockets = new ArrayList<>();
        consoleLines = new ArrayList<>();
        lock = new Object();
    }

    public static void start(){
        instance.server = new VDConnectionServer(4750);
    }

    public static void stop(){

    }

    public void addConsoleLine(ConsoleLine line){
        synchronized (lock){
            consoleLines.add(line);

            for(WebSocket w : sockets){
                Objects.requireNonNull(newConsoleLines.get(w)).add(line);
            }
        }
    }

    public void handle(WebSocket socket, String message){
        long timestamp = System.currentTimeMillis();

        Object o = gson.fromJson(message, Object.class);

        LinkedTreeMap<String, Object> map = (LinkedTreeMap) o;
        if(map.containsKey("opcode")){
            int opcode = ((Double) Objects.requireNonNull(map.get("opcode"))).intValue();
            switch(opcode){
                case 0:
                    handleHeartbeat(socket, timestamp);
                    break;
                case 2:
                    handleOpmodeChange(message);
                    break;
            }
        }
    }

    public void handleHeartbeat(WebSocket socket, long timestamp){
        TelemetryPacket packet = new TelemetryPacket();

        synchronized (lock){
            packet.newConsoleMessages = Objects.requireNonNull(newConsoleLines.get(socket)).toArray(new ConsoleLine[1]);

        }

        socket.send(gson.toJson(packet));
    }

    public void handleOpmodeChange(String packet){
        OpmodeChangePacket changePacket = gson.fromJson(packet, OpmodeChangePacket.class);


    }

    public void handleNewConnection(WebSocket socket){
        NewConnectionPacket packet = new NewConnectionPacket();

        synchronized (lock) {
            sockets.add(socket);
            newConsoleLines.put(socket, new ArrayList<>());
            packet.consoleLines = consoleLines.toArray(new ConsoleLine[0]);
        }

        socket.send(gson.toJson(packet));
    }

    public void handleConnectionExit(WebSocket socket){
        synchronized (lock) {
            sockets.remove(socket);
            newConsoleLines.remove(socket);
        }
    }
}
