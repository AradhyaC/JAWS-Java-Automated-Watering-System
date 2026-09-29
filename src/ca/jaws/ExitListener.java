package ca.jaws;

import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.*;
import org.firmata4j.ssd1306.*;

import java.io.IOException;
import java.util.Timer;

/*
 * AUTHOR:   Aradhya Chawla
 * Function: Event listener to properly reset devices, disconnect Arduino and abort program
 * */
public class ExitListener implements PinEventListener{
    private final Pin BUTTON;
    private final Pin PUMP;
    private final Pin LED;
    private final Pin SOUND;
    private final SSD1306 OLED;
    private final Timer T;
    private final IODevice GROVE;
    // constructor
    ExitListener(IODevice grove, Pin buttonPin, Pin pump, Pin led, Pin sound, SSD1306 oled, Timer t) {
        BUTTON = buttonPin;
        PUMP = pump;
        LED = led;
        SOUND = sound;
        OLED = oled;
        T = t;
        GROVE = grove;
    }

    @Override
    public void onModeChange(IOEvent ioEvent) {

    }

    @Override
    public void onValueChange(IOEvent event) {
        try {
            // Exit Button Logic
            if (BUTTON.getValue() == 1){
                T.cancel();                                             // Cancels timer
                T.purge();                                              // Purges all TimerTasks
                System.out.println("::::     Exiting     ::::");        // Exit message
                PUMP.removeAllEventListeners();                         // Removes pump event listener
                PUMP.setValue(0);                                       // Switches off pump
                SOUND.setValue(0);                                      // Switches off buzzer
                LED.setValue(0);                                        // Switches off LED
                OLED.getCanvas().clear();                               // Clears OLED Canvas
                OLED.clear();                                           // Resets OLED
                Thread.sleep(500);                                // Gives OLED time to clear before turn off
                OLED.turnOff();                                         // Switches OLED off
                Thread.sleep(100);                                // Allows pump to turn back on
                PUMP.setValue(0);                                       // Switches off pump
                GROVE.stop();                                           // Disconnects Grove board
                StdDraw.save("moisture_vs_time_graph.png");     // Saves Line Graph
                System.out.println(":::: Program Aborted ::::");        // Program exit complete message
                System.exit(0);                                   // Aborts program runtime
            }
        } catch (IOException e) { e.printStackTrace(); } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
