package no.uib.jsparklines.data;

import java.io.Serializable;
import no.uib.jsparklines.renderers.util.Util;

/**
 * Object storing data about one data point in an XYZ plot.
 *
 * @author Harald Barsnes
 */
public class XYZDataPoint implements Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = -5876541315146855069L;
    /**
     * The x value.
     */
    private double x;
    /**
     * The y value.
     */
    private double y;
    /**
     * The z value.
     */
    private double z;

    /**
     * Create a new XYZDataPoint.
     *
     * @param x the x value
     * @param y the y value
     * @param z the z value
     */
    public XYZDataPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * Returns the z value.
     *
     * @return the z value
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the z value.
     *
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns the (x,y,x) value as a string. Note that the values are rounded
     * to two decimals.
     *
     * @return the values as a string
     */
    public String toString() {
        return "(" + Util.roundDouble(x, 2) + "," + Util.roundDouble(y, 2) + "," + Util.roundDouble(z, 2) + ")";
    }
}
