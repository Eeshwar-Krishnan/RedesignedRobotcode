package Drive.AdvDrive.GVF;

import com.qualcomm.robotcore.util.RobotLog;

import Drive.DriveConstants;
import Hardware.HardwareSystems.UGSystems.DrivetrainSystem;
import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.Position;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;
import State.Action.Action;
import Utils.PathUtils.Path;
import Utils.PathUtils.PathUtil;
import Utils.PathUtils.Profiling.LinearProfile;

public class GVFDrive implements Action {
    private final DrivetrainSystem drivetrainSystem;
    private final double speed, linTol, kps;
    private final Angle rotTol;
    private final Position position;
    private final Path path;
    private LinearProfile profile;
    private double lastProject;

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, LinearProfile profile, double speed, double kps, double linTol, Angle rotTol){
        this.drivetrainSystem = drivetrainSystem;
        this.position = position;
        this.path = path;
        this.speed = speed;
        this.kps = kps;
        this.linTol = linTol;
        this.rotTol = rotTol;
        this.profile = profile;
        this.lastProject = 0;
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, LinearProfile profile, double speed){
        this(drivetrainSystem, position, path, profile, speed, 0, 2, Angle.degrees(5));
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, LinearProfile profile, double speed, double kps){
        this(drivetrainSystem, position, path, profile, speed, kps, 2, Angle.degrees(5));
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, LinearProfile profile){
        this(drivetrainSystem, position, path, profile, 1, 0.15, 2, Angle.degrees(5));
    }

    @Override
    public void update() {
        double val = PathUtil.projectClosest(position, path, lastProject);
        Position closestPoint = path.get(val);
        Vector2 posDelta = closestPoint.getPos().subtract(position.getPos());

        if(posDelta.length() > 1 || Double.isNaN(val)){
            //Double check the project was right because we are so far away from where we expect
            val = PathUtil.project(position, path);
            closestPoint = path.get(val);
            posDelta = closestPoint.getPos().subtract(position.getPos());
        }

        double pathDist = posDelta.length();
        double pathReturnVel = Math.sqrt(2 * DriveConstants.MAX_LIN_ACCEL * pathDist);

        double currMaxVel = profile.getL(val * path.getSegments().size());

        //Vector2 norm = posDelta.normalize().scale(currMaxVel == DriveConstants.MAX_LIN_SPEED ? (pathReturnVel) : Math.min(pathReturnVel, DriveConstants.MAX_LIN_SPEED - currMaxVel));
        Vector2 tang = path.deriv(val).getVector2().normalize().scale(currMaxVel);

        Vector2 norm = tang.rotate(Angle.degrees(-90)).normalize().scale(Math.min(pathReturnVel, DriveConstants.MAX_LIN_SPEED));

        double weight = MathUtils.clamp(posDelta.length() * kps, 0, 1);

        if(closestPoint.getPos().distanceTo(path.getEndpoint().getPos()) < linTol){
            weight = 1;
            norm = path.getEndpoint().getPos().subtract(position.getPos()).normalize().scale(DriveConstants.MAX_LIN_ACCEL);
        }

        Vector2 vel = norm.subtract(tang).scale(weight).add(tang);

        //Vector2 vel = tang.add(norm);

        double r = vel.length();
            double theta = Math.atan2(vel.getB(), vel.getA()) - position.getR().radians();
        Vector2 rotatedDist = MathUtils.toCartesian(r, theta);

        Angle angDelta = MathUtils.getRotDist(position.getR(), closestPoint.getR());

        double xSpeed = rotatedDist.getA();

        xSpeed = xSpeed / DriveConstants.MAX_LIN_SPEED;

        double ySpeed = rotatedDist.getB();

        ySpeed = ySpeed / DriveConstants.MAX_LIN_SPEED;

        double rSpeed = Math.min(DriveConstants.MAX_ROT_SPEED, Math.sqrt(2 * Math.abs(angDelta.radians()) * DriveConstants.MAX_ROT_ACCEL));

        rSpeed = rSpeed / DriveConstants.MAX_ROT_SPEED;

        Vector2 linVel = new Vector2(xSpeed * GVFConstants.strafeGain * speed, ySpeed * GVFConstants.forwardGain * speed);

        //linVel.setB(linVel.getB() * -1);

        drivetrainSystem.setPower(new Vector3(linVel, rSpeed * GVFConstants.rotationGain * speed * MathUtils.sign(-angDelta.radians())));
        //drivetrainSystem.setPower(Vector3.ZERO());
        RobotLog.ii("GVF", currMaxVel + " | " + closestPoint + " | " + norm + " | " + tang + " | " + angDelta.degrees() + " | " + vel + " | " + val + " | " + linVel + " | " + Math.toDegrees(theta));
        lastProject = val;
    }

    @Override
    public boolean shouldDeactivate() {
        return path.getEndpoint().getPos().distanceTo(position.getPos()) < linTol && MathUtils.getRotDist(position.getR(), path.getEndpoint().getR()).radians() < rotTol.radians();
    }
}
