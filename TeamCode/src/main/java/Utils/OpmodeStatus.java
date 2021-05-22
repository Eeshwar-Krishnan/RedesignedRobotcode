package Utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class OpmodeStatus {
    private static final OpmodeStatus instance = new OpmodeStatus();
    private LinearOpMode opmode;

    private OpmodeStatus(){
        opmode = null;
    }

    public static void attach(LinearOpMode opmode){
        instance.opmode = opmode;
    }

    public static boolean opmodeActive(){
        if(instance.opmode != null){
            return instance.opmode.isStopRequested() || !instance.opmode.opModeIsActive();
        }
        return false;
    }

    public static OpMode getOpmode(){
        return instance.opmode;
    }
}
