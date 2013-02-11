package no.uib.jsparklines.data;

import java.util.ArrayList;

/**
 * Stores a set of JSparklineDataSeries in an array.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDataset implements Comparable<JSparklinesDataset> {

    /**
     * The list of sparklines data series.
     */
    private ArrayList<JSparklinesDataSeries> data;

    /**
     * Creates a new JSparklineDataset.
     *
     * @param data the list of sparklines data series
     */
    public JSparklinesDataset(ArrayList<JSparklinesDataSeries> data) {
        this.data = data;
    }

    /**
     * Returns the list of sparkline data series.
     *
     * @return the list of sparkline data series
     */
    public ArrayList<JSparklinesDataSeries> getData() {
        return data;
    }

    /**
     * Sets the list of sparkline data series.
     *
     * @param data the data to set
     */
    public void setData(ArrayList<JSparklinesDataSeries> data) {
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

    /**
     * Compares based on the summed value of each dataset.
     */
    public int compareTo(JSparklinesDataset o) {

        double sumThis = 0.0;
        double sumOther = 0.0;

        for (int i = 0; i < this.getData().size(); i++) {
            JSparklinesDataSeries series = this.getData().get(i);

            for (int j = 0; j < series.getData().size(); j++) {
                sumThis += series.getData().get(j);
            }
        }

        for (int i = 0; i < o.getData().size(); i++) {
            JSparklinesDataSeries series = o.getData().get(i);

            for (int j = 0; j < series.getData().size(); j++) {
                sumOther += series.getData().get(j);
            }
        }

        return Double.compare(sumThis, sumOther);
    }
}
