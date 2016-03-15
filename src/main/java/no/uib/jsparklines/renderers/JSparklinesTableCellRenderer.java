package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.AreaRenderer;
import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.data.JSparklinesDataset;
import no.uib.jsparklines.renderers.util.ReferenceArea;
import no.uib.jsparklines.renderers.util.ReferenceLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

/**
 * Table cell renderer displaying JSparklines plots consisting of multiple
 * values per data series. Supported input: JSparklinesDataset objects. Other
 * object types are rendered using the DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * List of supported plot types.
     */
    public enum PlotType {

        /**
         * Bar chart.
         */
        barChart,
        /**
         * Line chart.
         */
        lineChart,
        /**
         * Pie chart.
         */
        pieChart,
        /**
         * Stacked bar chart.
         */
        stackedBarChart,
        /**
         * Stacked bar chart in percent.
         */
        stackedPercentBarChart,
        /**
         * Area chart.
         */
        areaChart,
        /**
         * Box plot.
         */
        boxPlot,
        /**
         * Up/down chart.
         */
        upDownChart,
        /**
         * Protein sequence chart.
         */
        proteinSequence,
        /**
         * Difference chart.
         */
        difference,
        /**
         * Stacked bar chart in integer with upper range.
         */
        stackedBarChartIntegerWithUpperRange
    }
    /**
     * If true, a black reference line is added to the protein sequence plots.
     * No effect on the other plot types.
     */
    private boolean showProteinSequenceReferenceLine = true;
    /**
     * The reference line width.
     */
    private double referenceLineWidth = 0.03;
    /**
     * The reference line color.
     */
    private Color referenceLineColor = Color.BLACK;
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
     * The current plot type.
     */
    private PlotType plotType;
    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The maximum value. Used to set the upper range for the charts.
     */
    private double maxValue = 0;
    /**
     * The minimum value. Used to set the lower range for the charts.
     */
    private double minValue = 0;
    /**
     * The plot orientation.
     */
    private PlotOrientation plotOrientation;
    /**
     * The color used to highlight the maximum values in the chart.
     */
    private Color maxValueColor = new Color(251, 51, 51);
    /**
     * The color used to highlight the minimum values in the chart.
     */
    private Color minValueColor = new Color(51, 51, 251);
    /**
     * The color used for the 'up values' in the Up/Down charts.
     */
    private Color upColor = new Color(251, 51, 51);
    /**
     * The color used for the 'down values' in the Up/Down charts.
     */
    private Color downColor = new Color(51, 51, 251);
    /**
     * If true the max and min values in line and area plots are highlighted.
     *
     * Note: experimental feature, not yet finished.
     */
    private boolean highlightMaxAndMin = false;
    /**
     * The width of the max and min highlights. See highlightMaxAndMin above.
     */
    private double widthOfMaxAndMinHighlight = 0.4;
    /**
     * A hashmap of the current reference lines. Key is the name of the
     * reference line.
     */
    private HashMap<String, ReferenceLine> referenceLines;
    /**
     * A hashmap of the current reference areas. Key is the name of the
     * reference area.
     */
    private HashMap<String, ReferenceArea> referenceAreas;
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
     * If true the value of the chart is shown next to the bar chart. This is
     * currently only supported for the plot type
     * 'stackedBarChartIntegerWithUpperRange' and only displays the first number
     * in the first data series.
     */
    private boolean showNumberAndChart = false;
    /**
     * If true the underlying numbers are shown instead of the charts. This is
     * currently only supported for the plot type
     * 'stackedBarChartIntegerWithUpperRange' and only displays the first number
     * in the first data series.
     */
    private boolean showNumbers = false;
    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;
    /**
     * The label used to display the number and the bar chart at the same time.
     */
    private JLabel valueLabel;

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when
     * creating pie charts where no upper range is used.
     *
     * @param plotType the plot type
     * @param plotOrientation the orientation of the plot
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation) {
        this(plotType, plotOrientation, 0.0, 0.0);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when
     * only positive values are to be plotted.
     *
     * @param plotType the plot type
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable (this is the same as setting the minimum value to 0)
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double maxValue) {
        this(plotType, plotOrientation, 0.0, maxValue);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when
     * positive and negative values are to be plotted.
     *
     * @param plotType the plot type
     * @param plotOrientation the orientation of the plot
     * @param minValue the minium value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double minValue, Double maxValue) {

        this.plotType = plotType;
        this.plotOrientation = plotOrientation;
        this.minValue = minValue;
        this.maxValue = maxValue;

        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));

        referenceLines = new HashMap<String, ReferenceLine>();
        referenceAreas = new HashMap<String, ReferenceArea>();

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the chart. This method is currently only supported for
     * the plot type 'stackedBarChartIntegerWithUpperRange' and only displays
     * the first number in the first data series.
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
     * False only display the chart. This method is currently only supported for
     * the plot type 'stackedBarChartIntegerWithUpperRange' and only displays
     * the first number in the first data series.
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
     * False only display the chart. This method is currently only supported for
     * the plot type 'stackedBarChartIntegerWithUpperRange' and only displays
     * the first number in the first data series.
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
     * False only display the chart. This method is currently only supported for
     * the plot type 'stackedBarChartIntegerWithUpperRange' and only displays
     * the first number in the first data series.
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
     * Set if the underlying numbers or the bar charts are to be shown. This
     * method is currently only supported for the plot type
     * 'stackedBarChartIntegerWithUpperRange' and only displays the first number
     * in the first data series.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    /**
     * If true, a black reference line is added to the protein sequence plots.
     * No effect on the other plot types.
     *
     * @param showProteinSequenceReferenceLine if true, a black reference line
     * is added to the protein sequence plots
     */
    public void showProteinSequenceReferenceLine(boolean showProteinSequenceReferenceLine) {
        this.showProteinSequenceReferenceLine = showProteinSequenceReferenceLine;
    }

    /**
     * If true, a black reference line is shown in the middle of the plot.
     *
     * @param showProteinSequenceReferenceLine if true, a black reference line
     * is shown in the middle of the plot
     * @param lineWidth the line width
     * @param color the color
     */
    public void showProteinSequenceReferenceLine(boolean showProteinSequenceReferenceLine, double lineWidth, Color color) {
        this.showProteinSequenceReferenceLine = showProteinSequenceReferenceLine;
        this.referenceLineWidth = lineWidth;
        this.referenceLineColor = color;
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
        if (value == null || !(value instanceof JSparklinesDataset)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // get the dataset
        JSparklinesDataset sparklineDataset = (JSparklinesDataset) value;
        ArrayList<Color> colors = new ArrayList<Color>();
        int dataCounter = 0;

        StringBuilder tooltip = new StringBuilder();
        tooltip.append("<html>");

        // show the number and/or the chart if option selected
        if ((showNumberAndChart || showNumbers) && plotType == PlotType.stackedBarChartIntegerWithUpperRange) {

            // set the decimal format symbol
            numberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            double sum = 0.0;

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {
                JSparklinesDataSeries series = sparklineDataset.getData().get(i);

                for (int j = 0; j < series.getData().size(); j++) {
                    sum += series.getData().get(j);
                }
            }

            if (showNumbers) {

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, (int) sum,
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

                Color bg = c.getBackground();
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure why.
                c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

                return c;
            }

            //valueLabel.setText(numberFormat.format(sum));
            valueLabel.setText("" + (int) sum);

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
        if (plotType == PlotType.barChart) {

            /////////////
            // BAR CHART
            /////////////
            DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                if (sparklineDataSeries.getSeriesLabel() != null) {
                    tooltip.append("<font color=rgb(");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                    tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                }

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    barChartDataset.addValue(sparklineDataSeries.getData().get(j), "1", Integer.valueOf(dataCounter++));
                    colors.add(sparklineDataSeries.getSeriesColor());
                }
            }

            chart = ChartFactory.createBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

            // fine tune the chart properites
            CategoryPlot plot = chart.getCategoryPlot();

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // add reference lines, if any
            Iterator<String> allReferencesLines = referenceLines.keySet().iterator();

            while (allReferencesLines.hasNext()) {
                ReferenceLine currentReferenceLine = referenceLines.get(allReferencesLines.next());
                plot.addRangeMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
            }

            // add reference areas, if any
            Iterator<String> allReferenceAreas = referenceAreas.keySet().iterator();

            while (allReferenceAreas.hasNext()) {
                ReferenceArea currentReferenceArea = referenceAreas.get(allReferenceAreas.next());
                IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
                marker.setAlpha(currentReferenceArea.getAlpha());
                plot.addRangeMarker(marker);
            }

            // set the axis range
            if (maxValue > 0) {
                plot.getRangeAxis().setRange(minValue, maxValue);
            }

            // add the dataset
            plot.setDataset(barChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set up the chart renderer
            BarChartColorRenderer renderer = new BarChartColorRenderer(colors);
            renderer.setShadowVisible(false);
            plot.setRenderer(renderer);

        } else if (plotType == PlotType.lineChart || plotType == PlotType.areaChart || plotType == PlotType.difference) {

            //////////////////////////////////
            // LINE, AREA or DIFFERENCE CHART
            //////////////////////////////////
            AbstractXYItemRenderer renderer;

            // set up the chart renderer
            if (plotType == PlotType.lineChart) {
                renderer = new XYLineAndShapeRenderer(true, false);
            } else if (plotType == PlotType.areaChart) {
                renderer = new AreaRenderer();
                ((AreaRenderer) renderer).setOutline(true);
            } else { // plotType == PlotType.difference
                renderer = new XYDifferenceRenderer(downColor, upColor, false);
            }

            // variables for storing the max and min values
            double plotMaxValue = Double.MIN_VALUE;
            double plotMinValue = Double.MAX_VALUE;
            int indexOfMaxValue = -1;
            int indexOfMinValue = -1;

            XYSeriesCollection lineChartDataset = new XYSeriesCollection();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                if (sparklineDataSeries.getSeriesLabel() != null) {
                    tooltip.append("<font color=rgb(");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                    tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                }

                XYSeries tempSeries = new XYSeries(i);
                XYSeries xAxisSeries = new XYSeries("x-axis");

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    tempSeries.add(j, sparklineDataSeries.getData().get(j));
                    xAxisSeries.add(j, 0);

                    if (sparklineDataSeries.getData().get(j) > plotMaxValue) {
                        plotMaxValue = sparklineDataSeries.getData().get(j);
                        indexOfMaxValue = j;
                    }

                    if (sparklineDataSeries.getData().get(j) < plotMinValue) {
                        plotMinValue = sparklineDataSeries.getData().get(j);
                        indexOfMinValue = j;
                    }
                }

                lineChartDataset.addSeries(tempSeries);

                if (plotType == PlotType.difference) {
                    lineChartDataset.addSeries(xAxisSeries);
                    renderer.setSeriesStroke(0, new BasicStroke(0));
                    renderer.setSeriesStroke(1, new BasicStroke(0));
                } else if (plotType == PlotType.lineChart) {
                    renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());
                    renderer.setSeriesStroke(i, sparklineDataSeries.getLineType());
                } else {
                    renderer.setSeriesFillPaint(i, new GradientPaint(
                            0f, 0f, sparklineDataSeries.getSeriesColor().brighter().brighter(),
                            0f, 0f, sparklineDataSeries.getSeriesColor().darker().darker()));
                    renderer.setSeriesOutlinePaint(i, sparklineDataSeries.getSeriesColor());
                }
            }

            chart = ChartFactory.createXYLineChart(null, null, null, lineChartDataset, plotOrientation, false, false, false);

            // fine tune the chart properites
            XYPlot plot = chart.getXYPlot();

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // add markers of max and min
            // note: experimental feature, not finished
            if (highlightMaxAndMin) {
                plot.addDomainMarker(new IntervalMarker(
                        indexOfMaxValue - widthOfMaxAndMinHighlight, indexOfMaxValue + widthOfMaxAndMinHighlight,
                        maxValueColor.brighter().brighter().brighter(),
                        new BasicStroke(1.0f), Color.lightGray, new BasicStroke(0.1f), 0.5f), Layer.BACKGROUND);
                plot.addDomainMarker(new IntervalMarker(
                        indexOfMinValue - widthOfMaxAndMinHighlight, indexOfMinValue + widthOfMaxAndMinHighlight,
                        minValueColor.brighter().brighter().brighter(),
                        new BasicStroke(1.0f), Color.lightGray, new BasicStroke(0.1f), 0.5f), Layer.BACKGROUND);
            }

            // add reference lines, if any
            Iterator<String> allReferencesLines = referenceLines.keySet().iterator();

            while (allReferencesLines.hasNext()) {
                ReferenceLine currentReferenceLine = referenceLines.get(allReferencesLines.next());
                plot.addRangeMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
            }

            // add reference areas, if any
            Iterator<String> allReferenceAreas = referenceAreas.keySet().iterator();

            while (allReferenceAreas.hasNext()) {
                ReferenceArea currentReferenceArea = referenceAreas.get(allReferenceAreas.next());
                IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
                marker.setAlpha(currentReferenceArea.getAlpha());
                plot.addRangeMarker(marker);
            }

            // set the axis range
            if (maxValue > 0) {
                plot.getRangeAxis().setRange(minValue, maxValue);
            }

            // add the dataset
            plot.setDataset(lineChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set the renderer
            plot.setRenderer(renderer);

        } else if (plotType == PlotType.pieChart) {

            //////////////
            // PIE CHART
            //////////////
            DefaultPieDataset pieDataset = new DefaultPieDataset();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                if (sparklineDataSeries.getSeriesLabel() != null) {
                    tooltip.append("<font color=rgb(");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                    tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                }

                double sumOfValues = 0.0;

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    sumOfValues += sparklineDataSeries.getData().get(j);
                }

                pieDataset.setValue(sparklineDataSeries.getSeriesLabel(), sumOfValues);
            }

            // create the chart
            chart = ChartFactory.createPieChart(null, pieDataset, false, false, false);

            // hide the labels and remove the shadow
            PiePlot piePlot = ((PiePlot) chart.getPlot());
            piePlot.setCircular(true);
            piePlot.setLabelGenerator(null);
            piePlot.setShadowXOffset(0);
            piePlot.setShadowYOffset(0);

            // set the series colors
            for (int i = 0; i < sparklineDataset.getData().size(); i++) {
                piePlot.setSectionPaint(sparklineDataset.getData().get(i).getSeriesLabel(), sparklineDataset.getData().get(i).getSeriesColor());
            }

        } else if (plotType == PlotType.stackedBarChart || plotType == PlotType.stackedPercentBarChart
                || plotType == PlotType.stackedBarChartIntegerWithUpperRange || plotType == PlotType.proteinSequence) {

            /////////////////////
            // STACKED BAR CHART
            /////////////////////
            DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

            StackedBarRenderer renderer = new StackedBarRenderer();
            renderer.setShadowVisible(false);

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                if (plotType != PlotType.stackedBarChartIntegerWithUpperRange) {
                    if (sparklineDataSeries.getSeriesLabel() != null) {
                        tooltip.append("<font color=rgb(");
                        tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                        tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                        tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                        tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                    }
                }

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {

                    barChartDataset.addValue(sparklineDataSeries.getData().get(j), "" + i, "" + j);
                    renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());

                    if (sparklineDataSeries.getSeriesLabel() != null && plotType == PlotType.stackedBarChartIntegerWithUpperRange) {
                        tooltip.append(sparklineDataSeries.getData().get(j).intValue());

                        if (i < sparklineDataset.getData().size() - 1) {
                            tooltip.append(" / ");
                        }
                    }
                }
            }

            chart = ChartFactory.createStackedBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

            // fine tune the chart properites
            CategoryPlot plot = chart.getCategoryPlot();

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // remove space before/after the range axis
            plot.getRangeAxis().setUpperMargin(0);
            plot.getRangeAxis().setLowerMargin(0);

            // add reference lines, if any
            Iterator<String> allReferencesLines = referenceLines.keySet().iterator();

            while (allReferencesLines.hasNext()) {
                ReferenceLine currentReferenceLine = referenceLines.get(allReferencesLines.next());
                plot.addRangeMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
            }

            // add reference areas, if any
            Iterator<String> allReferenceAreas = referenceAreas.keySet().iterator();

            while (allReferenceAreas.hasNext()) {
                ReferenceArea currentReferenceArea = referenceAreas.get(allReferenceAreas.next());
                IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
                marker.setAlpha(currentReferenceArea.getAlpha());
                plot.addRangeMarker(marker);
            }

            if (plotType == PlotType.stackedPercentBarChart || plotType == PlotType.proteinSequence) {
                renderer.setRenderAsPercentages(true);
            } else if (plotType == PlotType.stackedBarChartIntegerWithUpperRange) {
                plot.getRangeAxis().setRange(0, maxValue);
            } else if (maxValue > 0) {
                // set the axis range
                plot.getRangeAxis().setRange(minValue * sparklineDataset.getData().size(), maxValue * sparklineDataset.getData().size());
            }

            // add the dataset
            plot.setDataset(barChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            if (plotType == PlotType.proteinSequence && showProteinSequenceReferenceLine) {

                // add a reference line in the middle of the dataset
                DefaultCategoryDataset referenceLineDataset = new DefaultCategoryDataset();
                referenceLineDataset.addValue(1.0, "A", "B");
                plot.setDataset(1, referenceLineDataset);
                LayeredBarRenderer referenceLineRenderer = new LayeredBarRenderer();
                referenceLineRenderer.setSeriesBarWidth(0, referenceLineWidth);
                referenceLineRenderer.setSeriesFillPaint(0, referenceLineColor);
                referenceLineRenderer.setSeriesPaint(0, referenceLineColor);
                plot.setRenderer(1, referenceLineRenderer);
            }

            // set up the chart renderer
            plot.setRenderer(0, renderer);

        } else if (plotType == PlotType.boxPlot) {

            //////////////
            // BOX PLOT
            //////////////
            DefaultBoxAndWhiskerCategoryDataset boxPlotDataset = new DefaultBoxAndWhiskerCategoryDataset();

            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                ArrayList<Double> listValues = new ArrayList();

                if (sparklineDataSeries.getSeriesLabel() != null) {
                    tooltip.append("<font color=rgb(");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                    tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                }

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    listValues.add(sparklineDataSeries.getData().get(j));
                    renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());
                }

                boxPlotDataset.add(listValues, sparklineDataSeries.getSeriesLabel(), "1");
            }

            renderer.setMeanVisible(false);
            renderer.setMaximumBarWidth(0.5);

            CategoryPlot plot = new CategoryPlot(boxPlotDataset, new CategoryAxis(), new NumberAxis(), renderer);

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            plot.setOrientation(plotOrientation);

            chart = new JFreeChart(
                    null,
                    null,
                    plot,
                    false);

        } else if (plotType == PlotType.upDownChart) {

            /////////////////
            // UP/DOWN CHART
            /////////////////
            DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                if (sparklineDataSeries.getSeriesLabel() != null) {
                    tooltip.append("<font color=rgb(");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getRed()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getGreen()).append(",");
                    tooltip.append(sparklineDataSeries.getSeriesColor().getBlue()).append(")>");
                    tooltip.append(sparklineDataSeries.getSeriesLabel()).append("<br>");
                }

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {

                    if (sparklineDataSeries.getData().get(j) > 0) {
                        barChartDataset.addValue(1, "1", Integer.valueOf(dataCounter++));
                        colors.add(upColor);
                    } else {
                        barChartDataset.addValue(-1, "1", Integer.valueOf(dataCounter++));
                        colors.add(downColor);
                    }
                }
            }

            chart = ChartFactory.createBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

            // fine tune the chart properites
            CategoryPlot plot = chart.getCategoryPlot();

            // set the axis range
            plot.getRangeAxis().setRange(-1, 1);

            // add the dataset
            plot.setDataset(barChartDataset);

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set up the chart renderer
            BarChartColorRenderer renderer = new BarChartColorRenderer(colors);
            renderer.setShadowVisible(false);
            plot.setRenderer(renderer);
        }

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

        this.remove(1);
        this.add(chartPanel);

        return this;
    }

    /**
     * Add a reference line at a given data value.
     *
     * @param label the label for the reference
     * @param value the reference line value
     * @param lineWidth the line width, has to non-negative
     * @param lineColor the line color
     */
    public void addReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        referenceLines.put(label, new ReferenceLine(label, value, lineWidth, lineColor));
    }

    /**
     * Add a reference line at a given data value.
     *
     * @param referenceLine the reference line
     */
    public void addReferenceLine(ReferenceLine referenceLine) {
        referenceLines.put(referenceLine.getLabel(), referenceLine);
    }

    /**
     * Removes the reference line with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceLine(String label) {
        referenceLines.remove(label);
    }

    /**
     * Removes all the reference lines.
     */
    public void removeAllReferenceLines() {
        referenceLines = new HashMap<String, ReferenceLine>();
    }

    /**
     * Returns all the references lines as a hashmap, with the labels as the
     * keys.
     *
     * @return hashmap of all reference lines
     */
    public HashMap<String, ReferenceLine> getAllReferenceLines() {
        return referenceLines;
    }

    /**
     * Add a reference area.
     *
     * @param label the label for the reference area
     * @param start the start of the reference area
     * @param end the end of the reference area
     * @param areaColor the color of the reference area
     * @param alpha the alpha level of the reference area, range: 0.0 to 1.0
     */
    public void addReferenceArea(String label, double start, double end, Color areaColor, float alpha) {
        referenceAreas.put(label, new ReferenceArea(label, start, end, areaColor, alpha));
    }

    /**
     * Add a reference area.
     *
     * @param referenceArea the reference area
     */
    public void addReferenceArea(ReferenceArea referenceArea) {
        referenceAreas.put(referenceArea.getLabel(), referenceArea);
    }

    /**
     * Removes the reference area with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceArea(String label) {
        referenceAreas.remove(label);
    }

    /**
     * Removes all the reference areas.
     */
    public void removeAllReferenceAreas() {
        referenceAreas = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the references areas as a hashmap, with the labels as the
     * keys.
     *
     * @return hashmap of all reference areas
     */
    public HashMap<String, ReferenceArea> getAllReferenceAreas() {
        return referenceAreas;
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
     * Sets the plot type.
     *
     * @param plotType the plotType to set
     */
    public void setPlotType(PlotType plotType) {
        this.plotType = plotType;
    }

    /**
     * Get the color used to highlight the maximum values in the charts.
     *
     * @return the color used to highlight the maximum values in the charts
     */
    public Color getMaxValueColor() {
        return maxValueColor;
    }

    /**
     * Set the color used to highlight the maximum values in the charts.
     *
     * @param maxValueColor the color to set
     */
    public void setMaxValueColor(Color maxValueColor) {
        this.maxValueColor = maxValueColor;
    }

    /**
     * Get the color used to highlight the minimum values in the charts.
     *
     * @return the color used to highlight the minimum values in the charts
     */
    public Color getMinValueColor() {
        return minValueColor;
    }

    /**
     * Set the color used to highlight the minimum values in the charts and the
     * positive values in the difference plots..
     *
     * @param minValueColor the color to set
     */
    public void setMinValueColor(Color minValueColor) {
        this.minValueColor = minValueColor;
    }

    /**
     * Get the color used for the 'up values' in the Up/Down charts and the
     * positive values in the difference plots..
     *
     * @return the color used for the 'up values' in the Up/Down charts
     */
    public Color getUpColor() {
        return upColor;
    }

    /**
     * Set the color used for the 'up values' in the Up/Down charts.
     *
     * @param upColor the color for the 'up values' in the Up/Down charts
     */
    public void setUpColor(Color upColor) {
        this.upColor = upColor;
    }

    /**
     * Get the color used for the 'down values' in the Up/Down charts and the
     * negative values in the difference plots..
     *
     * @return the color used for the 'down values' in the Up/Down charts
     */
    public Color getDownColor() {
        return downColor;
    }

    /**
     * Set the color used for the 'down values' in the Up/Down charts and the
     * negative values in the difference plots..
     *
     * @param downColor the color for the 'down values' in the Up/Down charts
     */
    public void setDownColor(Color downColor) {
        this.downColor = downColor;
    }

    /**
     * Get the current plot orientation.
     *
     * @return the current plot orientation
     */
    public PlotOrientation getPlotOrientation() {
        return plotOrientation;
    }

    /**
     * Set the plot orientation.
     *
     * @param plotOrientation the new plot orientation
     */
    public void setPlotOrientation(PlotOrientation plotOrientation) {
        this.plotOrientation = plotOrientation;
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
    // @TODO: this ought to work but does not...
//    @Override
//    public String getToolTipText(MouseEvent event) {
//        return chartPanel.getToolTipText(event);
//    }   
}
