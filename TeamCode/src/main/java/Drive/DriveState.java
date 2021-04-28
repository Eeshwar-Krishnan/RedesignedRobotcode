package Drive;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import MathSystems.Position;
import State.Control.ControlState;
import State.StateMachine;

public abstract class DriveState extends ControlState {
    public Position position;
    public Drivetrain drivetrain;
    public DriveState(StateMachine stateMachine, Position position, Drivetrain drivetrain) {
        super(stateMachine);
        this.position = position;
        this.drivetrain = drivetrain;
    }
}
