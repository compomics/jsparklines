package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Contains methods related to calculating color gradients.
 *
 * @author Harald Barsnes
 */
public class GradientColorCoding {

    /**
     * An enumerator of the supported color gradient types. <br><br> Values
     * below zero uses the first color in the gradient name, while values above
     * zero uses the third color in the gradient.
     */
    public enum ColorGradient {

        RedBlackGreen, GreenBlackRed, GreenBlackBlue, BlueBlackGreen, GreenBlackYellow,
        YellowBlackGreen, GreenBlackPurple, PurpleBlackGreen, RedBlackMagenta, MagentaBlackRed,
        BlueBlackRed, RedBlackBlue, GreenWhiteRed, RedWhiteGreen, BlueWhiteRed, RedWhiteBlue,
        GreenWhiteBlue, BlueWhiteGreen
    }

    /**
     * Returns the gradient color using the currently selected color gradient.
     * Values below zero uses the first color in the gradient, while values
     * above zero uses the second color in the gradient. If the column contains
     * only positive values only the second color will be used.
     *
     * @param value the value to find the gradient color for
     * @param minValue the min value
     * @param maxValue the max value
     * @param colorGradient the color gradient to use
     * @return the gradient color
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
            if (colorGradient == ColorGradient.GreenBlackRed) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.RedBlackGreen) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (colorGradient == ColorGradient.GreenBlackBlue) {
                tempColor = new Color(0, 255 - (i * 10), 50 - (i * 2));
            } else if (colorGradient == ColorGradient.BlueBlackGreen) {
                tempColor = new Color(0, 50 - (i * 2), 255 - (i * 10));
            } else if (colorGradient == ColorGradient.GreenBlackYellow) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.YellowBlackGreen) {
                tempColor = new Color(255 - 10 * i, 255 - 10 * i, 0);
            } else if (colorGradient == ColorGradient.GreenBlackPurple) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (colorGradient == ColorGradient.PurpleBlackGreen) {
                tempColor = new Color(255 - 10 * i, 0, 255 - 10 * i);
            } else if (colorGradient == ColorGradient.RedBlackMagenta) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (colorGradient == ColorGradient.MagentaBlackRed) {
                tempColor = new Color(0, 255 - 10 * i, 255 - 10 * i);
            } else if (colorGradient == ColorGradient.GreenWhiteRed) {
                tempColor = new Color(15 + 10 * i, 255, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.RedWhiteGreen) {
                tempColor = new Color(255, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.BlueWhiteRed) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 255);
            } else if (colorGradient == ColorGradient.RedWhiteBlue) {
                tempColor = new Color(255, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.BlueBlackRed) {
                tempColor = new Color(50 - (i * 2), 0, 255 - (i * 10));
            } else if (colorGradient == ColorGradient.RedBlackBlue) {
                tempColor = new Color(255 - (i * 10), 0, 50 - (i * 2));
            } else if (colorGradient == ColorGradient.GreenWhiteBlue) {
                tempColor = new Color(15 + 10 * i, 255, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.BlueWhiteGreen) {
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
            if (colorGradient == ColorGradient.GreenBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.RedBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.GreenBlackBlue) {
                tempColor = new Color(0, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.BlueBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.GreenBlackYellow) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.YellowBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.GreenBlackPurple) {
                tempColor = new Color(15 + 10 * i, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.PurpleBlackGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (colorGradient == ColorGradient.RedBlackMagenta) {
                tempColor = new Color(0, 15 + 10 * i, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.MagentaBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.GreenWhiteRed) {
                tempColor = new Color(255, 230 - (10 - 1) * i, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.RedWhiteGreen) {
                tempColor = new Color(230 - (10 - 1) * i, 255, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.BlueWhiteRed) {
                tempColor = new Color(255, 230 - (10 - 1) * i, 230 - (10 - 1) * i);
            } else if (colorGradient == ColorGradient.RedWhiteBlue) {
                tempColor = new Color(230 - (10 - 1) * i, 230 - (10 - 1) * i, 255);
            } else if (colorGradient == ColorGradient.BlueBlackRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (colorGradient == ColorGradient.RedBlackBlue) {
                tempColor = new Color(0, 0, 15 + 10 * i);
            } else if (colorGradient == ColorGradient.GreenWhiteBlue) {
                tempColor = new Color(230 - (10 - 1) * i, 230 - (10 - 1) * i, 255);
            } else if (colorGradient == ColorGradient.BlueWhiteGreen) {
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
            if (colorGradient == ColorGradient.GreenBlackRed) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.RedBlackGreen) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.GreenBlackBlue) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.BlueBlackGreen) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.GreenBlackYellow) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.YellowBlackGreen) {
                backGroundColor = Color.YELLOW;
            } else if (colorGradient == ColorGradient.GreenBlackPurple) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.PurpleBlackGreen) {
                backGroundColor = new Color(255, 0, 255);
            } else if (colorGradient == ColorGradient.RedBlackMagenta) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.MagentaBlackRed) {
                backGroundColor = new Color(0, 255, 255);
            } else if (colorGradient == ColorGradient.GreenWhiteRed) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.RedWhiteGreen) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.BlueWhiteRed) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.RedWhiteBlue) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.BlueBlackRed) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.RedBlackBlue) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.GreenWhiteBlue) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.BlueWhiteGreen) {
                backGroundColor = Color.BLUE;
            }

        } else if (value.doubleValue() > maxValue) {

            // calculate the color for values bigger than the upper range
            if (colorGradient == ColorGradient.GreenBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.RedBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.GreenBlackBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.BlueBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.GreenBlackYellow) {
                backGroundColor = Color.YELLOW;
            } else if (colorGradient == ColorGradient.YellowBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.GreenBlackPurple) {
                backGroundColor = new Color(255, 0, 255);
            } else if (colorGradient == ColorGradient.PurpleBlackGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.RedBlackMagenta) {
                backGroundColor = new Color(0, 255, 255);
            } else if (colorGradient == ColorGradient.MagentaBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.GreenWhiteRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.RedWhiteGreen) {
                backGroundColor = Color.GREEN;
            } else if (colorGradient == ColorGradient.BlueWhiteRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.RedWhiteBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.BlueBlackRed) {
                backGroundColor = Color.RED;
            } else if (colorGradient == ColorGradient.RedBlackBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.GreenWhiteBlue) {
                backGroundColor = Color.BLUE;
            } else if (colorGradient == ColorGradient.BlueWhiteGreen) {
                backGroundColor = Color.GREEN;
            }
        }

        return backGroundColor;
    }
}
