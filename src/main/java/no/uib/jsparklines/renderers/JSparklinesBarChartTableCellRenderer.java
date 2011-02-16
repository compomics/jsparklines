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
     * An enumerator of the supported color gradient types.
     */
    public enum ColorGradient {

        redGreen, greenRed, greenBlue, blueGreen, greenYellow, yellowGreen, greenPurple, purpleGreen, redMagenta, magentaRed
    }
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
     * If true a red/green gradient coloring is used.
     */
    private boolean gradientColoring = false;
    /**
     * The currently selected color gradient.
     */
    private ColorGradient currentColorGradient = ColorGradient.greenRed;
    /**
     * If true, the values are shown as a heat map.
     */
    private boolean showAsHeatMap = false;

    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when only positive
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
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when only positive
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
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when only positive
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
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when positive
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
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when positive
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
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor when positive
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
     * Displays the values as a heat map using the selected color gradient.
     * To disable the heat map use null as the paramater.
     * <br><br>
     * <b>NB: the programmer has to make sure that the max and min values are
     * the same for all columns used in a heat map to ensure that the color
     * coding is comparable accross the columns. This method can not handle
     * this.</b>
     * <br><br>
     * Values below zero uses the first color in the gradient name, while values
     * above zero uses the second color in the gradient, i.e., if the column contains
     * only positive values only the second color will be used.
     * <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color gradient
     */
    public void showAsHeatMap(ColorGradient colorGradient) {

        this.showAsHeatMap = (colorGradient != null);
        gradientColoring = (colorGradient != null);
        this.currentColorGradient = colorGradient;

        if (showAsHeatMap) {
            if (Math.abs(minValue) > maxValue) {
                maxValue = Math.abs(minValue);
            }
        }
    }

    /**
     * Set the color gradient to use for the bars. To disable the color gradient
     * use null as the paramater.
     * <br><br>
     * Values below zero uses the first color in the gradient name, while values
     * above zero uses the second color in the gradient, i.e., if the column contains
     * only positive values only the second color will be used.
     * <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     * 
     * @param colorGradient the color gradient to use, null disables the color gradient
     */
    public void setGradientColoring(ColorGradient colorGradient) {

        this.gradientColoring = (colorGradient != null);

        this.currentColorGradient = colorGradient;

        if (gradientColoring) {
            if (Math.abs(minValue) > maxValue) {
                maxValue = Math.abs(minValue);
            }
        }
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

            if (showAsHeatMap) {
                dataset.addValue(maxValue, "1", "1");
            } else {
                dataset.addValue(((Double) value), "1", "1");
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

            if (showAsHeatMap) {
                dataset.addValue(maxValue, "1", "1");
            } else {
                dataset.addValue(((Integer) value), "1", "1");
            }

        } else if (value instanceof XYDataPoint) {

            if (((XYDataPoint) value).getX() < minimumChartValue && ((XYDataPoint) value).getX() > 0) {
                value = minimumChartValue;
            }

            if (showAsHeatMap) {
                dataset.addValue(maxValue, "1", "1");
            } else {
                dataset.addValue(((XYDataPoint) value).getX(), "1", "1");
            }
        }

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // set the axis range
        if (showAsHeatMap) {
            plot.getRangeAxis().setRange(0, maxValue);
        } else {
            plot.getRangeAxis().setRange(minValue, maxValue);
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
                currentColor = findGradientColor((Double) value);
                renderer = new BarChartColorRenderer(currentColor);
            } else {

                if (((Double) value).doubleValue() >= 0) {
                    currentColor = positiveValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                } else {
                    currentColor = negativeValuesColor;
                    renderer = new BarChartColorRenderer(currentColor);
                }
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

            if (gradientColoring) {
                currentColor = findGradientColor(((Integer) value).doubleValue());
                renderer = new BarChartColorRenderer(currentColor);
            } else {

                if (((Integer) value).intValue() >= 0) {
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
        }

        // make sure the background is the same as the table row color
        if (showAsHeatMap) {
            plot.setBackgroundPaint(currentColor);
            chartPanel.setBackground(currentColor);
            chart.setBackgroundPaint(currentColor);
            this.setBackground(currentColor);
        } else {
            plot.setBackgroundPaint(c.getBackground());
            chartPanel.setBackground(c.getBackground());
            chart.setBackgroundPaint(c.getBackground());
        }

        plot.setRenderer(renderer);

        return this;
    }

    /**
     * Returns the gradient color using the currently selected color gradient.
     * Values below zero uses the first color in the gradient, while values
     * above zero uses the second color in the gradient. If the column contains
     * only positive values only the second color will be used.
     *
     * @param value the value to find the gradient color for
     * @return      the gradient color
     */
    private Color findGradientColor(Double value) {

        int numberOfColorLevels = 50;
        double distanceBetweenCorrelationLevels = maxValue / (((double) numberOfColorLevels) / 2);

        Color backGroundColor = null;

        // find the color for the values below zero
        for (int i = 0; i < (numberOfColorLevels / 2); i++) {

            // find the lower and upper range for the current color
            Double lowerRange = new Double(-maxValue + (i * distanceBetweenCorrelationLevels));
            Double upperRange = new Double(-maxValue + ((i + maxValue) * distanceBetweenCorrelationLevels));

            Color tempColor = null;

            // calculate the current color
            if (currentColorGradient == ColorGradient.greenRed) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (currentColorGradient == ColorGradient.redGreen) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (currentColorGradient == ColorGradient.greenBlue) {
                tempColor = new Color(0, 255 - (i * 10), 50 - (i * 2));
            } else if (currentColorGradient == ColorGradient.blueGreen) {
                tempColor = new Color(0, 50 - (i * 2), 255 - (i * 10));
            } else if (currentColorGradient == ColorGradient.greenYellow) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (currentColorGradient == ColorGradient.yellowGreen) {
                tempColor = new Color(255 - 10 * i, 255 - 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.greenPurple) {
                tempColor = new Color(50 - (i * 2), 255 - (i * 10), 0);
            } else if (currentColorGradient == ColorGradient.purpleGreen) {
                tempColor = new Color(255 - 10 * i, 0, 255 - 10 * i);
            } else if (currentColorGradient == ColorGradient.redMagenta) {
                tempColor = new Color(255 - (i * 10), 50 - (i * 2), 0);
            } else if (currentColorGradient == ColorGradient.magentaRed) {
                tempColor = new Color(0, 255 - 10 * i, 255 - 10 * i);
            }

            // see of the value is in the wanted range
            if (value.doubleValue() >= lowerRange && value.doubleValue() < upperRange) {
                backGroundColor = tempColor;
            }
        }

        // find the color for the values above zero
        for (int i = 0; i < (numberOfColorLevels / 2); i++) {

            // find the lower and upper range for the current color
            Double lowerRange = new Double(0.0 + distanceBetweenCorrelationLevels * i);
            Double upperRange = new Double(0.0 + distanceBetweenCorrelationLevels * (i + maxValue));

            Color tempColor = null;

            // calculate the current color
            if (currentColorGradient == ColorGradient.greenRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            } else if (currentColorGradient == ColorGradient.redGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.greenBlue) {
                tempColor = new Color(0, 0, 15 + 10 * i);
            } else if (currentColorGradient == ColorGradient.greenBlue) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.greenYellow) {
                tempColor = new Color(15 + 10 * i, 15 + 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.yellowGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.greenPurple) {
                tempColor = new Color(15 + 10 * i, 0, 15 + 10 * i);
            } else if (currentColorGradient == ColorGradient.purpleGreen) {
                tempColor = new Color(0, 15 + 10 * i, 0);
            } else if (currentColorGradient == ColorGradient.redMagenta) {
                tempColor = new Color(0, 15 + 10 * i, 15 + 10 * i);
            } else if (currentColorGradient == ColorGradient.magentaRed) {
                tempColor = new Color(15 + 10 * i, 0, 0);
            }

            // see of the value is in the wanted range
            if ((value.doubleValue() >= lowerRange && value.doubleValue() < upperRange)
                    || (upperRange.doubleValue() == maxValue && value.doubleValue() == upperRange.doubleValue())) {
                backGroundColor = tempColor;
            }
        }

        // calculate the color for values outside the value range
        if (value.doubleValue() < minValue) {

            // calculate the color for values smaller than the lower range
            if (currentColorGradient == ColorGradient.greenRed) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.redGreen) {
                backGroundColor = Color.RED;
            } else if (currentColorGradient == ColorGradient.greenBlue) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.blueGreen) {
                backGroundColor = Color.BLUE;
            } else if (currentColorGradient == ColorGradient.greenYellow) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.yellowGreen) {
                backGroundColor = Color.YELLOW;
            } else if (currentColorGradient == ColorGradient.greenPurple) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.purpleGreen) {
                backGroundColor = new Color(255, 0, 255);
            } else if (currentColorGradient == ColorGradient.redMagenta) {
                backGroundColor = Color.RED;
            } else if (currentColorGradient == ColorGradient.magentaRed) {
                backGroundColor = new Color(0, 255, 255);
            }

        } else if (value.doubleValue() > maxValue) {

            // calculate the color for values bigger than the upper range
            if (currentColorGradient == ColorGradient.greenRed) {
                backGroundColor = Color.RED;
            } else if (currentColorGradient == ColorGradient.redGreen) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.greenBlue) {
                backGroundColor = Color.BLUE;
            } else if (currentColorGradient == ColorGradient.blueGreen) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.greenYellow) {
                backGroundColor = Color.YELLOW;
            } else if (currentColorGradient == ColorGradient.yellowGreen) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.greenPurple) {
                backGroundColor = new Color(255, 0, 255);
            } else if (currentColorGradient == ColorGradient.purpleGreen) {
                backGroundColor = Color.GREEN;
            } else if (currentColorGradient == ColorGradient.redMagenta) {
                backGroundColor = new Color(0, 255, 255);
            } else if (currentColorGradient == ColorGradient.magentaRed) {
                backGroundColor = Color.RED;
            }
        }

        return backGroundColor;
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
