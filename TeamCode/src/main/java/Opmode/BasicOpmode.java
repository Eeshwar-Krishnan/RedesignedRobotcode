package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Hardware.UltimateGoalHardware;
import Utils.OpmodeStatus;
import Utils.ProgramClock;
import State.EventSystem;
import State.StateMachine;

public abstract class BasicOpmode extends LinearOpMode {
    public StateMachine stateMachine;
    private final boolean runHardware;
    private boolean started = false;
    public UltimateGoalHardware hardware;
    public EventSystem eventSystem;

    public BasicOpmode(boolean runHardware){
        OpmodeStatus.attach(this);
        this.runHardware = runHardware;
        eventSystem = new EventSystem();
    }

    public BasicOpmode(){
        this(true);
    }

    public abstract void setup();

    public abstract void update();

    public void onStop(){}

    @Override
    public void runOpMode() {
        hardware = new UltimateGoalHardware(hardwareMap);
        if(runHardware)
            hardware.init();
        if(!runHardware)
            hardware.deactivate();
        setup();
        ProgramClock.update();
        while(opModeIsActive()){
            if(isStarted() && !started){
                eventSystem.triggerOnStart(stateMachine);
                started = true;
            }
            update();
            stateMachine.update();
            ProgramClock.update();
            telemetry.update();
            if(runHardware)
                hardware.heartbeat();
        }
        onStop();
        hardware.deactivate();
    }
}
