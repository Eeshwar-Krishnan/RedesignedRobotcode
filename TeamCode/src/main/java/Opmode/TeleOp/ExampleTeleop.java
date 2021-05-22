package Opmode.TeleOp;

import MathSystems.Vector.Vector3;
import Opmode.BasicOpmode;
import State.Control.ControlGroup;
import Utils.Gamepad.GamepadEx;
import Utils.Gamepad.GamepadManager;

public class ExampleTeleop extends BasicOpmode {
    ControlGroup shootGroup;
    GamepadManager manager;
    GamepadEx gamepadEx1, gamepadEx2;
    @Override
    public void setup() {
        manager = new GamepadManager(gamepad1, gamepad2);
        gamepadEx1 = manager.gamepadEx1;
        gamepadEx2 = manager.gamepadEx2;
        shootGroup = hardware.getShooter().getShooterControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL);
    }

    @Override
    public void update() {
        hardware.getDrivetrain().setDirection(new Vector3(gamepadEx1.left_stick_x.getValue(), gamepadEx1.left_stick_y.getValue(), gamepadEx1.right_stick_x.getValue()));
        hardware.getIntake().setIntakeSpeed(gamepadEx1.left_trigger.pressed() ? 1 : 0);

        if(gamepadEx1.right_trigger.pressed()){
            shootGroup.submit();
        }
    }


}
