package Opmodes.Examples.Auto;

import Drive.AdvDrive.GVF.GVFDrive;
import MathSystems.Angle;
import MathSystems.Position;
import Odometry.ConstantVOdometer;
import Opmodes.BasicOpmode;
import State.Action.ActionController;
import Utils.OpmodeStatus;
import Utils.Path.Path;
import Utils.Path.PathBuilder;

public class TestAuto extends BasicOpmode {
    ConstantVOdometer odometer;
    Position position, velocity;
    @Override
    public void setup() {
        odometer = new ConstantVOdometer(position, velocity);
        hardware.getOdometrySystem().attachOdometer(odometer);

        Path path = new PathBuilder(Position.ZERO())
                .lineTo(0, 50, Angle.degrees(90))
                .lineTo(50, 50, Angle.degrees(180))
                .lineTo(50, 0, Angle.degrees(270))
                .complete();

        GVFDrive drive = new GVFDrive(hardware.getDrivetrainSystem(), position, path);

        OpmodeStatus.bindOnStart(drive);
    }
}
