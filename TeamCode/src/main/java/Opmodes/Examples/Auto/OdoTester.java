package Opmodes.Examples.Auto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Drive.AdvDrive.GVF.GVFDrive;
import MathSystems.Angle;
import MathSystems.Position;
import Odometry.ConstantVOdometer;
import Opmodes.BasicOpmode;
import State.Action.ActionController;
import Utils.OpmodeStatus;
import Utils.PathUtils.ContinousPathBuilder;
import Utils.PathUtils.Path;
import Utils.PathUtils.Profiling.LinearProfile;
import Utils.PathUtils.Profiling.LinearProfiler;

@TeleOp
public class OdoTester extends BasicOpmode {
    ConstantVOdometer odometer;
    Position position, velocity;
    @Override
    public void setup() {
        position = Position.ZERO();
        velocity = Position.ZERO();

        odometer = new ConstantVOdometer(position, velocity);
        hardware.getOdometrySystem().attachOdometer(odometer);

        ActionController.addAction(() -> {
            telemetry.addData("Time Taken To Profile", time);
            telemetry.addData("Chub Latency", hardware.getChubLatency());
            telemetry.addData("Ehub Latency", hardware.getEhubLatency());
            telemetry.addData("Position", position);
            telemetry.addData("Velocity", velocity);
        });
    }
}
