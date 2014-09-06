package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.SignificantStatisticalCategoryDataset;
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import no.uib.jsparklines.renderers.util.StatisticalBarChartColorRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Table cell renderer displaying JSparklines bar charts with error bars.
 * Supported input: DefaultStatisticalCategoryDataset and
 * SignificantStatisticalCategoryDataset objects. Other object types are
 * rendered using the DefaultTableCellRenderer.
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
     * The label used to display the significance.
     */
    private JLabel signifianceLabel;
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
     * If true the value of the chart is shown next to the bar chart.
     */
    private boolean showNumberAndChart = false;
    /**
     * The decimal format for use when showing the numbers.
     */
    private DecimalFormat numberFormat = new DecimalFormat("0.00");
    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;
    /**
     * If true, a star is added after all the significant datasets. Only has an
     * effect on SignificantStatisticalCategoryDataset.
     */
    private boolean indicateSignificance = false;
    /**
     * The background color used for the plots. For plots using light colors,
     * it's recommended to use a dark background color, and for plots using
     * darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;
    /**
     * The currently selected color gradient.
     */
    private GradientColorCoding.ColorGradient currentColorGradient = GradientColorCoding.ColorGradient.RedBlackBlue;
    /**
     * The first color of the gradient is used for values close to the min
     * value, while the third color of the gradient is used for values close to
     * the max value. If only positive values are expected
     * (positiveColorGradient is true) the middle gradient color is used for the
     * halfway point between the min and max values. If both positive and
     * negative values are expected (positiveColorGradient is false) the middle
     * gradient color is used for values around zero.
     */
    private boolean positiveColorGradient = false;
    /**
     * If true a red/green gradient coloring is used.
     */
    private boolean gradientColoring = false;
    /**
     * The width of the error stroke bars.
     */
    private float errorBarWidth = 4;

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
     * @throws IllegalArgumentException if maxValue &lt; 0
     */
    public JSparklinesErrorBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, boolean largeNumbersAreGood) {

        if (largeNumbersAreGood) {
            positiveValuesColor = tableCellBarChartColorBig;
        } else {
            positiveValuesColor = tableCellBarChartColorSmall;
        }

        this.maxValue = maxValue;

        setUpRendererAndChart(plotOrientation);

        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue has to be a positive integer! Current value: " + maxValue + ".");
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
     * @throws IllegalArgumentException if maxValue &lt; 0
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
     * @throws IllegalArgumentException if minValue &gt; maxValue
     */
    public JSparklinesErrorBarChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue) {

        this.maxValue = maxValue;
        this.minValue = minValue;

        setUpRendererAndChart(plotOrientation);

        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue has to be smaller than maxValue! Current values: minValue: " + minValue + ", maxValue: " + maxValue + ".");
        }
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
     * @throws IllegalArgumentException if minValue &gt; maxValue
     */
    public JSparklinesErrorBarChartTableCellRenderer(
            PlotOrientation plotOrientation, Double minValue, Double maxValue,
            Color negativeValuesColor, Color positiveValuesColor) {

        this.negativeValuesColor = negativeValuesColor;
        this.positiveValuesColor = positiveValuesColor;

        this.maxValue = maxValue;
        this.minValue = minValue;
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

        signifianceLabel = new JLabel("");
        signifianceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        this.add(chartPanel);
        this.add(signifianceLabel);
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
    public void setGradientColoring(GradientColorCoding.ColorGradient colorGradient, boolean positiveColorGradient) {
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
    public void setGradientColoring(GradientColorCoding.ColorGradient colorGradient, boolean positiveColorGradient, Color plotBackgroundColor) {

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
     * Set the width of the error bars.
     *
     * @param errorBarWidth the width of the error bars
     */
    public void setErrorBarWidth(float errorBarWidth) {
        this.errorBarWidth = errorBarWidth;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // check if the cell contain a color object
        if (value == null || !(value instanceof DefaultStatisticalCategoryDataset)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // set the tooltip text
        DefaultStatisticalCategoryDataset tempSet = (DefaultStatisticalCategoryDataset) value;
        this.setToolTipText("<html>Intensity: " + roundDouble((Double) tempSet.getMeanValue(0, 0), 4) + "<br>"
                + "STDEV: " + roundDouble((Double) tempSet.getStdDevValue(0, 0), 4) + "</html>");

        // check if significance is to be indicated
        if (indicateSignificance && value instanceof SignificantStatisticalCategoryDataset) {
            if (((SignificantStatisticalCategoryDataset) value).isSignificant() != null
                    && ((SignificantStatisticalCategoryDataset) value).isSignificant()) {
                signifianceLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/asterisk.png")));
            }
        }

        // make sure that the significance area is shown if needed
        if (indicateSignificance) {
            signifianceLabel.setMinimumSize(new Dimension(10, 0));
        } else {
            signifianceLabel.setMinimumSize(new Dimension(0, 0));
        }

        // show the number _and_ the chart if option selected
        if (showNumberAndChart) {

            // set the decimal format symbol
            numberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            double temp = tempSet.getMeanValue(0, 0).doubleValue();
            valueLabel.setText(numberFormat.format(temp));

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
        StatisticalBarChartColorRenderer renderer;

        if (gradientColoring) {
            Color currentColor = GradientColorCoding.findGradientColor((Double) tempSet.getMeanValue(0, 0), minValue, maxValue, currentColorGradient, positiveColorGradient);
            renderer = new StatisticalBarChartColorRenderer(currentColor);
        } else {
            if ((Double) tempSet.getMeanValue(0, 0) >= 0) {
                renderer = new StatisticalBarChartColorRenderer(positiveValuesColor);
            } else {
                renderer = new StatisticalBarChartColorRenderer(negativeValuesColor);
            }
        }

        renderer.setErrorIndicatorStroke(new BasicStroke(errorBarWidth));
        plot.setRenderer(renderer);

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
     * Set if the significance is to be indicated or not. Note that significance
     * can only be indicated if SignificantStatisticalCategoryDataset is used.
     *
     * @param indicateSignificance
     */
    public void showSignificance(boolean indicateSignificance) {
        this.indicateSignificance = indicateSignificance;
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
}
