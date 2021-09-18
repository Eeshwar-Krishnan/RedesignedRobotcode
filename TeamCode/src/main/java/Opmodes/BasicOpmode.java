package Opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Hardware.UGHardwareController;
import State.Action.ActionController;
import Utils.OpmodeStatus;
import Utils.ProgramClock;

public abstract class BasicOpmode extends LinearOpMode {
    public UGHardwareController hardware;

    private Thread initThread, startThread;

    public abstract void setup();

    void init_async(){

    }

    void start_async(){

    }


    @Override
    public void runOpMode() throws InterruptedException {
        initThread = new Thread(this::init_async);
        startThread = new Thread(this::start_async);

        ActionController.initialize();

        OpmodeStatus.attach(this);

        hardware = new UGHardwareController(hardwareMap);
        hardware.initialize();

        setup();

        boolean started = false;

        initThread.start();
        while(!isStopRequested()){
            if(isStarted() && !started){
                started = true;
                OpmodeStatus.triggerStartActions();
                startThread.start();
            }
            hardware.update();
            ActionController.update();
            telemetry.update();
            ProgramClock.update();
        }
        initThread.interrupt();
        startThread.interrupt();
    }
}
