package no.uib.jsparklines.data;

import no.uib.jsparklines.renderers.util.Util;

/**
 * Object that stores data about one data point and a correspoding signficance 
 * value.
 *
 * @author Harald Barsnes
 */
public class ValueAndBooleanDataPoint implements Comparable<ValueAndBooleanDataPoint> {

    private double value;
    private boolean signigficant;

    /**
     * Create a new ValueAndBooleanDataPoint object.
     *
     * @param value
     * @param signigficant
     */
    public ValueAndBooleanDataPoint(double value, boolean signigficant) {
        this.value = value;
        this.signigficant = signigficant;
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
        return signigficant;
    }

    /**
     * @param signigficant the signigficant to set
     */
    public void setSignificant(boolean signigficant) {
        this.signigficant = signigficant;
    }

    /**
     * Returns the value as a string. Note that the value is rounded
     * to two decimals.
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
