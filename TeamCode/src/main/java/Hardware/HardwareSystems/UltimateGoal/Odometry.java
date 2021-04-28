package Hardware.HardwareSystems.UltimateGoal;

import com.qualcomm.robotcore.hardware.DcMotor;

import Hardware.HardwareSystems.HardwareSystem;
import Odometry.Odometer;

public class Odometry extends HardwareSystem {
    private DcMotor ol, oa, or;
    private double olOffset, oaOffset, orOffset;
    private double olPos, oaPos, orPos;
    private double lastOl, lastOa, lastOr;
    private volatile Odometer attachedOdometer = null;
    private final Object odometerLock = new Object();

    public Odometry(DcMotor ol, DcMotor oa, DcMotor or){
        this.ol = ol;
        this.oa = oa;
        this.or = or;
    }

    @Override
    public void calibrate() {
        olOffset = ol.getCurrentPosition();
        oaOffset = oa.getCurrentPosition();
        orOffset = or.getCurrentPosition();
    }

    @Override
    public void update() {
        lastOl = olPos;
        lastOa = oaPos;
        lastOr = orPos;
        olPos = ol.getCurrentPosition() - olOffset;
        oaPos = oa.getCurrentPosition() - oaOffset;
        orPos = or.getCurrentPosition() - orOffset;
        synchronized (odometerLock){
            if(attachedOdometer != null){
                attachedOdometer.update(this);
            }
        }
    }

    public double getOdometryLeft(){
        return olPos;
    }

    public double getOdometryRight(){
        return orPos;
    }

    public double getOdometryAux(){
        return oaPos;
    }

    public double getOdometryLeftInc(){
        return olPos - lastOl;
    }

    public double getOdometryRightInc(){
        return orPos - lastOr;
    }

    public double getOdometryAuxInc(){
        return oaPos - lastOa;
    }

    public void attachOdometer(Odometer odometer){
        synchronized (odometerLock) {
            this.attachedOdometer = odometer;
        }
    }
}
