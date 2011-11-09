package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.JSparklinesMultiLabelDataset;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;

/**
 * A renderer for 1-4 color labels displayed as equal size bar charts inside a table cell. 
 *
 * @author Harald Barsnes
 */
public class JSparklinesMultiLabelTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The background color used for the plots. For plots using light
     * colors, it's recommended to use a dark background color, and for
     * plots using darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;

    /**
     * Creates a new JSparklinesColorTableCellRenderer.
     */
    public JSparklinesMultiLabelTableCellRenderer() {
        setUpRendererAndChart();
    }

    /**
     * Sets up the table cell renderer.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart() {

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    /**
     * Set the plot background color.
     *
     * @param plotBackgroundColor
     */
    public void setBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    /**
     * Sets up the cell renderer for the given component.
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return the rendered cell
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value == null) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        String tooltips = null;

        if (value instanceof JSparklinesMultiLabelDataset) {

            JSparklinesMultiLabelDataset dataset = (JSparklinesMultiLabelDataset) value;

            if (dataset.getLabels().size() == 1) {
                chart = createChart((Color) dataset.getLabels().get(0).getColor());
                tooltips = dataset.getLabels().get(0).getLabel();
            } else if (dataset.getLabels().size() == 2) {
                chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                        (Color) dataset.getLabels().get(1).getColor());
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr></table></html>"; 
            } else if (dataset.getLabels().size() == 3) {
                chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                        (Color) dataset.getLabels().get(1).getColor(),
                        (Color) dataset.getLabels().get(2).getColor());
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr>"
                        + "<tr><td></td>"
                        + "<td>" + dataset.getLabels().get(2).getLabel() + "</td></tr></table></html>"; 
            } else if (dataset.getLabels().size() == 4) {
                chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                        (Color) dataset.getLabels().get(1).getColor(),
                        (Color) dataset.getLabels().get(2).getColor(),
                        (Color) dataset.getLabels().get(3).getColor(), true);
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr>"
                        + "<tr><td>" + dataset.getLabels().get(3).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(2).getLabel() + "</td></tr></table></html>";    
            } else {
                throw new IllegalArgumentException("JSparklinesMultiLabelTableCellRenderer only supports JSparklinesMultiLabelDataset objects of size 1-4!");
            }

        } else {
            throw new IllegalArgumentException("JSparklinesMultiLabelTableCellRenderer only supports JSparklinesMultiLabelDataset objects!");
        }


        // if we get this far we should have created the chart

        // set the tooltip text
        this.setToolTipText(tooltips);
        

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        chartPanel = new ChartPanel(chart);


        // make sure the background is the same as the table row color
        if (plotBackgroundColor != null && !isSelected) {
            chart.getPlot().setBackgroundPaint(plotBackgroundColor);
            chartPanel.setBackground(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {

            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            chart.getPlot().setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }

        if (isSelected) {
            chart.getPlot().setOutlinePaint(Color.WHITE);
        } else {
            chart.getPlot().setOutlinePaint(Color.DARK_GRAY);
        }

        this.removeAll();
        this.add(chartPanel);

        return this;
    }

    /**
     * Create the chart with one label.
     * 
     * @param labelA
     * @return 
     */
    private JFreeChart createChart(Color labelA) {
        return createChart(labelA, labelA, labelA, labelA, false);
    }

    /**
     * Create the chart with two labels.
     * 
     * @param labelA
     * @param labelB
     * @return 
     */
    private JFreeChart createChart(Color labelA, Color labelB) {
        return createChart(labelA, labelA, labelB, labelB, false);
    }

    /**
     * Create the chart with three labels.
     * 
     * @param labelA
     * @param labelB
     * @param labelC
     * @return 
     */
    private JFreeChart createChart(Color labelA, Color labelB, Color labelC) {
        return createChart(labelA, labelA, labelC, labelB, false);
    }

    /**
     * Create the chart with four labels.
     * 
     * @param labelA
     * @param labelB
     * @param labelC
     * @param labelD
     * @param all   if true, the provided label order is used, otherwise the order 
     *              is changed to make sure that the labels are correctly ordered
     * @return 
     */
    private JFreeChart createChart(Color labelA, Color labelB, Color labelC, Color labelD, boolean all) {

        LookupPaintScale paintScale = new LookupPaintScale(0, 4, Color.lightGray);

        if (all) {
            paintScale.add(1.0, labelD);
            paintScale.add(2.0, labelA);
            paintScale.add(3.0, labelC);
            paintScale.add(4.0, labelB);
        } else {
            paintScale.add(1.0, labelA);
            paintScale.add(2.0, labelB);
            paintScale.add(3.0, labelC);
            paintScale.add(4.0, labelD);
        }

        NumberAxis xAxis = new NumberAxis(null);
        NumberAxis yAxis = new NumberAxis(null);
        XYBlockRenderer renderer = new XYBlockRenderer();

        renderer.setPaintScale(paintScale);

        double[][] tempXYZData = new double[3][4];

        tempXYZData[0][0] = 0;
        tempXYZData[1][0] = 0;
        tempXYZData[2][0] = 1;

        tempXYZData[0][1] = 0;
        tempXYZData[1][1] = 1;
        tempXYZData[2][1] = 2;

        tempXYZData[0][2] = 1;
        tempXYZData[1][2] = 0;
        tempXYZData[2][2] = 3;

        tempXYZData[0][3] = 1;
        tempXYZData[1][3] = 1;
        tempXYZData[2][3] = 4;

        DefaultXYZDataset dataset2 = new DefaultXYZDataset();
        dataset2.addSeries("Series 1", tempXYZData);

        XYPlot plot = new XYPlot(dataset2, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // hide unwanted chart details
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        plot.setOutlineStroke(new BasicStroke(20));

        JFreeChart tempChart = new JFreeChart(null, plot);
        tempChart.removeLegend();
        tempChart.setBackgroundPaint(Color.white);
        return tempChart;
    }
}
