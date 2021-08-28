package Opmodes.Config;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import Drive.DriveConstants;
import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.Position;
import MathSystems.Vector.Vector3;
import Odometry.ConstantVOdometer;
import Odometry.Odometer;
import Odometry.SimpleOdometer;
import Opmodes.BasicOpmode;
import State.Action.Action;
import State.Action.ActionController;
import State.Action.StandardActions.DelayAction;
import State.StateMachine.StateMachine;
import Utils.OpmodeStatus;
import Utils.ProgramClock;
@TeleOp
public class RotationAccelConfig extends BasicOpmode {
    StateMachine rotationConfig;
    Odometer odometer;
    Position position, velocity;
    double rotAccel = DriveConstants.MAX_ROT_SPEED * 1.2, rotVel = 0;
    boolean rotVelTuneFinished = false, rotAccelTuneFinished = false;
    @Override
    public void setup() {
        odometer = new ConstantVOdometer(position, velocity);

        HashMap<String, Action> actions = new HashMap<>();

        actions.put("Rot Vel Tune", new Action() {
            final double accelTime = 3, coastTime = 3;

            final double rotSpeed = 1/accelTime;
            double timer;
            double angle;
            boolean finished;
            Angle lastAngle;
            @Override
            public void initialize() {
                timer = 0;
                angle = 0;
                finished = false;
                lastAngle = position.getR();
            }

            @Override
            public void update() {
                timer += ProgramClock.getFrameTimeSeconds();
                if(timer < accelTime){
                    hardware.getDrivetrainSystem().setPower(new Vector3(0, 0, rotSpeed * timer));
                }else if(timer >= accelTime && timer <= accelTime+coastTime){
                    angle += MathUtils.getRotDist(lastAngle, position.getR()).radians();
                    hardware.getDrivetrainSystem().setPower(new Vector3(0, 0, 1));
                }else{
                    hardware.getDrivetrainSystem().setPower(new Vector3(0, 0, -rotSpeed * ((accelTime+coastTime+accelTime) - timer)));
                }
                if(timer > 9){
                    hardware.getDrivetrainSystem().setPower(Vector3.ZERO());
                    rotVel = angle / coastTime;
                    finished = true;
                    rotVelTuneFinished = true;
                }
            }

            @Override
            public boolean shouldDeactivate() {
                return finished;
            }
        });

        actions.put("Wait For Robot Stop", new DelayAction(2000));

        actions.put("Rot Accel Tune", new Action() {
            double timer;
            boolean velReached, finished;

            @Override
            public void initialize() {
                timer = 0;
                velReached = false;
                finished = false;
            }

            @Override
            public void update() {
                if(!velReached){
                    hardware.getDrivetrainSystem().setPower(new Vector3(0, 0, 1));
                    timer += ProgramClock.getFrameTimeSeconds();
                    if(velocity.getR().radians() >= rotVel){
                        velReached = true;
                    }
                }else{
                    hardware.getDrivetrainSystem().setPower(Vector3.ZERO());
                    rotAccel = rotVel / timer;
                }
            }
        });

        OpmodeStatus.bindOnStart(() -> {
            if(!rotVelTuneFinished){
                telemetry.addLine("Tuning Velocity");
            }else if(!rotAccelTuneFinished){
                telemetry.addLine("Tuning Acceleration");
            }else{
                telemetry.addLine("Tuning Finished");
            }
            telemetry.addData("Angle", position.getR());
            telemetry.addData("Rotation Vel", rotVel);
            telemetry.addData("Rotation Accel", rotAccel);
        });
    }
}
