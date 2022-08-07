package org.tudalgo.algoutils.student.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;

/**
 * A utility class for reading properties from files located in {@code src/main/resources/}.
 */
public final class PropertyUtils {

    private PropertyUtils() {
    }

    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();

    /**
     * Returns the {@link Properties} for the file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the path to the property file
     * @return the properties
     */
    public static Properties getProperties(String path) {
        return PROPERTIES_MAP.computeIfAbsent(path, fn -> {
            var properties = new Properties();
            var stream = PropertyUtils.class.getResourceAsStream("/" + fn);
            try {
                properties.load(stream);
            } catch (IOException | IllegalArgumentException | NullPointerException e) {
                throw new RuntimeException(format("cannot load properties from file %s", path), e);
            }
            return properties;
        });
    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static String getStringProperty(String path, String key) {
        return getProperties(path).getProperty(key);
    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static boolean getBooleanProperty(String path, String key) {
        var value = getStringProperty(path, key);
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(format("cannot parse boolean from %s", value), e);
        }
    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static char getCharProperty(String path, String key) {
        var value = getStringProperty(path, key);
        if (value.length() > 1) {
            throw new RuntimeException(format("cannot parse char from %s", value));
        }
        return value.charAt(0);

    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static double getDoubleProperty(String path, String key) {
        var value = getStringProperty(path, key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(format("cannot parse double from %s", value), e);
        }
    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static int getIntProperty(String path, String key) {
        var value = getStringProperty(path, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(format("cannot parse int from %s", value), e);
        }
    }

    /**
     * Returns the value of property {@code key} in file specified by {@code path} located in {@code src/main/resources/}.
     *
     * @param path the name of the properties file
     * @param key  the name of the property
     * @return the value of the property
     */
    public static long getLongProperty(String path, String key) {
        var value = getStringProperty(path, key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(format("cannot parse long from %s", value), e);
        }
    }
}
