package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import Hardware.HardwareSystems.HardwareSystem;

public class Intake extends HardwareSystem {
    private final DcMotor intakeLower;
    private final DcMotor intakeTop;
    private final Servo stickLeft;
    private final Servo intakeShield;
    private double intakeSpeed;
    private double leftStick;
    private double shieldPosition;

    public Intake(DcMotor intakeLower, DcMotor intakeTop, Servo stickLeft, Servo intakeShield){
        this.intakeLower = intakeLower;
        this.intakeTop = intakeTop;
        this.stickLeft = stickLeft;
        this.intakeShield = intakeShield;
    }

    @Override
    public void calibrate() {

    }

    @Override
    public void update() {
        intakeTop.setPower(intakeSpeed);
        intakeLower.setPower(intakeSpeed);
        stickLeft.setPosition(leftStick);
        intakeShield.setPosition(shieldPosition);
    }

    public void setIntakeSpeed(double intakeSpeed) {
        this.intakeSpeed = intakeSpeed;
    }

    public void stickLeftDown(){
        this.leftStick = 0.1;
    }

    public void stickLeftUp(){
        this.leftStick = 0.9;
    }

    public void shieldDown(){
        this.shieldPosition = 0.1;
    }

    public void shieldUp(){
        this.shieldPosition = 0.9;
    }

    public void shieldIdle(){
        this.shieldPosition = 0.5;
    }
}
