package ca.jaws;

import org.firmata4j.*;

import java.io.IOException;
/*
 * AUTHOR:   Aradhya Chawla
 * Function: Event listener to listen for pump state and provide appropriate auditory and visual cues
 *           indicating pump operation
 * */
public class PumpListener implements PinEventListener {
    private final Pin LED;
    private final Pin PUMP;
    private final Pin SOUND;

    PumpListener(Pin pump, Pin led, Pin sound) {
        PUMP = pump;
        LED = led;
        SOUND = sound;
    }

    @Override
    public void onModeChange(IOEvent event) {}

    @Override
    public void onValueChange(IOEvent event) {
        // Switches on/off LED and Buzzer to indicate pump operation
        try {
            LED.setValue(PUMP.getValue());
            SOUND.setValue(PUMP.getValue());
        } catch (IOException e) { e.printStackTrace(); }
    }
}
