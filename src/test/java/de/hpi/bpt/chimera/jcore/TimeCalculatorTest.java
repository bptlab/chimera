package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.jcore.executionbehaviors.TimeCalculator;
import org.junit.Test;

import java.util.Date;

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