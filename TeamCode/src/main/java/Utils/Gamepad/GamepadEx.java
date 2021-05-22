package Utils.Gamepad;

import android.os.SystemClock;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.concurrent.atomic.AtomicLong;

public class GamepadEx{
    private Gamepad gamepad;
    private AtomicLong timestamp;

    public Button a, b, x, y,
            dpad_up, dpad_down, dpad_left, dpad_right,
            guide, start, back,
            left_bumper, right_bumper,
            left_stick_button, right_stick_button;

    public Slider left_stick_x, left_stick_y, right_stick_x, right_stick_y, left_trigger, right_trigger;

    public GamepadEx(Gamepad gamepad){
        this.gamepad = gamepad;

        a = new Button();
        b = new Button();
        x = new Button();
        y = new Button();

        dpad_up = new Button();
        dpad_down = new Button();
        dpad_left = new Button();
        dpad_right = new Button();

        guide = new Button();
        start = new Button();
        back = new Button();

        left_bumper = new Button();
        right_bumper = new Button();

        left_stick_button = new Button();
        right_stick_button = new Button();

        left_stick_x = new Slider();
        left_stick_y = new Slider();
        right_stick_x = new Slider();
        right_stick_y = new Slider();

        left_trigger = new Slider();
        right_trigger = new Slider();

        timestamp = new AtomicLong(SystemClock.uptimeMillis());

        gamepad.setJoystickDeadzone(0);
    }

    public void update(){
        if(gamepad.timestamp == timestamp.get()){
            a.set(gamepad.a);
            b.set(gamepad.b);
            x.set(gamepad.x);
            y.set(gamepad.y);

            dpad_up.set(gamepad.dpad_up);
            dpad_down.set(gamepad.dpad_down);
            dpad_left.set(gamepad.dpad_left);
            dpad_right.set(gamepad.dpad_right);

            guide.set(gamepad.guide);
            start.set(gamepad.start);
            back.set(gamepad.back);

            left_bumper.set(gamepad.left_bumper);
            right_bumper.set(gamepad.right_bumper);

            left_stick_button.set(gamepad.left_stick_button);
            right_stick_button.set(gamepad.right_stick_button);

            left_stick_x.setValue(gamepad.left_stick_x);
            left_stick_y.setValue(gamepad.left_stick_y);
            right_stick_x.setValue(gamepad.right_stick_x);
            right_stick_y.setValue(gamepad.right_stick_y);

            left_trigger.setValue(gamepad.left_trigger);
            right_trigger.setValue(gamepad.right_trigger);

            timestamp.set(gamepad.timestamp);
        }
    }

    public long getTimestamp(){
        return timestamp.get();
    }
}
