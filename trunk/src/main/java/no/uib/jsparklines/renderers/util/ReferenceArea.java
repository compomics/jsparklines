package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Reference area that can be added to a chart.
 *
 * @author Harald Barsnes.
 */
public class ReferenceArea {

    /**
     * The reference label.
     */
    private String label;
    /**
     * The start of the reference area.
     */
    private double start;
    /**
     * The end of the reference area.
     */
    private double end;
    /**
     * The color of the reference area.
     */
    private Color areaColor;
    /**
     * The alpha level of the reference area.
     */
    private float alpha;

    /**
     * Creates a new ReferenceArea.
     *
     * @param label the reference label
     * @param start the start of the reference area
     * @param end the end of the reference area
     * @param areaColor the color of the reference area
     * @param alpha the alpha level of the reference area
     * @throws IllegalArgumentException alpha must be in the range 0.0f to 1.0f
     */
    public ReferenceArea(String label, double start, double end, Color areaColor, float alpha) throws IllegalArgumentException {
        this.label = label;
        this.start = start;
        this.end = end;
        this.areaColor = areaColor;

        // check the validity of alpha
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("The alpha transparency must be in the range 0.0f to 1.0f!");
        } else {
            this.alpha = alpha;
        }
    }

    /**
     * Returns the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the start value.
     *
     * @return the start
     */
    public double getStart() {
        return start;
    }

    /**
     * Set the start value.
     *
     * @param start the start to set
     */
    public void setStart(double start) {
        this.start = start;
    }

    /**
     * Get the end value.
     *
     * @return the end
     */
    public double getEnd() {
        return end;
    }

    /**
     * Set the end value.
     *
     * @param end the end to set
     */
    public void setEnd(double end) {
        this.end = end;
    }

    /**
     * Get the area color.
     *
     * @return the areaColor
     */
    public Color getAreaColor() {
        return areaColor;
    }

    /**
     * Set the area color.
     *
     * @param areaColor the areaColor to set
     */
    public void setAreaColor(Color areaColor) {
        this.areaColor = areaColor;
    }

    /**
     * Get the alpha level.
     *
     * @return the alpha level
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Set the alpha level.
     *
     * @param alpha the alpha level to set
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
