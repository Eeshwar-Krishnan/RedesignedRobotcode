package org.firstinspires.ftc.robotcontroller.internal.VisDebug;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class VDConnectionServer extends WebSocketServer {

    public VDConnectionServer(int port){
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        VDConnectionController.getInstance().handleNewConnection(webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        VDConnectionController.getInstance().handleConnectionExit(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        VDConnectionController.getInstance().handle(webSocket, s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
