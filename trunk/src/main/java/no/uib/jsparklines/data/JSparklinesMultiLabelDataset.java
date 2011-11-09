
package no.uib.jsparklines.data;

import java.util.ArrayList;

/**
 * A dataset of JSparklinesMultiLabel for use with the JSparklinesMultiLabelTableCellRenderer. 
 * 
 * @author Harald Barsnes
 */
public class JSparklinesMultiLabelDataset {
    
    /**
     * The arraylist of labels.
     */
    private ArrayList<JSparklinesMultiLabel> labels;
    
    /**
     * Constructor for one label.
     * 
     * @param labelA the label
     */
    public JSparklinesMultiLabelDataset (JSparklinesMultiLabel labelA) {
        labels = new ArrayList<JSparklinesMultiLabel>();
        labels.add(labelA);
    }
    
    /**
     * Constructor for two labels.
     * 
     * @param labelA the first label
     * @param labelB the second label
     */
    public JSparklinesMultiLabelDataset (JSparklinesMultiLabel labelA, JSparklinesMultiLabel labelB) {
        labels = new ArrayList<JSparklinesMultiLabel>();
        labels.add(labelA);
        labels.add(labelB);
    }
    
    /**
     * Constructor for three labels.
     * 
     * @param labelA the first label
     * @param labelB the second label
     * @param labelC the third label
     */
    public JSparklinesMultiLabelDataset (JSparklinesMultiLabel labelA, JSparklinesMultiLabel labelB, JSparklinesMultiLabel labelC) {
        labels = new ArrayList<JSparklinesMultiLabel>();
        labels.add(labelA);
        labels.add(labelB);
        labels.add(labelC);
    }
    
    /**
     * Constructor for four labels.
     * 
     * @param labelA the first label
     * @param labelB the second label
     * @param labelC the third label
     * @param labelD the fourth label
     */
    public JSparklinesMultiLabelDataset (JSparklinesMultiLabel labelA, JSparklinesMultiLabel labelB, JSparklinesMultiLabel labelC, JSparklinesMultiLabel labelD) {
        labels = new ArrayList<JSparklinesMultiLabel>();
        labels.add(labelA);
        labels.add(labelB);
        labels.add(labelC);
        labels.add(labelD);
    }

    /**
     * Returns the labels (1-4). 
     * 
     * @return the labels
     */
    public ArrayList<JSparklinesMultiLabel> getLabels() {
        return labels;
    }
}
