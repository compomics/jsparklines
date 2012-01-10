package no.uib.jsparklines.data;

import no.uib.jsparklines.renderers.util.Util;

/**
 * Object that stores data about one data point in an XY plot.
 *
 * @author Harald Barsnes
 */
public class XYDataPoint implements Comparable<XYDataPoint> {

    private double x;
    private double y;
    /**
     * If true, the compare method will use the first data point. False will 
     * use the sum. First data point is the default.
     */
    private boolean compareBasedOnFirstDataPoint = true;

    /**
     * Create a new XYDataPoint.
     *
     * @param x
     * @param y
     */
    public XYDataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Create a new XYDataPoint.
     *
     * @param x
     * @param y
     * @param compareBasedOnFirstDataPoint if true, the compare method will use the first data point, false will use the sum
     */
    public XYDataPoint(double x, double y, boolean compareBasedOnFirstDataPoint) {
        this.x = x;
        this.y = y;
        this.compareBasedOnFirstDataPoint = compareBasedOnFirstDataPoint;
    }
    
    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the x-value as a string. Note that the values are rounded
     * to two decimals.
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
