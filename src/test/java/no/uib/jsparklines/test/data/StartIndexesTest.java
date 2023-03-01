package no.uib.jsparklines.test.data;

import java.util.ArrayList;
import junit.framework.TestCase;
import no.uib.jsparklines.data.StartIndexes;
import org.junit.Assert;

/**
 * Test the StartIndexes class.
 *
 * @author Harald Barsnes
 */
public class StartIndexesTest extends TestCase {

    /**
     * Test the StartIndexes class and the toString and compareTo methods.
     *
     * @throws Exception
     */
    public void testStartIndexes() throws Exception {

        // set up data
        ArrayList<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        StartIndexes indexes = new StartIndexes(data);

        // test toString
        Assert.assertEquals("1,2,3", indexes.toString());

        // test compareTo
        data = new ArrayList<Integer>();
        data.add(1);
        data.add(2);
        data.add(3);
        StartIndexes indexes2 = new StartIndexes(data);

        Assert.assertTrue(indexes.compareTo(indexes2) == 0);

        data = new ArrayList<Integer>();
        data.add(2);
        data.add(1);
        data.add(3);
        indexes2 = new StartIndexes(data);

        Assert.assertTrue(indexes.compareTo(indexes2) == -1);
    }
}
