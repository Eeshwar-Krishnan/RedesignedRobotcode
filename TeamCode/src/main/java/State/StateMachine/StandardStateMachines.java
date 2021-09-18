package State.StateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import Hardware.HardwareSystems.UGSystems.ShooterSystem;
import State.Action.Action;
import State.Action.StandardActions.DelayAction;

public class StandardStateMachines {
    public static class UltimateGoal {
        public static StateMachine shootStatemachine(ShooterSystem system) {
            LinkedHashMap<String, Action> actions = new LinkedHashMap<>();
            actions.put("Index In", system::setIndexIn);
            actions.put("Index In Delay", new DelayAction(100));
            actions.put("Index Out", system::setIndexOut);
            actions.put("Index Out Delay", new DelayAction(100));
            return new StateMachine(actions, StateMachine.ENDTYPE.END_ALL);
        }
    }
}
