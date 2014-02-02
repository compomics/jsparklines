package no.uib.jsparklines.data;

import java.io.Serializable;
import java.util.ArrayList;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object that stores data points for use in the
 * JSparklinesArrayListBarChartTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class ArrrayListDataPoints implements Comparable<ArrrayListDataPoints>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 8439427205918618172L;
    /**
     * The array list of doubles to store.
     */
    private ArrayList<Double> data;
    /**
     * The sum of the values in the list. Null if not set.
     */
    private Double sum = null;

    /**
     * Create a new ArrrayListDataPoints.
     *
     * @param data
     */
    public ArrrayListDataPoints(ArrayList<Double> data) {
        this.data = data;
    }

    /**
     * Returns the array list.
     *
     * @return the array list
     */
    public ArrayList<Double> getData() {
        return data;
    }

    /**
     * Set the array list.
     *
     * @param data the array list
     */
    public void setData(ArrayList<Double> data) {
        this.data = data;
        sum = null;
    }

    /**
     * Return the sum of the values in the list.
     *
     * @return the sum of the values in the list
     */
    public double getSum() {
        if (sum != null) {
            return sum;
        } else {
            sum = 0.0;
            for (Double temp : data) {
                sum += temp;
            }
            return sum;
        }
    }

    /**
     * Returns all the values as a string. Note that the values are rounded to
     * two decimals.
     *
     * @return the values as a string
     */
    public String toString() {
        String valuesAsString = "";
        for (double temp : data) {
            valuesAsString += Util.roundDouble(temp, 2);
        }
        return valuesAsString;
    }

    /**
     * Compares based on the sum of the values in the data arrays.
     */
    public int compareTo(ArrrayListDataPoints o) {
        return Double.compare(this.getSum(), o.getSum());
    }
}
