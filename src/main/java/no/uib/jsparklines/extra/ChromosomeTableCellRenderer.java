package no.uib.jsparklines.extra;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.Chromosome;

/**
 * Table cell renderer for chromosome objects, i.e., 1-n and X, Y, Z and W.
 * Supported input: Chromosome objects. Other object types are rendered using
 * the DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class ChromosomeTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * The font color to use for the selected cells.
     */
    private Color selectedFontColor;
    /**
     * The font color to use for the selected cells.
     */
    private Color notSelectedFontColor;
    
    /**
     * Create a new ChartPanelTableCellRenderer.
     * 
     * @param selectedFontColor font color to use for the selected cells
     * @param notSelectedFontColor font color to use for the selected cells
     */
    public ChromosomeTableCellRenderer(Color selectedFontColor, Color notSelectedFontColor) {
        this.selectedFontColor = selectedFontColor;
        this.notSelectedFontColor = notSelectedFontColor;
    }
    
    /**
     * Set the font colors.
     * 
     * @param selectedFontColor font color to use for the selected cells
     * @param notSelectedFontColor font color to use for the selected cells
     */
    public void setFontColors(Color selectedFontColor, Color notSelectedFontColor) {
        this.selectedFontColor = selectedFontColor;
        this.notSelectedFontColor = notSelectedFontColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        JLabel valueLabel = new JLabel();
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));

        // respect focus and hightlighting
        Color bg = c.getBackground();

        valueLabel.setBorder(c.getBorder());
        valueLabel.setOpaque(c.isOpaque());
        valueLabel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
  
        // update the link color depending on if the row is selected or not
        if (isSelected) {
            valueLabel.setForeground(selectedFontColor);
        } else {
            valueLabel.setForeground(notSelectedFontColor);
        }

        if (value != null && value instanceof Chromosome) {
            valueLabel.setText("" + ((Chromosome) value).toString());
        }

        return valueLabel;
    }
}
