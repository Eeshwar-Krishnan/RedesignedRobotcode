package State.Control;

import Utils.ProgramClock;
import State.StateMachine;

public class TimerControlState extends ControlState {
    private long timer;
    public TimerControlState(StateMachine stateMachine, long time) {
        super(stateMachine);
        this.timer = time;
    }

    @Override
    public boolean shouldTerminate() {
        return timer <= 0;
    }

    @Override
    public void update() {
        timer -= ProgramClock.getFrameTimeMillis();
    }
}
