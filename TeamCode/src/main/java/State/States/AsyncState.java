package State.States;

import State.StateMachine;

public abstract class AsyncState {
    public final StateMachine stateMachine;
    public String name;

    public AsyncState(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void onInit(){}

    public abstract void update();

    public void onStop(){}

    public void terminate(){
        stateMachine.terminateState(name);
    }

    public boolean submit(){
        return stateMachine.appendState(String.valueOf(Math.random()).replace(".", ""), this);
    }

    public boolean submit(String name){
        return stateMachine.appendState(name, this);
    }
}
