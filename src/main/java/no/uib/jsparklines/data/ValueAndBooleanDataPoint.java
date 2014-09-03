package no.uib.jsparklines.data;

import java.io.Serializable;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object storing data about one data point and a corresponding significance
 * value.
 *
 * @author Harald Barsnes
 */
public class ValueAndBooleanDataPoint implements Comparable<ValueAndBooleanDataPoint>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = -2630056389073106208L;
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
     * @param value the value
     * @param significant is it significant?
     */
    public ValueAndBooleanDataPoint(double value, boolean significant) {
        this.value = value;
        this.significant = significant;
    }

    /**
     * Returns the value.
     * 
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns the significance.
     * 
     * @return the significance
     */
    public boolean isSignificant() {
        return significant;
    }

    /**
     * Set the significance.
     * 
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
