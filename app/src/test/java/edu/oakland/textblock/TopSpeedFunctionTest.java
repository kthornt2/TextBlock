package edu.oakland.textblock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TopSpeedFunctionTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0,"0.0 mph"}, {1.11111111, "1.11 mph"}, {1.12211212121212, "1.12 mph"}, {100, "100.0 mph"},
                {.666, "0.67 mph"}});
    }

    private double mSpeedInput;

    private String mSpeedExpected;

    public TopSpeedFunctionTest(double input, String expected) {
        mSpeedInput= input;
        mSpeedExpected= expected;
    }

    @Test
    public void test() {
        GpsDataHandler gpsDataHandler = new GpsDataHandler();
        assertEquals(mSpeedExpected,gpsDataHandler.topSpeedFunction(mSpeedInput));
    }
}