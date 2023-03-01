package no.uib.jsparklines.test.data;

import java.awt.Color;
import java.util.ArrayList;
import junit.framework.TestCase;
import no.uib.jsparklines.data.JSparklines3dDataSeries;
import no.uib.jsparklines.data.JSparklines3dDataset;
import no.uib.jsparklines.data.XYZDataPoint;
import org.junit.Assert;

/**
 * Test the JSparklines3dDataset class.
 *
 * @author Harald Barsnes
 */
public class JSparklines3dDatasetTest extends TestCase {

    /**
     * Test the JSparklines3dDataset class and the toString method.
     *
     * @throws Exception
     */
    public void testJSparklines3dDataset() throws Exception {

        // set up the data
        ArrayList<JSparklines3dDataSeries> data = new ArrayList<>();

        ArrayList<XYZDataPoint> xyz = new ArrayList<>();
        xyz.add(new XYZDataPoint(1.1, 1.2, 1.3));
        xyz.add(new XYZDataPoint(2.1, 2.2, 2.3));
        xyz.add(new XYZDataPoint(3.1, 3.2, 3.3));
        JSparklines3dDataSeries dataSeries = new JSparklines3dDataSeries(xyz, Color.RED, "test");
        data.add(dataSeries);

        xyz = new ArrayList<>();
        xyz.add(new XYZDataPoint(4.1, 4.2, 4.3));
        xyz.add(new XYZDataPoint(5.1, 5.2, 5.3));
        xyz.add(new XYZDataPoint(6.1, 6.2, 6.3));
        dataSeries = new JSparklines3dDataSeries(xyz, Color.BLUE, "test2");
        data.add(dataSeries);

        JSparklines3dDataset dataset = new JSparklines3dDataset(data);

        // test toString
        Assert.assertEquals(
                "[(1.1, 1.2, 1.3), (2.1, 2.2, 2.3), (3.1, 3.2, 3.3)], "
                + "[(4.1, 4.2, 4.3), (5.1, 5.2, 5.3), (6.1, 6.2, 6.3)]",
                dataset.toString()
        );
    }
}
