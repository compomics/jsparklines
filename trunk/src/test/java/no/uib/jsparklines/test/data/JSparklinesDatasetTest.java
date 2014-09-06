package no.uib.jsparklines.test.data;

import java.awt.Color;
import java.util.ArrayList;
import junit.framework.Assert;
import junit.framework.TestCase;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.data.JSparklinesDataset;

/**
 * Test the JSparklinesDataset class.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDatasetTest extends TestCase {

    /**
     * Test the JSparklinesDataset class and the toString and compareTo methods.
     *
     * @throws Exception
     */
    public void testJSparklinesDataset() throws Exception {

        // set up the data
        ArrayList<JSparklinesDataSeries> allData = new ArrayList<JSparklinesDataSeries>();

        ArrayList<Double> data = new ArrayList<Double>();
        data.add(1.1);
        data.add(1.2);
        data.add(1.3);
        allData.add(new JSparklinesDataSeries(data, Color.RED, "test"));

        data = new ArrayList<Double>();
        data.add(2.1);
        data.add(2.2);
        data.add(2.3);
        allData.add(new JSparklinesDataSeries(data, Color.RED, "test2"));

        JSparklinesDataset dataset = new JSparklinesDataset(allData);

        // test toString
        Assert.assertEquals("[1.1,1.2,1.3], [2.1,2.2,2.3]", dataset.toString());

        // test compare to
        ArrayList<JSparklinesDataSeries> allData2 = new ArrayList<JSparklinesDataSeries>();

        data = new ArrayList<Double>();
        data.add(1.1);
        data.add(1.2);
        data.add(1.3);
        allData2.add(new JSparklinesDataSeries(data, Color.RED, "test"));

        data = new ArrayList<Double>();
        data.add(2.1);
        data.add(2.2);
        data.add(2.3);
        allData2.add(new JSparklinesDataSeries(data, Color.RED, "test2"));

        JSparklinesDataset dataset2 = new JSparklinesDataset(allData2);

        Assert.assertTrue(dataset.compareTo(dataset2) == 0);

        allData2 = new ArrayList<JSparklinesDataSeries>();

        data = new ArrayList<Double>();
        data.add(1.1);
        data.add(1.2);
        data.add(1.3);
        allData2.add(new JSparklinesDataSeries(data, Color.RED, "test"));

        data = new ArrayList<Double>();
        data.add(2.1);
        data.add(2.2);
        data.add(2.4);
        allData2.add(new JSparklinesDataSeries(data, Color.RED, "test2"));

        dataset2 = new JSparklinesDataset(allData2);

        Assert.assertTrue(dataset.compareTo(dataset2) == -1);
    }
}
