package no.uib.jsparklines.renderers;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Table cell renderer displaying integers as icons. Supported input: Integer
 * objects. Other object types are rendered using the DefaultTableCellRenderer.
 *
 * @author Harald Barsnes
 */
public class JSparklinesIntegerIconTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * The integer to icon mappings.
     */
    private HashMap<Integer, ImageIcon> icons;
    /**
     * The integer to icon tooltip mappings.
     */
    private HashMap<Integer, String> tooltips;
    /**
     * The default icon, can be null.
     */
    private ImageIcon defaultIcon;
    /**
     * The default tooltip, can be null.
     */
    private String defaultTooltip;

    /**
     * Creates a new JSparklinesIntegerIconTableCellRenderer, where all integer
     * cell values are displayed as an icon as defined by the icons hash map.
     *
     * @param icons a HashMap with the integer to icon mappings
     */
    public JSparklinesIntegerIconTableCellRenderer(HashMap<Integer, ImageIcon> icons) {
        this(null, null, icons, new HashMap<Integer, String>());
    }

    /**
     * Creates a new JSparklinesIntegerIconTableCellRenderer, where all integer
     * cell values are displayed as an icon as defined by the icons hash map.
     *
     * @param icons a HashMap with the integer to icon mappings
     * @param tooltips a HashMap with the integer to tooltip mappings
     */
    public JSparklinesIntegerIconTableCellRenderer(HashMap<Integer, ImageIcon> icons, HashMap<Integer, String> tooltips) {
        this(null, null, icons, tooltips);
    }

    /**
     * Creates a new JSparklinesIntegerIconTableCellRenderer, where all integer
     * cell values are displayed as an icon as defined by the icons hash map.
     *
     * @param defaultIcon the icon to use if an integer without a mapped icon is
     * @param defaultTooltip the default tooltip, can be null found, can be null
     * @param icons a HashMap with the integer to icon mappings
     */
    public JSparklinesIntegerIconTableCellRenderer(ImageIcon defaultIcon, String defaultTooltip, HashMap<Integer, ImageIcon> icons) {
        this(defaultIcon, defaultTooltip, icons, new HashMap<Integer, String>());
    }

    /**
     * Creates a new JSparklinesIntegerIconTableCellRenderer, where all integer
     * cell values are displayed is as an icon as defined by the icons hash map.
     *
     * @param defaultIcon the icon to use if an integer without a mapped icon is
     * found, can be null
     * @param defaultTooltip the default tooltip, can be null
     * @param icons a HashMap with the integer to icon mappings
     * @param tooltips a HashMap with the integer to tooltip mappings
     */
    public JSparklinesIntegerIconTableCellRenderer(ImageIcon defaultIcon, String defaultTooltip, HashMap<Integer, ImageIcon> icons, HashMap<Integer, String> tooltips) {
        this.defaultIcon = defaultIcon;
        this.defaultTooltip = defaultTooltip;
        this.icons = icons;
        this.tooltips = tooltips;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        Color bg = label.getBackground();
        // We have to create a new color object because Nimbus returns
        // a color of type DerivedColor, which behaves strange, not sure why.
        label.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));

        // set the icon to use for the boolean values
        if (value != null && value instanceof Integer) {

            label.setText(null);
            label.setHorizontalAlignment(SwingConstants.CENTER);

            Integer intValue = (Integer) value;
            ImageIcon icon = icons.get(intValue);

            if (icon != null) {
                label.setIcon(icon);
                label.setToolTipText(tooltips.get(intValue));
            } else {
                label.setIcon(defaultIcon);
                label.setToolTipText(defaultTooltip);
            }

        } else {
            label.setIcon(null);
            label.setToolTipText(null);
        }

        return label;
    }
}
