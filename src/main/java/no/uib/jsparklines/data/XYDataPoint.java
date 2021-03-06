package no.uib.jsparklines.data;

import java.io.Serializable;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object storing data about one data point in an XY plot.
 *
 * @author Harald Barsnes
 */
public class XYDataPoint implements Comparable<XYDataPoint>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 8439427205918618172L;
    /**
     * The x value.
     */
    private double x;
    /**
     * The y value.
     */
    private double y;
    /**
     * If true, the compare method will use the first data point. False will use
     * the sum. First data point is the default.
     */
    private boolean compareBasedOnFirstDataPoint = true;

    /**
     * Empty default constructor.
     */
    public XYDataPoint() {
    }
    
    /**
     * Create a new XYDataPoint.
     *
     * @param x the x value
     * @param y the y value
     */
    public XYDataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new XYDataPoint.
     *
     * @param x the x value
     * @param y the y value
     * @param compareBasedOnFirstDataPoint if true, the compare method will use
     * the first data point, false will use the sum
     */
    public XYDataPoint(double x, double y, boolean compareBasedOnFirstDataPoint) {
        this.x = x;
        this.y = y;
        this.compareBasedOnFirstDataPoint = compareBasedOnFirstDataPoint;
    }

    /**
     * Returns the x value.
     *
     * @return the x value
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x value.
     *
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y value.
     *
     * @return the y value
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y value.
     *
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the x-value as a string. Note that the values are rounded to two
     * decimals.
     *
     * @return the xy-values as a string
     */
    public String toString() {
        return "" + Util.roundDouble(x, 2);
    }

    /**
     * Compares based on the x-value or the sum of the x- and y-value, depending
     * on the compareBasedOnFirstDataPoint settings.
     */
    public int compareTo(XYDataPoint o) {
        if (compareBasedOnFirstDataPoint) {
            return Double.compare(this.x, o.x);
        } else {
            return Double.compare(this.x + this.y, o.x + o.y);
        }
    }
}
