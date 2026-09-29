package ca.jaws;

import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.*;
/*
 * AUTHOR:   Aradhya Chawla
 * Function: TimerTask responsible for changing pump state, updating the LinkedHashMap with new data,
 *           passing information to be displayed to the OLED screen, and refreshing the graph.
 * */
public class PlantTask extends TimerTask {
    private final static int THRESHOLD_HIGH = 53; // Upper bound of threshold percentage
    private final static int THRESHOLD_LOW = 47; // Lower bound of threshold percentage
    private final   Pin     MOISTURE;
    private final   Pin     PUMP;
    private final   SSD1306 OLED;
    private final   LinkedHashMap MP;
    private final   double  ST;
    public PlantTask(Pin mSense, Pin gPump, SSD1306 OLEDObj, LinkedHashMap<Long, Integer> moistPairs, double startTime) {
        MOISTURE = mSense;
        PUMP = gPump;
        OLED = OLEDObj;
        MP = moistPairs;
        ST = startTime;
    }

    @Override
    public void run() {
        try {
            // Gets percentage of moisture
            var mVal = moisturePercentage.getMoisturePercentage(MOISTURE.getValue());

            // Gets elapsed time
            double elapsedTime = Math.round(((double) System.currentTimeMillis() /1000) - ST);

            // Overall Average Moisture Level
            int average = 0;
            if( MP.size() > 0 ){ average = (MP.values().stream().mapToInt(i -> (int) i).sum())/MP.size(); }

            // Updates Grove screen info
            new MoistureOut(OLED, mVal, PUMP.getValue(), average);

            // Switches pump on/off depending on moisture level
            if (mVal < THRESHOLD_LOW){
                PUMP.setValue(1);
            } else { PUMP.setValue(0);}

            // Updates HashMap with elapsed time and moisture percentage
            MP.put(elapsedTime, mVal);

            // Call to update graph
            new UpdateGraph(MP);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
