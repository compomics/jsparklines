
package no.uib.jsparklines.data;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Object containing a sparkline data series to be added to a JSparklinesDataset.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDataSeries {

    /**
     * The data to plot.
     */
    private ArrayList<Double> data;
    /**
     * The colors to use for this data series.
     */
    private Color seriesColor;

    /**
     * Creates a new JSparklinesDataSeries.
     *
     * @param data the data to plot
     * @param seriesColor the color to use for the series
     */
    public JSparklinesDataSeries(ArrayList<Double> data, Color seriesColor) {
        this.data = data;
        this.seriesColor = seriesColor;
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
}
