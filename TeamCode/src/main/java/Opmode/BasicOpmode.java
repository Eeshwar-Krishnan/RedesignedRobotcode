package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import Hardware.UltimateGoalHardware;
import MathSystems.ProgramClock;
import State.EventSystem;
import State.StateMachine;

public abstract class BasicOpmode extends LinearOpMode {
    public StateMachine stateMachine;
    private final boolean runHardware;
    private boolean started = false;
    public UltimateGoalHardware hardware;
    public EventSystem eventSystem;

    public BasicOpmode(boolean runHardware){
        this.runHardware = runHardware;
        eventSystem = new EventSystem();
    }

    public BasicOpmode(){
        this(true);
    }

    public abstract void setup();

    public abstract void update();

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new UltimateGoalHardware(hardwareMap);
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
        }
        hardware.deactivate();
    }
}
