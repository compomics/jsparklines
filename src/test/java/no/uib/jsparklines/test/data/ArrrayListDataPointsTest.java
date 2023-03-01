package no.uib.jsparklines.test.data;

import java.util.ArrayList;
import junit.framework.TestCase;
import no.uib.jsparklines.data.ArrrayListDataPoints;
import no.uib.jsparklines.renderers.JSparklinesArrayListBarChartTableCellRenderer.ValueDisplayType;
import org.junit.Assert;

/**
 * Test the ArrrayListDataPoints class.
 *
 * @author Harald Barsnes
 */
public class ArrrayListDataPointsTest extends TestCase {

    /**
     * Tests the ArrrayListDataPoints class and the methods getSum,
     * getSumExceptLast, toString and compareTo.
     *
     * @throws Exception
     */
    public void testArrrayListDataPoints() throws Exception {

        // set up the test data set
        ArrayList<Double> data = new ArrayList<>();
        data.add(1.0);
        data.add(3.0);
        data.add(6.0);
        ArrrayListDataPoints temp = new ArrrayListDataPoints(data, ValueDisplayType.firstNumberOnly);

        // test getSum
        Assert.assertTrue(temp.getSum() == 10);

        // test getSumExceptLast, 
        Assert.assertTrue(temp.getSumExceptLast() == 4);

        // test toString
        Assert.assertEquals("1.0, 3.0, 6.0", temp.toString());

        // test compareTo using firstNumberOnly
        ArrayList<Double> data2 = new ArrayList<>();
        data2.add(1.0);
        data2.add(3.0);
        data2.add(6.0);
        ArrrayListDataPoints temp2 = new ArrrayListDataPoints(data2, ValueDisplayType.firstNumberOnly);
        Assert.assertTrue(temp.compareTo(temp2) == 0);

        data2 = new ArrayList<Double>();
        data2.add(3.0);
        data2.add(1.0);
        data2.add(6.0);
        temp2 = new ArrrayListDataPoints(data2, ValueDisplayType.firstNumberOnly);
        Assert.assertTrue(temp.compareTo(temp2) == -1);

        // test compareTo using sumExceptLastNumber
        temp.setDataSortingType(ValueDisplayType.sumExceptLastNumber);
        temp2.setDataSortingType(ValueDisplayType.sumExceptLastNumber);
        Assert.assertTrue(temp.compareTo(temp2) == 0);

        data2 = new ArrayList<>();
        data2.add(3.0);
        data2.add(6.0);
        data2.add(1.0);
        temp2.setData(data2);
        Assert.assertTrue(temp.compareTo(temp2) == -1);

        // test compareTo using sumOfNumbers
        temp.setDataSortingType(ValueDisplayType.sumOfNumbers);
        temp2.setDataSortingType(ValueDisplayType.sumOfNumbers);
        Assert.assertTrue(temp.compareTo(temp2) == 0);

        data2 = new ArrayList<>();
        data2.add(3.0);
        data2.add(5.0);
        data2.add(1.0);
        temp2.setData(data2);
        Assert.assertTrue(temp.compareTo(temp2) == 1);
    }
}
