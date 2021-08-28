package Utils.GamepadEx;

import java.util.ArrayList;

import State.Action.Action;
import State.Action.ActionController;

public class Trigger {
    private double value, lastValue, threshold;
    private final ArrayList<Action> onChangeStates;
    private Button button;

    public Trigger(){
        value = 0;
        lastValue = 0;
        onChangeStates = new ArrayList<>();
        threshold = 0.1;
        button = new Button();
    }

    public void update(double value){
        this.value = value;
        if(Math.abs(this.value - lastValue) > 0.001){
            ActionController.addActions(onChangeStates);
            lastValue = value;
            button.update(this.value>=threshold);
        }
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public boolean pressed(){
        return value >= threshold;
    }

    public Action bindOnValueChange(Action action){
        onChangeStates.add(action);
        return action;
    }

    public Action bindOnPress(Action action){
        button.bindOnPress(action);
        return action;
    }

    public Action bindOnRelease(Action action){
        button.bindOnRelease(action);
        return action;
    }
}