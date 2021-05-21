package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import Hardware.HardwareSystems.HardwareSystem;
import MathSystems.Vector3;
import MathSystems.Vector4;

public class Drivetrain extends HardwareSystem {
    private DcMotorEx bl, br, tl, tr;
    private Vector4 power = new Vector4(0, 0, 0, 0);

    public Drivetrain(DcMotor bl, DcMotor br, DcMotor tl, DcMotor tr, VoltageSensor battery){
        this.bl = (DcMotorEx) bl;
        this.br = (DcMotorEx) br;
        this.tl = (DcMotorEx) tl;
        this.tr = (DcMotorEx) tr;
    }

    @Override
    public void calibrate() {

    }

    @Override
    public void update() {
        bl.setPower(power.getA());
        br.setPower(power.getB());
        tl.setPower(power.getC());
        tr.setPower(power.getD());
    }

    public void setBrake(){
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        tl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        tr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setFloat(){
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        tl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        tr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setPower(Vector4 power) {
        this.power = power;
    }

    public void setDirection(Vector3 direction){
        this.power = new Vector4(-direction.getB() + direction.getA() - direction.getC(), direction.getB() + direction.getA() - direction.getC(), -direction.getB() - direction.getA() - direction.getC(), direction.getB() - direction.getA() - direction.getC());
    }
}
