package no.uib.jsparklines.data;

import java.io.Serializable;
import java.util.ArrayList;
import no.uib.jsparklines.renderers.JSparklinesArrayListBarChartTableCellRenderer.ValueDisplayType;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object storing data points for use in the
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
     * The sum of the value in the list except the last one. Null if not set.
     */
    private Double sumExceptLast = null;
    /**
     * The value used to sort (and display) the data.
     */
    private ValueDisplayType valueDisplayType;

    /**
     * Create a new ArrrayListDataPoints.
     *
     * @param data the data
     * @param valueDisplayType the way to sort (and display) the data
     */
    public ArrrayListDataPoints(ArrayList<Double> data, ValueDisplayType valueDisplayType) {
        this.data = data;
        this.valueDisplayType = valueDisplayType;
    }

    /**
     * Returns the value sort (and display) type.
     *
     * @return the value sort (and display) type
     */
    public ValueDisplayType getDataSortingType() {
        return valueDisplayType;
    }

    /**
     * Set the value sort (and display) type.
     *
     * @param valueDisplayType the way to sort (and display) the data
     */
    public void setDataSortingType(ValueDisplayType valueDisplayType) {
        this.valueDisplayType = valueDisplayType;
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
        if (sum != null && sumExceptLast != null) {
            return sum;
        } else {
            sum = 0.0;
            sumExceptLast = 0.0;
            for (int i = 0; i < data.size(); i++) {
                Double temp = data.get(i);
                sum += temp;
                if (i < data.size() - 1) {
                    sumExceptLast += temp;
                }
            }
            return sum;
        }
    }

    /**
     * Return the sum of the values in the list.
     *
     * @return the sum of the values in the list
     */
    public double getSumExceptLast() {
        if (sum != null && sumExceptLast != null) {
            return sumExceptLast;
        } else {
            sum = 0.0;
            sumExceptLast = 0.0;
            for (int i = 0; i < data.size(); i++) {
                Double temp = data.get(i);
                sum += temp;
                if (i < data.size() - 1) {
                    sumExceptLast += temp;
                }
            }
            return sumExceptLast;
        }
    }

    /**
     * Returns all the values as a comma separated string. Note that the values
     * are rounded to two decimals.
     *
     * @return the values as a string
     */
    public String toString() {
        String valuesAsString = "";
        for (double temp : data) {
            if (!valuesAsString.isEmpty()) {
                valuesAsString += ", ";
            }
            valuesAsString += Util.roundDouble(temp, 2);
        }
        return valuesAsString;
    }

    /**
     * Compares based on the sum of the values in the data arrays.
     */
    public int compareTo(ArrrayListDataPoints o) {

        if (valueDisplayType == null || valueDisplayType == ValueDisplayType.sumOfNumbers) {
            return Double.compare(this.getSum(), o.getSum());
        } else if (valueDisplayType == ValueDisplayType.sumExceptLastNumber) {
            return Double.compare(this.getSumExceptLast(), o.getSumExceptLast());
        } else {

            if (this.getData().isEmpty() && o.getData().isEmpty()) {
                return 0;
            }
            if (this.getData().isEmpty() && !o.getData().isEmpty()) {
                return 1;
            }
            if (!this.getData().isEmpty() && o.getData().isEmpty()) {
                return -1;
            }

            return Double.compare(this.getData().get(0), o.getData().get(0));
        }
    }
}
