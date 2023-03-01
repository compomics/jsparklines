package no.uib.jsparklines.test.data;

import junit.framework.TestCase;
import no.uib.jsparklines.data.XYDataPoint;
import org.junit.Assert;

/**
 * Test the XYDataPoint class.
 *
 * @author Harald Barsnes
 */
public class XYDataPointTest extends TestCase {

    /**
     * Test the XYDataPoint class and the compareTo method.
     *
     * @throws Exception
     */
    public void testXYDataPoint() throws Exception {

        // test compareTo
        XYDataPoint data = new XYDataPoint(1.0, 2.0, true);
        XYDataPoint data2 = new XYDataPoint(1.0, 2.0, true);
        Assert.assertTrue(data.compareTo(data2) == 0);
        
        data = new XYDataPoint(2.0, 1.0, true);
        data2 = new XYDataPoint(1.0, 2.0, true);
        Assert.assertTrue(data.compareTo(data2) == 1);
        
        data = new XYDataPoint(2.0, 1.0, false);
        data2 = new XYDataPoint(1.0, 2.0, false);
        Assert.assertTrue(data.compareTo(data2) == 0);
    }
}
