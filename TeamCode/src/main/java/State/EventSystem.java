package State;

import java.util.HashMap;

import State.States.AsyncState;

public class EventSystem {
    private HashMap<String, AsyncState> onStart;
    public EventSystem(){
        onStart = new HashMap<>();
    }

    public void submitOnStart(String name, AsyncState state){
        onStart.put(name, state);
    }

    public void triggerOnStart(StateMachine stateMachine){
        stateMachine.submitAll(onStart);
    }
}
