package ca.jaws;

/*
 * AUTHOR:   Aradhya Chawla
 * Function: Responsible for converting raw sensor voltages to understandable percentages.
 * */
public class moisturePercentage {
    /*
     * NOTE: The Moisture Sensor in my code was attached to the 3.3V Vin instead of the 5V.
     *       Attaching to the 5V Vin will change Threshold values.
     * */
    private final static double AIR = 400.0;
    private final static double WATER = 260.0;
    public static int getMoisturePercentage(long moistureValue){
        // Returns raw sensor value converted to rounded up percent level of moisture
        // Rounded up instead of down since it's better to overcompensate than under-compensate
        var mPercent = (int) Math.ceil(100.0 - (((moistureValue - WATER)/(AIR-WATER))*100.0));

        // Applies upper and lower bound to moisture percentage
        if (mPercent > 100){ mPercent = 100; } else if (mPercent < 0) { mPercent = 0; }

        return mPercent;
    }
}
