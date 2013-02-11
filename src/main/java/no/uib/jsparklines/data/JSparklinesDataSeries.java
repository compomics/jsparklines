package no.uib.jsparklines.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object containing a sparkline data series to be added to a
 * JSparklinesDataset.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDataSeries {

    /**
     * The data to plot.
     */
    private ArrayList<Double> data;
    /**
     * The color to use for this data series.
     */
    private Color seriesColor;
    /**
     * The label to use for the series.
     */
    private String seriesLabel;
    /**
     * The properties for the line when displayed in a line chart.
     */
    private BasicStroke lineType;
    /**
     * The default width of the lines in line plots.
     */
    private float lineWidth = 5;

    /**
     * Creates a new JSparklinesDataSeries.
     *
     * @param data the data to plot
     * @param seriesColor the color to use for the series
     * @param seriesLabel the data series label
     */
    public JSparklinesDataSeries(ArrayList<Double> data, Color seriesColor, String seriesLabel) {
        this.data = data;
        this.seriesColor = seriesColor;
        this.seriesLabel = seriesLabel;

        lineType = new BasicStroke(lineWidth, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND);
    }

    /**
     * Creates a new JSparklinesDataSeries.
     *
     * @param data the data to plot
     * @param seriesColor the color to use for the series
     * @param seriesLabel the data series label
     * @param lineType the properties of the line when displayed as a line chart
     */
    public JSparklinesDataSeries(ArrayList<Double> data, Color seriesColor, String seriesLabel, BasicStroke lineType) {
        this.data = data;
        this.seriesColor = seriesColor;
        this.seriesLabel = seriesLabel;
        this.lineType = lineType;
    }

    /**
     * Returns the sparkline data.
     *
     * @return the sparkline data
     */
    public ArrayList<Double> getData() {
        return data;
    }

    /**
     * Sets the sparkline data.
     *
     * @param data the data to set
     */
    public void setData(ArrayList<Double> data) {
        this.data = data;
    }

    /**
     * Returns the series color.
     *
     * @return the seriesColor
     */
    public Color getSeriesColor() {
        return seriesColor;
    }

    /**
     * Sets the series color.
     *
     * @param seriesColor the seriesColor to set
     */
    public void setSeriesColor(Color seriesColor) {
        this.seriesColor = seriesColor;
    }

    /**
     * Returns the label for the series.
     *
     * @return the seriesLabel
     */
    public String getSeriesLabel() {
        return seriesLabel;
    }

    /**
     * Set the label for the series.
     *
     * @param seriesLabel the seriesLabel to set
     */
    public void setSeriesLabel(String seriesLabel) {
        this.seriesLabel = seriesLabel;
    }

    /**
     * Returns the properties of the line. Only used when the chart is displayed
     * as a line chart.
     *
     * @return the lineType
     */
    public BasicStroke getLineType() {
        return lineType;
    }

    /**
     * Set the properties of the line. Only used when the chart is displayed as
     * a line chart.
     *
     * @param lineType the lineType to set
     */
    public void setLineType(BasicStroke lineType) {
        this.lineType = lineType;
    }

    /**
     * Returns the value as a string. Note that the values are rounded to two
     * decimals.
     *
     * @return the values as a string
     */
    public String toString() {

        if (data.isEmpty()) {
            return "";
        }

        StringBuilder temp = new StringBuilder();
        temp.append(Util.roundDouble(data.get(0), 2));

        for (int i = 1; i < data.size(); i++) {
            temp.append(",").append(Util.roundDouble(data.get(i), 2));
        }

        return temp.toString();
    }
}
