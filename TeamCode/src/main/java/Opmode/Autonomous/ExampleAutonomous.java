package Opmode.Autonomous;

import Drive.SimpleDrive.SimpleDriveFactory;
import Hardware.HardwareSystems.UltimateGoal.Shooter;
import MathSystems.Angle;
import MathSystems.Path.Path;
import MathSystems.Path.PathBuilder;
import MathSystems.Position;
import MathSystems.Vector3;
import Odometry.Odometer;
import Odometry.SimpleOdometer;
import Opmode.BasicOpmode;
import State.Control.ControlGroup;
import State.Control.ControlState;
import State.States.AsyncState;

public class ExampleAutonomous extends BasicOpmode {
    public Position position, velocity;
    public int someGameVariable = 0;
    public SimpleDriveFactory factory;
    @Override
    public void setup() {
        position = Position.ZERO();
        velocity = Position.ZERO();
        Odometer odometer = new SimpleOdometer(position, velocity);
        hardware.getOdometry().attachOdometer(odometer);

        factory = new SimpleDriveFactory(stateMachine, hardware.getDrivetrain(), position);

        stateMachine.submit("Check For Some Variable", new AsyncState(stateMachine) {
            @Override
            public void update() {
                someGameVariable = ((int)(Math.random() * 3)) + 1;
                if(isStarted())
                    terminate();
            }
        });

        eventSystem.submitOnStart("Action 1", doAction1("Switch 1", position.clone()));

        stateMachine.appendState("Switch 1", new AsyncState(stateMachine) {
            @Override
            public void update() {
                if(someGameVariable == 1){
                    stateMachine.submit("Action2", doAction2("Stop", position.clone()));
                }
            }
        });

        stateMachine.appendState("Stop", new AsyncState(stateMachine) {
            @Override
            public void update() {
                hardware.getDrivetrain().setDirection(Vector3.ZERO());
            }
        });
    }

    @Override
    public void update() {

    }

    public ControlGroup doAction1(final String nextState, Position position){
        Path path1 = new PathBuilder(position)
                .lineTo(20, 50, Angle.degrees(10))
                .complete();
        ControlState state1 = factory.buildGVF(path1).complete();
        ControlState state2 = Shooter.getShooterControlGroup(stateMachine, hardware, ControlGroup.STOP_CONDITION.TERMINATE_ALL);
        Path path2 = new PathBuilder(path1.getEndpoint())
                .lineTo(0, 50, Angle.degrees(-10)).complete();
        ControlState state3 = factory.buildGVF(path2).complete();
        ControlState state4 = Shooter.getShooterControlGroup(stateMachine, hardware, ControlGroup.STOP_CONDITION.TERMINATE_ALL);
        ControlState state5 = new ControlState(stateMachine) {
            @Override
            public boolean shouldTerminate() {
                return true;
            }

            @Override
            public void update() {
                stateMachine.activateState(nextState);
            }
        };
        return new ControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL, state1, state2, state3, state4, state5);
    }

    public ControlGroup doAction2(final String nextState, Position startPos){
        Path path1 = new PathBuilder(startPos)
                .lineTo(-20, someGameVariable == 0 ? 10 : someGameVariable == 1 ? 20 : 30, Angle.degrees(0))
                .complete();
        ControlState state1 = factory.buildGVF(path1).complete();
        ControlState state2 = Shooter.getShooterControlGroup(stateMachine, hardware, ControlGroup.STOP_CONDITION.TERMINATE_ALL);
        ControlState state3 = new ControlState(stateMachine) {
            @Override
            public boolean shouldTerminate() {
                return true;
            }

            @Override
            public void update() {
                stateMachine.activateState(nextState);
            }
        };
        return new ControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL, state1, state2, state3);
    }
}
