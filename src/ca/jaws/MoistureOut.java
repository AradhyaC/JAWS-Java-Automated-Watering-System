package ca.jaws;

import org.firmata4j.ssd1306.*;

/*
 * AUTHOR:   Aradhya Chawla
 * Function: Responsible for formatting and outputting received information to the OLED screen.
 * */
public class MoistureOut {
    private final int AIR = new moisturePercentage().getMoisturePercentage(400);
    private final int THRESHOLD_HIGH = 53; // Upper bound of threshold percentage
    private final int THRESHOLD_LOW = 47; // Lower bound of threshold percentage
    private final int WATER = new moisturePercentage().getMoisturePercentage(260);
    public MoistureOut(SSD1306 OLED, int MoistVal, long pState, int average){
        // Variables for moisture sensor and pump state messages
        String moistOut = null;
        String pumpOut = null;

        // Sets moisture sensor state message
        if (MoistVal == AIR) {
            moistOut = "!!  Sensor in Air  !!";
        } else if (MoistVal < THRESHOLD_LOW) {
            moistOut = "!!     Soil Dry    !!";
        } else if (MoistVal <= THRESHOLD_HIGH){
            moistOut = "!!   Soil Watered  !!";
        } else if (MoistVal < WATER) {
            moistOut = "!!  Soil Saturated !!";
        } else { moistOut = "!! Sensor in Water !!"; }

        // Sets pump state message
        if (pState == 1){
            pumpOut = "ON";
        } else {pumpOut = "OFF";}

        // Displays moisture sensor state, pump state, and overall average moisture level
        OLED.getCanvas().clear();
        OLED.getCanvas().drawString(0, 0, "Moisture Sensor");
        OLED.getCanvas().drawHorizontalLine(0, 10, 128, MonochromeCanvas.Color.BRIGHT);
        OLED.getCanvas().drawString(0, 20, "LVL:");
        OLED.getCanvas().setTextsize(2);
        // Display moisture percentage
        OLED.getCanvas().drawString(24, 20, Integer.toString(MoistVal));
        OLED.getCanvas().setTextsize(1);
        OLED.getCanvas().drawString(67, 20, "PMP:");
        OLED.getCanvas().setTextsize(2);
        // Display pump state
        OLED.getCanvas().drawString(91, 20, pumpOut);
        OLED.getCanvas().setTextsize(1);
        // Display moisture sensor state
        OLED.getCanvas().drawString(0, 42, moistOut);
        // Display overall average moisture
        OLED.getCanvas().drawString(0, 52, "Avg Moisture: "+average);
        OLED.display();
    }
}
