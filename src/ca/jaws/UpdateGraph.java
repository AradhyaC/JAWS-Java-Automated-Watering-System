package ca.jaws;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * AUTHOR:   Aradhya Chawla
 * Function: Updates the dynamic graph allowing it to dynamically grow
 * NOTE:     Dynamic sometimes flashes because enableDoubleBuffering()
 *           is not present in this (Maven) version of StdLib. Only the
 *           direct download file at
 *           https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java
 *           offers that functionality.
 */
public class UpdateGraph {
    //Experimentally acquired sensor values for soil moisture ~~~
    private final static int    AIR =           400;//          |
    private final static int    DRY_SOIL =      370;//          |
    private final static int    THRESHOLD =     330;//          |
    private final static int    SATURATED =     280;//          |
    private final static int    WATER =         260;//          |
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private LinkedHashMap MP;

    public UpdateGraph(LinkedHashMap moisturePairs){
        MP = moisturePairs;

        StdDraw.clear();

        //Title
        StdDraw.setFont(new Font("Arial", Font.BOLD,25));
        StdDraw.text((double) MP.size() /2,105,"Graph of Moisture Level Over Time");
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN,16));

        // Setting Y-axis
        StdDraw.setYscale(-8, 110);                                     // Y-axis scale
        StdDraw.line(0,-1,0,100);                        // Y-axis line
        StdDraw.text(0, 102, "Y");                             // Y-axis "Y" label
        StdDraw.text(-2,50,"Moisture Level",90);       // Y-axis label

        // Setting Y-axis descriptors for moisture level
        StdDraw.setPenColor(StdDraw.GREEN.darker().darker());           // Dark green for descriptors
        StdDraw.setFont(new Font("Arial", Font.BOLD,10));    // Distinct font for descriptors
        StdDraw.text(3, moisturePercentage.getMoisturePercentage(WATER), "WATER");
        StdDraw.text(3, moisturePercentage.getMoisturePercentage(SATURATED), "SATURATED");
        StdDraw.text(3, moisturePercentage.getMoisturePercentage(THRESHOLD), "THRESHOLD");
        StdDraw.text(3, moisturePercentage.getMoisturePercentage(DRY_SOIL), "DRY SOIL");
        // Air descriptor offset to below X-axis line
        StdDraw.text(3, moisturePercentage.getMoisturePercentage(AIR) - 1, "AIR");
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN,16));      // default font settings

        // Setting X-axis
        StdDraw.setXscale(-3,MP.size() + 2);                                     // X-axis scale
        StdDraw.line(-1,0.5, MP.size()+0.8,0.5);                  // X-axis line
        StdDraw.text(MP.size()+1, 0, "X");                              // X-axis "X" label
        StdDraw.text(0, -3, "O");                                       // X-axis Origin label
        StdDraw.text((double) MP.size() /2,-8,"Elapsed Time (s)");      // X-axis label

            /* Drawing blue data points, a red connecting line between points,
               and 10s markers on X-axis by iterating through LinkedHashMap */
        for (Map.Entry<Double, Integer> entry : (Iterable<Map.Entry<Double, Integer>>) MP.entrySet()) {

            // Drawing blue unicode bullet characters for graph points
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setFont(new Font("Arial", Font.PLAIN,25));
            // Get points from LinkedHashMap and draw to graph
            StdDraw.text(entry.getKey()+1,entry.getValue(),"•");
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setFont(new Font("SansSerif", Font.PLAIN,16));

            // Connecting lines
            if (entry.getKey() >= 1) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(0.005);
                StdDraw.line(
                        entry.getKey() + 1,
                        entry.getValue() + 0.5,
                        entry.getKey(),
                        ((int) MP.get(entry.getKey() - 1)) + 0.5
                );
            }

            // 10s markers on X-axis
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLACK);
            if ((entry.getKey() % 10 == 0) && (entry.getKey() != 0)) {
                StdDraw.text(entry.getKey(), -3, Double.toString(entry.getKey()));
            }
        }
    }
}
