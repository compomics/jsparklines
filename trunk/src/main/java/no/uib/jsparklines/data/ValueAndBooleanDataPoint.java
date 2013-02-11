package no.uib.jsparklines.data;

import no.uib.jsparklines.renderers.util.Util;

/**
 * Object that stores data about one data point and a corresponding significance
 * value.
 *
 * @author Harald Barsnes
 */
public class ValueAndBooleanDataPoint implements Comparable<ValueAndBooleanDataPoint> {

    /**
     * The value.
     */
    private double value;
    /**
     * Is the value significant?
     */
    private boolean significant;

    /**
     * Create a new ValueAndBooleanDataPoint object.
     *
     * @param value
     * @param significant
     */
    public ValueAndBooleanDataPoint(double value, boolean significant) {
        this.value = value;
        this.significant = significant;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the significance
     */
    public boolean isSignificant() {
        return significant;
    }

    /**
     * @param signigficant the significance to set
     */
    public void setSignificant(boolean signigficant) {
        this.significant = signigficant;
    }

    /**
     * Returns the value as a string. Note that the value is rounded to two
     * decimals.
     *
     * @return the values as a string
     */
    public String toString() {
        return "" + Util.roundDouble(value, 2);
    }

    /**
     * Compares based on the value.
     */
    public int compareTo(ValueAndBooleanDataPoint o) {
        return Double.compare(this.value, o.value);
    }
}
