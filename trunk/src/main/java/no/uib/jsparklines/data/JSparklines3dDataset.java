package no.uib.jsparklines.data;

import java.util.ArrayList;

/**
 * Stores a set of JSparkline3dDataSeries in an array.
 *
 * @author Harald Barsnes
 */
public class JSparklines3dDataset {

    /**
     * The list of sparklines 3D data series.
     */
    private ArrayList<JSparklines3dDataSeries> data;

    /**
     * Creates a new JSparkline3dDataset.
     *
     * @param data the list of 3D sparklines data series
     */
    public JSparklines3dDataset(ArrayList<JSparklines3dDataSeries> data) {
        this.data = data;
    }

    /**
     * Returns the list of sparkline 3D data series.
     *
     * @return the list of sparkline 3D data series
     */
    public ArrayList<JSparklines3dDataSeries> getData() {
        return data;
    }

    /**
     * Sets the list of sparkline 3D data series.
     *
     * @param data the data to set
     */
    public void setData(ArrayList<JSparklines3dDataSeries> data) {
        this.data = data;
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

        String temp = "";

        temp += "[" + data.get(0).toString() + "]";

        for (int i = 1; i < data.size(); i++) {
            temp += ",[" + data.get(i).toString() + "]";
        }

        return temp;
    }
}
