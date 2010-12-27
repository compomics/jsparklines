package no.uib.jsparklines.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
 * A renderer for displaying a JSparklines bar chart inside a table cell.
 * Assumes that the cell values are of type Double or Integer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesBarChartTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * A reference to a standard table cell renderer.
     */
    private TableCellRenderer delegate;
    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The maximum value. Used to set the maximum range for the chart.
     */
    private double maxValue = 1;
    /**
     * The minimum value. Used to set the minmum range for the chart.
     */
    private double minValue = 0;
    /**
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * The colors to use for the bars with negative numbers.
     */
    private Color negativeValuesColor;
    /**
     * The colors to use for the bars with positive numbers.
     */
    private Color positiveValuesColor;

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when only positive
     * values are to be plotted.
     *
     * @param plotOrientation       the orientation of the plot
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     *                              (this is the same as setting the minimum value to 0)
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, Color positiveValuesColor) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when positive
     * and negative values are to be plotted.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     * @param negativeValuesColor   the color to use for the negative values if two sided data is shown
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor) {

        this.negativeValuesColor = negativeValuesColor;
        this.positiveValuesColor = positiveValuesColor;

        this.maxValue = maxValue;
        this.minValue = minValue;

        delegate = new DefaultTableCellRenderer();
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);
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
     * Set the minimum value.
     *
     * @param minValue the minimum value
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * Set if the underlying numbers or the bar charts are to be shown.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
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

        JComponent c = (JComponent) delegate.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // if show numbers, format as number and return
        if (showNumbers) {

            if (value instanceof Double) {
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, roundDouble(((Double) value).doubleValue(), 2),
                        isSelected, hasFocus, row, column);

                if (Math.abs(new Double("" + value)) < 0.01) {
                    c.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
                }

            } else {
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, (Integer) value,
                        isSelected, hasFocus, row, column);
            }

            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

            return c;
        }

        // set the tooltip text
        if (value instanceof Double) {

            if (Math.abs(new Double("" + value)) < 0.01) {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
            } else {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 2));
            }

        } else {
            this.setToolTipText("" + value);
        }

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (value instanceof Double) {
            if (((Double) value).doubleValue() < 0.01 && ((Double) value).doubleValue() > 0) {
                dataset.addValue(0.01, "1", "1");
            } else {
                dataset.addValue(((Double) value), "1", "1");
            }
        } else {
            dataset.addValue(((Integer) value), "1", "1");
        }

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // set the axis range
        plot.getRangeAxis().setRange(minValue, maxValue);

        // make sure the background is the same as the table row color
        plot.setBackgroundPaint(c.getBackground());
        chartPanel.setBackground(c.getBackground());
        chart.setBackgroundPaint(c.getBackground());

        // add the dataset
        plot.setDataset(dataset);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // set up the chart renderer
        CategoryItemRenderer renderer;

        if (value instanceof Double) {
            if (((Double) value).doubleValue() >= 0) {
                renderer = new JSparklinesRenderer(positiveValuesColor);
            } else {
                renderer = new JSparklinesRenderer(negativeValuesColor);
            }
        } else {
            if (((Integer) value).intValue() >= 0) {
                renderer = new JSparklinesRenderer(positiveValuesColor);
            } else {
                renderer = new JSparklinesRenderer(negativeValuesColor);
            }
        }

        plot.setRenderer(renderer);

        return this;
    }

    /**
     * Set the color used for the negative values.
     *
     * @param negativeValuesColor the color used for the negative values
     */
    public void setNegativeValuesColor(Color negativeValuesColor) {
        this.negativeValuesColor = negativeValuesColor;
    }

    /**
     * Set the color used for the positive values.
     *
     * @param positiveValuesColor the color used for the positive values
     */
    public void setPositiveValuesColor(Color positiveValuesColor) {
        this.positiveValuesColor = positiveValuesColor;
    }

    /**
     * Rounds of a double value to the wanted number of decimalplaces
     *
     * @param d the double to round of
     * @param places number of decimal places wanted
     * @return double - the new double
     */
    private static double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10, (double) places);
    }
}
