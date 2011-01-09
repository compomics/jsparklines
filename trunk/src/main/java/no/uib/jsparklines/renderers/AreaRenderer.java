
package no.uib.jsparklines.renderers;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;

import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.util.ShapeUtilities;

/**
 * Custom renderer.
 *
 * From an example found at www.jfree.org.
 * Complete link: www.jfree.org/phpBB2/viewtopic.php?f=10&t=15596
 */
public class AreaRenderer extends XYAreaRenderer {

    /**
     * A state object used by this renderer.
     */
    static class AreaRendererState extends XYItemRendererState {

        /** Working storage for the area under one series. */
        public Polygon area;
        /** Working line that can be recycled. */
        public Line2D line;

        /**
         * Creates a new state.
         *
         * @param info the plot rendering info.
         */
        public AreaRendererState(PlotRenderingInfo info) {
            super(info);
            this.area = new Polygon();
            this.line = new Line2D.Double();
        }
    }

    /**
     * Constructs a new renderer.
     */
    public AreaRenderer() {
        this(AREA);
    }

    /**
     * Constructs a new renderer.
     *
     * @param type the type of the renderer.
     */
    public AreaRenderer(int type) {
        this(type, null, null);
    }

    /**
     * Constructs a new renderer.
     * <p>
     * To specify the type of renderer, use one of the constants: SHAPES, LINES,
     * SHAPES_AND_LINES, AREA or AREA_AND_SHAPES.
     *
     * @param type the type of renderer.
     * @param toolTipGenerator the tool tip generator to use
     * (<code>null</code> permitted).
     * @param urlGenerator the URL generator (<code>null</code> permitted).
     */
    public AreaRenderer(int type, XYToolTipGenerator toolTipGenerator, XYURLGenerator urlGenerator) {
        super(type, toolTipGenerator, urlGenerator);
    }

    /**
     * Initialises the renderer and returns a state object that should be
     * passed to all subsequent calls to the drawItem() method.
     *
     * @param g2 the graphics device.
     * @param dataArea the area inside the axes.
     * @param plot the plot.
     * @param data the data.
     * @param info an optional info collection object to return data back to
     * the caller.
     *
     * @return A state object for use by the renderer.
     */
    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset data, PlotRenderingInfo info) {
        AreaRendererState state = new AreaRendererState(info);
        return state;
    }

    /**
     * Draws the visual representation of a single data item.
     *
     * @param g2 the graphics device.
     * @param state the renderer state.
     * @param dataArea the area within which the data is being drawn.
     * @param info collects information about the drawing.
     * @param plot the plot (can be used to obtain standard color information
     * etc).
     * @param domainAxis the domain axis.
     * @param rangeAxis the range axis.
     * @param dataset the dataset.
     * @param series the series index (zero-based).
     * @param item the item index (zero-based).
     * @param crosshairState crosshair information for the plot
     * (<code>null</code> permitted).
     * @param pass the pass index.
     */
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {

        if (!getItemVisible(series, item)) {
            return;
        }

        AreaRendererState areaState = (AreaRendererState) state;

        // get the data point...
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);

        if (Double.isNaN(y1)) {
            y1 = 0.0;
        }

        double transX1 = domainAxis.valueToJava2D(x1, dataArea, plot.getDomainAxisEdge());
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, plot.getRangeAxisEdge());

        // get the previous point and the next point so we can calculate a
        // "hot spot" for the area (used by the chart entity)...
        int itemCount = dataset.getItemCount(series);
        double x0 = dataset.getXValue(series, Math.max(item - 1, 0));
        double y0 = dataset.getYValue(series, Math.max(item - 1, 0));

        if (Double.isNaN(y0)) {
            y0 = 0.0;
        }

        double transX0 = domainAxis.valueToJava2D(x0, dataArea, plot.getDomainAxisEdge());
        double transY0 = rangeAxis.valueToJava2D(y0, dataArea, plot.getRangeAxisEdge());

        double x2 = dataset.getXValue(series, Math.min(item + 1, itemCount - 1));
        double y2 = dataset.getYValue(series, Math.min(item + 1, itemCount - 1));

        if (Double.isNaN(y2)) {
            y2 = 0.0;
        }

        double transX2 = domainAxis.valueToJava2D(x2, dataArea, plot.getDomainAxisEdge());
        double transY2 = rangeAxis.valueToJava2D(y2, dataArea, plot.getRangeAxisEdge());

        double transZero = rangeAxis.valueToJava2D(0.0, dataArea, plot.getRangeAxisEdge());

        Polygon hotspot = null;

        if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {

            hotspot = new Polygon();
            hotspot.addPoint((int) transZero, (int) ((transX0 + transX1) / 2.0));
            hotspot.addPoint((int) ((transY0 + transY1) / 2.0), (int) ((transX0 + transX1) / 2.0));
            hotspot.addPoint((int) transY1, (int) transX1);
            hotspot.addPoint((int) ((transY1 + transY2) / 2.0), (int) ((transX1 + transX2) / 2.0));
            hotspot.addPoint((int) transZero, (int) ((transX1 + transX2) / 2.0));

        } else { // vertical orientation

            hotspot = new Polygon();
            hotspot.addPoint((int) ((transX0 + transX1) / 2.0), (int) transZero);
            hotspot.addPoint((int) ((transX0 + transX1) / 2.0), (int) ((transY0 + transY1) / 2.0));
            hotspot.addPoint((int) transX1, (int) transY1);
            hotspot.addPoint((int) ((transX1 + transX2) / 2.0), (int) ((transY1 + transY2) / 2.0));
            hotspot.addPoint((int) ((transX1 + transX2) / 2.0), (int) transZero);
        }

        if (item == 0) { // create a new area polygon for the series

            areaState.area = new Polygon();

            // the first point is (x, 0)
            double zero = rangeAxis.valueToJava2D(0.0, dataArea, plot.getRangeAxisEdge());

            if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                areaState.area.addPoint((int) transX1, (int) zero);
            } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                areaState.area.addPoint((int) zero, (int) transX1);
            }
        }

        // Add each point to Area (x, y)
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            areaState.area.addPoint((int) transX1, (int) transY1);
        } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            areaState.area.addPoint((int) transY1, (int) transX1);
        }

        PlotOrientation orientation = plot.getOrientation();
        Paint paint = getItemPaint(series, item);
        Stroke stroke = getItemStroke(series, item);
        g2.setPaint(paint);
        g2.setStroke(stroke);

        Shape shape = null;

        if (getPlotShapes()) {
            shape = getItemShape(series, item);
            if (orientation == PlotOrientation.VERTICAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, transX1,
                        transY1);
            } else if (orientation == PlotOrientation.HORIZONTAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, transY1,
                        transX1);
            }
            g2.draw(shape);
        }

        if (getPlotLines()) {
            if (item > 0) {
                if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    areaState.line.setLine(transX0, transY0, transX1, transY1);
                } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                    areaState.line.setLine(transY0, transX0, transY1, transX1);
                }
                g2.draw(areaState.line);
            }
        }

        // Check if the item is the last item for the series.
        // and number of items > 0. We can't draw an area for a single point.
        if (getPlotArea() && item > 0 && item == (itemCount - 1)) {

            if (orientation == PlotOrientation.VERTICAL) {
                // Add the last point (x,0)
                areaState.area.addPoint((int) transX1, (int) transZero);
            } else if (orientation == PlotOrientation.HORIZONTAL) {
                // Add the last point (x,0)
                areaState.area.addPoint((int) transZero, (int) transX1);
            }

            Paint fillPaint = getItemFillPaint(series, item);
            if (fillPaint instanceof GradientPaint) {
                GradientPaint gp = (GradientPaint) fillPaint;
                GradientPaintTransformer t = new StandardGradientPaintTransformer();
                fillPaint = t.transform(gp, areaState.area.getBounds());
            }
            g2.setPaint(fillPaint);
            g2.fill(areaState.area);

            // draw an outline around the Area.
            if (isOutline()) {
                g2.setStroke(getItemOutlineStroke(series, item));
                g2.setPaint(getItemOutlinePaint(series, item));
                g2.draw(areaState.area);
            }
        }

        updateCrosshairValues(crosshairState, x1, y1, 0, 0, transX1, transY1, orientation);

        // collect entity and tool tip information...
        if (state.getInfo() != null) {

            EntityCollection entities = state.getEntityCollection();

            if (entities != null && hotspot != null) {

                String tip = null;
                XYToolTipGenerator generator = getToolTipGenerator(series, item);

                if (generator != null) {
                    tip = generator.generateToolTip(dataset, series, item);
                }

                String url = null;

                if (getURLGenerator() != null) {
                    url = getURLGenerator().generateURL(dataset, series, item);
                }

                XYItemEntity entity = new XYItemEntity(hotspot, dataset, series, item, tip, url);
                entities.add(entity);
            }
        }
    }
}
