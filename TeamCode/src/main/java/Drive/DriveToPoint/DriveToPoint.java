package Drive.DriveToPoint;

import Drive.DriveState;
import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import Hardware.RobotConstants;
import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.Position;
import MathSystems.Vector2;
import MathSystems.Vector3;
import State.StateMachine;

public class DriveToPoint extends DriveState {
    private double xPrec, yPrec, rPrec, speed, slowdownMod;
    private Position target;
    public DriveToPoint(StateMachine stateMachine, Position position, Drivetrain drivetrain, double xPrec, double yPrec, double rPrec, double speed, Position target, double slowdownMod) {
        super(stateMachine, position, drivetrain);
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = rPrec;
        this.speed = speed;
        this.target = target;
        this.slowdownMod = slowdownMod;
    }

    @Override
    public boolean shouldTerminate() {
        double xErr = Math.abs(position.getX() - target.getX());
        double yErr = Math.abs(position.getY() - target.getY());
        double rErr = Math.abs(MathUtils.getRadRotDist(position.getR().radians(), target.getR().radians()));
        return (xErr < xPrec) && (yErr < yPrec) && (rErr < rPrec);
    }

    @Override
    public void update() {
        Vector2 pos2 = position.getPos();
        Vector2 tar2 = position.getPos();

        Vector2 driveVec = tar2.subtract(pos2);
        if(Math.abs(driveVec.getA()) < 0){
            driveVec.setA(0);
        }
        if(Math.abs(driveVec.getB()) < 0){
            driveVec.setB(0);
        }
        double rErr = MathUtils.getRadRotDist(position.getR().radians(), target.getR().radians());
        if(Math.abs(rErr) < rPrec){
            rErr = 0;
        }
        double maxRSpeed = Math.sqrt(2 * rErr * (RobotConstants.R_ACCEL / slowdownMod));
        double rSpeed = MathUtils.sign(rErr) * Math.min(maxRSpeed, RobotConstants.R_MAX_SPEED);

        Vector2 rotated = driveVec.rotate(Angle.radians(position.getR().radians() + (rSpeed * RobotConstants.R_FF)));
        double maxSpeed = Math.sqrt(2 * rotated.getA() * (RobotConstants.LIN_ACCEL / slowdownMod));

        double linSpeed = MathUtils.sign(rotated.getA()) * Math.min(maxSpeed, RobotConstants.LIN_MAX_SPEED);

        Vector2 linVec = rotated.normalize();
        linVec.scale(maxSpeed);
        Vector3 vec = new Vector3(linVec, rSpeed);
        vec = new Vector3(vec.getA() / RobotConstants.LIN_MAX_SPEED, vec.getB() / RobotConstants.LIN_MAX_SPEED, vec.getC() / RobotConstants.R_MAX_SPEED);
        vec.set(vec.scale(speed));

        if(vec.getA() < RobotConstants.X_KS){
            vec.setA(MathUtils.sign(vec.getA()) * RobotConstants.X_KS);
        }
        if(vec.getB() < RobotConstants.Y_KS){
            vec.setB(MathUtils.sign(vec.getB()) * RobotConstants.Y_KS);
        }
        if(vec.getC() < RobotConstants.R_KS){
            vec.setC(MathUtils.sign(vec.getC()) * RobotConstants.R_KS);
        }

        drivetrain.setDirection(vec);
    }
}
