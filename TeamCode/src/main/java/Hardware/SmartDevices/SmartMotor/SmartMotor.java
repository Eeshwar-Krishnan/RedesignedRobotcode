package Hardware.SmartDevices.SmartMotor;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

public class SmartMotor {
    private final DcMotorEx motor;
    private double lastPower = 0;
    private boolean voltageCorrection;
    private double voltageCorrectionFactor = 1;
    private LynxModule module;

    public SmartMotor(DcMotor motor){
        this.motor = (DcMotorEx) motor;
        voltageCorrection = false;
    }

    public void enableVoltageCorrection(LynxModule motorModule){
        this.module = motorModule;
        this.voltageCorrection = true;
    }

    public void setVoltageCorrectionFactor(double voltageCorrectionFactor) {
        this.voltageCorrectionFactor = voltageCorrectionFactor;
    }

    public void setPower(double power){
        if(Math.abs(power - lastPower) > 0.001){
            if(voltageCorrection){
                double factor = (12 / module.getInputVoltage(VoltageUnit.VOLTS)) * voltageCorrectionFactor;
                motor.setPower(power * factor);
            }else {
                motor.setPower(power);
            }
            lastPower = power;
        }
    }

    public double getPower() {
        return lastPower;
    }

    public DcMotorEx getMotor() {
        return motor;
    }
}
