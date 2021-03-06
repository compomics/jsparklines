package no.uib.jsparklines.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Object storing start indexes for use in the
 * JSparklinesMultiIntervalChartTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class StartIndexes implements Comparable<StartIndexes>, Serializable {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 3564559645965619316L;
    /**
     * The start indexes.
     */
    private ArrayList<Integer> startIndexes;

    /**
     * Create a new StartIndexes.
     *
     * @param indexes the start indexes
     */
    public StartIndexes(ArrayList<Integer> indexes) {
        this.startIndexes = indexes;
    }

    /**
     * Returns the start indexes.
     * 
     * @return the start indexes
     */
    public ArrayList<Integer> getIndexes() {
        return startIndexes;
    }

    /**
     * Compares based on the first value in the list.
     */
    public int compareTo(StartIndexes o) {

        if (this.getIndexes().get(0) == o.getIndexes().get(0)) {
            return 0;
        }

        if (this.getIndexes().get(0) > o.getIndexes().get(0)) {
            return 1;
        }

        return -1;
    }

    @Override
    public String toString() {

        String tempString = "";

        for (int index : startIndexes) {
            tempString += index + ",";
        }

        if (!startIndexes.isEmpty()) {
            tempString = tempString.substring(0, tempString.length() - 1);
        }

        return tempString;
    }
}
