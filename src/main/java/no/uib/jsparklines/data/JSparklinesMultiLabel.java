package no.uib.jsparklines.data;

import java.awt.Color;
import java.io.Serializable;

/**
 * Color labels for use with the JSparklinesMultiLabelTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesMultiLabel implements Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = -8630809835909760166L;
    /**
     * The label, used as tooltip.
     */
    private String label;
    /**
     * The label color.
     */
    private Color color;

    /**
     * Creates a new JSparklinesMultiLabel.
     *
     * @param label the label to use as a tooltip
     * @param color the color for the label
     */
    public JSparklinesMultiLabel(String label, Color color) {
        this.label = label;
        this.color = color;
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
     * The label to set.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * The color to set.
     *
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
