package Opmode.TeleOp;

import Hardware.HardwareSystems.UltimateGoal.Shooter;
import MathSystems.Vector3;
import Opmode.BasicOpmode;
import State.Control.ControlGroup;
import State.States.AsyncState;

public class ExampleTeleop extends BasicOpmode {
    ControlGroup shootGroup = Shooter.getShooterControlGroup(stateMachine, hardware, ControlGroup.STOP_CONDITION.TERMINATE_ALL);
    @Override
    public void setup() {

    }

    @Override
    public void update() {
        hardware.getDrivetrain().setDirection(new Vector3(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x));
        hardware.getIntake().setIntakeSpeed(gamepad1.left_trigger);

        if(gamepad1.right_trigger > 0.1){
            stateMachine.submit("Shoot Group", shootGroup);
        }
    }


}
