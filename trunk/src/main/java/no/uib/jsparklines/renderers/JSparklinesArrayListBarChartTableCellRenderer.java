package no.uib.jsparklines.renderers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.renderers.util.ReferenceArea;
import no.uib.jsparklines.renderers.util.ReferenceLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A renderer for displaying JSparklines plots consisting of numbers as a
 * stacked bar chart inside a table cell. Supported datatype: ArrayList of
 * doubles.
 *
 * @author Harald Barsnes
 */
public class JSparklinesArrayListBarChartTableCellRenderer extends JLabel implements TableCellRenderer {

    /**
     * If true, the first number is shown as the value for plot. Otherwise the
     * total value is shown. No effect on the other plot types.
     */
    private boolean showFirstNumber = false;
    /**
     * The background color, if null the row color is used.
     */
    private Color backgroundColor;

    /**
     * Turns of the gradient painting for the bar charts.
     */
    static {
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
    }
    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The maximum value. Used to set the upper range for the charts.
     */
    private double maxValue = 0;
    /**
     * The plot orientation.
     */
    private PlotOrientation plotOrientation;
    /**
     * The colors to use for the plot.
     */
    private ArrayList<Color> colors;
    /**
     * The color used to fill the rest of the chart up to the max value. Set to
     * null if no filling should be used.
     */
    private Color fillColor = null;
    /**
     * A hashmap of the current reference lines. Key is the name of the
     * reference line.
     */
    private HashMap<String, ReferenceLine> referenceLines;
    /**
     * A hashmap of the current reference areas. Key is the name of the
     * reference area.
     */
    private HashMap<String, ReferenceArea> referenceAreas;
    /**
     * The decimal format for use when showing the numbers.
     */
    private DecimalFormat numberFormat = new DecimalFormat("0.00");
    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;
    /**
     * If true the value of the chart is shown next to the bar chart.
     */
    private boolean showNumberAndChart = false;
    /**
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;
    /**
     * The label used to display the number and the bar chart at the same time.
     */
    private JLabel valueLabel;

    /**
     * Creates a new JSparkLinesTableCellRenderer.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param colors the colors to use for the plot
     * @param showFirstNumber if true, the first value is shown when showing the
     * values, false shows the sum
     */
    public JSparklinesArrayListBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue,
            ArrayList<Color> colors, boolean showFirstNumber) {
        this(plotOrientation, maxValue, colors, null, showFirstNumber);
    }

    /**
     * Creates a new JSparkLinesTableCellRenderer.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param colors the colors to use for the plot
     * @param fillColor the color used to fill the rest of the chart up to the
     * max value (set to null if no filling should be used)
     * @param showFirstNumber if true, the first value is shown when showing the
     * values, false shows the sum
     */
    public JSparklinesArrayListBarChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue,
            ArrayList<Color> colors, Color fillColor, boolean showFirstNumber) {

        this.plotOrientation = plotOrientation;
        this.maxValue = maxValue;
        this.colors = colors;
        this.fillColor = fillColor;
        this.showFirstNumber = showFirstNumber;

        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));

        referenceLines = new HashMap<String, ReferenceLine>();
        referenceAreas = new HashMap<String, ReferenceArea>();

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the chart.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        showNumberAndChart(showNumberAndChart, widthOfLabel, numberFormat);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the chart.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param numberFormat the decimal format to use when showing the numbers
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, DecimalFormat numberFormat) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        this.numberFormat = numberFormat;
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the chart.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param font the font to use for the label
     * @param horizontalAlignement the horizontal alignment of the text in the
     * label: one of the following constants defined in SwingConstants: LEFT,
     * CENTER, RIGHT, LEADING or TRAILING.
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement) {
        showNumberAndChart(showNumberAndChart, widthOfLabel, font, horizontalAlignement, numberFormat);
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the chart.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     * @param font the font to use for the label
     * @param horizontalAlignement the horizontal alignment of the text in the
     * label: one of the following constants defined in SwingConstants: LEFT,
     * CENTER, RIGHT, LEADING or TRAILING.
     * @param numberFormat the decimal format to use when showing the numbers
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement, DecimalFormat numberFormat) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        labelHorizontalAlignement = horizontalAlignement;
        valueLabel.setFont(font);
        this.numberFormat = numberFormat;
    }

    /**
     * Set if the underlying numbers or the bar charts are to be shown.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    /**
     * If true, the first number is shown as the value. Otherwise the total
     * value is shown.
     *
     * @param showFirstNumber
     */
    public void showFirstNumber(boolean showFirstNumber) {
        this.showFirstNumber = showFirstNumber;
    }

    /**
     * Sets up the cell renderer for the given cell.
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

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());

        if (backgroundColor != null) {
            setBackground(backgroundColor);
        } else {
            setBackground(c.getBackground());
        }

        // if the cell is empty, simply return
        if (value == null) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        if (value instanceof String) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        if (!(value instanceof ArrayList)) {
            return c;
        } else {
            try {
                ArrayList<Double> temp = (ArrayList<Double>) value;
            } catch (ClassCastException e) {
                return c;
            }
        }

        // get the dataset
        ArrayList<Double> values = (ArrayList<Double>) value;

        double sumValues = 0;
        for (int i = 0; i < values.size(); i++) {
            sumValues += values.get(i);
        }

        // show the number and/or the chart if option selected
        if (showNumberAndChart || showNumbers) {

            // set the decimal format symbol
            numberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            double tempValue = sumValues;

            if (showFirstNumber) {
                if (!values.isEmpty()) {
                    tempValue = values.get(0);
                }
            }

            if (showNumbers) {

                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, numberFormat.format(tempValue),
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

                Color bg = c.getBackground();
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure why.
                c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

                return c;
            }

            valueLabel.setText(numberFormat.format(tempValue));

            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            valueLabel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

            // add some padding
            if (labelHorizontalAlignement == SwingConstants.RIGHT) {
                valueLabel.setText(valueLabel.getText() + "  ");
            } else if (labelHorizontalAlignement == SwingConstants.LEFT) {
                valueLabel.setText("  " + valueLabel.getText());
            }

            valueLabel.setForeground(c.getForeground());

            // set the horizontal text alignment and the label size
            valueLabel.setHorizontalAlignment(labelHorizontalAlignement);
            valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
            valueLabel.setSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setMaximumSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setPreferredSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setVisible(true);

        } else {
            valueLabel.setMinimumSize(new Dimension(0, 0));
            valueLabel.setSize(0, 0);
            valueLabel.setVisible(false);
        }

        // create the chart
        DefaultCategoryDataset barChartDataset = new DefaultCategoryDataset();

        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setShadowVisible(false);

        String tooltip = "";
        for (int i = 0; i < values.size(); i++) {
            barChartDataset.addValue(values.get(i), "" + i, "" + 0);
            renderer.setSeriesPaint(i, colors.get(i));
            tooltip += values.get(i).intValue();
            if (i < values.size() - 1) {
                tooltip += " / ";
            }
        }

        if (fillColor != null) {
            double fillValue = maxValue - sumValues;

            if (fillValue > 0) {
                barChartDataset.addValue(fillValue, "" + values.size(), "" + 0);
                renderer.setSeriesPaint(values.size(), fillColor);
            }
        }

        chart = ChartFactory.createStackedBarChart(null, null, null, barChartDataset, plotOrientation, false, false, false);

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // remove space before/after the domain axis
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);

        // remove space before/after the range axis
        plot.getRangeAxis().setUpperMargin(0);
        plot.getRangeAxis().setLowerMargin(0);

        // add reference lines, if any
        Iterator<String> allReferencesLines = referenceLines.keySet().iterator();

        while (allReferencesLines.hasNext()) {
            ReferenceLine currentReferenceLine = referenceLines.get(allReferencesLines.next());
            plot.addRangeMarker(new ValueMarker(currentReferenceLine.getValue(), currentReferenceLine.getLineColor(), new BasicStroke(currentReferenceLine.getLineWidth())));
        }

        // add reference areas, if any
        Iterator<String> allReferenceAreas = referenceAreas.keySet().iterator();

        while (allReferenceAreas.hasNext()) {
            ReferenceArea currentReferenceArea = referenceAreas.get(allReferenceAreas.next());
            IntervalMarker marker = new IntervalMarker(currentReferenceArea.getStart(), currentReferenceArea.getEnd(), currentReferenceArea.getAreaColor());
            marker.setAlpha(currentReferenceArea.getAlpha());
            plot.addRangeMarker(marker);
        }

        plot.getRangeAxis().setRange(0, maxValue);

        // add the dataset
        plot.setDataset(barChartDataset);

        // hide unwanted chart details
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        // set up the chart renderer
        plot.setRenderer(0, renderer);

        // set the tooltip
        setToolTipText(tooltip);

        // hide the outline
        chart.getPlot().setOutlineVisible(false);

        // make sure the background is the same as the table row color
        if (backgroundColor != null) {
            chart.getPlot().setBackgroundPaint(backgroundColor);
            chart.setBackgroundPaint(backgroundColor);
        } else {
            chart.getPlot().setBackgroundPaint(c.getBackground());
            chart.setBackgroundPaint(c.getBackground());
        }

        // create the chart panel and add it to the table cell
        chartPanel = new ChartPanel(chart);

        if (backgroundColor != null) {
            chartPanel.setBackground(backgroundColor);
        } else {
            chartPanel.setBackground(c.getBackground());
        }

        this.remove(1);
        this.add(chartPanel);

        return this;
    }

    /**
     * Add a reference line at a given data value.
     *
     * @param label the label for the reference
     * @param value the reference line value
     * @param lineWidth the line width
     * @param lineColor the line color
     */
    public void addReferenceLine(String label, double value, float lineWidth, Color lineColor) {
        referenceLines.put(label, new ReferenceLine(label, value, lineWidth, lineColor));
    }

    /**
     * Add a reference line at a given data value.
     *
     * @param referenceLine
     */
    public void addReferenceLine(ReferenceLine referenceLine) {
        referenceLines.put(referenceLine.getLabel(), referenceLine);
    }

    /**
     * Removes the reference line with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceLine(String label) {
        referenceLines.remove(label);
    }

    /**
     * Removes all the reference lines.
     */
    public void removeAllReferenceLines() {
        referenceLines = new HashMap<String, ReferenceLine>();
    }

    /**
     * Returns all the references lines as a hashmap, with the labels as the
     * keys.
     *
     * @return hashmap of all reference lines
     */
    public HashMap<String, ReferenceLine> getAllReferenceLines() {
        return referenceLines;
    }

    /**
     * Add a reference area.
     *
     * @param label the label for the reference area
     * @param start the start of the reference area
     * @param end the end of the reference area
     * @param areaColor the color of the reference area
     * @param alpha the alpha level of the reference area
     */
    public void addReferenceArea(String label, double start, double end, Color areaColor, float alpha) {
        referenceAreas.put(label, new ReferenceArea(label, start, end, areaColor, alpha));
    }

    /**
     * Add a reference area.
     *
     * @param referenceArea
     */
    public void addReferenceArea(ReferenceArea referenceArea) {
        referenceAreas.put(referenceArea.getLabel(), referenceArea);
    }

    /**
     * Removes the reference area with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceArea(String label) {
        referenceAreas.remove(label);
    }

    /**
     * Removes all the reference areas.
     */
    public void removeAllReferenceAreas() {
        referenceAreas = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the references areas as a hashmap, with the labels as the
     * keys.
     *
     * @return hashmap of all reference areas
     */
    public HashMap<String, ReferenceArea> getAllReferenceAreas() {
        return referenceAreas;
    }

    /**
     * Set the maximum value.
     *
     * @param maxValue the maximum value
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Get the colors used for the chart.
     *
     * @return the color used for the chart
     */
    public ArrayList<Color> getColors() {
        return colors;
    }

    /**
     * Set the colors used for the chart.
     *
     * @param colors the colors to set
     */
    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    /**
     * Get the current plot orientation.
     *
     * @return the current plot orientation
     */
    public PlotOrientation getPlotOrientation() {
        return plotOrientation;
    }

    /**
     * Set the plot orientation.
     *
     * @param plotOrientation the new plot orientation
     */
    public void setPlotOrientation(PlotOrientation plotOrientation) {
        this.plotOrientation = plotOrientation;
    }

    /**
     * Returns a reference to the chart panel.
     *
     * @return the chart panel.
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Set the background color. If not set the background color of the given
     * row will be used.
     *
     * @param color the new background color
     */
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }
    // @TODO: this ought to work but does not...
//    @Override
//    public String getToolTipText(MouseEvent event) {
//        return chartPanel.getToolTipText(event);
//    }   

    /**
     * Returns the maximum value.
     *
     * @return the maxValue
     */
    public double getMaxValue() {
        return maxValue;
    }
}
