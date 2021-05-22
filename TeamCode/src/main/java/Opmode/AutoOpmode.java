package Opmode;

import Drive.SimpleDrive.SimpleDriveFactory;
import Utils.Path.PathBuilder;
import MathSystems.Position;
import Odometry.Odometer;
import Odometry.SimpleOdometer;
import State.States.AsyncState;
import Utils.ThreadManager;

public abstract class AutoOpmode extends BasicOpmode {
    public Position position, velocity;
    public SimpleDriveFactory factory;
    public Odometer odometer;
    private Runnable runThread;
    private final Object startLock = new Object();
    public PathBuilder pathBuilder;

    @Override
    public void setup() {
        position = Position.ZERO();
        velocity = Position.ZERO();
        factory = new SimpleDriveFactory(stateMachine, hardware.getDrivetrain(), position);
        odometer = new SimpleOdometer(position, velocity);
        hardware.getOdometry().attachOdometer(odometer);
        pathBuilder = new PathBuilder(Position.ZERO());

        eventSystem.submitOnStart("Trigger Start Lock", new AsyncState(stateMachine) {
            @Override
            public void update() {
                synchronized (startLock) {
                    startLock.notifyAll();
                }
            }
        });

        initialize();
        waitForStart();
        runThread = new Runnable() {
            @Override
            public void run() {
                synchronous();
            }
        };
        ThreadManager.execute(runThread);
    }

    @Override
    public void update() {

    }

    public void waitUntilStart(){
        synchronized (startLock){
            try {
                startLock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public abstract void initialize();

    public abstract void synchronous();
}