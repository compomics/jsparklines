
package no.uib.jsparklines.data;

import java.util.ArrayList;

/**
 * Stores a set of JSparklineDataSeries in an array.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDataset {

    /**
     * The list of sparklines data series.
     */
    private ArrayList<JSparklinesDataSeries> data;

    /**
     * Creates a new JSparklineDataset.
     *
     * @param data the list of sparklines data series
     */
    public JSparklinesDataset (ArrayList<JSparklinesDataSeries> data) {
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
        
        temp += "[" + data.get(0).toString() + "]";
        
        for (int i=1; i<data.size(); i++) {
            temp += ",[" + data.get(i).toString() + "]";
        }
        
        return temp;
    }
}
