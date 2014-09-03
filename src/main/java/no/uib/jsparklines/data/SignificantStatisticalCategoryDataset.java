package no.uib.jsparklines.data;

import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Extention of DefaultStatisticalCategoryDataset adding a boolean
 * indicating if the dataset is significant or not.
 *
 * @author Harald Barsnes
 */
public class SignificantStatisticalCategoryDataset extends DefaultStatisticalCategoryDataset {

    /**
     * A boolean indicating if the dataset is significant or not.
     */
    private Boolean significant;

    /**
     * Returns true if the dataset is significant. False if it is not. If not
     * set returns null.
     *
     * @return true if the dataset is significant, null if not set
     */
    public Boolean isSignificant() {
        return significant;
    }

    /**
     * Set if the dataset is significant or not.
     *
     * @param significant if the dataset is significant or not
     */
    public void setSignificant(boolean significant) {
        this.significant = significant;
    }
}
