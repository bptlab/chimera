package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 *
 */
public class TimeCalculatorTest {
    @Test
    public void testTimeAdding() {
        Date now = new Date();
        String interval = "PT1M30S";
        TimeCalculator calculator = new TimeCalculator();
        Date newDate = calculator.getDatePlusInterval(now, interval);

        // TODO test exact time
        assert(newDate.after(now));
    }
}