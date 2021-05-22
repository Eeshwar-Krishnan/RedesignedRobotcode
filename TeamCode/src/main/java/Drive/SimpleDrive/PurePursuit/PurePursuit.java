package Drive.SimpleDrive.PurePursuit;

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

public class PurePursuit extends DriveState {
    private double xPrec, yPrec, rPrec, speed, slowdownMod, radius;
    private Path path;

    public PurePursuit(StateMachine stateMachine, Position position, Drivetrain drivetrain, double xPrec, double yPrec, double rPrec, double speed, Path path, double slowdownMod, double radius) {
        super(stateMachine, position, drivetrain);
        this.xPrec = xPrec;
        this.yPrec = yPrec;
        this.rPrec = rPrec;
        this.speed = speed;
        this.path = path;
        this.slowdownMod = slowdownMod;
        this.radius = radius;
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
        boolean found = false;
        Vector2 target = Vector2.ZERO();
        Vector2 intersect1 = Vector2.ZERO(), intersect2 = Vector2.ZERO();
        double minDist = Double.MAX_VALUE;
        Vector2 closest = Vector2.ZERO();
        int idx1 = 0, idx2 = 0;
        for(int i = 1; i < path.getPathLines().size(); i ++){
            double numIntersect = findLineCircleIntersections(position.getX(), position.getY(), radius, path.getPathLines().get(i-1), path.getPathLines().get(i), intersect1, intersect2);
            if(numIntersect == 1){
                target.set(intersect1);
                found = true;
                idx1 = i;
            }else if(numIntersect == 2){
                if(intersect1.distanceTo(path.getPathLines().get(i)) < intersect2.distanceTo(path.getPathLines().get(i))){
                    target.set(intersect1);
                    found = true;
                    idx1 = i;
                }else{
                    target.set(intersect2);
                    found = true;
                    idx1 = i;
                }
            }
            Vector2 closestPoint = MathUtils.getClosestPoint(path.getPathLines().get(i-1), path.getPathLines().get(i), position.getPos());
            double dist = closestPoint.distanceTo(position.getPos());
            if(dist < minDist){
                minDist = dist;
                closest.set(closestPoint);
                idx2 = i;
            }
        }
        if(!found){
            target.set(closest);
            idx1 = idx2;
        }

        double rTarget = path.getLines().get(idx1).getR().radians();
        double rError = MathUtils.getRadRotDist(position.getR().radians(), rTarget);
        double maxRSpeed = Math.sqrt(2 * (RobotConstants.R_ACCEL / slowdownMod) * rError);
        double rSpeed = MathUtils.sign(rError) * Math.min(maxRSpeed, RobotConstants.R_MAX_SPEED);

        ArrayList<Vector2> remaining = new ArrayList<>();
        remaining.add(position.getPos());
        remaining.addAll(path.getPathLines().subList(idx1, path.getPathLines().size()));
        double remainingDist = MathUtils.arcLength(remaining);
        double maxLinSpeed = Math.sqrt(2 * (RobotConstants.LIN_ACCEL / slowdownMod) * remainingDist);

        Vector2 rotated = target.rotate(Angle.radians(position.getR().radians() + (rSpeed * RobotConstants.R_FF)));
        Vector2 driveVec = rotated.normalize();
        driveVec.set(driveVec.scale(maxLinSpeed));
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

    private static double findLineCircleIntersections(
            double cx, double cy, double radius,
            Vector2 point1, Vector2 point2, Vector2 intersect1, Vector2 intersect2) {
        double dx, dy, A, B, C, det, t;

        dx = point2.getA() - point1.getA();
        dy = point2.getB() - point1.getB();

        A = dx * dx + dy * dy;
        B = 2 * (dx * (point1.getA() - cx) + dy * (point1.getB() - cy));
        C = (point1.getA() - cx) * (point1.getA() - cx) +
                (point1.getB() - cy) * (point1.getB() - cy) -
                radius * radius;

        det = B * B - 4 * A * C;
        if ((A <= 0.0000001) || (det < 0)) {
            // No real solutions.
            return 0;
        } else if (det == 0) {
            // One solution.
            t = -B / (2 * A);
            Vector2 intersection1 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            if(!MathUtils.inBetween(point1, point2, intersection1))
                return 0;
            Vector2 intersection2 = new Vector2(Double.NaN, Double.NaN);
            intersect1.set(intersection1);
            intersect2.set(intersection2);
            return 1;
        } else {
            // Two solutions.
            t = (float) ((-B + Math.sqrt(det)) / (2 * A));
            Vector2 intersection1 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            t = (float) ((-B - Math.sqrt(det)) / (2 * A));
            Vector2 intersection2 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            if(!MathUtils.inBetween(point1, point2, intersection1) || !MathUtils.inBetween(point1, point2, intersection2)){
                if(!MathUtils.inBetween(point1, point2, intersection1) && !MathUtils.inBetween(point1, point2, intersection2)){
                    return 0;
                }else if(!MathUtils.inBetween(point1, point2, intersection1)){
                    intersect1.set(intersection2);
                    return 1;
                }else{
                    intersect1.set(intersection1);
                    return 1;
                }
            }

            intersect1.set(intersection1);
            intersect2.set(intersection2);
            return 2;
        }
    }
}
