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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYZDataset;

/**
 * Table cell renderer for 1-4 color labels displayed either a pie chart or as
 * equal parts of a square. Supported input: JSparklinesMultiLabelDataset
 * objects. Other object types are rendered using the DefaultTableCellRenderer.
 *
 * @see no.uib.jsparklines.JSparklinesMultiLabelDemo
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
     * The background color used for the plots. For plots using light colors,
     * it's recommended to use a dark background color, and for plots using
     * darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;
    /**
     * If true a pie chart is used to display the label colors, false uses a
     * square. False is the default.
     */
    private boolean circle = false;

    /**
     * Creates a new JSparklinesColorTableCellRenderer.
     */
    public JSparklinesMultiLabelTableCellRenderer() {
        setUpRendererAndChart();
    }

    /**
     * If true a pie chart is used to display the label colors, false uses a
     * square. False is the default.
     *
     * @param circle if true, a pie chart is used to display the label colors,
     * false uses a square
     */
    public void setCircle(boolean circle) {
        this.circle = circle;
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
     * @param plotBackgroundColor the plot background color
     */
    public void setBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // if the cell is empty, simply return
        if (value == null || !(value instanceof JSparklinesMultiLabelDataset)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        JSparklinesMultiLabelDataset dataset = (JSparklinesMultiLabelDataset) value;
        String tooltips = null;

        if (dataset.getLabels().size() == 1) {
            chart = createChart((Color) dataset.getLabels().get(0).getColor());
            tooltips = dataset.getLabels().get(0).getLabel();
        } else if (dataset.getLabels().size() == 2) {
            chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                    (Color) dataset.getLabels().get(1).getColor());
            if (circle) {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(1).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(0).getLabel() + "</td></tr></table></html>";
            } else {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr></table></html>";
            }
        } else if (dataset.getLabels().size() == 3) {
            chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                    (Color) dataset.getLabels().get(1).getColor(),
                    (Color) dataset.getLabels().get(2).getColor());
            if (circle) {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(1).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(0).getLabel() + "</td></tr>"
                        + "<tr><td>" + dataset.getLabels().get(2).getLabel() + "</td>"
                        + "<td></td></tr></table></html>";
            } else {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr>"
                        + "<tr><td></td>"
                        + "<td>" + dataset.getLabels().get(2).getLabel() + "</td></tr></table></html>";
            }
        } else if (dataset.getLabels().size() == 4) {
            chart = createChart((Color) dataset.getLabels().get(0).getColor(),
                    (Color) dataset.getLabels().get(1).getColor(),
                    (Color) dataset.getLabels().get(2).getColor(),
                    (Color) dataset.getLabels().get(3).getColor(), true);
            if (circle) {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(3).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(0).getLabel() + "</td></tr>"
                        + "<tr><td>" + dataset.getLabels().get(2).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr></table></html>";
            } else {
                tooltips = "<html><table border=\"0\">"
                        + "<tr><td>" + dataset.getLabels().get(0).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(1).getLabel() + "</td></tr>"
                        + "<tr><td>" + dataset.getLabels().get(3).getLabel() + "</td>"
                        + "<td>" + dataset.getLabels().get(2).getLabel() + "</td></tr></table></html>";
            }
        } else {
            throw new IllegalArgumentException("JSparklinesMultiLabelTableCellRenderer only supports JSparklinesMultiLabelDataset objects of size 1-4!");
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

        // @TODO: perhaps the colors below should not be hardcoded...
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
     * @param labelA the color for label A
     * @return the chart
     */
    private JFreeChart createChart(Color labelA) {
        return createChart(labelA, labelA, labelA, labelA, false);
    }

    /**
     * Create the chart with two labels.
     *
     * @param labelA the color for label A
     * @param labelB the color for label B
     * @return the chart
     */
    private JFreeChart createChart(Color labelA, Color labelB) {
        return createChart(labelA, labelA, labelB, labelB, false);
    }

    /**
     * Create the chart with three labels.
     *
     * @param labelA the color for label A
     * @param labelB the color for label B
     * @param labelC the color for label C
     * @return the chart
     */
    private JFreeChart createChart(Color labelA, Color labelB, Color labelC) {
        return createChart(labelA, labelA, labelC, labelB, false);
    }

    /**
     * Create the chart with four labels.
     *
     * @param labelA the color for label A
     * @param labelB the color for label B
     * @param labelC the color for label C
     * @param labelD the color for label D
     * @param all if true, the provided label order is used, otherwise the order
     * is changed to make sure that the labels are correctly ordered
     * @return the chart
     */
    private JFreeChart createChart(Color labelA, Color labelB, Color labelC, Color labelD, boolean all) {

        Plot plot;

        if (circle) {

            DefaultPieDataset pieDataset = new DefaultPieDataset();

            pieDataset.setValue("A", 1);
            pieDataset.setValue("B", 1);
            pieDataset.setValue("C", 1);
            pieDataset.setValue("D", 1);

            // create the chart
            chart = ChartFactory.createPieChart(null, pieDataset, false, false, false);

            plot = ((PiePlot) chart.getPlot());
            ((PiePlot) plot).setCircular(true);
            ((PiePlot) plot).setLabelGenerator(null);
            ((PiePlot) plot).setShadowXOffset(0);
            ((PiePlot) plot).setShadowYOffset(0);
            ((PiePlot) plot).setSectionOutlinesVisible(false);

            // set the series colors
            ((PiePlot) plot).setSectionPaint("A", labelA);
            ((PiePlot) plot).setSectionPaint("B", labelB);
            ((PiePlot) plot).setSectionPaint("C", labelC);
            ((PiePlot) plot).setSectionPaint("D", labelD);

            plot.setOutlineStroke(new BasicStroke(20));

            chart.getPlot().setOutlineVisible(false);

        } else {

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

            plot = new XYPlot(dataset2, xAxis, yAxis, renderer);
            plot.setBackgroundPaint(Color.WHITE);
            ((XYPlot) plot).setDomainGridlinePaint(Color.white);
            ((XYPlot) plot).setRangeGridlinePaint(Color.white);

            // hide unwanted chart details
            ((XYPlot) plot).getRangeAxis().setVisible(false);
            ((XYPlot) plot).getDomainAxis().setVisible(false);
            ((XYPlot) plot).setRangeGridlinesVisible(false);
            ((XYPlot) plot).setDomainGridlinesVisible(false);

            plot.setOutlineStroke(new BasicStroke(20));
        }

        JFreeChart tempChart = new JFreeChart(null, plot);
        tempChart.removeLegend();
        tempChart.setBackgroundPaint(Color.white);
        return tempChart;
    }
}
