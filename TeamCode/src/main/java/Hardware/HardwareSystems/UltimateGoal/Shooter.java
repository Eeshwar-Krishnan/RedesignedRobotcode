package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

import Hardware.HardwareSystems.HardwareSystem;
import Hardware.UltimateGoalHardware;
import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.PIDSystem;
import State.Control.ControlGroup;
import State.Control.ControlState;
import State.Control.TimerControlState;
import State.StateMachine;

public class Shooter extends HardwareSystem {
    private static final double MIN_ANGLE = -22, MAX_ANGLE = 27;

    private double target = 0, offset = 0;
    private double index, pitch, turret;
    private final PIDSystem system = new PIDSystem(1.5, 0, 0);
    private final DcMotorEx shooter1;
    private final DcMotorEx shooter2;
    private final Servo indexer, shooterPitch, shooterTurret;

    public Shooter(DcMotor shooter1, DcMotor shooter2, Servo indexer, Servo shooterPitch, Servo shooterTurret){
        this.shooter1 = (DcMotorEx) shooter1;
        this.shooter2 = (DcMotorEx) shooter2;
        this.indexer = indexer;
        this.shooterPitch = shooterPitch;
        this.shooterTurret = shooterTurret;
    }

    @Override
    public void calibrate() {

    }

    @Override
    public void update() {
        double vel = shooter2.getVelocity();
        double corr = offset + system.getCorrection(target - vel);
        shooter2.setPower(corr);
        shooter1.setPower(corr);
        indexer.setPosition(index);
        shooterPitch.setPosition(pitch);
        shooterTurret.setPosition(turret);
    }

    public void setTarget(double target) {
        this.target = target;
        this.offset = target/6;
    }

    public void indexIn(){
        index = 0.7;
    }

    public void indexOut(){
        index = 0.925;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void aimAt(Angle angle){
        double degrees = MathUtils.clamp(angle.degrees(), MIN_ANGLE, MAX_ANGLE);
        turret = ((degrees + MIN_ANGLE) / (MIN_ANGLE + MAX_ANGLE));
    }

    public static ControlGroup getShooterControlGroup(StateMachine stateMachine, final UltimateGoalHardware hardware, ControlGroup.STOP_CONDITION stop_condition){
        ArrayList<ControlState> shootStates = new ArrayList<>();

        shootStates.add(new ControlState(stateMachine) {
            int timer = 2;
            @Override
            public void onInit() {
                timer = 2;
            }

            @Override
            public boolean shouldTerminate() {
                return timer <= 0;
            }

            @Override
            public void update() {
                hardware.getShooter().indexIn();
                timer --;
            }
        });

        shootStates.add(new TimerControlState(stateMachine, 80));

        shootStates.add(new ControlState(stateMachine) {
            int timer = 2;
            @Override
            public void onInit() {
                timer = 2;
            }

            @Override
            public boolean shouldTerminate() {
                return timer <= 0;
            }

            @Override
            public void update() {
                hardware.getShooter().indexOut();
                timer --;
            }
        });

        shootStates.add(new TimerControlState(stateMachine, 100));

        return new ControlGroup(stateMachine, stop_condition, shootStates);
    }
}
