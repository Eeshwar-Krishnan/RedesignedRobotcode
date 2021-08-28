package Odometry;

import Hardware.HardwareSystems.UGSystems.Odometry;
import MathSystems.Position;
import MathSystems.Vector.Vector2;
import MathSystems.Vector.Vector3;

public abstract class Odometer {
    protected Position position, velocity;
    public Odometer(Position position, Position velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void update(Odometry odometry) {
        setPositionAndVelocity(getStaticIncrements(getRelativeIncrements(odometry), odometry), position, velocity);
    }

    public abstract void reset();
    public abstract Vector2 getRelativeIncrements(Odometry odometry);
    public abstract Vector3 getStaticIncrements(Vector2 relativeIncrements, Odometry odometry);
    public abstract void setPositionAndVelocity(Vector3 staticIncrements, Position position, Position velocity);
}
