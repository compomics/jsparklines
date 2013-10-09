package no.uib.jsparklines.data;

import java.io.Serializable;

/**
 * Object that stores data about one data point in an XY plot.
 *
 * @author Harald Barsnes
 */
public class Chromosome implements Comparable<Chromosome>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 2505328876343336528L;
    /**
     * The chromosome number or X, Y, Z or W.
     */
    private Integer chromosomeNumber;
    /**
     * Integer value representing the X chromosome.
     */
    private final Integer X = Integer.MAX_VALUE - 4;
    /**
     * Integer value representing the Y chromosome.
     */
    private final Integer Y = Integer.MAX_VALUE - 3;
    /**
     * Integer value representing the Z chromosome.
     */
    private final Integer Z = Integer.MAX_VALUE - 2;
    /**
     * Integer value representing the W chromosome.
     */
    private final Integer W = Integer.MAX_VALUE - 1;
    /**
     * Integer value representing an unknown chromosome.
     */
    private final Integer UNKNOWN = Integer.MAX_VALUE;

    /**
     * Create a new Chromosome.
     *
     * @param chromosomeNumber the chromosomeNumber, i.e., a positive number or
     * X, Y, Z or W, null is allowed
     */
    public Chromosome(String chromosomeNumber) {

        if (chromosomeNumber == null) {
            this.chromosomeNumber = UNKNOWN;
        } else {
            try {
                this.chromosomeNumber = Integer.parseInt(chromosomeNumber);
            } catch (NumberFormatException e) {
                // see if it is X, Y, Z, or W
                if (chromosomeNumber.equalsIgnoreCase("X")) {
                    this.chromosomeNumber = X;
                } else if (chromosomeNumber.equalsIgnoreCase("Y")) {
                    this.chromosomeNumber = Y;
                } else if (chromosomeNumber.equalsIgnoreCase("Z")) {
                    this.chromosomeNumber = Z;
                } else if (chromosomeNumber.equalsIgnoreCase("W")) {
                    this.chromosomeNumber = W;
                } else {
                    throw new IllegalArgumentException(chromosomeNumber + " has to be a positive integer or X, Y, Z or W.");
                }
            }
        }
    }

    /**
     * Returns the chromosome number as a string.
     *
     * @return the chromosome number as a string
     */
    public String toString() {
        if (chromosomeNumber.intValue() == X.intValue()) {
            return "X";
        } else if (chromosomeNumber.intValue() == Y.intValue()) {
            return "Y";
        } else if (chromosomeNumber.intValue() == Z.intValue()) {
            return "Z";
        } else if (chromosomeNumber.intValue() == W.intValue()) {
            return "W";
        } else if (chromosomeNumber.intValue() == UNKNOWN.intValue()) {
            return "";
        } else {
            return "" + chromosomeNumber;
        }
    }

    /**
     * Compares based on the chromosome numbers. X, Y, Z and W are ordered at
     * the end, in this order.
     */
    public int compareTo(Chromosome o) {

        if (o == null) {
            return 1;
        }

        if (this.chromosomeNumber == null) {
            return -1;
        }

        if (o.chromosomeNumber == null) {
            return 1;
        }

        return Double.compare(this.chromosomeNumber, o.chromosomeNumber);
    }
}