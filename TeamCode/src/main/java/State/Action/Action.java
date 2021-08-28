package State.Action;

public interface Action {

    default void initialize(){}

    void update();

    default void onEnd(){}

    default boolean shouldDeactivate(){return true;}
}
