package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Includes general help methods that are used by the other classes.
 *
 * @author Harald Barsnes
 */
public class Util {

    /**
     * Rounds a double value to the wanted number of decimal places.
     *
     * @param d the double to round of
     * @param places number of decimal places wanted
     * @return double - the new double
     */
    public static double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10, (double) places);
    }

    /**
     * Converts a color to hex format for use in HTML tags.
     *
     * @param color the color to convert
     * @return the color in hex format
     */
    public static String color2Hex(Color color) {
        return Integer.toHexString(color.getRGB() & 0x00ffffff);
    }
}
