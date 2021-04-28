package State.Control;

import java.util.ArrayList;
import java.util.Arrays;

import State.StateMachine;
import State.States.AsyncState;

public class ControlGroup extends ControlState {
    private ArrayList<ControlState> states;
    private int index = -1;
    private STOP_CONDITION stop_condition;
    private boolean finished = false;

    public ControlGroup(StateMachine stateMachine, STOP_CONDITION stop_condition, ArrayList<ControlState> states) {
        super(stateMachine);
        this.states = new ArrayList<>();
        this.states.addAll(states);
        this.stop_condition = stop_condition;
    }

    public ControlGroup(StateMachine stateMachine, STOP_CONDITION stop_condition, ControlState... states){
        super(stateMachine);
        this.states = new ArrayList<>();
        this.states.addAll(Arrays.asList(states));
        this.stop_condition = stop_condition;
    }

    @Override
    public void update() {
        if(index == -1){
            index = 0;
            stateMachine.submit(name + index, states.get(index));
        }else if(states.get(index).shouldTerminate()){
            stateMachine.terminateState(name + index);
            index ++;
            if(index >= states.size()){
                if(stop_condition == STOP_CONDITION.TERMINATE_ALL){
                    stateMachine.terminateState(name);
                }else if(stop_condition == STOP_CONDITION.REPEAT_LAST){
                    index --;
                }else {
                    index = 0;
                    stateMachine.submit(name + index, states.get(index));
                }
                finished = true;
            }else{
                stateMachine.submit(name + index, states.get(index));
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return finished;
    }

    public enum STOP_CONDITION{
        TERMINATE_ALL,
        REPEAT_LAST,
        LOOP
    }
}
