package no.uib.jsparklines.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.StartIndexes;
import no.uib.jsparklines.renderers.util.GradientColorCoding.ColorGradient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Table cell renderer displaying multiple intervals. Supported input:
 * StartIndexes objects. Other object types are rendered using the
 * DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesMultiIntervalChartTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * Turns of the gradient painting for the interval charts.
     */
    static {
        IntervalBarRenderer.setDefaultBarPainter(new StandardBarPainter());
    }
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
     * The label used to display the number and the interval chart at the same
     * time.
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
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * If true the value of the chart is shown next to the interval chart.
     */
    private boolean showNumberAndChart = false;
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
     * The colors to use for the intervals with positive numbers.
     */
    private Color positiveValuesColor = new Color(255, 51, 51);
    /**
     * If true a red/green gradient coloring is used.
     */
    private boolean gradientColoring = false;
    /**
     * The width of the interval used to highlight the value.
     */
    private double widthOfInterval;
    /**
     * If true, a black reference line is added in the center of the plot.
     */
    private boolean showReferenceLine = false;
    /**
     * The reference line width.
     */
    private double referenceLineWidth = 0.03;
    /**
     * The reference line color.
     */
    private Color referenceLineColor = Color.BLACK;
    /**
     * The plot orientation.
     */
    private PlotOrientation plotOrientation;

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. Use this
     * constructor when positive and negative values are to be plotted.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param widthOfInterval the width of the interval used to highlight the
     * value
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     */
    public JSparklinesMultiIntervalChartTableCellRenderer(
            PlotOrientation plotOrientation, Double maxValue, Double widthOfInterval,
            Color positiveValuesColor) {

        this.positiveValuesColor = positiveValuesColor;

        this.maxValue = maxValue;
        this.widthOfInterval = widthOfInterval;
        this.plotOrientation = plotOrientation;

        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Sets up the table cell renderer and the interval chart.
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
     * If true, a black reference line is shown in the middle of the plot.
     *
     * @param showReferenceLine if true, a black reference line is shown in the
     * middle of the plot
     */
    public void showReferenceLine(boolean showReferenceLine) {
        this.showReferenceLine = showReferenceLine;
    }

    /**
     * If true, a black reference line is shown in the middle of the plot.
     *
     * @param showReferenceLine if true, a reference line is shown in the
     * middle of the plot
     * @param lineWidth the line width
     * @param color the color
     */
    public void showReferenceLine(boolean showReferenceLine, double lineWidth, Color color) {
        this.showReferenceLine = showReferenceLine;
        this.referenceLineWidth = lineWidth;
        this.referenceLineColor = color;
    }

    /**
     * Set the color gradient to use for the intervals. To disable the color
     * gradient use null as the parameter. <br><br> Values below zero uses the
     * first color in the gradient name, while values above zero uses the third
     * color in the gradient. <br><br> Note that the max value is set to the
     * maximum absolute value of the max and min values in order to make the
     * color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     */
    public void setGradientColoring(ColorGradient colorGradient) {
        setGradientColoring(colorGradient, null);
    }

    /**
     * Set the color gradient to use for the intervals. To disable the color
     * gradient use null as the parameter. <br><br> Values below zero uses the
     * first color in the gradient name, while values above zero uses the third
     * color in the gradient. <br><br> Note that the max value is set to the
     * maximum absolute value of the max and min values in order to make the
     * color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color
     * gradient
     * @param plotBackgroundColor the background color to use, for gradients
     * using white as the "middle" color, it's recommended to use a dark
     * background color
     */
    public void setGradientColoring(ColorGradient colorGradient, Color plotBackgroundColor) {

        this.gradientColoring = (colorGradient != null);
        this.plotBackgroundColor = plotBackgroundColor;

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
     * If true the number will be shown together with the interval chart in the
     * cell. False only display the interval chart. This method is not to be
     * confused with the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
    }

    /**
     * If true the number will be shown together with the interval chart in the
     * cell. False only display the interval chart. This method is not to be
     * confused with the showNumbers-method that only displays the numbers.
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
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        labelHorizontalAlignement = horizontalAlignement;
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
     * Set if the underlying numbers or the interval charts are to be shown.
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

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());

        if (plotBackgroundColor != null) {
            setBackground(plotBackgroundColor);
        } else {
            setBackground(c.getBackground());
        }

        // if the cell is empty, simply return
        if (value == null || !(value instanceof StartIndexes)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // get the actual data to display
        StartIndexes startIndexes = (StartIndexes) value;
        ArrayList<Integer> indexes = startIndexes.getIndexes();

        // if show numbers, format as number and return
        if (showNumbers) {

            String numbersAsString = "";

            if (indexes.isEmpty()) {
                numbersAsString = null;
            } else {
                for (int i = 0; i < indexes.size() - 1; i++) {
                    numbersAsString += indexes.get(i) + ",";
                }

                numbersAsString += indexes.get(indexes.size() - 1);
            }

            c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, numbersAsString,
                    isSelected, hasFocus, row, column);

            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

            return c;
        }

        // set the tooltip text
        String numbersAsString;

        if (indexes.isEmpty()) {
            numbersAsString = null;
        } else {
            numbersAsString = "<html>";

            for (int i = 0; i < indexes.size() - 1; i++) {
                numbersAsString += indexes.get(i) + ",";
            }

            numbersAsString += indexes.get(indexes.size() - 1);
            numbersAsString += "</html>";
        }

        this.setToolTipText(numbersAsString);

        // show the number _and_ the chart if option selected
        if (showNumberAndChart) {

            if (indexes.isEmpty()) {
                valueLabel.setText(null);
            } else if (indexes.size() > 1) {
                valueLabel.setText(indexes.size() + "x");
            } else { // == 1
                valueLabel.setText("" + indexes.get(0));
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

        // create the chart
        DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setShadowVisible(false);

        int indexCounter = 0;
        int seriesCounter = 0;

        for (int i = 0; i < indexes.size(); i++) {

            // add filler to the left
            barChartDataset.addValue(indexes.get(i) - 1 - indexCounter, "" + seriesCounter, "1");
            renderer.setSeriesPaint(seriesCounter++, new Color(0, 0, 0, 0));
            indexCounter += indexes.get(i) - 1 - indexCounter;

            // add the mark
            barChartDataset.addValue(widthOfInterval, "" + seriesCounter, "1");
            renderer.setSeriesPaint(seriesCounter++, positiveValuesColor);
            indexCounter += widthOfInterval;
        }

        if (!indexes.isEmpty()) {
            // add filler to the right
            barChartDataset.addValue(maxValue - indexCounter, "" + seriesCounter, "1");
            renderer.setSeriesPaint(seriesCounter++, new Color(0, 0, 0, 0));
        }

        chart = ChartFactory.createStackedBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // set the axis range
        plot.getRangeAxis().setRange(minValue, maxValue * 1.02);

        // remove space before/after the domain axis
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);

        // remove space before/after the range axis
        plot.getRangeAxis().setUpperMargin(0);
        plot.getRangeAxis().setLowerMargin(0);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // add a reference line in the middle of the dataset
        if (showReferenceLine) {
            DefaultCategoryDataset referenceLineDataset = new DefaultCategoryDataset();
            referenceLineDataset.addValue(maxValue * 1.02, "A", "B");
            plot.setDataset(1, referenceLineDataset);
            LayeredBarRenderer referenceLineRenderer = new LayeredBarRenderer();
            referenceLineRenderer.setSeriesBarWidth(0, referenceLineWidth);
            referenceLineRenderer.setSeriesFillPaint(0, referenceLineColor);
            referenceLineRenderer.setSeriesPaint(0, referenceLineColor);
            plot.setRenderer(1, referenceLineRenderer);
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

        plot.setRenderer(0, renderer);

        // make sure the background is the same as the table row color
        if (plotBackgroundColor != null) {
            chart.getPlot().setBackgroundPaint(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {
            chart.getPlot().setBackgroundPaint(c.getBackground());
            chart.setBackgroundPaint(c.getBackground());
        }

        // create the chart panel and add it to the table cell
        chartPanel = new ChartPanel(chart);

        if (plotBackgroundColor != null) {
            chartPanel.setBackground(plotBackgroundColor);
        } else {
            chartPanel.setBackground(c.getBackground());
        }

        this.remove(1);
        this.add(chartPanel);

        return this;
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
     * Set the color used for the positive values.
     *
     * @param positiveValuesColor the color used for the positive values
     */
    public void setPositiveValuesColor(Color positiveValuesColor) {
        this.positiveValuesColor = positiveValuesColor;
    }
}
