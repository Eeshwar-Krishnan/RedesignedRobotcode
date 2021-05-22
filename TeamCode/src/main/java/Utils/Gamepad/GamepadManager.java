package Utils.Gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import Utils.OpmodeStatus;
import Utils.ThreadManager;

public class GamepadManager implements Runnable {
    private Gamepad gamepad1, gamepad2;
    public GamepadEx gamepadEx1, gamepadEx2;
    private AtomicBoolean active;
    private ExecutorService service;

    public GamepadManager(Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);
        active = new AtomicBoolean(true);
        ThreadManager.execute(this);
    }

    @Override
    public void run() {
        while(active.get()){
            if(gamepad1.timestamp != gamepadEx1.getTimestamp()){
                gamepadEx1.update();
            }
            if(gamepad2.timestamp != gamepadEx2.getTimestamp()){
                gamepadEx2.update();
            }
            if(((gamepad1.timestamp - gamepadEx1.getTimestamp() > 5000) && gamepad1.timestamp - gamepadEx1.getTimestamp() > 5000) || !OpmodeStatus.opmodeActive()){
                //TODO
                //There is probably a better way to handle this
                //But if it has been 5 seconds since the last gamepad update
                //Then the opmode is probably emergency stopped or the phone has dc'd
                //So we should get the heck out of dodge immediately
                active.set(false);
            }
        }
    }

    public void stop(){
        active.set(false);
    }
}