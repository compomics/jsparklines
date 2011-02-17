
package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Contains methods related to calculating color gradients.
 *
 * @author Harald Barsnes
 */
public class GradientColorCoding {

    /**
     * An enumerator of the supported color gradient types.
     * <br><br>
     * Values below zero uses the first color in the gradient name, while values
     * above zero uses the third color in the gradient.
     */
    public enum ColorGradient {

        redBlackGreen, greenBlackRed, greenBlackBlue, blueBlackGreen, greenBlackYellow,
        yellowBlackGreen, greenBlackPurple, purpleBlackGreen, redBlackMagenta, magentaBlackRed,
        greenWhiteRed, redWhiteGreen, blueWhiteRed, redWhiteBlue, blueBlackRed, redBlackBlue,
        greenWhiteBlue, blueWhiteGreen
    }

     /**
     * Returns the gradient color using the currently selected color gradient.
     * Values below zero uses the first color in the gradient, while values
     * above zero uses the second color in the gradient. If the column contains
     * only positive values only the second color will be used.
     *
     * @param value the value to find the gradient color for
     * @return      the gradient color
     */
    public static Color findGradientColor(Double value, Double minValue, Double maxValue, ColorGradient colorGradient) {

        int numberOfColorLevels = 50;
        double distanceBetweenCorrelationLevels = maxValue / (((double) numberOfColorLevels) / 2);

        Color backGroundColor = null;

        // find the color for the values below zero
        for (int i = 0; i < (numberOfColorLevels / 2); i++) {

            // find the lower and upper range for the current color
            Double lowerRange = new Double(-maxValue + (i * distanceBetweenCorrelationLevels));
            Double upperRange = new Double(-maxValue + ((i + maxValue) * distanceBetweenCorrelationLevels));

            Color tempColor = null;

            // calculate the current color
            if (colorGradient == ColorGradient.greenBlackRed) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.redBlackGreen) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (colorGradient == ColorGradient.greenBlackBlue) {
                tempColor = new Color(0, 255 - (i * 10), 50 - (i * 2));
            } else if (colorGradient == ColorGradient.blueBlackGreen) {
                tempColor = new Color(0, 50 - (i * 2), 255 - (i * 10));
            } else if (colorGradient == ColorGradient.greenBlackYellow) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.yellowBlackGreen) {
                tempColor = new Color(255 - 10 * i, 255 - 10 * i, 0);
            } else if (colorGradient == ColorGradient.greenBlackPurple) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.purpleBlackGreen) {
                tempColor = new Color(255 - 10 * i, 0, 255 - 10 * i);
            } else if (colorGradient == ColorGradient.redBlackMagenta) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (colorGradient == ColorGradient.magentaBlackRed) {
                tempColor = new Color(0, 255 - 10 * i, 255 - 10 * i);
            } else if (colorGradient == ColorGradient.greenWhiteRed) {
                tempColor = new Color(15 + 10 * i, 255, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.redWhiteGreen) {
                tempColor = new Color(255, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.blueWhiteRed) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 255);
            } else if (colorGradient == ColorGradient.redWhiteBlue) {
                tempColor = new Color(255, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.blueBlackRed) {
                tempColor = new Color(50 - (i * 2), 0, 255 - (i * 10));
            } else if (colorGradient == ColorGradient.redBlackBlue) {
                tempColor = new Color(255 - (i * 10), 0, 50 - (i * 2));
            } else if (colorGradient == ColorGradient.greenWhiteBlue) {
                tempColor = new Color(15 + 10 * i, 255, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.blueWhiteGreen) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 255);
            }

            // see of the value is in the wanted range
            if (value.doubleValue() >= lowerRange && value.doubleValue() < upperRange) {
                backGroundColor = tempColor;
            }
        }

        // find the color for the values above zero
        for (int i = 0; i < (numberOfColorLevels / 2); i++) {

            // find the lower and upper range for the current color
            Double lowerRange = new Double(0.0 + distanceBetweenCorrelationLevels * i);
            Double upperRange = new Double(0.0 + distanceBetweenCorrelationLevels * (i + maxValue));

            Color tempColor = null;

            // calculate the current color
            if (colorGradient == ColorGradient.greenBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.redBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.greenBlackBlue) {
                tempColor = new Color(0, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.blueBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.greenBlackYellow) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.yellowBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.greenBlackPurple) {
                tempColor = new Color(15 + 10 * i, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.purpleBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.redBlackMagenta) {
                tempColor = new Color(0, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.magentaBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.greenWhiteRed) {
                tempColor = new Color(255, 230 - (10 - 1) * i, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.redWhiteGreen) {
                tempColor = new Color(230 - (10 - 1) * i, 255, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.blueWhiteRed) {
                tempColor = new Color(255, 230 - (10 - 1) * i, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.redWhiteBlue) {
                tempColor = new Color(230 - (10 - 1) * i, 230 - (10 - 1) * i, 255);
            } else if (colorGradient == ColorGradient.blueBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.redBlackBlue) {
                tempColor = new Color(0, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.greenWhiteBlue) {
                tempColor = new Color(230 - (10 - 1) * i, 230 - (10 - 1) * i, 255);
            } else if (colorGradient == ColorGradient.blueWhiteGreen) {
                tempColor = new Color(230 - (10 - 1) * i, 255, 230 - (10 - 1) * i);
            }

            // see of the value is in the wanted range
            if ((value.doubleValue() >= lowerRange && value.doubleValue() < upperRange)
                    || (upperRange.doubleValue() == maxValue && value.doubleValue() == upperRange.doubleValue())) {
                backGroundColor = tempColor;
            }
        }

        // calculate the color for values outside the value range
        if (value.doubleValue() < minValue) {

            // calculate the color for values smaller than the lower range
            if (colorGradient == ColorGradient.greenBlackRed) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.redBlackGreen) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.greenBlackBlue) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.blueBlackGreen) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.greenBlackYellow) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.yellowBlackGreen) {
                backGroundColor = Color.YELLOW;
            } else if (colorGradient == ColorGradient.greenBlackPurple) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.purpleBlackGreen) {
                backGroundColor = new Color(255, 0, 255);
            } else if (colorGradient == ColorGradient.redBlackMagenta) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.magentaBlackRed) {
                backGroundColor = new Color(0, 255, 255);
            } else if (colorGradient == ColorGradient.greenWhiteRed) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.redWhiteGreen) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.blueWhiteRed) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.redWhiteBlue) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.blueBlackRed) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.redBlackBlue) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.greenWhiteBlue) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.blueWhiteGreen) {
                backGroundColor = Color.BLUE;
            }

        } else if (value.doubleValue() > maxValue) {

            // calculate the color for values bigger than the upper range
            if (colorGradient == ColorGradient.greenBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.redBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.greenBlackBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.blueBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.greenBlackYellow) {
                backGroundColor = Color.YELLOW;
            } else if (colorGradient == ColorGradient.yellowBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.greenBlackPurple) {
                backGroundColor = new Color(255, 0, 255);
            } else if (colorGradient == ColorGradient.purpleBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.redBlackMagenta) {
                backGroundColor = new Color(0, 255, 255);
            } else if (colorGradient == ColorGradient.magentaBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.greenWhiteRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.redWhiteGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.blueWhiteRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.redWhiteBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.blueBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.redBlackBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.greenWhiteBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.blueWhiteGreen) {
                backGroundColor = Color.GREEN;
            }
        }

        return backGroundColor;
    }
}
