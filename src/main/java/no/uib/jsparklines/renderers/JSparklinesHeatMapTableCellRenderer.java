package no.uib.jsparklines.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Table cell renderer displaying JSparklinesDataSeries as heat maps where the
 * double values are used the color coding. Supported input:
 * JSparklinesDataSeries objects. Other object types are rendered using the
 * DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesHeatMapTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * The background color, if null the row color is used.
     */
    private Color backgroundColor;

    /**
     * Turns of the gradient painting for the bar charts.
     */
    static {
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
    }
    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The maximum absolute value. Used to set the upper range for the heat map
     * color.
     */
    private double maxValue;
    /**
     * The currently selected color gradient.
     */
    private GradientColorCoding.ColorGradient currentColorGradient = GradientColorCoding.ColorGradient.RedBlackBlue;
    /**
     * The first color of the gradient is used for values close to the min
     * value, while the third color of the gradient is used for values close to
     * the max value. If only positive values are expected
     * (positiveColorGradient is true) the gradient color is used for the
     * halfway point between the min and max values. If both positive and
     * negative values are expected (positiveColorGradient is false) the middle
     * gradient color is used for values around zero.
     */
    private boolean positiveColorGradient = false;

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when
     * creating pie charts where no upper range is used.
     *
     * @param currentColorGradient the color gradient
     * @param maxValue the maximum heat map value
     */
    public JSparklinesHeatMapTableCellRenderer(GradientColorCoding.ColorGradient currentColorGradient, double maxValue) {

        this.currentColorGradient = currentColorGradient;
        this.maxValue = maxValue;

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(chartPanel);
    }

    /**
     * Set the maximum value.
     *
     * @param maxValue the maximum value
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Returns a reference to the chart panel.
     *
     * @return the chart panel.
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Set the background color. If not set the background color of the given
     * row will be used.
     *
     * @param color the new background color
     */
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    /**
     * Set the color gradient to use for the bars. <br><br> The first color of
     * the gradient is used for values close to the min value, while the third
     * color of the gradient is used for values close to the max value. If only
     * positive values are expected (positiveColorGradient is true) the middle
     * gradient color is used for the halfway point between the min and max
     * values. If both positive and negative values are expected
     * (positiveColorGradient is false) the middle gradient color is used for
     * values around zero.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     * @param positiveColorGradient if true only positive values are expected
     * and the middle gradient color is used for the halfway point between the
     * min and max values, if false the middle gradient color is used for values
     * around zero
     * @param plotBackgroundColor the background color to use, for gradients
     * using white as the "middle" color, it's recommended to use a dark
     * background color
     */
    public void setGradientColoring(GradientColorCoding.ColorGradient colorGradient, boolean positiveColorGradient, Color plotBackgroundColor) {
        this.positiveColorGradient = positiveColorGradient;
        if (plotBackgroundColor != null) {
            this.backgroundColor = plotBackgroundColor;
        }
        this.currentColorGradient = colorGradient;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());

        if (backgroundColor != null) {
            setBackground(backgroundColor);
        } else {
            setBackground(c.getBackground());
        }

        // check for valid object type
        if (value == null || !(value instanceof JSparklinesDataSeries)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // get the dataset
        JSparklinesDataSeries dataSeries = (JSparklinesDataSeries) value;

        StringBuilder tooltip = new StringBuilder(); // @TODO: add tooltip?
        tooltip.append("<html>");

        DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setShadowVisible(false);

        for (int i = 0; i < dataSeries.getData().size(); i++) {
            barChartDataset.addValue(1.0, "" + i, "" + i);
            Color currentColor = GradientColorCoding.findGradientColor(dataSeries.getData().get(i), -maxValue, maxValue, currentColorGradient, positiveColorGradient);
            renderer.setSeriesPaint(i, currentColor);
        }

        chart = ChartFactory.createStackedBarChart(null, null, null, barChartDataset, PlotOrientation.VERTICAL, false, false, false);

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // remove space before/after the domain axis
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);

        // remove space before/after the range axis
        plot.getRangeAxis().setUpperMargin(0);
        plot.getRangeAxis().setLowerMargin(0);

        // add the dataset
        plot.setDataset(barChartDataset);

        // hide unwanted chart details
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        // set up the chart renderer
        plot.setRenderer(0, renderer);

        // set the tooltip
        if (!tooltip.toString().equalsIgnoreCase("<html>")) {
            setToolTipText(tooltip.append("</html>").toString());
        } else {
            setToolTipText(null);
        }

        // hide the outline
        chart.getPlot().setOutlineVisible(false);

        // make sure the background is the same as the table row color
        if (backgroundColor != null) {
            chart.getPlot().setBackgroundPaint(backgroundColor);
            chart.setBackgroundPaint(backgroundColor);
        } else {
            chart.getPlot().setBackgroundPaint(c.getBackground());
            chart.setBackgroundPaint(c.getBackground());
        }

        // create the chart panel and add it to the table cell
        chartPanel = new ChartPanel(chart);

        if (backgroundColor != null) {
            chartPanel.setBackground(backgroundColor);
        } else {
            chartPanel.setBackground(c.getBackground());
        }

        this.removeAll();
        this.add(chartPanel);

        return this;
    }
}
