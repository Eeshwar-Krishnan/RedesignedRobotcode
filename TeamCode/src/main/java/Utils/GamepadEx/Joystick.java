package Utils.GamepadEx;

import java.util.ArrayList;

import State.Action.Action;
import State.Action.ActionController;

public class Joystick {
    private double lastX, lastY, x, y, deadzone;
    private final ArrayList<Action> onXChange, onYChange, onChange;

    public Joystick(){
        this.onXChange = new ArrayList<>();
        this.onYChange = new ArrayList<>();
        this.onChange = new ArrayList<>();
        this.lastX = 0;
        this.lastY = 0;
        this.x = 0;
        this.y = 0;
        deadzone = 0.1;
    }

    public void update(double x, double y){
        if(Math.abs(x) < deadzone){
            x = 0;
        }
        if(Math.abs(y) < deadzone){
            y = 0;
        }
        this.x = x;
        this.y = y;
        if(Math.abs(this.x - this.lastX) < 0.001){
            ActionController.addActions(onXChange);
        }
        if(Math.abs(this.y - this.lastY) < 0.001){
            ActionController.addActions(onYChange);
        }
        if(Math.abs(this.x - this.lastX) < 0.001 || Math.abs(this.y - this.lastY) < 0.001){
            ActionController.addActions(onChange);
        }
    }

    public void setDeadzone(double deadzone){
        this.deadzone = deadzone;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Action bindOnXChange(Action action){
        this.onXChange.add(action);
        return action;
    }

    public Action bindOnYChange(Action action){
        this.onYChange.add(action);
        return action;
    }

    public Action bindOnValueChange(Action action){
        this.onChange.add(action);
        return action;
    }
}
