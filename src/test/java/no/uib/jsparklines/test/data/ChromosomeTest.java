package no.uib.jsparklines.test.data;

import junit.framework.TestCase;
import no.uib.jsparklines.data.Chromosome;
import org.junit.Assert;

/**
 * Test the Chromosome class.
 *
 * @author Harald Barsnes
 */
public class ChromosomeTest extends TestCase {

    /**
     * Test the Chromosome class and the methods compareTo and toString.
     *
     * @throws Exception
     */
    public void testChromosome() throws Exception {

        // test toString
        Chromosome chromosome = new Chromosome("1");
        Assert.assertEquals("1", chromosome.toString());
        chromosome = new Chromosome("X");
        Assert.assertEquals("X", chromosome.toString());
        chromosome = new Chromosome("Y");
        Assert.assertEquals("Y", chromosome.toString());
        chromosome = new Chromosome("Z");
        Assert.assertEquals("Z", chromosome.toString());
        chromosome = new Chromosome("W");
        Assert.assertEquals("W", chromosome.toString());
        chromosome = new Chromosome(null);
        Assert.assertEquals("", chromosome.toString());

        // test compareTo
        chromosome = new Chromosome("2");
        Chromosome chromosome2 = new Chromosome("2");
        Assert.assertTrue(chromosome.compareTo(chromosome2) == 0);
        chromosome2 = new Chromosome("3");
        Assert.assertTrue(chromosome.compareTo(chromosome2) == -1);
        chromosome2 = new Chromosome("1");
        Assert.assertTrue(chromosome.compareTo(chromosome2) == 1);
        chromosome2 = new Chromosome("X");
        Assert.assertTrue(chromosome.compareTo(chromosome2) == -1);
    }
}
