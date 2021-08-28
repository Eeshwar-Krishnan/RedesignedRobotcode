package Odometry;

import Hardware.HardwareSystems.UGSystems.OdometrySystem;
import MathSystems.Angle;
import MathSystems.ConstantVMathUtil;
import MathSystems.Position;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;
import Utils.ProgramClock;

public class ConstantVOdometer extends Odometer {
    private final Position prevPosition;
    public ConstantVOdometer(Position position, Position velocity) {
        super(position, velocity);
        prevPosition = position.clone();
    }

    @Override
    public void reset() {
        this.position.set(Position.ZERO());
        this.velocity.set(Position.ZERO());
        this.prevPosition.set(Position.ZERO());
    }

    @Override
    public Vector2 getRelativeIncrements(OdometrySystem odometrySystem) {
        double rot = (odometrySystem.getOdometryRight() - odometrySystem.getOdometryLeft()) / 2.0;
        rot *= OdometryConstants.ODOMETRY_CPR;
        return new Vector2((odometrySystem.getOdometryLeftInc() + odometrySystem.getOdometryRightInc())/2.0, odometrySystem.getOdometryAuxInc() - (rot * OdometryConstants.ODOMETRY_RPA));
    }

    @Override
    public Vector3 getStaticIncrements(Vector2 relativeIncrements, OdometrySystem odometrySystem) {
        double rot = (odometrySystem.getOdometryRight() - odometrySystem.getOdometryLeft()) / 2.0;
        rot *= OdometryConstants.ODOMETRY_CPR;
        Vector2 rotated = ConstantVMathUtil.toRobotCentric(relativeIncrements.getA(), relativeIncrements.getB(), rot);
        return rotated.toVector3((odometrySystem.getOdometryRightInc() - odometrySystem.getOdometryLeftInc()) / 2.0);
    }

    @Override
    public void setPositionAndVelocity(Vector3 staticIncrements, Position position, Position velocity) {
        Vector2 linInc = staticIncrements.getVector2().scale(OdometryConstants.ODOMETRY_CPI);
        position.set(position.add(linInc, Angle.radians(staticIncrements.getC())));
        double tau = (2 * Math.PI);
        position.setR(Angle.radians(((position.getR().radians() % tau) + tau) % tau));
        double dt = ProgramClock.getFrameTimeSeconds();
        velocity.set(position.add(prevPosition.invert()).scale(dt));
        prevPosition.set(position);
    }
}
