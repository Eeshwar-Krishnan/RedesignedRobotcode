package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import Hardware.HardwareSystems.HardwareSystem;

public class Intake extends HardwareSystem {
    private final DcMotor intakeLower;
    private final DcMotor intakeTop;
    private final Servo stickLeft;
    private final Servo stickRight;
    private double intakeSpeed;
    private double leftStick;
    private double rightStick;

    public Intake(DcMotor intakeLower, DcMotor intakeTop, Servo stickLeft, Servo stickRight){
        this.intakeLower = intakeLower;
        this.intakeTop = intakeTop;
        this.stickLeft = stickLeft;
        this.stickRight = stickRight;
    }

    @Override
    public void calibrate() {

    }

    @Override
    public void update() {
        intakeTop.setPower(intakeSpeed);
        intakeLower.setPower(intakeSpeed);
        stickLeft.setPosition(leftStick);
        stickRight.setPosition(rightStick);
    }

    public void setIntakeSpeed(double intakeSpeed) {
        this.intakeSpeed = intakeSpeed;
    }

    public void stickLeftDown(){
        this.leftStick = 0.1;
    }

    public void stickRightDown(){
        this.rightStick = 0.1;
    }

    public void stickLeftUp(){
        this.leftStick = 0.9;
    }

    public void stickRightUp(){
        this.rightStick = 0.9;
    }
}
