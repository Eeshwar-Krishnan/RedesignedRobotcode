package State.StateMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import State.Action.Action;
import State.Action.ActionController;

public class StateMachine implements Action {
    private final ENDTYPE endtype;
    private final HashMap<String, Action> states;
    private final ArrayList<String> stateNames;
    private int stateIdx = 0;
    private boolean deactivate = false;

    public StateMachine(HashMap<String, Action> states, ENDTYPE endtype){
        this.states = new HashMap<>();
        this.states.putAll(states);
        this.stateNames = new ArrayList<>();
        this.stateNames.addAll(states.keySet());
        this.endtype = endtype;
    }

    @Override
    public void initialize() {
        deactivate = false;
        if(states.size() == 0){
            deactivate = true;
        }else{
            ActionController.addAction(states.get(0));
        }
    }

    @Override
    public void update() {
        if(stateIdx < 0){
            return;
        }
        if(Objects.requireNonNull(states.get(stateNames.get(stateIdx))).shouldDeactivate()){
            stateIdx ++;
            if(stateIdx >= states.size()){
                switch (endtype){
                    case LOOP:
                        stateIdx = 0;
                        ActionController.addAction(states.get(stateNames.get(stateIdx)));
                        break;
                    case CONTINUE_LAST:
                        stateIdx --;
                        ActionController.addAction(states.get(stateNames.get(stateIdx)));
                        break;
                    case END_ALL:
                        stateIdx = -1;
                        deactivate = true;
                        break;
                }
            }else{
                ActionController.addAction(states.get(stateNames.get(stateIdx)));
            }
        }
    }

    public void shutdown(){
        deactivate = true;
    }

    public String currentState(){
        return stateNames.get(stateIdx);
    }

    @Override
    public boolean shouldDeactivate() {
        return deactivate;
    }

    public enum ENDTYPE{
        LOOP,
        CONTINUE_LAST,
        END_ALL
    }
}