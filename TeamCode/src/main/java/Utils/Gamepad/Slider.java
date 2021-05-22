package Utils.Gamepad;

import Utils.AtomicDouble;

/**
 * Utility class for Slider components of gamepads. Sliders are different from buttons in that sliders return values from -1 to 1
 * Some examples of sliders are joysticks and triggers
 * Class is threadsafe because GamepadEx is threadsafe
 */
public class Slider {
    private AtomicDouble value, deadzone;

    public Slider(){
        value = new AtomicDouble();
        this.deadzone = new AtomicDouble(0.05);
    }

    /**
     * Sets the value of the slider
     * @param value Value of the slider
     */
    public void setValue(double value){
        this.value.set(value);
    }

    /**
     * Treats the slider like a button, returning if its absolute value is greater then the deadzone
     * This is very useful for sliders like the gamepad triggers
     * @return if the slider is "pressed"
     */
    public boolean pressed(){
        return !inDeadzone();
    }

    /**
     * Gets the value of the slider. If the absolute value of the slider is less than the deadzone, 0 is returned
     * @return the adjusted value of the sliders
     */
    public double getValue(){
        return inDeadzone() ? 0.0 : value.get();
    }

    private boolean inDeadzone(){
        return Math.abs(value.get()) < deadzone.get();
    }
}
