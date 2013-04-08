package no.uib.jsparklines.extra;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartPanel;

/**
 * A table cell renderer for a ChartPanel.
 *
 * @author Harald Barsnes
 */
public class ChartPanelTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * A reference to a standard table cell renderer.
     */
    private TableCellRenderer delegate = new DefaultTableCellRenderer();

    /**
     * Create a new ChartPanelTableCellRenderer.
     */
    public ChartPanelTableCellRenderer() {
    }

    /**
     * Sets up the cell renderer for the given cell.
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return the rendered cell
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) delegate.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        if (value != null && value instanceof ChartPanel) {

            ChartPanel chartPanel = (ChartPanel) value;

            // respect cell highlighting
            Color bg = c.getBackground();
            chartPanel.getChart().getPlot().setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.getChart().setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));


            // add border when cell is selected
            //        if (hasFocus) {
            //            chartPanel.setBorder(c.getBorder());
            //        } else {
            //            chartPanel.setBorder(null);
            //        }

            return chartPanel;
        } else {
            return c;
        }
    }
}
