package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.renderers.util.StatisticalBarChartColorRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * A renderer for displaying a JSparklines bar charts with error bars inside a
 * table cell. Assumes that the cell values are of type
 * DefaultStatisticalCategoryDataset.
 *
 * @author Harald Barsnes
 */
public class JSparklinesErrorBarChartTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The minimum value to display as a chart. Values smaller than this lower
     * limit are shown as this minimum value when shown as a chart. This to make
     * sure that the chart is visible at all.
     */
    private double minimumChartValue = 0.05;
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
     * The label used to display the number and the bar chart at the same time.
     */
    private JLabel valueLabel;
    /**
     * The maximum value. Used to set the maximum range for the chart.
     */
    private double maxValue = 1;
    /**
     * The minimum value. Used to set the minimum range for the chart.
     */
    private double minValue = 0;
    /**
     * The colors to use for the bars with negative numbers.
     */
    private Color negativeValuesColor = new Color(51, 51, 255);
    /**
     * The colors to use for the bars with positive numbers.
     */
    private Color positiveValuesColor = new Color(255, 51, 51);
    /**
     * The color to use for bar charts shown inside a table cell when a big
     * value is "good".
     */
    private Color tableCellBarChartColorBig = new Color(110, 196, 97);
    /**
     * The color to use for bar charts shown inside a table cell when a small
     * value is "good".
     */
    private Color tableCellBarChartColorSmall = new Color(247, 247, 23);
    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;
    /**
     * The background color used for the plots. For plots using light colors,
     * it's recommended to use a dark background color, and for plots using
     * darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when only positive values are to be plotted. This constructor uses
     * default colors for the bars. If you want to set your own colors, use one
     * of the other constructors.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable (this is the same as setting the minimum value to 0)
     * @param largeNumbersAreGood makes sure that different colors are used for
     * bars where large numbers are "good", versus when small numbers are "good"
     */
    public JSparklinesErrorBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, boolean largeNumbersAreGood) {

        if (largeNumbersAreGood) {
            positiveValuesColor = tableCellBarChartColorBig;
        } else {
            positiveValuesColor = tableCellBarChartColorSmall;
        }

        this.maxValue = maxValue;

        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when only positive values are to be plotted.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable (this is the same as setting the minimum value to 0)
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     */
    public JSparklinesErrorBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, Color positiveValuesColor) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. This constructor
     * uses default colors for the bars. If you want to set your own colors, use
     * one of the other constructors.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minium value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     */
    public JSparklinesErrorBarChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue) {

        this.maxValue = maxValue;
        this.minValue = minValue;

        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. Note that to use the
     * significance color coding the object in the table cell has to be of type
     * XYDataPoint.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minium value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param negativeValuesColor the color to use for the negative values if
     * two sided data is shown
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     */
    public JSparklinesErrorBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor) {

        this.negativeValuesColor = negativeValuesColor;
        this.positiveValuesColor = positiveValuesColor;

        this.maxValue = maxValue;
        this.minValue = minValue;
        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Sets up the table cell renderer and the bar chart.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart(PlotOrientation plotOrientation) {

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));

        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
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

        // if the cell is empty, simply return
        if (value == null) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }
        
        if (value instanceof String) {
            //((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // set the tooltip text
        if (value instanceof DefaultStatisticalCategoryDataset) {
            DefaultStatisticalCategoryDataset tempSet = (DefaultStatisticalCategoryDataset) value;
            this.setToolTipText("<html>Intensity: " + roundDouble((Double) tempSet.getMeanValue(0, 0), 4) + "<br>"
                    + "STDEV: " + roundDouble((Double) tempSet.getStdDevValue(0, 0), 4) + "</html>");
        }

        valueLabel.setMinimumSize(new Dimension(0, 0));
        valueLabel.setSize(0, 0);
        valueLabel.setVisible(false);

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the bar chart

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // set the axis range
        plot.getRangeAxis().setRange(minValue, maxValue);

        // add the dataset
        plot.setDataset((DefaultStatisticalCategoryDataset) value);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // set up the chart renderer
        StatisticalBarChartColorRenderer renderer = null;

        if (value instanceof DefaultStatisticalCategoryDataset) {

            DefaultStatisticalCategoryDataset tempSet = (DefaultStatisticalCategoryDataset) value;

            if ((Double) tempSet.getMeanValue(0, 0) >= 0) {
                renderer = new StatisticalBarChartColorRenderer(positiveValuesColor);
            } else {
                renderer = new StatisticalBarChartColorRenderer(negativeValuesColor);
            }
        }


        // make sure the background is the same as the table row color

        if (plotBackgroundColor != null && !isSelected) {
            plot.setBackgroundPaint(plotBackgroundColor);
            chartPanel.setBackground(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }

        renderer.setErrorIndicatorStroke(new BasicStroke(4)); // @TODO: this should not be hardcoded!!

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
