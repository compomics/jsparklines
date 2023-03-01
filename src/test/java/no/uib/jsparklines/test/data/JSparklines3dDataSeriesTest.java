package no.uib.jsparklines.test.data;

import java.awt.Color;
import java.util.ArrayList;
import junit.framework.TestCase;
import no.uib.jsparklines.data.JSparklines3dDataSeries;
import no.uib.jsparklines.data.XYZDataPoint;
import org.junit.Assert;

/**
 * Test the JSparklines3dDataSeries class.
 *
 * @author Harald Barsnes
 */
public class JSparklines3dDataSeriesTest extends TestCase {

    /**
     * Test the JSparklines3dDataSeries class and the toString method.
     *
     * @throws Exception
     */
    public void testJSparklines3dDataSeries() throws Exception {

        // set up the data
        ArrayList<XYZDataPoint> data = new ArrayList<>();
        data.add(new XYZDataPoint(1.1, 1.2, 1.3));
        data.add(new XYZDataPoint(2.1, 2.2, 2.3));
        data.add(new XYZDataPoint(3.1, 3.2, 3.3));
        JSparklines3dDataSeries dataSeries = new JSparklines3dDataSeries(data, Color.RED, "test");

        // test toString
        String temp = dataSeries.toString();
        String expected = "(1.1, 1.2, 1.3), (2.1, 2.2, 2.3), (3.1, 3.2, 3.3)";
        Assert.assertEquals(expected, temp);
    }
}
