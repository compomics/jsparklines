package no.uib.jsparklines.renderers.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 * Custom renderer making it possible to use different colors for individual
 * data bars in 3D bar charts.
 *
 * @author Harald Barsnes
 */
public class BarChartColorRenderer extends BarRenderer {

    /**
     * The colors to use.
     */
    private ArrayList<Color> colors;
    /**
     * The main bar chart color. Used when only one color is to be used.
     */
    private Color mainColor = null;

    /**
     * Creates a new renderer.
     *
     * @param colors the colors to use.
     */
    public BarChartColorRenderer(ArrayList<Color> colors) {
        super();
        this.colors = colors;
    }

    /**
     * Creates a new renderer. Use this when all the bars are to have the same
     * color.
     *
     * @param color the color to use.
     */
    public BarChartColorRenderer(Color color) {
        super();
        mainColor = color;
    }

    @Override
    public Paint getItemPaint(final int row, final int column) {

        if (mainColor != null) {
            return mainColor;
        }

        if (colors != null && colors.size() > column) {
            return this.colors.get(column);
        } else {
            return Color.BLACK; // this default color should never be visible on screen
        }
    }

    @Override
    public void drawBackground(Graphics2D g2, CategoryPlot plot, Rectangle2D dataArea) {
        // Given that the 3D effect is not used, the drawBackground method
        // is overwritten. If this is not done the hardcoded grey corners
        // of the background wall is shown.
    }
}
