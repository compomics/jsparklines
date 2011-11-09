package no.uib.jsparklines.data;

import java.awt.Color;

/**
 * Color labels for use with the JSparklinesMultiLabelTableCellRenderer.
 * 
 * @author Harald Barsnes
 */
public class JSparklinesMultiLabel {

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
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
