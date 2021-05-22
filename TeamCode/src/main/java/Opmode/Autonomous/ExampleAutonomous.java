package Opmode.Autonomous;

import MathSystems.Angle;
import Opmode.AutoOpmode;
import State.Control.ControlGroup;
import State.States.AsyncState;

public class ExampleAutonomous extends AutoOpmode {
    public int someGameVariable = 0;

    @Override
    public void initialize() {
        hardware.getDrivetrain().setBrake();
        hardware.getIntake().shieldUp();
        new AsyncState(stateMachine) {
            @Override
            public void update() {
                someGameVariable = (int) (Math.random() * 2);

                if(isStarted())
                    terminate();
            }
        }.submit();
    }

    @Override
    public void synchronous() {
        waitUntilStart();
        hardware.getIntake().shieldDown();

        pathBuilder.lineTo(-2.5, 50);
        factory.buildGVF(pathBuilder.complete()).complete().yield();

        hardware.getShooter().targetLeftPowershot(stateMachine).yield();
        hardware.getShooter().getShooterControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL).yield();

        hardware.getShooter().targetCenterPowershot(stateMachine).yield();
        hardware.getShooter().getShooterControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL).yield();

        hardware.getShooter().targetRightPowershot(stateMachine).yield();
        hardware.getShooter().getShooterControlGroup(stateMachine, ControlGroup.STOP_CONDITION.TERMINATE_ALL).yield();

        hardware.getIntake().shieldUp();

        if(someGameVariable == 1){

        }else{
            pathBuilder.reset();
            if(someGameVariable == 0){
                pathBuilder.lineTo(35, 84);
                factory.buildGVF(pathBuilder.complete()).complete().yield();
                //TODO: Drop The Wobble
                pathBuilder.reset();
                pathBuilder.lineTo(35, 108);
                pathBuilder.lineTo(40, 118);
            }else{
                pathBuilder.lineTo(30, 122);
                //TODO: Drop The Wobble
            }
            factory.buildGVF(pathBuilder.complete()).complete().yield();

            pathBuilder.reset();
            pathBuilder.lineTo(25, 108, Angle.degrees(45));
            pathBuilder.lineTo(-22, 105, Angle.degrees(90));
            factory.buildGVF(pathBuilder.complete()).complete().yield();

            pathBuilder.reset();
            pathBuilder.lineTo(20, 105);
            factory.buildGVF(pathBuilder.complete()).complete().yield();

            pathBuilder.reset();
            pathBuilder.lineTo(20, 122);
            factory.buildGVF(pathBuilder.complete()).setSpeed(0.4).complete().yield();

            pathBuilder.reset();
            pathBuilder.lineTo(-18, 122);
            factory.buildGVF(pathBuilder.complete()).complete().yield();
        }
    }
}
