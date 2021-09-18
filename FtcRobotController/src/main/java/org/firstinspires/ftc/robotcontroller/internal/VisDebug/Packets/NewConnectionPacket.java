package org.firstinspires.ftc.robotcontroller.internal.VisDebug.Packets;

public class NewConnectionPacket {
    private int opcode = 3;

    public ConsoleLine[] consoleLines;

    public class ConsoleLine{
        public String item;
        public String type;
    }
}
