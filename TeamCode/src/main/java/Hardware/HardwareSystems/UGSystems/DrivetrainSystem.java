package Hardware.HardwareSystems.UGSystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import Hardware.HardwareSystems.HardwareSystem;
import Hardware.SmartDevices.SmartMotor.SmartMotor;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;

public class DrivetrainSystem implements HardwareSystem {
    private final SmartMotor bl, br, tl, tr;
    private double blPower, brPower, tlPower, trPower;

    public DrivetrainSystem(DcMotor bl, DcMotor br, DcMotor tl, DcMotor tr){
        this.bl = new SmartMotor(bl);
        this.br = new SmartMotor(br);
        this.tl = new SmartMotor(tl);
        this.tr = new SmartMotor(tr);
    }

    @Override
    public void update() {
        bl.setPower(blPower);
        br.setPower(brPower);
        tl.setPower(tlPower);
        tr.setPower(trPower);
    }

    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior zeroPowerBehaviour){
        bl.getMotor().setZeroPowerBehavior(zeroPowerBehaviour);
        br.getMotor().setZeroPowerBehavior(zeroPowerBehaviour);
        tl.getMotor().setZeroPowerBehavior(zeroPowerBehaviour);
        tr.getMotor().setZeroPowerBehavior(zeroPowerBehaviour);
    }

    public void setPower(Vector3 direction){
        blPower = -direction.getB() + direction.getA() - direction.getC();
        brPower = direction.getB() + direction.getA() - direction.getC();
        tlPower = -direction.getB() - direction.getA() - direction.getC();
        trPower = direction.getB() - direction.getA() - direction.getC();
    }
}
