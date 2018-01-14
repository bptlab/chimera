package de.hpi.bpt.chimera.execution.event.behavior;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.util.Date;

/**
 *
 */
public class TimeCalculator {

	// For now supports dates in form of PT1M30S
	public Date getDatePlusInterval(Date start, String timeInterval) {
		//
		Date beginning = new Date(start.getTime());
		try {
			Duration duration = DatatypeFactory.newInstance().newDuration(timeInterval);
			duration.addTo(beginning);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Time calculation failed.");
		}
		return beginning;
	}
}
