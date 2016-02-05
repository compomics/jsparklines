package no.uib.jsparklines.data;

import java.io.Serializable;

/**
 * Object storing data about one data point in a XY plot.
 *
 * @author Harald Barsnes
 */
public class Chromosome implements Comparable<Chromosome>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 2505328876343336528L;
    /**
     * The chromosome name.
     */
    private String chromosomeName;
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
    public final static Integer UNKNOWN = Integer.MAX_VALUE;

    /**
     * Create a new Chromosome.
     *
     * @param chromosomeName the name of the chromosome, integer or X, Y, Z, W
     * is preferred
     */
    public Chromosome(String chromosomeName) {

        this.chromosomeName = chromosomeName;

        if (chromosomeName == null) {
            this.chromosomeNumber = UNKNOWN;
        } else {
            try {
                this.chromosomeNumber = Integer.parseInt(chromosomeName);
            } catch (NumberFormatException e) {
                // see if it is X, Y, Z, or W
                if (chromosomeName.equalsIgnoreCase("X")) {
                    this.chromosomeNumber = X;
                } else if (chromosomeName.equalsIgnoreCase("Y")) {
                    this.chromosomeNumber = Y;
                } else if (chromosomeName.equalsIgnoreCase("Z")) {
                    this.chromosomeNumber = Z;
                } else if (chromosomeName.equalsIgnoreCase("W")) {
                    this.chromosomeNumber = W;
                } else {
                    this.chromosomeNumber = chromosomeName.hashCode();
                }
            }
        }
    }

    /**
     * Returns the chromosome as a string.
     *
     * @return the chromosome as a string
     */
    public String toString() {
        if (chromosomeName == null) {
            return "";
        } else {
            return chromosomeName;
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
