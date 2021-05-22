package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

import Hardware.HardwareSystems.HardwareSystem;
import MathSystems.Angle;
import MathSystems.MathUtils;
import Utils.PIDSystem;
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

    private VisionSystem visionSystem;

    public Shooter(VisionSystem visionSystem, DcMotor shooter1, DcMotor shooter2, Servo indexer, Servo shooterPitch, Servo shooterTurret){
        this.shooter1 = (DcMotorEx) shooter1;
        this.shooter2 = (DcMotorEx) shooter2;
        this.indexer = indexer;
        this.shooterPitch = shooterPitch;
        this.shooterTurret = shooterTurret;
        this.visionSystem = visionSystem;
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

    private ControlGroup targetPowershot(StateMachine stateMachine, final int powershot){
        ControlState state1 = new ControlState(stateMachine) {
            @Override
            public boolean shouldTerminate() {
                return true;
            }

            @Override
            public void update() {
                aimAt(Angle.degrees(visionSystem.getPowershots()[powershot]));
            }
        };
        ControlState state2 = new TimerControlState(stateMachine, 150);
        return new ControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL, state1, state2);
    }

    public ControlGroup targetLeftPowershot(StateMachine stateMachine){
        return targetPowershot(stateMachine, 0);
    }

    public ControlGroup targetCenterPowershot(StateMachine stateMachine){
        return targetPowershot(stateMachine, 1);
    }

    public ControlGroup targetRightPowershot(StateMachine stateMachine){
        return targetPowershot(stateMachine, 2);
    }

    public ControlGroup getShooterControlGroup(StateMachine stateMachine, ControlGroup.STOP_CONDITION stop_condition){
        ArrayList<ControlState> shootStates = new ArrayList<>();

        shootStates.add(new ControlState(stateMachine) {

            @Override
            public boolean shouldTerminate() {
                return true;
            }

            @Override
            public void update() {
                indexIn();
            }
        });

        shootStates.add(new TimerControlState(stateMachine, 80));

        shootStates.add(new ControlState(stateMachine) {

            @Override
            public boolean shouldTerminate() {
                return true;
            }

            @Override
            public void update() {
                indexOut();
            }
        });

        shootStates.add(new TimerControlState(stateMachine, 100));

        return new ControlGroup(stateMachine, stop_condition, shootStates);
    }
}
