package Drive.SimpleDrive.GuidingVectorField;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import MathSystems.Angle;
import Utils.Path.Path;
import MathSystems.Position;
import State.StateMachine;

public class GVFBuilder {
    private StateMachine stateMachine;
    private Position position;
    private Path path;
    private Drivetrain drivetrain;
    private double xPrec, yPrec, rPrec, speed, slowdownMod, linDev;

    public GVFBuilder(StateMachine stateMachine, Drivetrain drivetrain, Position position, Path path){
        this.stateMachine = stateMachine;
        this.drivetrain = drivetrain;
        this.position = position;
        this.path = path;
        this.xPrec = 3;
        this.yPrec = 3;
        this.rPrec = Math.toRadians(5);
        this.speed = 1;
        this.slowdownMod = 1;
        this.linDev = 5;
    }

    public GVFBuilder setPrecision(double precision, Angle anglePrecision){
        this.xPrec = precision;
        this.yPrec = precision;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public GVFBuilder setPrecision(double xPrec, double yPrec, Angle anglePrecision){
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public GVFBuilder setSpeed(double speed){
        this.speed = speed;
        return this;
    }

    public GVFBuilder setSlowdownMod(double slowdownMod){
        this.slowdownMod = slowdownMod;
        return this;
    }

    public GVFBuilder setMaximumPathDeviation(double deviation){
        this.linDev = deviation;
        return this;
    }

    public GVFFollower complete(){
        return new GVFFollower(stateMachine, position, drivetrain, xPrec, yPrec, rPrec, speed, path, slowdownMod, linDev);
    }
}
