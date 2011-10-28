
package no.uib.jsparklines.data;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Object containing a 3D sparkline data series to be added to a JSparklines3dDataset.
 *
 * @author Harald Barsnes
 */
public class JSparklines3dDataSeries {

    /**
     * The data to plot.
     */
    private ArrayList<XYZDataPoint> data;
    /**
     * The color to use for this data series.
     */
    private Color seriesColor;
    /**
     * The label to use for the series.
     */
    private String seriesLabel;

    /**
     * Creates a new JSparklines3dDataSeries.
     *
     * @param data          the data to plot
     * @param seriesColor   the color to use for the series
     * @param seriesLabel   the data series label
     */
    public JSparklines3dDataSeries(ArrayList<XYZDataPoint> data, Color seriesColor, String seriesLabel) {
        this.data = data;
        this.seriesColor = seriesColor;
        this.seriesLabel = seriesLabel;
    }

    /**
     * Returns the sparkline data.
     *
     * @return the sparkline data
     */
    public ArrayList<XYZDataPoint> getData() {
        return data;
    }

    /**
     * Sets the sparkline data.
     *
     * @param data the data to set
     */
    public void setData(ArrayList<XYZDataPoint> data) {
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
     * Returns the value as a string. Note that the values are rounded
     * to two decimals.
     *
     * @return the values as a string
     */
    public String toString() {
        
        if (data.isEmpty()) {
            return "";
        }
        
        String temp = "";
        
        temp += data.get(0).toString();
        
        for (int i=1; i<data.size(); i++) {
            temp += "," + data.get(i).toString();
        }
        
        return temp;
    }
}
