package org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets;

import java.util.Map;

public class TelemetryPacket {
    private int opcode = 1;

    public long overheadMs;

    public String[] telemetryMessages;
    public NewConnectionPacket.ConsoleLine[] newConsoleMessages;

    public Map<String, Double> motors, servos;

    public String[] opmodes;
    public String currentOpmode;
    public String opmodeStatus;
}
