package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.data.JSparklinesDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * A renderer for displaying JSparklines plots consisting of multiple values
 * inside a table cell.
 *
 * @author Harald Barsnes
 */
public class JSparklinesTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * An enumerator of the plot types.
     */
    public enum PlotType {
        barChart, lineChart
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
    private double maxValue = 1;
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
    private int lineWidth = 5;

    /**
     * Creates a new JSparkLinesTableCellRenderer. Used this constructor when only positive
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
     * Creates a new JSparkLinesTableCellRenderer. Used this constructor when only positive
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
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double maxValue, int lineWidth) {
        this(plotType, plotOrientation, 0.0, maxValue, lineWidth);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer. Used this constructor when positive
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
     * Creates a new JSparkLinesTableCellRenderer. Used this constructor when positive
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
    public JSparklinesTableCellRenderer(PlotType plotType, PlotOrientation plotOrientation, Double minValue, Double maxValue, int lineWidth) {

        this.plotType = plotType;
        this.plotOrientation = plotOrientation;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.lineWidth = lineWidth;

        delegate = new DefaultTableCellRenderer();
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());
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
     * @param minValue the minumum  value
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * Set the line with to use for line charts.
     *
     * @param lineWidth the line width to set
     */
    public void setLineWidth(int lineWidth) {
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

            // set the axis range
            plot.getRangeAxis().setRange(minValue, maxValue);

            // add the dataset
            plot.setDataset(barChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set up the chart renderer
            JSparklinesRenderer renderer = new JSparklinesRenderer(colors);
            renderer.setShadowVisible(false);
            plot.setRenderer(renderer);

        } else if (plotType == PlotType.lineChart) {

            // set up the chart renderer
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);

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
                }

                lineChartDataset.addSeries(tempSeries);
                renderer.setSeriesPaint(i, sparklineDataSeries.getSeriesColor());
                renderer.setSeriesStroke(i, new BasicStroke(lineWidth, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
            }

            chart = ChartFactory.createXYLineChart(null, null, null, lineChartDataset, plotOrientation, false, false, false);

            // fine tune the chart properites
            XYPlot plot = chart.getXYPlot();

            // set the axis range
            plot.getRangeAxis().setRange(minValue, maxValue);

            // add the dataset
            plot.setDataset(lineChartDataset);

            // hide unwanted chart details
            plot.getRangeAxis().setVisible(false);
            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);

            // set the renderer
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
     * Returns a reference to the chart panel.
     *
     * @return the chart panel.
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }
}
