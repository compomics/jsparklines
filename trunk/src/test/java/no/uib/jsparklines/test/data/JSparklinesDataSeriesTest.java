package no.uib.jsparklines.test.data;

import java.awt.Color;
import java.util.ArrayList;
import junit.framework.Assert;
import junit.framework.TestCase;
import no.uib.jsparklines.data.JSparklinesDataSeries;

/**
 * Test the JSparklinesDataSeries class.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDataSeriesTest extends TestCase {

    /**
     * Test the JSparklinesDataSeries class and the toString method.
     *
     * @throws Exception
     */
    public void testJSparklinesDataSeries() throws Exception {

        // set up the data
        ArrayList<Double> data = new ArrayList<Double>();
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        JSparklinesDataSeries dataSeries = new JSparklinesDataSeries(data, Color.RED, "test");

        // test toString
        Assert.assertEquals("1.0,2.0,3.0", dataSeries.toString());
    }
}
