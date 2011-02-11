package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.XYDataPoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A renderer for displaying a JSparklines bar chart inside a table cell.
 * Assumes that the cell values are of type Integer, Short, Byte, Long,
 * Double, Float or XYDataPoint.
 *
 * @author Harald Barsnes
 */
public class JSparklinesBarChartTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The minimum value to display as a chart. Values smaller than this lower
     * limit are shown as this minimum value when shown as a chart. This to make
     * sure that the chart is visible at all.
     */
    private double minimumChartValue = 0.05;
    /**
     * Used to decide how many decimals to include in the tooltip. If the number
     * is smaller than the lower limit, 8 decimnals are shown, otherwise only
     * 2 decimals are used.
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
     * The label used to display the number and the bar chart at the same
     * time.
     */
    private JLabel valueLabel;
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
    private Color negativeValuesColor = new Color(51, 51, 255);
    /**
     * The colors to use for the bars with positive numbers.
     */
    private Color positiveValuesColor = new Color(255, 51, 51);
    /**
     * The color to use for the non-significant values.
     */
    private Color nonSignificantColor = Color.GRAY;
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
     * The upper level for when to use the significant values color.
     */
    private double significanceLevel = 1;
    /**
     * If true the value of the chart is shown next to the bar chart.
     */
    private boolean showNumberAndChart = false;
    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when only positive
     * values are to be plotted. This constructor uses default colors for the bars. If you want to
     * set your own colors, use one of the other constructors.
     *
     * @param plotOrientation       the orientation of the plot
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     *                              (this is the same as setting the minimum value to 0)
     * @param largeNumbersAreGood   makes sure that different colors are used for bars where large numbers
     *                              are "good", versus when small numbers are "good"
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, boolean largeNumbersAreGood) {

        if (largeNumbersAreGood) {
            positiveValuesColor = tableCellBarChartColorBig;
        } else {
            positiveValuesColor = tableCellBarChartColorSmall;
        }

        this.maxValue = maxValue;

        setUpRendererAndChart(plotOrientation);
    }

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
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when only positive
     * values are to be plotted. Note that to use the significance color coding the object in the
     * table cell has to be of type XYDataPoint.
     *
     * @param plotOrientation       the orientation of the plot
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     *                              (this is the same as setting the minimum value to 0)
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     * @param nonSignificantColor   the color to use for the non-significant values
     * @param significanceLevel     the upper level for when to use the significant values color
     */
    public JSparklinesBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double maxValue, Color positiveValuesColor,
            Color nonSignificantColor, double significanceLevel) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor, nonSignificantColor, significanceLevel);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when positive
     * and negative values are to be plotted. This constructor uses default colors for the bars.
     * If you want to set your own colors, use one of the other constructors.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue) {

        this.maxValue = maxValue;
        this.minValue = minValue;

        setUpRendererAndChart(plotOrientation);
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
    public JSparklinesBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor) {
        this(plotOrientation, minValue, maxValue, negativeValuesColor, positiveValuesColor, Color.GRAY, 1);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Used this constructor when positive
     * and negative values are to be plotted. Note that to use the significance color coding the 
     * object in the table cell has to be of type XYDataPoint.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     * @param negativeValuesColor   the color to use for the negative values if two sided data is shown
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     * @param nonSignificantColor   the color to use for the non-significant values
     * @param significanceLevel     the upper level for when to use the significant values color
     */
    public JSparklinesBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor,
            Color nonSignificantColor, double significanceLevel) {

        this.negativeValuesColor = negativeValuesColor;
        this.positiveValuesColor = positiveValuesColor;

        this.maxValue = maxValue;
        this.minValue = minValue;

        this.nonSignificantColor = nonSignificantColor;
        this.significanceLevel = significanceLevel;

        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Sets up the table cell renderer and the bar chart.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart(PlotOrientation plotOrientation) {

        delegate = new DefaultTableCellRenderer();
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // @TODO: perhaps this could be set by the user?
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize()-2f)); // @TODO: perhaps this could be set by the user?

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);
        
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart    if true the number and the chart is shown in the cell
     * @param widthOfLabel          the width used to display the label containing the number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart    if true the number and the chart is shown in the cell
     * @param widthOfLabel          the width used to display the label containing the number
     * @param font                  the font to use for the label
     * @param horizontalAlignement  the horizontal alignent of the text in the label:
     *                              one of the following constants defined in SwingConstants:
     *                              LEFT, CENTER, RIGHT, LEADING or TRAILING.
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        valueLabel.setHorizontalAlignment(horizontalAlignement);
        valueLabel.setFont(font);
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

            } else if (value instanceof XYDataPoint) {

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, ((XYDataPoint) value).toString(),
                        isSelected, hasFocus, row, column);

                if (Math.abs(new Double("" + ((XYDataPoint) value).getX())) < tooltipLowerValue) {
                    c.setToolTipText("" + roundDouble(new Double("" + ((XYDataPoint) value).getX()).doubleValue(), 8));
                }
            }

            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

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

        } else if (value instanceof XYDataPoint) {
            if (Math.abs(((XYDataPoint) value).getX()) < tooltipLowerValue) {
                this.setToolTipText("" + roundDouble(((XYDataPoint) value).getX(), 8));
            } else {
                this.setToolTipText("" + roundDouble(((XYDataPoint) value).getX(), 2));
            }
        }

        // show the number _and_ the chart if option selected
        if (showNumberAndChart) {

            if (value instanceof Double || value instanceof Float) {
                valueLabel.setText("" + roundDouble(Double.valueOf("" + value).doubleValue(), 2));
            } else if (value instanceof Integer
                || value instanceof Short
                || value instanceof Long
                || value instanceof Short) {
                valueLabel.setText("" + Integer.valueOf("" + value).intValue());
            } else if (value instanceof XYDataPoint) {
                valueLabel.setText("" + roundDouble(((XYDataPoint) value).getX(), 2));
            }

            valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
            valueLabel.setVisible(true);
        } else {
            valueLabel.setMinimumSize(new Dimension(0, 0));
            valueLabel.setSize(0, 0);
            valueLabel.setVisible(false);
        }

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (value instanceof Double || value instanceof Float) {

            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }

            if (((Double) value).doubleValue() < minimumChartValue && ((Double) value).doubleValue() > 0) {
                value = minimumChartValue;
            }

            dataset.addValue(((Double) value), "1", "1");

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

            dataset.addValue(((Integer) value), "1", "1");

        } else if (value instanceof XYDataPoint) {

            if (((XYDataPoint) value).getX() < minimumChartValue && ((XYDataPoint) value).getX() > 0) {
                value = minimumChartValue;
            }

            dataset.addValue(((XYDataPoint) value).getX(), "1", "1");
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
        CategoryItemRenderer renderer = null;

        if (value instanceof Double || value instanceof Float) {

            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }

            if (((Double) value).doubleValue() >= 0) {
                renderer = new BarChartColorRenderer(positiveValuesColor);
            } else {
                renderer = new BarChartColorRenderer(negativeValuesColor);
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

            if (((Integer) value).intValue() >= 0) {
                renderer = new BarChartColorRenderer(positiveValuesColor);
            } else {
                renderer = new BarChartColorRenderer(negativeValuesColor);
            }

        } else if (value instanceof XYDataPoint) {

            if (((XYDataPoint) value).getX() >= 0) {
                if (((XYDataPoint) value).getY() >= significanceLevel) {
                    renderer = new BarChartColorRenderer(nonSignificantColor);
                } else {
                    renderer = new BarChartColorRenderer(positiveValuesColor);
                }
            } else {
                if (((XYDataPoint) value).getY() >= significanceLevel) {
                    renderer = new BarChartColorRenderer(nonSignificantColor);
                } else {
                    renderer = new BarChartColorRenderer(negativeValuesColor);
                }
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
     * Set the lower limit for the values before using 8 decimals for the tooltip.
     *
     * @param tooltipLowerValue the tooltipLowerValue to set
     */
    public void setTooltipLowerValue(double tooltipLowerValue) {
        this.tooltipLowerValue = tooltipLowerValue;
    }

    /**
     * Return the color to use for the non-significant values.
     *
     * @return the color to use for the non-significant values
     */
    public Color getNonSignificantColor() {
        return nonSignificantColor;
    }

    /**
     * Set the color to use for the non-significant values.
     *
     * @param nonSignificantColor the color to set
     */
    public void setNonSignificantColor(Color nonSignificantColor) {
        this.nonSignificantColor = nonSignificantColor;
    }

    /**
     * Returns the lower significance level. Values above this level will be
     * colored with the positive/negative values colors, while values below
     * the threshold will be colored with the non-significant color.
     *
     * @return the lower significance level
     */
    public double getSignificanceLevel() {
        return significanceLevel;
    }

    /**
     * Set the lower significance level. Values above this level will be
     * colored with the positive/negative values colors, while values below
     * the threshold will be colored with the non-significant color.
     *
     * @param significanceLevel the lower significance level to set
     */
    public void setSignificanceLevel(double significanceLevel) {
        this.significanceLevel = significanceLevel;
    }
}
