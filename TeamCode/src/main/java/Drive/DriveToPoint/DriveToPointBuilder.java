package Drive.DriveToPoint;

import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import MathSystems.Angle;
import MathSystems.Position;
import State.StateMachine;

public class DriveToPointBuilder {
    private StateMachine stateMachine;
    private Position position, target;
    private Drivetrain drivetrain;
    private double xPrec, yPrec, rPrec, speed, slowdownMod;

    public DriveToPointBuilder(StateMachine stateMachine, Drivetrain drivetrain, Position position, Position target){
        this.stateMachine = stateMachine;
        this.drivetrain = drivetrain;
        this.position = position;
        this.target = target;
        this.xPrec = 3;
        this.yPrec = 3;
        this.rPrec = Math.toRadians(5);
        this.speed = 1;
        this.slowdownMod = 1;
    }

    public DriveToPointBuilder setPrecision(double precision, Angle anglePrecision){
        this.xPrec = precision;
        this.yPrec = precision;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public DriveToPointBuilder setPrecision(double xPrec, double yPrec, Angle anglePrecision){
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = anglePrecision.radians();
        return this;
    }

    public DriveToPointBuilder setSpeed(double speed){
        this.speed = speed;
        return this;
    }

    public DriveToPointBuilder setSlowdownMod(double slowdownMod){
        this.slowdownMod = slowdownMod;
        return this;
    }

    public DriveToPoint complete(){
        return new DriveToPoint(stateMachine, position, drivetrain, xPrec, yPrec, rPrec, speed, target, slowdownMod);
    }
}
