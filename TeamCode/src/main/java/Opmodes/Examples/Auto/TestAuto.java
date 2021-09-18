package Opmodes.Examples.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashMap;
import java.util.LinkedHashMap;

import Drive.AdvDrive.GVF.GVFDrive;
import MathSystems.Angle;
import MathSystems.Position;
import MathSystems.Vector.Vector3;
import Odometry.ConstantVOdometer;
import Opmodes.BasicOpmode;
import State.Action.Action;
import State.Action.ActionController;
import State.StateMachine.StateMachine;
import Utils.OpmodeStatus;
import Utils.PathUtils.ContinousPathBuilder;
import Utils.PathUtils.Path;
import Utils.PathUtils.Profiling.LinearProfile;
import Utils.PathUtils.Profiling.LinearProfiler;
import Utils.ProgramClock;

@Autonomous
public class TestAuto extends BasicOpmode {
    ConstantVOdometer odometer;
    Position position, velocity;
    @Override
    public void setup() {
        position = Position.ZERO();
        velocity = Position.ZERO();
        odometer = new ConstantVOdometer(position, velocity);
        hardware.getOdometrySystem().attachOdometer(odometer);

        hardware.getDrivetrainSystem().setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE);

        Path path = new ContinousPathBuilder(new Position(0, 0, Angle.degrees(0)))
                .lineTo(new Position(0, 50, Angle.degrees(0)))
                .lineTo(new Position(50, 50, Angle.degrees(0)))
                .build();
        long start = System.currentTimeMillis();
        LinearProfile profile = LinearProfiler.profile(path, 60, 10, 30, 5, 100);
        long time = System.currentTimeMillis() - start;

        GVFDrive drive = new GVFDrive(hardware.getDrivetrainSystem(), position, path, profile);

        ActionController.addAction(() -> {
            telemetry.addData("Time Taken To Profile", time);
            telemetry.addData("Chub Latency", hardware.getChubLatency());
            telemetry.addData("Ehub Latency", hardware.getEhubLatency());
            telemetry.addData("Pos", position);
            telemetry.addData("FPS", 1.0 / ProgramClock.getFrameTimeSeconds());
        });

        LinkedHashMap<String, Action> states = new LinkedHashMap<>();
        states.put("Drive", drive);
        states.put("Stop", () -> hardware.getDrivetrainSystem().setPower(Vector3.ZERO()));

        StateMachine stateMachine = new StateMachine(states, StateMachine.ENDTYPE.END_ALL);

        OpmodeStatus.bindOnStart(stateMachine);
    }
}
