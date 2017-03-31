package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.JSparklines3dDataSeries;
import no.uib.jsparklines.data.JSparklines3dDataset;
import no.uib.jsparklines.renderers.util.ReferenceArea;
import no.uib.jsparklines.renderers.util.ReferenceLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;

/**
 * Table cell renderer displaying JSparklines 3D plots consisting of multiple
 * values per data series. Supported input: JSparklines3dDataset objects. Other
 * object types are rendered using the DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklines3dTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * List of supported plot types for JSparklines3dTableCellRenderer.
     */
    public enum PlotType {

        /**
         * Scatter plot.
         */
        scatterPlot,
        /**
         * Bubble plot.
         */
        bubblePlot
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
     * The maximum x-axis value.
     */
    private double maxXValue;
    /**
     * The minimum x-axis value.
     */
    private double minXValue;
    /**
     * The maximum y-axis value.
     */
    private double maxYValue = 0;
    /**
     * The minimum y-axis value.
     */
    private double minYValue = 0;
    /**
     * A hashmap of the current x-axis reference lines. Key is the name of the
     * reference line.
     */
    private HashMap<String, ReferenceLine> referenceLinesXAxis;
    /**
     * A hashmap of the current x-axis reference areas. Key is the name of the
     * reference area.
     */
    private HashMap<String, ReferenceArea> referenceAreasXAxis;
    /**
     * A hashmap of the current y-axis reference lines. Key is the name of the
     * reference line.
     */
    private HashMap<String, ReferenceLine> referenceLinesYAxis;
    /**
     * A hashmap of the current y-axis reference areas. Key is the name of the
     * reference area.
     */
    private HashMap<String, ReferenceArea> referenceAreasYAxis;

    /**
     * Creates a new JSparkLines3dTableCellRenderer.
     *
     * @param plotType the plot type
     * @param minXValue the minimum x value to be plotted, used to make sure that
     * all plots in the same column has the same minimum x value and are thus
     * comparable
     * @param maxXValue the maximum x value to be plotted, used to make sure
     * that all plots in the same column has the same maximum x value and are
     * thus comparable
     * @param minYValue the minimum y value to be plotted, used to make sure that
     * all plots in the same column has the same minimum y value and are thus
     * comparable
     * @param maxYValue the maximum y value to be plotted, used to make sure
     * that all plots in the same column has the same maximum y value and are
     * thus comparable
     * @throws IllegalArgumentException if minXValue &gt; maxXValue or minYValue
     * &gt; maxYValue
     */
    public JSparklines3dTableCellRenderer(PlotType plotType, Double minXValue, Double maxXValue, Double minYValue, Double maxYValue) {

        this.plotType = plotType;
        this.minXValue = minXValue;
        this.maxXValue = maxXValue;
        this.minYValue = minYValue;
        this.maxYValue = maxYValue;

        referenceLinesXAxis = new HashMap<String, ReferenceLine>();
        referenceAreasXAxis = new HashMap<String, ReferenceArea>();
        referenceLinesYAxis = new HashMap<String, ReferenceLine>();
        referenceAreasYAxis = new HashMap<String, ReferenceArea>();

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        if (minXValue > maxXValue) {
            throw new IllegalArgumentException("minXValue has to be smaller than maxXValue! Current values: minXValue: " + minXValue + ", maxXValue: " + maxXValue + ".");
        }
        if (minYValue > maxYValue) {
            throw new IllegalArgumentException("minYValue has to be smaller than maxYValue! Current values: minYValue: " + minYValue + ", maxYValue: " + maxYValue + ".");
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

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

        if (!(value instanceof JSparklines3dDataset)) {
            return c;
        }

        // get the dataset
        JSparklines3dDataset sparkline3dDataset = (JSparklines3dDataset) value;
        ArrayList<Color> colors = new ArrayList<Color>();

        String tooltip = "<html>";

        // create the chart
        if (plotType == PlotType.scatterPlot || plotType == PlotType.bubblePlot) {

            ///////////////////////////
            // SCATTER and BUBBLE PLOT
            ///////////////////////////
            DefaultXYDataset xyDataset = null;
            DefaultXYZDataset xyzDataset = null;

            if (plotType == PlotType.scatterPlot) {
                xyDataset = new DefaultXYDataset();
            } else {
                xyzDataset = new DefaultXYZDataset();
            }

            for (int i = 0; i < sparkline3dDataset.getData().size(); i++) {

                JSparklines3dDataSeries sparkline3dDataSeries = sparkline3dDataset.getData().get(i);

                tooltip += "<font color=rgb("
                        + sparkline3dDataSeries.getSeriesColor().getRed() + ","
                        + sparkline3dDataSeries.getSeriesColor().getGreen() + ","
                        + sparkline3dDataSeries.getSeriesColor().getBlue() + ")>"
                        + sparkline3dDataSeries.getSeriesLabel() + "<br>";

                colors.add(sparkline3dDataSeries.getSeriesColor());

                double[][] data;

                if (plotType == PlotType.scatterPlot) {
                    data = new double[2][sparkline3dDataSeries.getData().size()];
                } else {
                    data = new double[3][sparkline3dDataSeries.getData().size()];
                }

                for (int j = 0; j < sparkline3dDataSeries.getData().size(); j++) {
                    data[0][j] = sparkline3dDataSeries.getData().get(j).getX();
                    data[1][j] = sparkline3dDataSeries.getData().get(j).getY();

                    if (plotType == PlotType.bubblePlot) {
                        data[2][j] = sparkline3dDataSeries.getData().get(j).getZ();
                    }
                }

                if (plotType == PlotType.scatterPlot) {
                    xyDataset.addSeries(sparkline3dDataSeries.getSeriesLabel(), data);
                } else {
                    xyzDataset.addSeries(sparkline3dDataSeries.getSeriesLabel(), data);
                }
            }

            if (plotType == PlotType.scatterPlot) {
                chart = ChartFactory.createScatterPlot(null, null, null, xyDataset, PlotOrientation.VERTICAL, false, false, false);
            } else {
                chart = ChartFactory.createBubbleChart(null, null, null, xyzDataset, PlotOrientation.VERTICAL, false, false, false);
            }

            // fine tune the chart properites
            XYPlot plot = chart.getXYPlot();

            // remove space before/after the domain axis
            plot.getDomainAxis().setUpperMargin(0);
            plot.getDomainAxis().setLowerMargin(0);

            // add x-axis reference lines, if any
            Iterator<String> allReferencesLines = referenceLinesXAxis.keySet().iterator();

            while (allReferencesLines.hasNext()) {
                ReferenceLine currentReferenceLine = referenceLinesXAxis.get(allReferencesLines.next());
                plot.addDomainMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
            }

            // add x-axis reference areas, if any
            Iterator<String> allReferenceAreas = referenceAreasXAxis.keySet().iterator();

            while (allReferenceAreas.hasNext()) {
                ReferenceArea currentReferenceArea = referenceAreasXAxis.get(allReferenceAreas.next());
                IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
                marker.setAlpha(currentReferenceArea.getAlpha());
                plot.addDomainMarker(marker);
            }

            // add y-axis reference lines, if any
            allReferencesLines = referenceLinesYAxis.keySet().iterator();

            while (allReferencesLines.hasNext()) {
                ReferenceLine currentReferenceLine = referenceLinesYAxis.get(allReferencesLines.next());
                plot.addRangeMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
            }

            // add y-axis reference areas, if any
            allReferenceAreas = referenceAreasYAxis.keySet().iterator();

            while (allReferenceAreas.hasNext()) {
                ReferenceArea currentReferenceArea = referenceAreasYAxis.get(allReferenceAreas.next());
                IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
                marker.setAlpha(currentReferenceArea.getAlpha());
                plot.addRangeMarker(marker);
            }

            // set the axis ranges
            plot.getDomainAxis().setRange(minXValue, maxXValue);
            plot.getRangeAxis().setRange(minYValue, maxYValue);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set up the chart colors
            if (plotType == PlotType.scatterPlot) {
                for (int i = 0; i < xyDataset.getSeriesCount(); i++) {
                    plot.getRenderer().setSeriesPaint(i, colors.get(i));
                }
            } else {
                for (int i = 0; i < xyzDataset.getSeriesCount(); i++) {
                    plot.getRenderer().setSeriesPaint(i, colors.get(i));
                }
            }
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
     * Add a reference line at a given x-axis data value.
     *
     * @param label the label for the reference
     * @param value the reference line value
     * @param lineWidth the line width, has to non-negative
     * @param lineColor the line color
     */
    public void addXAxisReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        referenceLinesXAxis.put(label, new ReferenceLine(label, value, lineWidth, lineColor));
    }

    /**
     * Removes the x-axis reference line with the given label. Does nothing if
     * no reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeXAxisReferenceLine(String label) {
        referenceLinesXAxis.remove(label);
    }

    /**
     * Removes all x-axis reference lines.
     */
    public void removeAllXAxisReferenceLines() {
        referenceLinesXAxis = new HashMap<String, ReferenceLine>();
    }

    /**
     * Returns all the x-axis references lines as a hashmap, with the labels as
     * the keys.
     *
     * @return hashmap of all reference lines
     */
    public HashMap<String, ReferenceLine> getAllXAxisReferenceLines() {
        return referenceLinesXAxis;
    }

    /**
     * Add a x-axis reference area.
     *
     * @param label the label for the reference area
     * @param start the start of the reference area
     * @param end the end of the reference area
     * @param areaColor the color of the area
     * @param alpha the alpha level, range: 0.0 to 1.0
     */
    public void addXAxisReferenceArea(String label, double start, double end, Color areaColor, float alpha) {
        referenceAreasXAxis.put(label, new ReferenceArea(label, start, end, areaColor, alpha));
    }

    /**
     * Removes the x-axis reference area with the given label. Does nothing if
     * no reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeXAxisReferenceArea(String label) {
        referenceAreasXAxis.remove(label);
    }

    /**
     * Removes all the x-axis reference areas.
     */
    public void removeAllXAxisReferenceAreas() {
        referenceAreasXAxis = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the x-axis references areas as a hashmap, with the labels as
     * the keys.
     *
     * @return hashmap of all reference areas
     */
    public HashMap<String, ReferenceArea> getXAxisAllReferenceAreas() {
        return referenceAreasXAxis;
    }

    /**
     * Add a reference line at a given y-axis data value.
     *
     * @param label the label for the reference
     * @param value the reference line value
     * @param lineWidth the line width, has to non-negative
     * @param lineColor the line color
     */
    public void addYAxisReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        referenceLinesYAxis.put(label, new ReferenceLine(label, value, lineWidth, lineColor));
    }

    /**
     * Removes the y-axis reference line with the given label. Does nothing if
     * no reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeYAxisReferenceLine(String label) {
        referenceLinesYAxis.remove(label);
    }

    /**
     * Removes all y-axis reference lines.
     */
    public void removeAllYAxisReferenceLines() {
        referenceLinesYAxis = new HashMap<String, ReferenceLine>();
    }

    /**
     * Returns all the y-axis references lines as a hashmap, with the labels as
     * the keys.
     *
     * @return hashmap of all reference lines
     */
    public HashMap<String, ReferenceLine> getAllYAxisReferenceLines() {
        return referenceLinesYAxis;
    }

    /**
     * Add a y-axis reference area.
     *
     * @param label the label for the reference area
     * @param start the start of the reference area
     * @param end the end of the reference area
     * @param areaColor the color of the area
     * @param alpha the alpha level, range: 0.0 to 1.0
     */
    public void addYAxisReferenceArea(String label, double start, double end, Color areaColor, float alpha) {
        referenceAreasYAxis.put(label, new ReferenceArea(label, start, end, areaColor, alpha));
    }

    /**
     * Removes the y-axis reference area with the given label. Does nothing if
     * no reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeYAxisReferenceArea(String label) {
        referenceAreasYAxis.remove(label);
    }

    /**
     * Removes all the y-axis reference areas.
     */
    public void removeAllYAxisReferenceAreas() {
        referenceAreasYAxis = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the y-axis references areas as a hashmap, with the labels as
     * the keys.
     *
     * @return hashmap of all reference areas
     */
    public HashMap<String, ReferenceArea> getYAxisAllReferenceAreas() {
        return referenceAreasYAxis;
    }

    /**
     * Set the maximum x value.
     *
     * @param maxXValue the maximum x value
     */
    public void setMaxXValue(double maxXValue) {
        this.maxXValue = maxXValue;
    }

    /**
     * Set the minimum x value.
     *
     * @param minXValue the minimum x value
     */
    public void setMinXValue(double minXValue) {
        this.minXValue = minXValue;
    }

    /**
     * Set the maximum y value.
     *
     * @param maxYValue the maximum y value
     */
    public void setMaxYValue(double maxYValue) {
        this.maxYValue = maxYValue;
    }

    /**
     * Set the minimum y value.
     *
     * @param minYValue the minimum y value
     */
    public void setMinYValue(double minYValue) {
        this.minYValue = minYValue;
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
     * Returns a reference to the chart panel.
     *
     * @return the chart panel.
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }
}
