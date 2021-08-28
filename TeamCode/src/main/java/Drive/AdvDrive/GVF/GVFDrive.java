package Drive.AdvDrive.GVF;

import Drive.DriveConstants;
import Hardware.HardwareSystems.UGSystems.DrivetrainSystem;
import MathSystems.Angle;
import MathSystems.MathUtils;
import MathSystems.Position;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;
import State.Action.Action;
import Utils.PID.PIDSystem;
import Utils.Path.Path;

public class GVFDrive implements Action {
    private final DrivetrainSystem drivetrainSystem;
    private final double speed, linTol, kps;
    private final Angle rotTol;
    private final Position position;
    private final Path path;

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, double speed, double kps, double linTol, Angle rotTol){
        this.drivetrainSystem = drivetrainSystem;
        this.position = position;
        this.path = path;
        this.speed = speed;
        this.kps = kps;
        this.linTol = linTol;
        this.rotTol = rotTol;
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, double speed){
        this(drivetrainSystem, position, path, speed, 0.15, 3, Angle.degrees(5));
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path, double speed, double kps){
        this(drivetrainSystem, position, path, speed, kps, 3, Angle.degrees(5));
    }

    public GVFDrive(DrivetrainSystem drivetrainSystem, Position position, Path path){
        this(drivetrainSystem, position, path, 1, 0.15, 3, Angle.degrees(5));
    }

    @Override
    public void update() {
        Position closestPoint = MathUtils.getClosestPoint(position, path);
        Vector2 posDelta = closestPoint.getPos().subtract(position.getPos());
        double r = posDelta.length();
        double theta = Math.atan2(posDelta.getB(), posDelta.getA()) - position.getR().radians();
        Vector2 rotatedDist = MathUtils.toCartesian(r, theta);

        Angle angDelta = MathUtils.getRotDist(position.getR(), closestPoint.getR());

        double xSpeed = Math.min(DriveConstants.MAX_LIN_SPEED, Math.sqrt(2 * Math.abs(rotatedDist.getA()) * DriveConstants.MAX_LIN_ACCEL));
        double ySpeed = Math.min(DriveConstants.MAX_LIN_SPEED, Math.sqrt(2 * Math.abs(rotatedDist.getB()) * DriveConstants.MAX_LIN_ACCEL));
        double rSpeed = Math.min(DriveConstants.MAX_ROT_SPEED, Math.sqrt(2 * Math.abs(angDelta.radians()) * DriveConstants.MAX_ROT_ACCEL));

        xSpeed = (xSpeed / DriveConstants.MAX_LIN_SPEED) * speed;
        ySpeed = (ySpeed / DriveConstants.MAX_LIN_SPEED) * speed;
        rSpeed = (rSpeed / DriveConstants.MAX_ROT_SPEED) * speed;

        Vector2 linVel = new Vector2(xSpeed * GVFConstants.strafeGain, ySpeed * GVFConstants.forwardGain).normalize();

        drivetrainSystem.setPower(new Vector3(linVel, rSpeed * GVFConstants.rotationGain));
    }

    @Override
    public boolean shouldDeactivate() {
        return path.getEndpoint().getPos().distanceTo(position.getPos()) < linTol && MathUtils.getRotDist(position.getR(), path.getEndpoint().getR()).radians() < rotTol.radians();
    }
}
