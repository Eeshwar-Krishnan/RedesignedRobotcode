package Opmodes.Examples.Teleop;

import MathSystems.Vector.Vector3;
import Opmodes.BasicOpmode;
import State.Action.ActionController;
import State.StateMachine.StandardStateMachines;
import Utils.GamepadEx.GamepadEx;
import Utils.ProgramClock;

public class ExampleUGTeleop extends BasicOpmode {
    private GamepadEx gamepadEx1, gamepadEx2;
    private Vector3 dtDirection;
    private double angle = 0;

    @Override
    public void setup() {
        dtDirection = Vector3.ZERO();

        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        ActionController.addAction(gamepadEx1);
        ActionController.addAction(gamepadEx2);

        gamepadEx1.left_joystick.bindOnValueChange(() -> {
            dtDirection.setA(gamepadEx1.left_joystick.getX());
            dtDirection.setB(gamepadEx1.left_joystick.getY());
        });

        gamepadEx1.right_joystick.bindOnXChange(() -> dtDirection.setC(gamepadEx1.left_joystick.getX()));

        ActionController.addAction(() -> hardware.getDrivetrainSystem().setPower(dtDirection));

        gamepadEx1.left_trigger.bindOnPress(() -> ActionController.addAction(StandardStateMachines.UltimateGoal.shootStatemachine(hardware.getShooterSystem())));

        gamepadEx1.right_bumper.bindOnPress(hardware.getIntakeSystem()::intake);
        gamepadEx1.right_bumper.bindOnRelease(hardware.getIntakeSystem()::stopIntake);

        gamepadEx1.right_trigger.bindOnPress(hardware.getIntakeSystem()::shieldDown);
        gamepadEx1.right_trigger.bindOnRelease(hardware.getIntakeSystem()::shieldIdle);

        gamepadEx1.left_bumper.bindOnPress(hardware.getIntakeSystem()::outtake);
        gamepadEx1.left_bumper.bindOnRelease(hardware.getIntakeSystem()::stopIntake);

        gamepadEx1.dpad_right.bindOnPress(hardware.getShooterSystem()::setShooterPowershot);
        gamepadEx1.dpad_left.bindOnPress(hardware.getShooterSystem()::setShooterPowershot);
        gamepadEx1.dpad_down.bindOnPress(hardware.getShooterSystem()::setShooterPowershot);
        gamepadEx1.dpad_up.bindOnPress(hardware.getShooterSystem()::setShooterPowershot);

        gamepadEx1.dpad_right.bindOnRelease(hardware.getShooterSystem()::setShooterHighgoal);
        gamepadEx1.dpad_left.bindOnRelease(hardware.getShooterSystem()::setShooterHighgoal);
        gamepadEx1.dpad_down.bindOnRelease(hardware.getShooterSystem()::setShooterHighgoal);
        gamepadEx1.dpad_up.bindOnRelease(hardware.getShooterSystem()::setShooterHighgoal);

        gamepadEx2.dpad_right.bindOnPress(() -> angle ++);
        gamepadEx2.dpad_left.bindOnPress(() -> angle --);

        ActionController.addAction(() -> {
            angle = hardware.getShooterSystem().clampTurretAngle(angle);
            hardware.getShooterSystem().setTurretAngle(angle);
        });

        ActionController.addAction(() -> {
            telemetry.addData("Logic Timings", (1.0 / ProgramClock.getFrameTimeSeconds()) + " LPS | " + ProgramClock.getFrameTimeMillis() + " ms");
            telemetry.addData("Chub", (1000.0 / hardware.getChubLatency()) + " LPS | " + hardware.getChubLatency() + " ms");
            telemetry.addData("Ehub", (1000.0 / hardware.getEhubLatency()) + " LPS | " + hardware.getEhubLatency() + " ms");
        });
    }
}
