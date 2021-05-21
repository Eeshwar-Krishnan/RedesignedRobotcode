package Drive.SimpleDrive;

import Drive.SimpleDrive.DriveToPoint.DriveToPointBuilder;
import Drive.SimpleDrive.GuidingVectorField.GVFBuilder;
import Drive.SimpleDrive.PurePursuit.PurePursuitBuilder;
import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import MathSystems.Path.Path;
import MathSystems.Position;
import State.StateMachine;

public class SimpleDriveFactory {
    private StateMachine stateMachine;
    private Drivetrain drivetrain;
    private Position position;

    public SimpleDriveFactory(StateMachine stateMachine, Drivetrain drivetrain, Position position){
        this.stateMachine = stateMachine;
        this.drivetrain = drivetrain;
        this.position = position;
    }

    /**
     * Constructs a simple DriveToPoint "follower". This "follower" ignores the given path, instead just driving to the path endpoint directly
     * @param path The path to follow. Only the endpoint is used (Path is not respected)
     * @return Builder that can be used to edit specifics of the DriveToPoint follower
     */
    public DriveToPointBuilder buildSimple(Path path){
        return new DriveToPointBuilder(stateMachine, drivetrain, position, path.getEndpoint());
    }

    /**
     * Constructs a hybrid pure-pursuit follower, that does respect the path.
     * The system uses pure pursuit when within radius units of a line, and a vector field follower when not within that radius.
     * Pure pursuit rounds out straight lines, but is limited because it does not fully utilize a holonomic drivetrain, as it is designed for tank drivetrains
     * @param path The path to follow. Path is respected
     * @return Builder that can be used to edit specifics of the PurePursuit follower
     */
    public PurePursuitBuilder buildPurePursuit(Path path){
        return new PurePursuitBuilder(stateMachine, drivetrain, position, path);
    }

    /**
     * Constructs a Guiding Vector Field follower, that does respect the path.
     * GVF uses vector fields to construct a best-to-follow vector in order to follow the given line, deviating at most by maxLineDeviation
     * GVF is ideal for holonomic drivetrains, as it can take advantage of the drivetrain's ability to move in multiple directions while rotating
     * @param path The path to follow. Path is respected
     * @return Builder that can be used to edit specifics of the GVFFollower
     */
    public GVFBuilder buildGVF(Path path){
        return new GVFBuilder(stateMachine, drivetrain, position, path);
    }
}
