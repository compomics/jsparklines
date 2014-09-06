package no.uib.jsparklines.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import no.uib.jsparklines.test.data.ArrrayListDataPointsTest;
import no.uib.jsparklines.test.data.ChromosomeTest;
import no.uib.jsparklines.test.data.JSparklines3dDataSeriesTest;
import no.uib.jsparklines.test.data.JSparklines3dDatasetTest;
import no.uib.jsparklines.test.data.JSparklinesDataSeriesTest;
import no.uib.jsparklines.test.data.JSparklinesDatasetTest;
import no.uib.jsparklines.test.data.StartIndexesTest;
import no.uib.jsparklines.test.data.XYDataPointTest;

/**
 * This class represents the full suite of test for the JSparklines project.
 *
 * @author Harald Barsnes
 */
public class FullSuite extends TestCase {

    public FullSuite() {
        super("Full test suite for the JSparklines project.");
    }

    public static Test suite() {
        TestSuite ts = new TestSuite("Test suite for the JSparklines project.");

        ts.addTest(new TestSuite(ArrrayListDataPointsTest.class));
        ts.addTest(new TestSuite(ChromosomeTest.class));
        ts.addTest(new TestSuite(JSparklines3dDataSeriesTest.class));
        ts.addTest(new TestSuite(JSparklines3dDatasetTest.class));
        ts.addTest(new TestSuite(JSparklinesDataSeriesTest.class));
        ts.addTest(new TestSuite(JSparklinesDatasetTest.class));
        ts.addTest(new TestSuite(StartIndexesTest.class));
        ts.addTest(new TestSuite(XYDataPointTest.class));

        return ts;
    }
}
