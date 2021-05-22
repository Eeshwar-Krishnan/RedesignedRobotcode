package State.Control;

import State.StateMachine;
import State.States.AsyncState;

public abstract class ControlState extends AsyncState {

    public ControlState(StateMachine stateMachine) {
        super(stateMachine);
    }

    public abstract boolean shouldTerminate();

    public void yield(){
        synchronized (stateMachine){
            submit();
        }
        while(!shouldTerminate());
    }
}
