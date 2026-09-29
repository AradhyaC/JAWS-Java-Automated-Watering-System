package ca.jaws;

import org.junit.*;

import java.util.Random;

/*
 * AUTHOR:   Aradhya Chawla
 * Function: Unit testing to test conversion, bounds, and randomized cases
 * */

public class UnitTesting {
    //Experimentally acquired sensor values for soil moisture ~~~
    private final static int    AIR =           400;//          |
    private final static int    DRY_SOIL =      370;//          |
    private final static int    THRESHOLD =     330;//          |
    private final static int    SATURATED =     280;//          |
    private final static int    WATER =         260;//          |
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void TestValueConversion(){
        // Testing conversion of raw values measured from sensor
       Assert.assertArrayEquals( "ERROR: Values not converting correctly",
               new int[]{0, 22, 50, 86, 100},
               new int[]{
                       moisturePercentage.getMoisturePercentage(AIR),
                       moisturePercentage.getMoisturePercentage(DRY_SOIL),
                       moisturePercentage.getMoisturePercentage(THRESHOLD),
                       moisturePercentage.getMoisturePercentage(SATURATED),
                       moisturePercentage.getMoisturePercentage(WATER)
               }
       );
    }

    @Test
    public void TestBounds(){
        // Testing Lower bound (any value above AIR = 400)
        Assert.assertEquals(
                "ERROR: Lower Bound not correctly set",
                0,
                moisturePercentage.getMoisturePercentage(450)
        );

        // Testing Lower bound (any value below WATER = 260)
        Assert.assertEquals(
                "ERROR: Upper Bound not correctly set",
                100,
                moisturePercentage.getMoisturePercentage(200)
        );
    }

    @Test
    public void TestRandom(){
        // Testing 100 random values (should always default to a value between 0 and 100)
        for (int i = 0; i < 100; i++) {
            var randomVal = moisturePercentage.getMoisturePercentage(new Random().nextInt());
            Assert.assertTrue(
                    "ERROR: Bounds not correctly set",
                    (randomVal <= 100) && (randomVal >= 0)
                    );
        }
    }
}
