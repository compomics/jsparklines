package no.uib.jsparklines.extra;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A render that displays icons instead of true or false. Assumes that
 * the cell values are of type Boolean.
 *
 * @author Harald Barsnes
 */
public class TrueFalseIconRenderer implements TableCellRenderer {

    /**
     * The icon to use for the true values.
     */
    private ImageIcon trueIcon;
    /**
     * The icon to use for the false values.
     */
    private ImageIcon falseIcon;
    /**
     * The tooltip to use for the true values.
     */
    private String trueToolTip = null;
    /**
     * The tooltip to use for the false values.
     */
    private String falseToolTip = null;

    /**
     * Creates a new IconRenderer.
     *
     * @param trueIcon the icon to use for cells containing TRUE
     * @param falseIcon the icon to use for cells containing FALSE
     */
    public TrueFalseIconRenderer(ImageIcon trueIcon, ImageIcon falseIcon) {
        this.trueIcon = trueIcon;
        this.falseIcon = falseIcon;
    }
    
    /**
     * Creates a new IconRenderer.
     *
     * @param trueIcon      the icon to use for cells containing TRUE
     * @param falseIcon     the icon to use for cells containing FALSE
     * @param trueToolTip   the tooltip to use for the true values
     * @param falseToolTip  the tooltip to use for the false values
     */
    public TrueFalseIconRenderer(ImageIcon trueIcon, ImageIcon falseIcon, String trueToolTip, String falseToolTip) {
        this.trueIcon = trueIcon;
        this.falseIcon = falseIcon;
        this.trueToolTip = trueToolTip;
        this.falseToolTip = falseToolTip;
    }

    /**
     * Sets up the cell renderer for the given component.
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return the rendered cell
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        Color bg = label.getBackground();
        // We have to create a new color object because Nimbus returns
        // a color of type DerivedColor, which behaves strange, not sure why.
        label.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

        // set the icon to use for the boolean values
        if (value instanceof Boolean) {
            
            label.setText(null);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            if ((Boolean) value == true) {
                label.setIcon(trueIcon);
                label.setToolTipText(trueToolTip);
            } else {
                label.setIcon(falseIcon);
                label.setToolTipText(falseToolTip);
            }
        }

        return label;
    }
}
