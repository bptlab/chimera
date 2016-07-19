package de.hpi.bpt.chimera.settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for accessing Properties from resources/config.properties.
 */
public final class PropertyLoader {

	private PropertyLoader() { }

	/**
	 * Name of properties file.
	 */
	public static final String PROPERTIES_FILE = "config.properties";
	/**
	 * Folder for properties file.
	 */
	public static final String PROPERTIES_FOLDER = "resources";
	private static Properties props = new Properties();

	//TODO should an exception be thrown if the property file could not be read?
	static {
		String path = PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE;

		InputStream is = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			is = classLoader.getResourceAsStream(PROPERTIES_FILE);
			props.load(is);
		} catch (IOException e) {
			System.err.println("Could not read configuration file " + path);
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 * @param key Key for the Property to be returned
	 * @return the property
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
	 * @param key
	 * @param value
     */
	public static void setProperty(String key, String value) {
		if (!props.containsKey(key)) {
			String errorMsg = String.format("Property %s was not present in property file", key);
			throw new IllegalArgumentException(errorMsg);
		}
		props.setProperty(key, value);
	}

}
