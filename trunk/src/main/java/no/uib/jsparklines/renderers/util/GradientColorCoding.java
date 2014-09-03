package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Contains methods related to calculating color gradients.
 *
 * @author Harald Barsnes
 * @author Lennart Martens
 */
public class GradientColorCoding {

    /**
     * List of supported color gradient types. <br><br> Values below zero uses
     * the first color in the gradient name, while values above zero uses the
     * third color in the gradient.
     */
    public enum ColorGradient {

        /**
         * Red via black to green.
         */
        RedBlackGreen, 
        /**
         * Green via black to red.
         */
        GreenBlackRed, 
        /**
         * Green via black to blue.
         */
        GreenBlackBlue, 
        /**
         * Blue via black to green.
         */
        BlueBlackGreen, 
        /**
         * Green via black to yellow.
         */
        GreenBlackYellow,
        /**
         * Yellow via black to green.
         */
        YellowBlackGreen, 
        /**
         * Green via black to purple.
         */
        GreenBlackPurple, 
        /**
         * Purple via black to green.
         */
        PurpleBlackGreen, 
        /**
         * Red via black to magenta.
         */
        RedBlackMagenta, 
        /**
         * Magenta via black to red.
         */
        MagentaBlackRed,
        /**
         * Blue via black to red.
         */
        BlueBlackRed, 
        /**
         * Red via black to blue.
         */
        RedBlackBlue, 
        /**
         * Green via white to red.
         */
        GreenWhiteRed, 
        /**
         * Red via white to green.
         */
        RedWhiteGreen, 
        /**
         * Blue via white to red.
         */
        BlueWhiteRed, 
        /**
         * Red via white to blue.
         */
        RedWhiteBlue,
        /**
         * Green via white to blue.
         */
        GreenWhiteBlue, 
        /**
         * Blue via white to green.
         */
        BlueWhiteGreen
    }

    /**
     * Returns the gradient color using the currently selected color gradient.
     * The first color of the gradient is used for values close to the min
     * value, while the third color of the gradient is used for values close to
     * the max value. If only positive values are expected (positiveValuesOnly
     * is true) the middle gradient color is used for the halfway point between
     * the min and max values. If both positive and negative values are expected
     * (positiveValuesOnmiddlely is false) the middle gradient color is used for
     * values around zero.
     *
     * @param aValue the value to find the gradient color for
     * @param aMinValue the min value
     * @param aMaxValue the max value
     * @param colorGradient the color gradient to use
     * @param positiveValuesOnly if true only positive values are expected and
     * the middle gradient color is used for the halfway point between the min
     * and max values, if false the middle gradient color is used for values
     * around zero
     * @return the gradient color
     */
    public static Color findGradientColor(Double aValue, Double aMinValue, Double aMaxValue, ColorGradient colorGradient, boolean positiveValuesOnly) {

        Double minValue, maxValue, value;

        // check whether the current color gradient should only take into account positive values
        if (positiveValuesOnly) {
            double range = aMaxValue - aMinValue;
            double half = (range / 2) + aMinValue;
            minValue = aMinValue - half;
            maxValue = aMaxValue - half;
            value = aValue - half;
            colorGradient = ColorGradient.BlueWhiteRed;
        } else {
            minValue = aMinValue;
            maxValue = aMaxValue;
            value = aValue;
        }

        int numberOfColorLevels = 50;
        double distanceBetweenCorrelationLevels = maxValue / (((double) numberOfColorLevels) / 2);

        Color backGroundColor = null;

        // find the color for the values below zero
        for (int i = 0; i < (numberOfColorLevels / 2); i++) {

            // find the lower and upper range for the current color
            Double lowerRange = new Double(-maxValue + (i * distanceBetweenCorrelationLevels));
            Double upperRange = new Double(-maxValue + ((i + 1) * distanceBetweenCorrelationLevels));

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
            Double upperRange = new Double(0.0 + distanceBetweenCorrelationLevels * (i + 1));

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
