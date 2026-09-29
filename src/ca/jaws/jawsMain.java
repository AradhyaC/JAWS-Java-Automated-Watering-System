package ca.jaws;

import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Timer;
import edu.princeton.cs.introcs.*;

/*
* NOTE: The Moisture Sensor in my code was attached to the 3.3V Vin instead of the 5V.
*       Attaching to the 5V Vin will change Threshold values.
* */

/*
* AUTHOR:   Aradhya Chawla
* Function: Main class for JAWS
* */

public class jawsMain {
    static final String PORT = "COM8";  //COM Port

    public static void main(String[] args) throws InterruptedException, IOException {
        // Grove Initialization
        var grove = new FirmataDevice(PORT);
        grove.start();
        grove.ensureInitializationIsDone();

        // OLED Initialization
        I2CDevice i2cDevice = grove.getI2CDevice(Peripherals.I2C0);
        SSD1306 OLEDObj = new SSD1306(i2cDevice, SSD1306.Size.SSD1306_128_64);
        OLEDObj.init();
        OLEDObj.clear();

        // Inputs
        var mSense = grove.getPin(Peripherals.A1);     // Moisture Sensor on A1
        mSense.setMode(Pin.Mode.ANALOG);

        var gSound = grove.getPin(Peripherals.D3);     // Buzzer on D3
        gSound.setMode(Pin.Mode.PWM);

        var gLED = grove.getPin(Peripherals.D4);       // LED on D4
        gLED.setMode(Pin.Mode.OUTPUT);

        var gButton = grove.getPin(Peripherals.D6);    // Button on D6
        gButton.setMode(Pin.Mode.INPUT);

        var gPump = grove.getPin(Peripherals.D7);      // MOSFET w/ Pump on D7
        gPump.setMode(Pin.Mode.OUTPUT);

        //LinkedHashMap to hold value pairs from Moisture Sensor in correct order
        LinkedHashMap<Long, Integer> moistPairs = new LinkedHashMap<>();

        // Initialize graph properties
        StdDraw.setCanvasSize(1000, 500); // Set Canvas size
        StdDraw.setPenRadius(0.002);            // Initial Pen radius
        StdDraw.setPenColor(StdDraw.BLACK);     // Initial Pen colour

        // Timer initialized early to pass to ExitListener
        Timer t = new Timer();

        // Setting up Pump pin listener to generate a sound and light when pump is on
        var PumpListener = new PumpListener(gPump,gLED,gSound);
        gPump.addEventListener(PumpListener);

        // Setting up Exit Button to reset all pins, cancel Timer and disconnect grove board
        var ExitListener = new ExitListener(grove,gButton,gPump,gLED,gSound,OLEDObj,t);
        gButton.addEventListener(ExitListener);

        // Get program start time
        var startTime = System.currentTimeMillis() / 1000.0;

        // TimerTask to constantly check moisture level, update graph, and update OLED screen
        var tTask = new PlantTask(mSense, gPump, OLEDObj, moistPairs, startTime);
        t.schedule(tTask, 0, 1000);
    }
}
