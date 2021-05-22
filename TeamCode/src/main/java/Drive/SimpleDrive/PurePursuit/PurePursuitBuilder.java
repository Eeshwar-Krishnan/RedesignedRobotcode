package Drive.SimpleDrive.PurePursuit;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import MathSystems.Angle;
import Utils.Path.Path;
import MathSystems.Position;
import State.StateMachine;

public class PurePursuitBuilder {
    private StateMachine stateMachine;
    private Position position;
    private Path path;
    private Drivetrain drivetrain;
    private double xPrec, yPrec, rPrec, speed, slowdownMod, radius;

    public PurePursuitBuilder(StateMachine stateMachine, Drivetrain drivetrain, Position position, Path path){
        this.stateMachine = stateMachine;
        this.drivetrain = drivetrain;
        this.position = position;
        this.path = path;
        this.xPrec = 3;
        this.yPrec = 3;
        this.rPrec = Math.toRadians(5);
        this.speed = 1;
        this.slowdownMod = 1;
        this.radius = 5;
    }

    public PurePursuitBuilder setPrecision(double precision, Angle anglePrecision){
        this.xPrec = precision;
        this.yPrec = precision;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public PurePursuitBuilder setPrecision(double xPrec, double yPrec, Angle anglePrecision){
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public PurePursuitBuilder setSpeed(double speed){
        this.speed = speed;
        return this;
    }

    public PurePursuitBuilder setSlowdownMod(double slowdownMod){
        this.slowdownMod = slowdownMod;
        return this;
    }

    public PurePursuitBuilder setRadius(double radius){
        this.radius = radius;
        return this;
    }

    public PurePursuit complete(){
        return new PurePursuit(stateMachine, position, drivetrain, xPrec, yPrec, rPrec, speed, path, slowdownMod, radius);
    }
}
