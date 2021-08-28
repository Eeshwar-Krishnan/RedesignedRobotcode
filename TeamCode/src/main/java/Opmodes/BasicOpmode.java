package Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Hardware.UGHardwareController;
import State.Action.ActionController;
import Utils.OpmodeStatus;
import Utils.ProgramClock;

public abstract class BasicOpmode extends LinearOpMode {
    public UGHardwareController hardware;

    public abstract void setup();

    @Override
    public void runOpMode() throws InterruptedException {
        OpmodeStatus.attach(this);

        hardware = new UGHardwareController(hardwareMap);
        hardware.initialize();

        setup();

        boolean started = false;

        while(opModeIsActive()){
            if(isStarted() && !started){
                started = true;
                OpmodeStatus.triggerStartActions();
            }
            hardware.update();
            ActionController.update();
            telemetry.update();
            ProgramClock.update();
        }
    }
}
