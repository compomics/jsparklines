package no.uib.jsparklines.extra;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell render fixing a bug occurring in the Nimbus look and feel with
 * alternating row color coding. Using this cell renderer makes sure that the
 * correct background color is used. Supported input: Boolean objects. Other
 * object types are rendered using the DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class NimbusCheckBoxRenderer extends DefaultTableCellRenderer {

    /**
     * The default JCheckBox renderer.
     */
    private JCheckBox renderer;

    /**
     * Creates a new NimbusCheckBoxRenderer.
     */
    public NimbusCheckBoxRenderer() {
        renderer = new JCheckBox();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && value instanceof Boolean) {

            Boolean b = (Boolean) value;
            renderer.setSelected(b);
            renderer.setOpaque(true);

            if (isSelected) {
                renderer.setForeground(table.getSelectionForeground());
                renderer.setBackground(table.getSelectionBackground());
            } else {
                Color bg = getBackground();
                renderer.setForeground(getForeground());

                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure why.
                renderer.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            }

            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            return renderer;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
