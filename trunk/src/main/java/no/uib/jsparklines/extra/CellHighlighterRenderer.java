package no.uib.jsparklines.extra;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A render that highlights columns ending with a given string, by using a
 * different background color. Supported input is String.
 *
 * @author Harald Barsnes
 */
public class CellHighlighterRenderer implements TableCellRenderer {

    /**
     * The default background color.
     */
    private Color backgroundColor;
    /**
     * The highlight background color.
     */
    private Color highlightColor;
    /**
     * One of the following constants defined in SwingConstants: LEFT, CENTER
     * (the default for image-only labels), RIGHT, LEADING (the default for
     * text-only labels) or TRAILING.
     */
    private int align;
    /**
     * The label the strings have to end with in order to be highlighted.
     */
    private String highlightLabel;

    /**
     * Creates a new CellHighlighterRenderer.
     *
     * @param backgroundColor default background color
     * @param highlightColor highlight background color
     * @param align SwingConstant: LEFT, CENTER, RIGHT, LEADING or TRAILING.
     * @param highlightLabel the label the strings have to end with in order to
     * be highlighted
     */
    public CellHighlighterRenderer(Color backgroundColor, Color highlightColor, int align, String highlightLabel) {
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
        this.align = align;
        this.highlightLabel = highlightLabel;
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

        JLabel label = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setHorizontalAlignment(align);
        label.setBackground(backgroundColor);

        // set the highlight background color
        if (value instanceof String) {
            if (((String) value).endsWith(highlightLabel) && !isSelected) {
                label.setBackground(highlightColor);
            }
        }

        return label;
    }
}
