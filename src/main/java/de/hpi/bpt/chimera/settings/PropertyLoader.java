package de.hpi.bpt.chimera.settings;

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
