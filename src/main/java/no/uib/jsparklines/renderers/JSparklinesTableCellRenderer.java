package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.AreaRenderer;
import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
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
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

/**
 * A renderer for displaying JSparklines plots consisting of multiple values
 * per data series inside a table cell.
 *
 * @author Harald Barsnes
 */
public class JSparklinesTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * An enumerator of the supported plot types.
     */
    public enum PlotType {

        barChart, lineChart, pieChart, stackedBarChart, stackedPercentBarChart, areaChart, boxPlot, upDownChart
    }

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
     * The with of the lines in the line plots.
     */
    private float lineWidth = 5;
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
     * If true the max and min values in line and area plots are
     * highlighted.
     *
     * Note: experimental feature, not yet finished.
     */
    private boolean highlightMaxAndMin = false;
    /**
     * The width of the max and min highlights.
     * See highlightMaxAndMin above.
     */
    private double widthOfMaxAndMinHighlight = 0.4;
    /**
     * A hashmap of the current reference lines. Key is the name of
     * the reference line.
     */
    private HashMap<String, ReferenceLine> referenceLines;
    /**
     * A hashmap of the current reference areas. Key is the name of 
     * the reference area.
     */
    private HashMap<String, ReferenceArea> referenceAreas;

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when
     * creating pie charts where no upper range is used.
     *
     * @param plotType          the plot type
     * @param plotOrientation   the orientation of the plot
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation) {
        this(plotType, plotOrientation, 0.0, 0.0);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when only positive
     * values are to be plotted.
     *
     * @param plotType          the plot type
     * @param plotOrientation   the orientation of the plot
     * @param maxValue          the maximum value to be plotted, used to make sure that all plots
     *                          in the same column has the same maxium value and are thus comparable
     *                          (this is the same as setting the minimum value to 0)
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double maxValue) {
        this(plotType, plotOrientation, 0.0, maxValue);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when only positive
     * values are to be plotted.
     *
     * @param plotType          the plot type
     * @param plotOrientation   the orientation of the plot
     * @param maxValue          the maximum value to be plotted, used to make sure that all plots
     *                          in the same column has the same maxium value and are thus comparable
     *                          (this is the same as setting the minimum value to 0)
     * @param lineWidth         sets the width of the lines used in the line charts, has no effect
     *                          on bar charts
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double maxValue, float lineWidth) {
        this(plotType, plotOrientation, 0.0, maxValue, lineWidth);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when positive
     * and negative values are to be plotted.
     *
     * @param plotType          the plot type
     * @param plotOrientation   the orientation of the plot
     * @param minValue          the minium value to be plotted, used to make sure that all plots
     *                          in the same column has the same minmum value and are thus comparable
     * @param maxValue          the maximum value to be plotted, used to make sure that all plots
     *                          in the same column has the same maxium value and are thus comparable
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double minValue, Double maxValue) {
        this(plotType, plotOrientation, minValue, maxValue, 5);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Use this constructor when positive
     * and negative values are to be plotted.
     *
     * @param plotType          the plot type
     * @param plotOrientation   the orientation of the plot
     * @param minValue          the minium value to be plotted, used to make sure that all plots
     *                          in the same column has the same minmum value and are thus comparable
     * @param maxValue          the maximum value to be plotted, used to make sure that all plots
     *                          in the same column has the same maxium value and are thus comparable
     * @param lineWidth         sets the width of the lines used in the line charts, has no effect
     *                          on bar charts
     */
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double minValue, Double maxValue, float lineWidth) {

        this.plotType = plotType;
        this.plotOrientation = plotOrientation;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.lineWidth = lineWidth;

        referenceLines = new HashMap<String, ReferenceLine>();
        referenceAreas = new HashMap<String, ReferenceArea>();

        delegate = new DefaultTableCellRenderer();
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());
    }

    /**
     * Sets up the cell renderer for the given cell.
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

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // if the cell is empty, simply return
        if (value == null) {
            return c;
        }

        if (!(value instanceof JSparklinesDataset)) {
            return c;
        }

        // get the dataset
        JSparklinesDataset sparklineDataset = (JSparklinesDataset) value;
        ArrayList<Color> colors = new ArrayList<Color>();
        int dataCounter = 0;

        String tooltip = "<html>";

        // create the chart
        if (plotType == PlotType.barChart) {

            /////////////
            // BAR CHART
            /////////////

            DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    barChartDataset.addValue(sparklineDataSeries.getData().get(j), "1", new Integer(dataCounter++));
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

        } else if (plotType == PlotType.lineChart || plotType == PlotType.areaChart) {

            //////////////////////
            // LINE or AREA CHART
            //////////////////////

            AbstractXYItemRenderer renderer;

            // set up the chart renderer
            if (plotType == PlotType.lineChart) {
                renderer = new XYLineAndShapeRenderer(true, false);
            } else {
                renderer = new AreaRenderer();
                ((AreaRenderer) renderer).setOutline(true);
            }

            // variables for storing the max and min values
            double plotMaxValue = Double.MIN_VALUE;
            double plotMinValue = Double.MAX_VALUE;
            int indexOfMaxValue = -1;
            int indexOfMinValue = -1;

            XYSeriesCollection lineChartDataset = new XYSeriesCollection();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

                XYSeries tempSeries = new XYSeries(sparklineDataSeries.getSeriesLabel());

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    tempSeries.add(j, sparklineDataSeries.getData().get(j));

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

                if (plotType == PlotType.lineChart) {
                    renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());
                    renderer.setSeriesStroke(i, new BasicStroke(lineWidth, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
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

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

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

        } else if (plotType == PlotType.stackedBarChart || plotType == PlotType.stackedPercentBarChart) {

            /////////////////////
            // STACKED BAR CHART
            /////////////////////

            DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

            StackedBarRenderer renderer = new StackedBarRenderer();
            renderer.setShadowVisible(false);

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {
                    barChartDataset.addValue(sparklineDataSeries.getData().get(j), "" + i, "" + j);
                    renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());
                }
            }

            chart = ChartFactory.createStackedBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

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

            if (plotType == PlotType.stackedPercentBarChart) {
                renderer.setRenderAsPercentages(true);
            } else {
                // set the axis range
                if (maxValue > 0) {
                    plot.getRangeAxis().setRange(minValue * sparklineDataset.getData().size(), maxValue * sparklineDataset.getData().size());
                }
            }

            // add the dataset
            plot.setDataset(barChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set up the chart renderer
            plot.setRenderer(renderer);

        } else if (plotType == PlotType.boxPlot) {

            //////////////
            // BOX PLOT
            //////////////

            DefaultBoxAndWhiskerCategoryDataset boxPlotDataset = new DefaultBoxAndWhiskerCategoryDataset();

            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();

            for (int i = 0; i < sparklineDataset.getData().size(); i++) {

                JSparklinesDataSeries sparklineDataSeries = sparklineDataset.getData().get(i);

                ArrayList<Double> listValues = new ArrayList();

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

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

                tooltip += "<font color=rgb("
                        + sparklineDataSeries.getSeriesColor().getRed() + ","
                        + sparklineDataSeries.getSeriesColor().getGreen() + ","
                        + sparklineDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparklineDataSeries.getSeriesLabel() + "<br>";

                for (int j = 0; j < sparklineDataSeries.getData().size(); j++) {

                    if (sparklineDataSeries.getData().get(j) > 0) {
                        barChartDataset.addValue(1, "1", new Integer(dataCounter++));
                        colors.add(upColor);
                    } else {
                        barChartDataset.addValue(-1, "1", new Integer(dataCounter++));
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
        setToolTipText(tooltip + "</html>");

        // hide the outline
        chart.getPlot().setOutlineVisible(false);

        // make sure the background is the same as the table row color
        chart.getPlot().setBackgroundPaint(c.getBackground());
        chart.setBackgroundPaint(c.getBackground());

        // create the chart panel and add it to the table cell
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(c.getBackground());
        this.removeAll();
        this.add(chartPanel);

        return this;
    }

    /**
     * Add a reference line at a given data value.
     *
     * @param label the label for the reference
     * @param value the reference line value
     * @param lineWidth the line width
     * @param lineColor the line color
     */
    public void addReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        referenceLines.put(label, new ReferenceLine(label, value, lineWidth, lineColor));
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
     * Returns all the references lines as a hashmap, with the labels
     * as the keys.
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
     * @param areaColor the color of the area
     */
    public void addReferenceArea(String label, double start, double end, Color areaColor, float alpha) {
        referenceAreas.put(label, new ReferenceArea(label, start, end, areaColor, alpha));
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
     * Returns all the references areas as a hashmap, with the labels
     * as the keys.
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
     * Set the line with to use for line charts.
     *
     * @param lineWidth the line width to set
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
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
     * Set the color used to highlight the minimum values in the charts.
     *
     * @param minValueColor the color to set
     */
    public void setMinValueColor(Color minValueColor) {
        this.minValueColor = minValueColor;
    }

    /**
     * Get the color used for the 'up values' in the Up/Down charts.
     *
     * @return tthe color used for the 'up values' in the Up/Down charts
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
     * Get the color used for the 'down values' in the Up/Down charts.
     *
     * @return tthe color used for the 'down values' in the Up/Down charts
     */
    public Color getDownColor() {
        return downColor;
    }

    /**
     * Set the color used for the 'down values' in the Up/Down charts.
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
}
