package State;

import java.util.HashMap;

import State.States.AsyncState;

public class StateMachine {
    private final HashMap<String, AsyncState> states;
    private final HashMap<String, AsyncState> activeStates;
    private final HashMap<String, AsyncState> queriedStates;

    public StateMachine(){
        states = new HashMap<>();
        queriedStates = new HashMap<>();
        activeStates = new HashMap<>();
    }

    public void update(){
        activeStates.putAll(queriedStates);
        queriedStates.clear();
        for(String s : activeStates.keySet()){
            activeStates.get(s).update();
        }
    }

    public boolean appendState(String name, AsyncState state){
        if(!states.containsKey(name)){
            state.setName(name);
            states.put(name, state);
            return true;
        }
        return false;
    }

    public void appendStates(HashMap<String, AsyncState> states){
        for(String s : states.keySet()){
            appendState(s, states.get(s));
        }
    }

    public boolean activateState(String name){
        if(states.containsKey(name) && !queriedStates.containsKey(name)){
            queriedStates.put(name, states.get(name));
            return true;
        }
        return false;
    }

    public boolean submit(String name, AsyncState state){
        boolean added = appendState(name, state);
        if(added)
            return activateState(name);
        return false;
    }

    public void submitAll(HashMap<String, AsyncState> states){
        for(String s : states.keySet()){
            submit(s, states.get(s));
        }
    }

    public boolean terminateState(String state){
        if(states.containsKey(state)){
            states.get(state).onStop();
            states.remove(state);
            return true;
        }
        return false;
    }

    public boolean logicStateActive(String state){
        return states.containsKey(state);
    }
}
