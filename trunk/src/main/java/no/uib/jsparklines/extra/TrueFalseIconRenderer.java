package no.uib.jsparklines.extra;

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
     * A reference to a standard table cell renderer.
     */
    private TableCellRenderer delegate;
    /**
     * The icon to use for the true values.
     */
    private ImageIcon trueIcon;
    /**
     * The icon to use for the false values.
     */
    private ImageIcon falseIcon;

    /**
     * Creates a new IconRenderer.
     *
     * @param trueIcon the icon to use for cells containing TRUE
     * @param falseIcon the icon to use for cells containing FALSE
     */
    public TrueFalseIconRenderer(ImageIcon trueIcon, ImageIcon falseIcon) {
        this.delegate = new DefaultTableCellRenderer();
        this.trueIcon = trueIcon;
        this.falseIcon = falseIcon;
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

        JLabel label = (JLabel) delegate.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // set the icon to use for the boolean values
        if (value instanceof Boolean) {
            
            label.setText(null);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            if ((Boolean) value == true) {
                label.setIcon(trueIcon);
            } else {
                label.setIcon(falseIcon);
            }
        }

        return label;
    }
}
