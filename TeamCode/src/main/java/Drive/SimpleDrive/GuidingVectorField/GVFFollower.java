package Drive.SimpleDrive.GuidingVectorField;

import java.util.ArrayList;

import Drive.DriveState;
import Hardware.HardwareSystems.UltimateGoal.Drivetrain;
import Hardware.RobotConstants;
import MathSystems.Angle;
import MathSystems.MathUtils;
import Utils.Path.Path;
import MathSystems.Position;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;
import State.StateMachine;

public class GVFFollower extends DriveState {
    private double xPrec, yPrec, rPrec, speed, slowdownMod, lineDev;
    private Path path;

    public GVFFollower
            (StateMachine stateMachine, Position position, Drivetrain drivetrain, double xPrec, double yPrec, double rPrec,
             double speed, Path path, double slowdownMod, double lineDev) {
        super(stateMachine, position, drivetrain);
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = rPrec;
        this.speed = speed;
        this.path = path;
        this.slowdownMod = slowdownMod;
        this.lineDev = lineDev;
    }

    @Override
    public boolean shouldTerminate() {
        double xErr = Math.abs(position.getX() - path.getEndpoint().getX());
        double yErr = Math.abs(position.getY() - path.getEndpoint().getY());
        double rErr = Math.abs(MathUtils.getRadRotDist(position.getR().radians(), path.getEndpoint().getR().radians()));
        return (xErr < xPrec) && (yErr < yPrec) && (rErr < rPrec);
    }

    @Override
    public void update() {
        double minDist = Double.MAX_VALUE;
        Vector2 closestPoint = Vector2.ZERO(), start = Vector2.ZERO();
        Position target = new Position(0, 0, Angle.degrees(0));
        int idx = 0;
        for(int i = 1; i < path.getPathLines().size(); i ++){
            Vector2 tmp = MathUtils.getClosestPoint(path.getPathLines().get(i-1), path.getPathLines().get(i), position.getPos());
            double dist = tmp.distanceTo(position.getPos());
            if(dist < minDist){
                minDist = dist;
                closestPoint.set(tmp);
                target.set(path.getLines().get(i));
                start.set(path.getPathLines().get(i-1));
                idx = i;
            }
        }
        double lineDist = minDist;
        Vector2 tang = target.getPos().subtract(start).normalize();
        if(idx == path.getPathLines().size()-1){
            tang.set(target.getPos().subtract(position.getPos()));
        }
        double kf = MathUtils.clamp((1.0/lineDev) * lineDev, 0, 1);
        tang.scale(1-kf);

        Vector2 normal = closestPoint.subtract(position.getPos()).normalize();
        double maxNormVel = Math.sqrt(2 * (RobotConstants.LIN_ACCEL / slowdownMod) * lineDist);
        normal.scale(maxNormVel);

        ArrayList<Vector2> remaining = new ArrayList<>();
        remaining.add(position.getPos());
        remaining.addAll(path.getPathLines().subList(idx, path.getPathLines().size()));
        double remainingDist = MathUtils.arcLength(remaining);
        double maxTanSpeed = Math.sqrt(2 * (RobotConstants.LIN_ACCEL / slowdownMod) * remainingDist);
        tang.scale(maxTanSpeed);

        double rTarget = target.getR().radians();
        double rErr = MathUtils.getRadRotDist(position.getR().radians(), target.getR().radians());
        double maxRSpeed = Math.sqrt(2 * RobotConstants.R_ACCEL * rErr);
        double rSpeed = MathUtils.sign(rErr) * Math.min(maxRSpeed, RobotConstants.R_MAX_SPEED);

        Vector2 comb = normal.add(tang);
        Vector2 rotated = comb.rotate(Angle.radians(position.getR().radians() + (rSpeed * RobotConstants.R_FF)));
        Vector2 driveVec = rotated.clone();
        driveVec.set(driveVec.scale(1/RobotConstants.LIN_MAX_SPEED));

        rSpeed = rSpeed / RobotConstants.R_MAX_SPEED;

        Vector3 vec = new Vector3(driveVec, rSpeed);
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
