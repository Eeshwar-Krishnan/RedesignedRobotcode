package Utils.Gamepad;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility Button class to simplify some common gamepad operations
 * Class is thread safe because GamepadEx is threadsafe
 */
public class Button {
    private AtomicBoolean toggled, state, lastState;
    private AtomicLong timestamp;

    public Button(){
        toggled = new AtomicBoolean(false);
        state = new AtomicBoolean(false);
        lastState = new AtomicBoolean(false);
        timestamp = new AtomicLong(System.currentTimeMillis());
    }

    /**
     * Set the state of the button
     * @param state the state of the button. True is pressed, false is released
     */
    public void set(boolean state){
        this.state.set(state);
        if(this.state.get() != this.lastState.get()){
            this.toggled.set(!this.toggled.get());
            this.lastState.set(state);
            timestamp.set(System.currentTimeMillis());
        }
    }

    /**
     * Get the current state of the button
     * @return the current state of the button. True is pressed, false is released
     */
    public boolean pressed(){
        return this.state.get();
    }

    /**
     * Gets the toggle state of the button
     * The toggle state switches from true to false and vise versa every time the button is pressed
     * @return The toggle state of the button
     */
    public boolean toggled(){
        return this.toggled.get();
    }

    /**
     * Gets the timestamp of the last time the button state was changed
     * This can be used to determine how long a button has been pressed or released
     * @return The last time that the state of the button was changed
     */
    public long getStateTimestamp(){
        return timestamp.get();
    }
}