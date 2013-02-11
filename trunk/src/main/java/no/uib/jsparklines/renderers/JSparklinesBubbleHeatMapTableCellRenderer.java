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
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import no.uib.jsparklines.renderers.util.GradientColorCoding.ColorGradient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;

/**
 * A renderer for displaying a JSparklines bubble heat maps inside table cells.
 * Assumes that the cell values are of type Integer, Short, Byte, Long, Double
 * or Float.
 *
 * @author Harald Barsnes
 */
public class JSparklinesBubbleHeatMapTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * The minimum value to display as a chart. Values smaller than this lower
     * limit are shown as this minimum value when shown as a chart. This to make
     * sure that the chart is visible at all.
     */
    private double minimumChartValue = 1;
    /**
     * Used to decide how many decimals to include in the tooltip. If the number
     * is smaller than the lower limit, 8 decimals are shown, otherwise only 2
     * decimals are used.
     */
    private double tooltipLowerValue = 0.01;
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
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * The currently selected color gradient.
     */
    private ColorGradient currentColorGradient = ColorGradient.RedBlackBlue;

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. This constructor
     * uses default colors for the bars. If you want to set your own colors, use
     * one of the other constructors.
     *
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param colorGradient the color gradient to use
     */
    public JSparklinesBubbleHeatMapTableCellRenderer(Double maxValue, ColorGradient colorGradient) {

        this.maxValue = maxValue;
        this.currentColorGradient = colorGradient;

        delegate = new DefaultTableCellRenderer();
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());
    }

    /**
     * Set the color gradient.
     *
     * @param colorGradient
     */
    public void setGradientColoring(ColorGradient colorGradient) {
        this.currentColorGradient = colorGradient;
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

        if (value == null) {
            return c;
        }

        // if show numbers, format as number and return
        if (showNumbers) {

            if (value instanceof Double || value instanceof Float) {

                if (value instanceof Float) {
                    value = ((Float) value).doubleValue();
                }

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, roundDouble(((Double) value).doubleValue(), 2),
                        isSelected, hasFocus, row, column);

                if (Math.abs(new Double("" + value)) < tooltipLowerValue) {
                    c.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
                }

            } else if (value instanceof Integer
                    || value instanceof Short
                    || value instanceof Long
                    || value instanceof Short) {

                if (value instanceof Short) {
                    value = ((Short) value).intValue();
                } else if (value instanceof Long) {
                    value = ((Long) value).intValue();
                } else if (value instanceof Short) {
                    value = ((Short) value).intValue();
                }

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, (Integer) value,
                        isSelected, hasFocus, row, column);

            }

            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);

            return c;
        }

        // set the tooltip text
        if (value instanceof Double || value instanceof Float) {

            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }

            if (Math.abs(new Double("" + value)) < tooltipLowerValue) {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
            } else {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 2));
            }

        } else if (value instanceof Integer
                || value instanceof Short
                || value instanceof Long
                || value instanceof Short) {

            this.setToolTipText("" + value);

        }

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the jFreeChart dataset
        DefaultXYZDataset xyzDataset = new DefaultXYZDataset();
        Color bubbleColor = Color.BLACK;

        if (value instanceof Double || value instanceof Float) {

            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }

            if (((Double) value).doubleValue() < minimumChartValue && ((Double) value).doubleValue() > 0) {
                value = minimumChartValue;
            }

            double[][] data = new double[3][1];
            data[0][0] = maxValue / 2;
            data[1][0] = maxValue / 2;

            if (((Double) value).doubleValue() < 0) {
                data[2][0] = Math.abs(((Double) value).doubleValue());
            } else {
                data[2][0] = (Double) value;
            }

            xyzDataset.addSeries("1", data);
            bubbleColor = GradientColorCoding.findGradientColor(((Double) value).doubleValue(), -maxValue, maxValue, currentColorGradient);

        } else if (value instanceof Integer
                || value instanceof Short
                || value instanceof Long
                || value instanceof Short) {

            if (value instanceof Short) {
                value = ((Short) value).intValue();
            } else if (value instanceof Long) {
                value = ((Long) value).intValue();
            } else if (value instanceof Short) {
                value = ((Short) value).intValue();
            }

            if (((Integer) value).intValue() < minimumChartValue && ((Integer) value).intValue() > 0) {
                value = Double.valueOf(minimumChartValue).intValue();
            }

            double[][] data = new double[3][1];
            data[0][0] = maxValue / 2;
            data[1][0] = maxValue / 2;
            data[2][0] = ((Integer) value).doubleValue();

            if (((Integer) value).doubleValue() < 0) {
                data[2][0] = Math.abs(((Integer) value).doubleValue());
            } else {
                data[2][0] = (Double) value;
            }

            xyzDataset.addSeries("1", data);
            bubbleColor = GradientColorCoding.findGradientColor(((Integer) value).doubleValue(), -maxValue, maxValue, currentColorGradient);

        }

        chart = ChartFactory.createBubbleChart(null, null, null, xyzDataset, PlotOrientation.VERTICAL, false, false, false);

        // fine tune the chart properites
        XYPlot plot = chart.getXYPlot();

        // remove space before/after the domain axis
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);

        plot.getRangeAxis().setUpperMargin(0);
        plot.getRangeAxis().setLowerMargin(0);

        // set the axis ranges
        plot.getDomainAxis().setRange(maxValue * 0.4, maxValue * 0.6);
        plot.getRangeAxis().setRange(maxValue * 0.25, maxValue * 0.75);

        // hide unwanted chart details
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);


        // set the bubble color
        plot.getRenderer().setSeriesPaint(0, bubbleColor);


        // create the chart
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(c.getBackground());
        this.removeAll();
        this.add(chartPanel);

        // hide the outline
        chart.getPlot().setOutlineVisible(false);

        // make sure the background is the same as the table row color
        plot.setBackgroundPaint(c.getBackground());
        chartPanel.setBackground(c.getBackground());
        chart.setBackgroundPaint(c.getBackground());

        return this;
    }

    /**
     * Rounds of a double value to the wanted number of decimal places
     *
     * @param d the double to round of
     * @param places number of decimal places wanted
     * @return double - the new double
     */
    private static double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10, (double) places);
    }

    /**
     * Returns the minimum chart value to plot.
     *
     * @return the minimumChartValue
     */
    public double getMinimumChartValue() {
        return minimumChartValue;
    }

    /**
     * Set the minimum chart value to plot.
     *
     * @param minimumChartValue the minimumChartValue to set
     */
    public void setMinimumChartValue(double minimumChartValue) {
        this.minimumChartValue = minimumChartValue;
    }

    /**
     * Returns the lower value before using 8 decimals for the tooltip.
     *
     * @return the tooltipLowerValue
     */
    public double getTooltipLowerValue() {
        return tooltipLowerValue;
    }

    /**
     * Set the lower limit for the values before using 8 decimals for the
     * tooltip.
     *
     * @param tooltipLowerValue the tooltipLowerValue to set
     */
    public void setTooltipLowerValue(double tooltipLowerValue) {
        this.tooltipLowerValue = tooltipLowerValue;
    }
}
