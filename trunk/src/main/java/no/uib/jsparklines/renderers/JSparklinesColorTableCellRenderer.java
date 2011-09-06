package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A renderer for displaying colored equal size bar charts inside a table cell. 
 * Assumes that the cell values are of type Color.
 *
 * @author Harald Barsnes
 */
public class JSparklinesColorTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The background color used for the plots. For plots using light
     * colors, it's recommended to use a dark background color, and for
     * plots using darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;
    /**
     * The color tooltip mappings.
     */
    private HashMap<Color, String> tooltips;

    /**
     * Creates a new JSparklinesColorTableCellRenderer.
     */
    public JSparklinesColorTableCellRenderer() {
        this.tooltips = new HashMap<Color, String>();
        setUpRendererAndChart();
    }
    
    /**
     * Creates a new JSparklinesColorTableCellRenderer.
     *
     * @param tooltips      a HashMap with the integer to tooltip mappings 
     */
    public JSparklinesColorTableCellRenderer(HashMap<Color, String> tooltips) {
        this.tooltips = tooltips;
        setUpRendererAndChart();
    }

    /**
     * Sets up the table cell renderer and the bar chart.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart() {

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(chartPanel);
    }

    /**
     * Set the plot background color.
     *
     * @param plotBackgroundColor
     */
    public void setBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
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

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value == null) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        if (!(value instanceof Color)) {
            throw new IllegalArgumentException("JSparklinesColorTableCellRenderer only supports Color objects!");
        }

        // set the tooltip text
        if (tooltips.get((Color) value) != null) {
            this.setToolTipText(tooltips.get((Color) value));
        } else {
            this.setToolTipText(null);
        }

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(new Integer(1), "1", "1");
        
        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // add the dataset
        plot.setDataset(dataset);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // set up the chart renderer
        CategoryItemRenderer renderer = new BarChartColorRenderer((Color) value);


        // make sure the background is the same as the table row color
        if (plotBackgroundColor != null && !isSelected) {
            plot.setBackgroundPaint(plotBackgroundColor);
            chartPanel.setBackground(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {

//            // handle the special case with Nimbus LAF and alternating colors
//            if (UIManager.getLookAndFeel().getName().equalsIgnoreCase("Nimbus") && isSelected) {
//                renderer = new BarChartColorRenderer(Color.WHITE);
//            }

            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }

        plot.setRenderer(renderer);

        return this;
    }
}
