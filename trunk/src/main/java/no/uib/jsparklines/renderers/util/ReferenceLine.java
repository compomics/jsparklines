package no.uib.jsparklines.renderers.util;

import java.awt.Color;

/**
 * Reference line that can be added to a chart.
 *
 * @author Harald Barsnes
 */
public class ReferenceLine {

    /**
     * The label of the reference line.
     */
    private String label;
    /**
     * The value of the reference line.
     */
    private double value;
    /**
     * The width of the line.
     */
    private float lineWidth;
    /**
     * The color of the line.
     */
    private Color lineColor;

    /**
     * Create a new reference line.
     *
     * @param label the label of the reference line
     * @param value the value of the reference line
     * @param lineWidth the line width
     * @param lineColor the line color
     */
    public ReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        this.label = label;
        this.value = value;
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
    }

    /**
     * Return the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Return the line width.
     *
     * @return the lineWidth
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Set the line width,
     *
     * @param lineWidth the lineWidth to set
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Set the line color.
     *
     * @return the lineColor
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Set the line color.
     *
     * @param lineColor the lineColor to set
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
