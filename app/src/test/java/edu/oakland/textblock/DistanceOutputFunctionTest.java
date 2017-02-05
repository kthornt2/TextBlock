package edu.oakland.textblock;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;

import java.util.Arrays;import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class DistanceOutputFunctionTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0,"0.0 miles"}, { 10,"0.01 miles"}, {100,"0.06 miles"}
        });
    }

    private double fInput;

    private String fExpected;

    public DistanceOutputFunctionTest(double input, String expected) {
        fInput= input;
        fExpected= expected;
    }

    @Test
    public void test() {
        GpsDataHandler gpsDataHandler = new GpsDataHandler();
        assertEquals(fExpected,gpsDataHandler.outputDistanceFunction(fInput));
    }
}