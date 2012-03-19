package no.uib.jsparklines.data;

import java.util.ArrayList;

/**
 * Object that stores start indexes for use in the
 * JSparklinesMultiIntervalChartTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class StartIndexes implements Comparable<StartIndexes> {

    private ArrayList<Integer> startIndexes;

    /**
     * Create a new StartIndexes.
     *
     * @param indexes 
     */
    public StartIndexes(ArrayList<Integer> indexes) {
        this.startIndexes = indexes;
    }

    /**
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
}
