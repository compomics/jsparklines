package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.ValueAndBooleanDataPoint;
import no.uib.jsparklines.data.XYDataPoint;
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import no.uib.jsparklines.renderers.util.GradientColorCoding.ColorGradient;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Table cell renderer displaying JSparklines bar charts. Supported input:
 * Integer, Short, Byte, Long, Double, Float, XYDataPoint and
 * ValueAndBooleanDataPoint objects. Other object types are rendered using the
 * DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesBarChartTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;
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
     * The maximum log value (base 10). Used to set the maximum range for the
     * log chart.
     */
    private double maxLogValue = 1;
    /**
     * The minimum log value (base 10). Used to set the minimum range for the
     * log chart.
     */
    private double minLogValue = 0;
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
     * The decimal format for use when showing the numbers.
     */
    private DecimalFormat numberFormat = new DecimalFormat("0.00");
    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;
    /**
     * If true a red/green gradient coloring is used.
     */
    private boolean gradientColoring = false;
    /**
     * The background color used for the plots. For plots using light colors,
     * it's recommended to use a dark background color, and for plots using
     * darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;
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
     * If true, the values are shown as a heat map.
     */
    private boolean showAsHeatMap = false;
    /**
     * The color to use for the border for the cells when heat map is used.
     * Makes sure that the user can see which cells are selected.
     */
    private Color heatMapBorderColor = Color.lightGray;
    /**
     * If true, the values are log scaled before displaying the bar. Note that
     * the scaling has no effect on the actual value only on the visualization
     * of the values.
     */
    private boolean logScale = false;

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
     * @throws IllegalArgumentException if maxValue &lt; 0.0
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, boolean largeNumbersAreGood) {

        if (largeNumbersAreGood) {
            positiveValuesColor = tableCellBarChartColorBig;
        } else {
            positiveValuesColor = tableCellBarChartColorSmall;
        }

        this.maxValue = maxValue;
        if (maxValue != 0) {
            maxLogValue = Math.log10(maxValue);
        }

        setUpRendererAndChart(plotOrientation);

        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue has to be non-negative! Current value: " + maxValue + ".");
        }
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
     * @throws IllegalArgumentException if maxValue &lt; 0.0
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, Color positiveValuesColor) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when only positive values are to be plotted. Note that to use the
     * significance color coding the object in the table cell has to be of type
     * XYDataPoint.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable (this is the same as setting the minimum value to 0)
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     * @param nonSignificantColor the color to use for the non-significant
     * values
     * @param significanceLevel the upper level for when to use the significant
     * values color
     * @throws IllegalArgumentException if maxValue &lt; 0.0
     */
    public JSparklinesBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double maxValue, Color positiveValuesColor,
            Color nonSignificantColor, double significanceLevel) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor, nonSignificantColor, significanceLevel);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. This constructor
     * uses default colors for the bars. If you want to set your own colors, use
     * one of the other constructors.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minimum value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @throws IllegalArgumentException if minValue &gt; maxValue
     */
    public JSparklinesBarChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue) {

        this.maxValue = maxValue;
        this.minValue = minValue;

        setUpRendererAndChart(plotOrientation);

        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue has to be smaller than maxValue! Current values: minValue: " + minValue + ", maxValue: " + maxValue + ".");
        }
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minimum value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param negativeValuesColor the color to use for the negative values if
     * two sided data is shown
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     * @throws IllegalArgumentException if minValue &gt; maxValue
     */
    public JSparklinesBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor) {
        this(plotOrientation, minValue, maxValue, negativeValuesColor, positiveValuesColor, Color.GRAY, 1);
    }

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. Note that to use the
     * significance color coding the object in the table cell has to be of type
     * XYDataPoint.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minimum value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param negativeValuesColor the color to use for the negative values if
     * two sided data is shown
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     * @param nonSignificantColor the color to use for the non-significant
     * values
     * @param significanceLevel the upper level for when to use the significant
     * values color
     * @throws IllegalArgumentException if minValue &gt; maxValue
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

        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue has to be smaller than maxValue! Current values: minValue: " + minValue + ", maxValue: " + maxValue + ".");
        }
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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
    }

    /**
     * Displays the values as a heat map using the selected color gradient. To
     * disable the heat map use null as the parameter. <br><br> <b>NB: the
     * programmer has to make sure that the max and min values are the same for
     * all columns used in a heat map to ensure that the color coding is
     * comparable across the columns. This method can not handle this.</b>
     * <br><br> The first color of the gradient is used for values close to the
     * min value, while the third color of the gradient is used for values close
     * to the max value. If only positive values are expected
     * (positiveColorGradient is true) the middle gradient color is used for the
     * halfway point between the min and max values. If both positive and
     * negative values are expected (positiveColorGradient is false) the middle
     * gradient color is used for values around zero. <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     * @param positiveColorGradient if true only positive values are expected
     * and the middle gradient color is used for the halfway point between the
     * min and max values, if false the middle gradient color is used for values
     * around zero
     */
    public void showAsHeatMap(ColorGradient colorGradient, boolean positiveColorGradient) {

        this.showAsHeatMap = (colorGradient != null);
        gradientColoring = (colorGradient != null);
        this.currentColorGradient = colorGradient;
        this.positiveColorGradient = positiveColorGradient;

        if (showAsHeatMap) {
            if (Math.abs(minValue) > maxValue) {
                maxValue = Math.abs(minValue);
            }
        }
    }

    /**
     * Set the color gradient to use for the bars. To disable the color gradient
     * use null as the parameter. <br><br> The first color of the gradient is
     * used for values close to the min value, while the third color of the
     * gradient is used for values close to the max value. If only positive
     * values are expected (positiveColorGradient is true) the middle gradient
     * color is used for the halfway point between the min and max values. If
     * both positive and negative values are expected (positiveColorGradient is
     * false) the middle gradient color is used for values around zero. <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     * @param positiveColorGradient if true only positive values are expected
     * and the middle gradient color is used for the halfway point between the
     * min and max values, if false the middle gradient color is used for values
     * around zero
     */
    public void setGradientColoring(ColorGradient colorGradient, boolean positiveColorGradient) {
        setGradientColoring(colorGradient, positiveColorGradient, null);
    }

    /**
     * Set the color gradient to use for the bars. To disable the color gradient
     * use null as the parameter. <br><br> The first color of the gradient is
     * used for values close to the min value, while the third color of the
     * gradient is used for values close to the max value. If only positive
     * values are expected (positiveColorGradient is true) the middle gradient
     * color is used for the halfway point between the min and max values. If
     * both positive and negative values are expected (positiveColorGradient is
     * false) the middle gradient color is used for values around zero. <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
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
    public void setGradientColoring(ColorGradient colorGradient, boolean positiveColorGradient, Color plotBackgroundColor) {

        this.gradientColoring = (colorGradient != null);
        this.positiveColorGradient = positiveColorGradient;
        if (plotBackgroundColor != null) {
            this.plotBackgroundColor = plotBackgroundColor;
        }
        this.currentColorGradient = colorGradient;

        if (gradientColoring) {
            if (Math.abs(minValue) > maxValue) {
                maxValue = Math.abs(minValue);
            }
        }
    }

    /**
     * Set the plot background color.
     *
     * @param plotBackgroundColor the plot background color
     */
    public void setBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        showNumberAndChart(showNumberAndChart, widthOfLabel, numberFormat);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param numberFormat the decimal format to use when showing the numbers
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, DecimalFormat numberFormat) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        this.numberFormat = numberFormat;
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param font the font to use for the label
     * @param horizontalAlignement the horizontal alignment of the text in the
     * label: one of the following constants defined in SwingConstants: LEFT,
     * CENTER, RIGHT, LEADING or TRAILING.
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement) {
        showNumberAndChart(showNumberAndChart, widthOfLabel, font, horizontalAlignement, numberFormat);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param font the font to use for the label
     * @param horizontalAlignement the horizontal alignment of the text in the
     * label: one of the following constants defined in SwingConstants: LEFT,
     * CENTER, RIGHT, LEADING or TRAILING.
     * @param numberFormat the decimal format to use when showing the numbers
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement, DecimalFormat numberFormat) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        labelHorizontalAlignement = horizontalAlignement;
        valueLabel.setFont(font);
        this.numberFormat = numberFormat;
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
                || value instanceof Byte
                || value instanceof XYDataPoint
                || value instanceof ValueAndBooleanDataPoint)) {
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

            } else if (value instanceof XYDataPoint) {

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, ((XYDataPoint) value).toString(),
                        isSelected, hasFocus, row, column);

                if (Math.abs(new Double("" + ((XYDataPoint) value).getX())) < tooltipLowerValue) {
                    c.setToolTipText("" + roundDouble(new Double("" + ((XYDataPoint) value).getX()).doubleValue(), 8));
                }

            } else if (value instanceof ValueAndBooleanDataPoint) {

                if (Double.isInfinite(((ValueAndBooleanDataPoint) value).getValue())) {
                    c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, ((ValueAndBooleanDataPoint) value).getValue(),
                            isSelected, hasFocus, row, column);
                } else {
                    c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, ((ValueAndBooleanDataPoint) value).toString(),
                            isSelected, hasFocus, row, column);

                    if (Math.abs(new Double("" + ((ValueAndBooleanDataPoint) value).getValue())) < tooltipLowerValue) {
                        c.setToolTipText("" + roundDouble(new Double("" + ((ValueAndBooleanDataPoint) value).getValue()).doubleValue(), 8));
                    }
                }
            }

            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

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

        } else if (value instanceof XYDataPoint) {
            if (Math.abs(((XYDataPoint) value).getX()) < tooltipLowerValue) {
                this.setToolTipText("" + roundDouble(((XYDataPoint) value).getX(), 8));
            } else {
                this.setToolTipText("" + roundDouble(((XYDataPoint) value).getX(), 2));
            }
        } else if (value instanceof ValueAndBooleanDataPoint) {

            if (Double.isInfinite(((ValueAndBooleanDataPoint) value).getValue())) {
                this.setToolTipText("" + ((ValueAndBooleanDataPoint) value).getValue());
            } else {
                this.setToolTipText("" + roundDouble(((ValueAndBooleanDataPoint) value).getValue(), 2));

                if (Math.abs(new Double("" + ((ValueAndBooleanDataPoint) value).getValue())) < tooltipLowerValue) {
                    this.setToolTipText("" + roundDouble(((ValueAndBooleanDataPoint) value).getValue(), 8));
                }
            }
        }

        // show the number _and_ the chart if option selected
        if (showNumberAndChart) {

            // set the decimal format symbol
            numberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            if (value instanceof Double || value instanceof Float) {
                double temp = Double.valueOf("" + value);
                valueLabel.setText(numberFormat.format(temp));
            } else if (value instanceof Integer
                    || value instanceof Short
                    || value instanceof Long
                    || value instanceof Byte) {
                valueLabel.setText("" + Integer.valueOf("" + value).intValue());
            } else if (value instanceof XYDataPoint) {
                double temp = ((XYDataPoint) value).getX();
                valueLabel.setText(numberFormat.format(temp));
            } else if (value instanceof ValueAndBooleanDataPoint) {
                double temp = ((ValueAndBooleanDataPoint) value).getValue();
                valueLabel.setText(numberFormat.format(temp));
            }

            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            valueLabel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

            // add some padding
            if (labelHorizontalAlignement == SwingConstants.RIGHT) {
                valueLabel.setText(valueLabel.getText() + "  ");
            } else if (labelHorizontalAlignement == SwingConstants.LEFT) {
                valueLabel.setText("  " + valueLabel.getText());
            }

            valueLabel.setForeground(c.getForeground());

            // set the horizontal text alignment and the label size
            valueLabel.setHorizontalAlignment(labelHorizontalAlignement);
            valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
            valueLabel.setSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setMaximumSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setPreferredSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
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

            if (logScale && ((Double) value).doubleValue() != 0) {
                value = Math.log10((Double) value);
            }

            if (showAsHeatMap) {
                if (logScale) {
                    dataset.addValue(maxValue, "1", "1");
                } else {
                    dataset.addValue(maxLogValue, "1", "1");
                }
            } else {
                dataset.addValue(((Double) value), "1", "1");
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

            if (((Integer) value).intValue() < minimumChartValue && ((Integer) value).intValue() > 0) {
                value = Double.valueOf(minimumChartValue).intValue();
            }

            if (logScale && ((Integer) value).intValue() != 0) {
                value = Math.log10(((Integer) value).doubleValue());
            }

            if (showAsHeatMap) {
                if (logScale) {
                    dataset.addValue(maxLogValue, "1", "1");
                } else {
                    dataset.addValue(maxValue, "1", "1");
                }
            } else {
                if (value instanceof Integer) {
                    dataset.addValue(((Integer) value), "1", "1");
                } else {
                    dataset.addValue(((Double) value), "1", "1");
                }
            }

        } else if (value instanceof XYDataPoint) {

            double tempX = ((XYDataPoint) value).getX();

            if (tempX < minimumChartValue && tempX > 0) {
                tempX = minimumChartValue;
            }

            if (logScale && ((Double) tempX).doubleValue() != 0) {
                tempX = Math.log10(tempX);
            }

            if (showAsHeatMap) {
                if (logScale) {
                    dataset.addValue(maxLogValue, "1", "1");
                } else {
                    dataset.addValue(maxValue, "1", "1");
                }
            } else {
                dataset.addValue(tempX, "1", "1");
            }

        } else if (value instanceof ValueAndBooleanDataPoint) {

            double tempX = ((ValueAndBooleanDataPoint) value).getValue();

            if (tempX < minimumChartValue && tempX > 0) {
                tempX = minimumChartValue;
            }

            if (logScale && ((Double) tempX).doubleValue() != 0) {
                tempX = Math.log10(tempX);
            }

            if (showAsHeatMap) {
                if (logScale) {
                    dataset.addValue(maxLogValue, "1", "1");
                } else {
                    dataset.addValue(maxValue, "1", "1");
                }
            } else {
                dataset.addValue(tempX, "1", "1");
            }
        }

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // set the axis range
        if (showAsHeatMap) {
            if (logScale) {
                plot.getRangeAxis().setRange(0, maxLogValue);
            } else {
                plot.getRangeAxis().setRange(0, maxValue);
            }
        } else {
            if (logScale) {
                plot.getRangeAxis().setRange(minLogValue, maxLogValue);
            } else {
                plot.getRangeAxis().setRange(minValue, maxValue);
            }
        }

        // add the dataset
        plot.setDataset(dataset);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // set up the chart renderer
        CategoryItemRenderer renderer = null;

        Color currentColor = c.getBackground();

        if (value instanceof Double || value instanceof Float) {

            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }

            if (gradientColoring) {
                if (logScale) {
                    currentColor = GradientColorCoding.findGradientColor((Double) value, minLogValue, maxLogValue, currentColorGradient, positiveColorGradient);
                } else {
                    currentColor = GradientColorCoding.findGradientColor((Double) value, minValue, maxValue, currentColorGradient, positiveColorGradient);
                }
                renderer = new BarChartColorRenderer(currentColor);
            } else {
                if (((Double) value).doubleValue() >= 0) {
                    currentColor = positiveValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = negativeValuesColor;
                    renderer = new BarChartColorRenderer(negativeValuesColor);
                }
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

            if (gradientColoring) {
                if (logScale) {
                    if (value instanceof Integer) {
                        currentColor = GradientColorCoding.findGradientColor(((Integer) value).doubleValue(), minLogValue, maxLogValue, currentColorGradient, positiveColorGradient);
                    } else {
                        currentColor = GradientColorCoding.findGradientColor((Double) value, minLogValue, maxLogValue, currentColorGradient, positiveColorGradient);
                    }
                } else {
                    currentColor = GradientColorCoding.findGradientColor(((Integer) value).doubleValue(), minValue, maxValue, currentColorGradient, positiveColorGradient);
                }
                renderer = new BarChartColorRenderer(currentColor);
            } else {

                boolean positiveValue;

                if (value instanceof Integer) {
                    positiveValue = ((Integer) value).intValue() >= 0;
                } else {
                    positiveValue = ((Double) value).doubleValue() >= 0;
                }

                if (positiveValue) {
                    currentColor = positiveValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = negativeValuesColor;
                    renderer = new BarChartColorRenderer(negativeValuesColor);
                }
            }

        } else if (value instanceof XYDataPoint) {

            if (((XYDataPoint) value).getX() >= 0) {
                if (((XYDataPoint) value).getY() >= significanceLevel) {
                    currentColor = nonSignificantColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = positiveValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                }
            } else {
                if (((XYDataPoint) value).getY() >= significanceLevel) {
                    currentColor = nonSignificantColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = negativeValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                }
            }

        } else if (value instanceof ValueAndBooleanDataPoint) {

            if (((ValueAndBooleanDataPoint) value).getValue() >= 0) {
                if (!((ValueAndBooleanDataPoint) value).isSignificant()) {
                    currentColor = nonSignificantColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = positiveValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                }
            } else {
                if (!((ValueAndBooleanDataPoint) value).isSignificant()) {
                    currentColor = nonSignificantColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = negativeValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                }
            }
        }

        // make sure the background is the same as the table row color
        if (showAsHeatMap) {

            if (isSelected) {
                if (table.getCellSelectionEnabled()) {
                    this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, heatMapBorderColor));
                } else {
                    this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, heatMapBorderColor)); // row selection, only add border at the top and bottom 
                }
            }

            plot.setBackgroundPaint(currentColor);
            chartPanel.setBackground(currentColor);
            chart.setBackgroundPaint(currentColor);
            this.setBackground(currentColor);

        } else {
            if (plotBackgroundColor != null && !isSelected) {
                plot.setBackgroundPaint(plotBackgroundColor);
                chartPanel.setBackground(plotBackgroundColor);
                chart.setBackgroundPaint(plotBackgroundColor);
            } else {

                if (table instanceof JXTable) {

                    ///////////////////////////////////////////////////
                    // NOTE: the JXTable support is experimental!!! 
                    ///////////////////////////////////////////////////
                    // @TODO: If you are using JXTables please consider revising/extending the code below...
                    // @TODO: this code should be moved and used across all the cell renderers
                    JXTable jxTable = (JXTable) table;

                    boolean useDefaultBackgroundColorApproach = false;

                    if (jxTable.getHighlighters().length > 0) {

                        // we need to figure out the background color resulting from the highlighters
                        if (jxTable.getHighlighters().length == 1) {

                            if (jxTable.getHighlighters()[0] instanceof ColorHighlighter) {

                                Color tempColor = ((ColorHighlighter) jxTable.getHighlighters()[0]).getBackground();

                                // note: alternate row highlighting is here assumed! isHighlighted should rather be used, but could get this to work...
                                if (row % 2 == 0 || isSelected) {
                                    useDefaultBackgroundColorApproach = true;
                                } else {
                                    plot.setBackgroundPaint(tempColor);
                                    chartPanel.setBackground(tempColor);
                                    chart.setBackgroundPaint(tempColor);
                                    this.setBackground(tempColor);
                                }

                            } else {
                                // not yet supported...
                                useDefaultBackgroundColorApproach = true;
                            }
                        } else {
                            // not yet supported...
                            useDefaultBackgroundColorApproach = true;
                        }
                    } else {
                        // no highlighters, the default approach is fine
                        useDefaultBackgroundColorApproach = true;
                    }

                    if (useDefaultBackgroundColorApproach) {
                        // We have to create a new color object because Nimbus returns
                        // a color of type DerivedColor, which behaves strange, not sure why.
                        Color bg = c.getBackground();
                        plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                        chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                        chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                        this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                    }
                } else {
                    // we have a normal JTable

                    // We have to create a new color object because Nimbus returns
                    // a color of type DerivedColor, which behaves strange, not sure why.
                    Color bg = c.getBackground();
                    plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                    chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                    chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
                    this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
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
     * colored with the positive/negative values colors, while values below the
     * threshold will be colored with the non-significant color.
     *
     * @return the lower significance level
     */
    public double getSignificanceLevel() {
        return significanceLevel;
    }

    /**
     * Set the lower significance level. Values above this level will be colored
     * with the positive/negative values colors, while values below the
     * threshold will be colored with the non-significant color.
     *
     * @param significanceLevel the lower significance level to set
     */
    public void setSignificanceLevel(double significanceLevel) {
        this.significanceLevel = significanceLevel;
    }

    /**
     * Returns the heat map cell border color.
     *
     * @return the heat map cell border color
     */
    public Color getHeatMapBorderColor() {
        return heatMapBorderColor;
    }

    /**
     * Set the the heat map cell border color.
     *
     * @param heatMapBorderColor the the heat map cell border color
     */
    public void setHeatMapBorderColor(Color heatMapBorderColor) {
        this.heatMapBorderColor = heatMapBorderColor;
    }

    /**
     * Returns true of log scale is used for the visualizations.
     *
     * @return true of log scale is used for the visualizations
     */
    public boolean isLogScale() {
        return logScale;
    }

    /**
     * Set if log scale is to be used for the visualizations.
     *
     * @param logScale if log scale is to be used for the visualizations
     */
    public void setLogScale(boolean logScale) {
        this.logScale = logScale;
        if (logScale) {
            if (maxValue != 0) {
                maxLogValue = Math.log10(maxValue);
            }
            if (minValue != 0) {
                minLogValue = Math.log10(minValue);
            }
        }
    }

    /**
     * Returns the maximum value.
     *
     * @return the maxValue
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Returns the minimum value.
     *
     * @return the minValue
     */
    public double getMinValue() {
        return minValue;
    }
}
