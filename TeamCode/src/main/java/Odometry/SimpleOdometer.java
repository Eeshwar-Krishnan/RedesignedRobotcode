package Odometry;

import Hardware.HardwareSystems.UltimateGoal.Odometry;
import Hardware.RobotConstants;
import MathSystems.Angle;
import MathSystems.Position;
import MathSystems.ProgramClock;
import MathSystems.Vector2;
import MathSystems.Vector3;
import State.StateMachine;

public class SimpleOdometer extends Odometer {
    private Position prevPosition;
    public SimpleOdometer(Position position, Position velocity) {
        super(position, velocity);
        prevPosition = position.clone();
    }

    @Override
    public void reset() {

    }

    @Override
    public Vector2 getRelativeIncrements(Odometry odometry) {
        double rot = (odometry.getOdometryRight() - odometry.getOdometryLeft()) / 2.0;
        rot *= RobotConstants.ODOMETRY_CPR;
        return new Vector2((odometry.getOdometryLeftInc() + odometry.getOdometryRightInc())/2.0, odometry.getOdometryAuxInc() - (rot * RobotConstants.ODOMETRY_RPA));
    }

    @Override
    public Vector3 getStaticIncrements(Vector2 relativeIncrements, Odometry odometry) {
        double rot = (odometry.getOdometryRight() - odometry.getOdometryLeft()) / 2.0;
        rot *= RobotConstants.ODOMETRY_CPR;
        Vector2 rotated = relativeIncrements.rotate(Angle.radians(rot));
        return rotated.toVector3((odometry.getOdometryRightInc() - odometry.getOdometryLeftInc()) / 2.0);
    }

    @Override
    public void setPositionAndVelocity(Vector3 staticIncrements, Position position, Position velocity) {
        Vector2 linInc = staticIncrements.getVector2().scale(RobotConstants.ODOMETRY_CPI);
        position.set(position.add(linInc, Angle.radians(staticIncrements.getC())));
        double dt = ProgramClock.getFrameTimeSeconds();
        velocity.set(position.add(prevPosition.invert()).scale(dt));
        prevPosition.set(position);
    }
}
