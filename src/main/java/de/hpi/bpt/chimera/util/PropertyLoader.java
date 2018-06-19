package de.hpi.bpt.chimera.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for accessing Properties from resources/config.properties.
 */
public final class PropertyLoader {


	public static final String PROPERTIES_FILE = "config.properties";
	private static final Logger log = Logger.getLogger(PropertyLoader.class);
	private static Properties props = new Properties();

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
			props.load(is);
		} catch (IOException e) {
			log.error("Property file not found! - " + e.getMessage(), e);
		}
	}

	private PropertyLoader() {
	}

	/**
	 * Read a property.
	 *
	 * @param key Key for the property to be returned
	 * @return The property
	 */
	public static String getProperty(String key) {
		if (!props.containsKey(key)) {
			String errorMsg = String.format("Property %s was not present in property file", key);
			throw new IllegalArgumentException(errorMsg);
		}
		return props.getProperty(key);
	}
	
	/**
	 * Reads a property that should contain an Integer value. 
	 * If the value is empty or cannot be parsed, this is logged and 0 is returned. 
	 * @param key
	 * @return the int value of the property
	 */
	public static int getIntProperty(String key) {
		String propertyValue = getProperty(key);
		if (! propertyValue.isEmpty()) {
			try {
				return Integer.parseInt(propertyValue);
			} catch (NumberFormatException e) {
				log.error(String.format("Parsing property value of property %s failed (was %s).", key, propertyValue), e);
				return 0;
			}
		}
		log.warn(String.format("Value of property %s is not set, treating it as 0", key));
		return 0;
	}

	/**
	 * Changing Properties for tests, e.g. the unicorn server url.
	 *
	 * @param key   Key for the property to be changed
	 * @param value New value it is changed to
	 */
	public static void setProperty(String key, String value) {
		if (!props.containsKey(key)) {
			String errorMsg = String.format("Property %s was not present in property file", key);
			throw new IllegalArgumentException(errorMsg);
		}
		props.setProperty(key, value);
	}

}
