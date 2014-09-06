package no.uib.jsparklines.renderers;

import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Table cell renderer displaying integers as colored equal size bar charts.
 * Supported input: Integer objects. Other object types are rendered using the
 * DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesIntegerColorTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;
    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;
    /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The label used to display the number and the bar chart at the same time.
     */
    private JLabel valueLabel;
    /**
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;
    /**
     * If true the value of the chart is shown next to the bar chart.
     */
    private boolean showNumberAndChart = false;
    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;
    /**
     * The background color used for the plots. For plots using light colors,
     * it's recommended to use a dark background color, and for plots using
     * darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;
    /**
     * The default color. Used if an un-mapped integer is found.
     */
    private Color defaultColor;
    /**
     * The integer to color mappings.
     */
    private HashMap<Integer, Color> colors;
    /**
     * The integer to color tooltip mappings.
     */
    private HashMap<Integer, String> tooltips;

    /**
     * Creates a new JSparklinesIntegerColorTableCellRenderer, where all integer
     * cell values are displayed as equal size bars, but using different colors
     * as defined by the colors hash map.
     *
     * @param defaultColor the color to use for the bars if an integer without a
     * mapped color is found
     * @param colors a HashMap with the integer to color mappings
     */
    public JSparklinesIntegerColorTableCellRenderer(Color defaultColor, HashMap<Integer, Color> colors) {
        this(defaultColor, colors, new HashMap<Integer, String>());
    }

    /**
     * Creates a new JSparklinesIntegerColorTableCellRenderer, where all integer
     * cell values are displayed as equal size bars, but using different colors
     * as defined by the colors hash map.
     *
     * @param defaultColor the color to use for the bars if an integer without a
     * mapped color is found
     * @param colors a HashMap with the integer to color mappings
     * @param tooltips a HashMap with the integer to tooltip mappings
     */
    public JSparklinesIntegerColorTableCellRenderer(Color defaultColor, HashMap<Integer, Color> colors, HashMap<Integer, String> tooltips) {

        this.defaultColor = defaultColor;
        this.colors = colors;
        this.tooltips = tooltips;

        setUpRendererAndChart();
    }

    /**
     * Sets up the table cell renderer and the bar chart.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart() {

        setName("Table.cellRenderer");
        setLayout(new BorderLayout());

        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
        this.chartPanel = new ChartPanel(chart);

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
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
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart if true the number and the chart is shown in
     * the cell
     * @param widthOfLabel the width used to display the label containing the
     * number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
    }

    /**
     * If true the number will be shown together with the bar chart in the cell.
     * False only display the bar chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
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
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        labelHorizontalAlignement = horizontalAlignement;
        valueLabel.setFont(font);
    }

    /**
     * Set if the underlying numbers or the bar charts are to be shown.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        // check if the cell contains an integer object
        if (value == null || !(value instanceof Integer)) {
            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }

        // if show numbers, format as number and return
        if (showNumbers) {

            c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, (Integer) value,
                    isSelected, hasFocus, row, column);

            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);

            Color bg = c.getBackground();
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

            return c;
        }

        // set the tooltip text
        if (tooltips.get((Integer) value) != null) {
            this.setToolTipText(tooltips.get((Integer) value));
        } else {
            this.setToolTipText("" + value);
        }

        // show the number _and_ the chart if option selected
        if (showNumberAndChart) {

            valueLabel.setText("" + Integer.valueOf("" + value).intValue());

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

        // respect focus and hightlighting
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());

        // create the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(Integer.valueOf(1), "1", "1");

        // fine tune the chart properites
        CategoryPlot plot = chart.getCategoryPlot();

        // add the dataset
        plot.setDataset(dataset);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        // set up the chart renderer
        CategoryItemRenderer renderer;

        if (colors.get((Integer) value) == null) {
            renderer = new BarChartColorRenderer(defaultColor);
        } else {
            renderer = new BarChartColorRenderer(colors.get((Integer) value));
        }

        // make sure the background is the same as the table row color
        if (plotBackgroundColor != null && !isSelected) {
            plot.setBackgroundPaint(plotBackgroundColor);
            chartPanel.setBackground(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {
            // We have to create a new color object because Nimbus returns
            // a color of type DerivedColor, which behaves strange, not sure why.
            Color bg = c.getBackground();
            plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }

        plot.setRenderer(renderer);

        return this;
    }

    /**
     * Return the color map.
     *
     * @return the colors
     */
    public HashMap<Integer, Color> getColors() {
        return colors;
    }

    /**
     * Set the color map.
     *
     * @param colors the colors to set
     */
    public void setColors(HashMap<Integer, Color> colors) {
        this.colors = colors;
    }

    /**
     * Returns the tooltips map.
     *
     * @return the tooltips
     */
    public HashMap<Integer, String> getTooltips() {
        return tooltips;
    }

    /**
     * Set the tooltip map.
     *
     * @param tooltips the tooltips to set
     */
    public void setTooltips(HashMap<Integer, String> tooltips) {
        this.tooltips = tooltips;
    }
}
