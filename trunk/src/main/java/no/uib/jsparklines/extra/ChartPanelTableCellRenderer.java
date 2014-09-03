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
 * Table cell renderer for generic org.jfree.chart.ChartPanel objects.
 *
 * @author Harald Barsnes
 */
public class ChartPanelTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * Create a new ChartPanelTableCellRenderer.
     */
    public ChartPanelTableCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
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
