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
 * Table cell renderer displaying JSparklines bubble heat maps. Supported input:
 * Integer, Short, Byte, Long, Double or Float objects. Other object types are
 * rendered using the DefaultTableCellRenderer.
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
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The maximum absolute value. Used to set the maximum range for the chart.
     */
    private double maxAbsValue = 1;
    /**
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * The currently selected color gradient.
     */
    private ColorGradient currentColorGradient = ColorGradient.RedBlackBlue;
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
     * Creates a new JSparklinesBubbleHeatMapTableCellRenderer.
     *
     * @param maxAbsValue the maximum absolute value to be plotted, used to make
     * sure that all plots in the same column has the same maximum value and are
     * thus comparable
     * @param colorGradient the color gradient to use
     * @param positiveColorGradient if true only positive values are expected
     * and the middle gradient color is used for the halfway point between the
     * min and max values, if false the middle gradient color is used for values
     * around zero
     */
    public JSparklinesBubbleHeatMapTableCellRenderer(Double maxAbsValue, ColorGradient colorGradient, boolean positiveColorGradient) {

        this.maxAbsValue = maxAbsValue;
        this.currentColorGradient = colorGradient;
        this.positiveColorGradient = positiveColorGradient;

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());
    }

    /**
     * Set the color gradient.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     * @param positiveColorGradient if true only positive values are expected
     * and the middle gradient color is used for the halfway point between the
     * min and max values, if false the middle gradient color is used for values
     * around zero
     */
    public void setGradientColoring(ColorGradient colorGradient, boolean positiveColorGradient) {
        this.currentColorGradient = colorGradient;
        this.positiveColorGradient = positiveColorGradient;
    }

    /**
     * Set the maximum absolute value.
     *
     * @param maxAbsValue the maximum absolute value
     */
    public void setMaxValue(double maxAbsValue) {
        this.maxAbsValue = maxAbsValue;
    }

    /**
     * Set if the underlying numbers or the bar charts are to be shown.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // check if the input is of a supported type
        boolean supportedObjectType = false;
        if (value != null
                && (value instanceof Double
                || value instanceof Float
                || value instanceof Integer
                || value instanceof Short
                || value instanceof Long
                || value instanceof Byte)) {
            supportedObjectType = true;
        }

        if (!supportedObjectType) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
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
                    || value instanceof Byte) {

                if (value instanceof Short) {
                    value = ((Short) value).intValue();
                } else if (value instanceof Long) {
                    value = ((Long) value).intValue();
                } else if (value instanceof Byte) {
                    value = ((Byte) value).intValue();
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
                || value instanceof Byte) {

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
            data[0][0] = maxAbsValue / 2;
            data[1][0] = maxAbsValue / 2;

            if (((Double) value).doubleValue() < 0) {
                data[2][0] = Math.abs(((Double) value).doubleValue());
            } else {
                data[2][0] = (Double) value;
            }

            xyzDataset.addSeries("1", data);
            bubbleColor = GradientColorCoding.findGradientColor(((Double) value).doubleValue(), -maxAbsValue, maxAbsValue, currentColorGradient, positiveColorGradient);

        } else if (value instanceof Integer
                || value instanceof Short
                || value instanceof Long
                || value instanceof Byte) {

            if (value instanceof Short) {
                value = ((Short) value).intValue();
            } else if (value instanceof Long) {
                value = ((Long) value).intValue();
            } else if (value instanceof Byte) {
                value = ((Byte) value).intValue();
            }

            if (((Integer) value).intValue() < minimumChartValue && ((Integer) value).intValue() > 0) {
                value = Double.valueOf(minimumChartValue).intValue();
            }

            double[][] data = new double[3][1];
            data[0][0] = maxAbsValue / 2;
            data[1][0] = maxAbsValue / 2;
            data[2][0] = ((Integer) value).doubleValue();

            if (((Integer) value).doubleValue() < 0) {
                data[2][0] = Math.abs(((Integer) value).doubleValue());
            } else {
                data[2][0] = (Double) value;
            }

            xyzDataset.addSeries("1", data);
            bubbleColor = GradientColorCoding.findGradientColor(((Integer) value).doubleValue(), -maxAbsValue, maxAbsValue, currentColorGradient, positiveColorGradient);
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
        plot.getDomainAxis().setRange(maxAbsValue * 0.4, maxAbsValue * 0.6);
        plot.getRangeAxis().setRange(maxAbsValue * 0.25, maxAbsValue * 0.75);

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
