package no.uib.jsparklines;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.data.JSparklinesDataset;
import no.uib.jsparklines.renderers.JSparklinesBarChartTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesTableCellRenderer;
import org.jfree.chart.plot.PlotOrientation;

/**
 * A demo of how to use JSparklines to show sparklines in Java tables.
 *
 * @author Harald Barsnes
 */
public class JSparklinesDemo extends javax.swing.JFrame {

    /**
     * The first example color.
     */
    private Color colorA = new Color(251, 51, 51);
    /**
     * The second example color.
     */
    private Color colorB = new Color(51, 51, 251);
    /**
     * The third example color.
     */
    private Color colorC = new Color(110, 196, 97);

    /**
     * Sets up the demo.
     */
    public JSparklinesDemo() {

        // set up the GUI
        initComponents();

        // set additional GUI properties
        setAdditionalGuiProperties();


        // add data to the single values example
        addDataSingleValues();

        // set the JSparklines renderers for the columns containing numbers in the first example table
        // note: JSparklines with single values are editable, so the columns can be 'editable' in the JTable
        singleValuesJTable.getColumn("Fold Change").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, -5.0, 5.0, colorB, colorA));
        singleValuesJTable.getColumn("Peptides").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 80.0, colorC));
        singleValuesJTable.getColumn("Coverage").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, colorC));


        // add data to the multiple values example
        double maxValue = 10;
        addDataMultipleValues(maxValue);

        // set the JSparklines renderers
        // note: JSparklines with multiple values are NOT editable, so remember to set the columns as 'not editable' in the JTable
        multipleValuesJTable.getColumn("Change").setCellRenderer(new JSparklinesTableCellRenderer(JSparklinesTableCellRenderer.PlotType.lineChart, PlotOrientation.VERTICAL, 0.0, maxValue));


        // add data to the multiple data series example
        addDataMultipleDataSeries(maxValue);

        // set the JSparklines renderers
        // note: JSparklines with multiple values are NOT editable, so remember to set the columns as 'not editable' in the JTable
        multipleDataSeriesJTable.getColumn("Change").setCellRenderer(new JSparklinesTableCellRenderer(JSparklinesTableCellRenderer.PlotType.areaChart, PlotOrientation.VERTICAL, 0.0, maxValue));
    }

    /**
     * Setup some additional GUI properties.
     */
    private void setAdditionalGuiProperties() {

        // disable the moving of columns
        singleValuesJTable.getTableHeader().setReorderingAllowed(false);
        multipleValuesJTable.getTableHeader().setReorderingAllowed(false);
        multipleDataSeriesJTable.getTableHeader().setReorderingAllowed(false);

        // increase the row height in the tables for bigger sparkline plots
        multipleValuesJTable.setRowHeight(30);
        multipleDataSeriesJTable.setRowHeight(30);

        // set the maximum with of the protein columns
        multipleValuesJTable.getColumn("Protein").setMaxWidth(70);
        multipleDataSeriesJTable.getColumn("Protein").setMaxWidth(70);

        // set the dialog icon and title
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/line_plot.GIF")));
        this.setTitle("JSparklines " + getVersion() + " - Demo");

        // make the viewports see-through (to show the gradient background)
        singleValuesJScrollPane.getViewport().setOpaque(false);
        multipleValuesJScrollPane.getViewport().setOpaque(false);
        multipleDataSeriesJScrollPane.getViewport().setOpaque(false);

        // set the renderer for the comboboxes
        multipleValuesJComboBox.setRenderer(new ComboBoxListCellRenderer());
        multipleDataSeriesJComboBox.setRenderer(new ComboBoxListCellRenderer());

        // locate the dialog in the middle of the screen
        setLocationRelativeTo(null);
    }

    /**
     * Add data to the single values example.
     */
    private void addDataSingleValues() {

        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein A", new Double(4.44), new Integer(12), new Integer(30)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein B", new Double(-2.19), new Integer(11), new Integer(13)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein C", new Double(1.86), new Integer(2), new Integer(5)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein D", new Double(-2.17), new Integer(17), new Integer(32)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein E", new Double(3.01), new Integer(32), new Integer(57)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein F", new Double(2.62), new Integer(12), new Integer(28)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein G", new Double(5.33), new Integer(16), new Integer(37)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein H", new Double(5.65), new Integer(47), new Integer(61)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein I", new Double(1.81), new Integer(23), new Integer(45)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein J", new Double(-1.91), new Integer(78), new Integer(34)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein K", new Double(2.6), new Integer(15), new Integer(31)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein L", new Double(2.3), new Integer(31), new Integer(44)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein M", new Double(-2.45), new Integer(5), new Integer(14)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein N", new Double(3.59), new Integer(18), new Integer(56)});
        ((DefaultTableModel) singleValuesJTable.getModel()).addRow(new Object[]{
                    "Protein O", new Double(2.24), new Integer(25), new Integer(43)});
    }

    /**
     * Add data to the multiple values example.
     *
     * @param maxValue the maximum (random) value
     */
    private void addDataMultipleValues(double maxValue) {

        final int NUMBER_OF_ROWS = 8;
        final int NUMBER_OF_VALUES = 20;
        final int MAX_VALUE = new Double(maxValue).intValue();

        // ----------------------------------------
        // create the data and add it to the table
        // ----------------------------------------

        Random random = new Random();

        for (int j = 0; j < NUMBER_OF_ROWS; j++) {

            ArrayList<Double> dataA = new ArrayList<Double>();

            for (int i = 0; i < NUMBER_OF_VALUES; i++) {
                dataA.add(new Double(random.nextInt(MAX_VALUE)));
            }

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, colorA, "Dataset A");

            // add to dataset
            ArrayList<JSparklinesDataSeries> sparkLineDataSeriesAll = new ArrayList<JSparklinesDataSeries>();
            sparkLineDataSeriesAll.add(sparklineDataseriesA);

            JSparklinesDataset dataset = new JSparklinesDataset(sparkLineDataSeriesAll);

            // add to table
            ((DefaultTableModel) multipleValuesJTable.getModel()).addRow(new Object[]{"Protein " + (j + 1), dataset});
        }
    }

    /**
     * Add data to the multiple data series example.
     *
     * @param maxValue the maximum (random) value
     */
    private void addDataMultipleDataSeries(double maxValue) {

        final int NUMBER_OF_ROWS = 8;
        final int NUMBER_OF_VALUES = 20;
        final int MAX_VALUE = new Double(maxValue).intValue();

        // ----------------------------------------
        // create the data and add it to the table
        // ----------------------------------------

        Random random = new Random();

        for (int j = 0; j < NUMBER_OF_ROWS; j++) {

            ArrayList<Double> dataA = new ArrayList<Double>();
            ArrayList<Double> dataB = new ArrayList<Double>();
            ArrayList<Double> dataC = new ArrayList<Double>();

            for (int i = 0; i < NUMBER_OF_VALUES; i++) {
                dataA.add(new Double(random.nextInt(MAX_VALUE - 1)));
                dataB.add(new Double(random.nextInt(MAX_VALUE)));
                dataC.add(new Double(random.nextInt(MAX_VALUE + 1)));
            }

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, colorA, "Dataset A");
            JSparklinesDataSeries sparklineDataseriesB = new JSparklinesDataSeries(dataB, colorB, "Dataset B");
            JSparklinesDataSeries sparklineDataseriesC = new JSparklinesDataSeries(dataC, colorC, "Dataset C");

            // add to dataset
            ArrayList<JSparklinesDataSeries> sparkLineDataSeriesAll = new ArrayList<JSparklinesDataSeries>();
            sparkLineDataSeriesAll.add(sparklineDataseriesA);
            sparkLineDataSeriesAll.add(sparklineDataseriesB);
            sparkLineDataSeriesAll.add(sparklineDataseriesC);

            JSparklinesDataset dataset = new JSparklinesDataset(sparkLineDataSeriesAll);

            // add to table
            ((DefaultTableModel) multipleDataSeriesJTable.getModel()).addRow(new Object[]{"Protein " + (j + 1), dataset});
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, 
                    getBackground().brighter().brighter(), 0, getHeight(),
                    getBackground().darker());

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                super.paintComponent(grphcs);
            }            
        }
        ;
        singleValuesJPanel = new javax.swing.JPanel();
        showJSparklinesJCheckBox = new javax.swing.JCheckBox();
        singleValuesJScrollPane = new javax.swing.JScrollPane();
        singleValuesJTable = new JTable() {
            public boolean getScrollableTracksViewportHeight( )
            {
                return true;
            }
        };
        multipleValuesJPanel = new javax.swing.JPanel();
        multipleValuesJScrollPane = new javax.swing.JScrollPane();
        multipleValuesJTable = new javax.swing.JTable();
        verticalPlotMultipleValuesJCheckBox = new javax.swing.JCheckBox();
        multipleValuesJComboBox = new javax.swing.JComboBox();
        multipleDataSeriesJPanel = new javax.swing.JPanel();
        multipleDataSeriesJScrollPane = new javax.swing.JScrollPane();
        multipleDataSeriesJTable = new javax.swing.JTable();
        multipleDataSeriesJComboBox = new javax.swing.JComboBox();
        verticalPlotMultipleDataSeriesJCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSparklines Demo");
        setResizable(false);

        gradientPanel.setOpaque(false);

        singleValuesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Single Values"));
        singleValuesJPanel.setOpaque(false);

        showJSparklinesJCheckBox.setSelected(true);
        showJSparklinesJCheckBox.setText("Show Sparklines");
        showJSparklinesJCheckBox.setToolTipText("Turn the sparklines on or off");
        showJSparklinesJCheckBox.setIconTextGap(8);
        showJSparklinesJCheckBox.setOpaque(false);
        showJSparklinesJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showJSparklinesJCheckBoxActionPerformed(evt);
            }
        });

        singleValuesJScrollPane.setOpaque(false);

        singleValuesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protein", "Fold Change", "Peptides", "Coverage"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        singleValuesJTable.setFillsViewportHeight(true);
        singleValuesJTable.setOpaque(false);
        singleValuesJTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        singleValuesJScrollPane.setViewportView(singleValuesJTable);

        javax.swing.GroupLayout singleValuesJPanelLayout = new javax.swing.GroupLayout(singleValuesJPanel);
        singleValuesJPanel.setLayout(singleValuesJPanelLayout);
        singleValuesJPanelLayout.setHorizontalGroup(
            singleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, singleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(singleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showJSparklinesJCheckBox)
                    .addComponent(singleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addContainerGap())
        );
        singleValuesJPanelLayout.setVerticalGroup(
            singleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, singleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(singleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showJSparklinesJCheckBox))
        );

        multipleValuesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Values"));
        multipleValuesJPanel.setOpaque(false);

        multipleValuesJScrollPane.setOpaque(false);

        multipleValuesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protein", "Change"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        multipleValuesJTable.setOpaque(false);
        multipleValuesJTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        multipleValuesJScrollPane.setViewportView(multipleValuesJTable);

        verticalPlotMultipleValuesJCheckBox.setSelected(true);
        verticalPlotMultipleValuesJCheckBox.setText("Vertical");
        verticalPlotMultipleValuesJCheckBox.setToolTipText("Set the orientation of the charts");
        verticalPlotMultipleValuesJCheckBox.setIconTextGap(8);
        verticalPlotMultipleValuesJCheckBox.setOpaque(false);
        verticalPlotMultipleValuesJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verticalPlotMultipleValuesJCheckBoxActionPerformed(evt);
            }
        });

        multipleValuesJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line", "Area", "Bar", "Box", "Up/Down" }));
        multipleValuesJComboBox.setToolTipText("Set the chart type");
        multipleValuesJComboBox.setMaximumSize(new java.awt.Dimension(48, 20));
        multipleValuesJComboBox.setOpaque(false);
        multipleValuesJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multipleValuesJComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout multipleValuesJPanelLayout = new javax.swing.GroupLayout(multipleValuesJPanel);
        multipleValuesJPanel.setLayout(multipleValuesJPanelLayout);
        multipleValuesJPanelLayout.setHorizontalGroup(
            multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(multipleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addGroup(multipleValuesJPanelLayout.createSequentialGroup()
                        .addComponent(verticalPlotMultipleValuesJCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(multipleValuesJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        multipleValuesJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {multipleValuesJComboBox, verticalPlotMultipleValuesJCheckBox});

        multipleValuesJPanelLayout.setVerticalGroup(
            multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(multipleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(multipleValuesJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verticalPlotMultipleValuesJCheckBox)))
        );

        multipleDataSeriesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Data Series"));
        multipleDataSeriesJPanel.setOpaque(false);

        multipleDataSeriesJScrollPane.setOpaque(false);

        multipleDataSeriesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protein", "Change"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        multipleDataSeriesJTable.setFillsViewportHeight(true);
        multipleDataSeriesJTable.setOpaque(false);
        multipleDataSeriesJTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        multipleDataSeriesJScrollPane.setViewportView(multipleDataSeriesJTable);

        multipleDataSeriesJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line", "Area", "Bar", "Stack", "Stack %", "Box", "Pie", "Up/Down" }));
        multipleDataSeriesJComboBox.setToolTipText("Set the chart type");
        multipleDataSeriesJComboBox.setMaximumSize(new java.awt.Dimension(48, 20));
        multipleDataSeriesJComboBox.setOpaque(false);
        multipleDataSeriesJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multipleDataSeriesJComboBoxActionPerformed(evt);
            }
        });

        verticalPlotMultipleDataSeriesJCheckBox.setSelected(true);
        verticalPlotMultipleDataSeriesJCheckBox.setText("Vertical");
        verticalPlotMultipleDataSeriesJCheckBox.setToolTipText("Set the orientation of the charts");
        verticalPlotMultipleDataSeriesJCheckBox.setIconTextGap(8);
        verticalPlotMultipleDataSeriesJCheckBox.setOpaque(false);
        verticalPlotMultipleDataSeriesJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verticalPlotMultipleDataSeriesJCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout multipleDataSeriesJPanelLayout = new javax.swing.GroupLayout(multipleDataSeriesJPanel);
        multipleDataSeriesJPanel.setLayout(multipleDataSeriesJPanelLayout);
        multipleDataSeriesJPanelLayout.setHorizontalGroup(
            multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(multipleDataSeriesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(multipleDataSeriesJPanelLayout.createSequentialGroup()
                        .addComponent(verticalPlotMultipleDataSeriesJCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(multipleDataSeriesJComboBox, 0, 87, Short.MAX_VALUE))
                    .addComponent(multipleDataSeriesJScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        multipleDataSeriesJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {multipleDataSeriesJComboBox, verticalPlotMultipleDataSeriesJCheckBox});

        multipleDataSeriesJPanelLayout.setVerticalGroup(
            multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleDataSeriesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(multipleDataSeriesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(multipleDataSeriesJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verticalPlotMultipleDataSeriesJCheckBox)))
        );

        javax.swing.GroupLayout gradientPanelLayout = new javax.swing.GroupLayout(gradientPanel);
        gradientPanel.setLayout(gradientPanelLayout);
        gradientPanelLayout.setHorizontalGroup(
            gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(singleValuesJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(multipleValuesJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(multipleDataSeriesJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gradientPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {multipleValuesJPanel, singleValuesJPanel});

        gradientPanelLayout.setVerticalGroup(
            gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(multipleDataSeriesJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(multipleValuesJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(singleValuesJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Turns the bar chart renderer on or off. When turned off the underlaying 
     * values are shown.
     * 
     * @param evt
     */
    private void showJSparklinesJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showJSparklinesJCheckBoxActionPerformed
        ((JSparklinesBarChartTableCellRenderer) singleValuesJTable.getColumn("Fold Change").getCellRenderer()).showNumbers(!showJSparklinesJCheckBox.isSelected());
        ((JSparklinesBarChartTableCellRenderer) singleValuesJTable.getColumn("Peptides").getCellRenderer()).showNumbers(!showJSparklinesJCheckBox.isSelected());
        ((JSparklinesBarChartTableCellRenderer) singleValuesJTable.getColumn("Coverage").getCellRenderer()).showNumbers(!showJSparklinesJCheckBox.isSelected());

        singleValuesJTable.revalidate();
        singleValuesJTable.repaint();
    }//GEN-LAST:event_showJSparklinesJCheckBoxActionPerformed

    /**
     * Updates the plot type.
     *
     * @param evt
     */
    private void multipleValuesJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multipleValuesJComboBoxActionPerformed

        String selectedPlotType = (String) multipleValuesJComboBox.getSelectedItem();

        // update the chart type
        if (selectedPlotType.equalsIgnoreCase("Line")) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.lineChart);
        } else if (selectedPlotType.equalsIgnoreCase("Bar")) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.barChart);
        } else if (selectedPlotType.equalsIgnoreCase("Area")) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.areaChart);
        } else if (selectedPlotType.equalsIgnoreCase("Box")) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.boxPlot);
        } else if (selectedPlotType.equalsIgnoreCase("Up/Down")) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.upDownChart);
        }

        // update the chart orientation
        if (verticalPlotMultipleValuesJCheckBox.isSelected()) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotOrientation(PlotOrientation.VERTICAL);
        } else {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotOrientation(PlotOrientation.HORIZONTAL);
        }

        // repaint the table to update the plot
        multipleValuesJTable.revalidate();
        multipleValuesJTable.repaint();
    }//GEN-LAST:event_multipleValuesJComboBoxActionPerformed

    /**
     * Updates the plot orientation.
     * 
     * @param evt
     */
    private void verticalPlotMultipleValuesJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verticalPlotMultipleValuesJCheckBoxActionPerformed
        multipleValuesJComboBoxActionPerformed(null);
    }//GEN-LAST:event_verticalPlotMultipleValuesJCheckBoxActionPerformed

    /**
     * Updates the plot type.
     *
     * @param evt
     */
    private void multipleDataSeriesJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multipleDataSeriesJComboBoxActionPerformed

        String selectedPlotType = (String) multipleDataSeriesJComboBox.getSelectedItem();

        // change the width of the columns to make sure that a pie chart becomes circular
        if (selectedPlotType.equalsIgnoreCase("Pie")) {
            multipleDataSeriesJTable.getColumn("Protein").setMaxWidth(Integer.MAX_VALUE);
            multipleDataSeriesJTable.getColumn("Change").setMaxWidth(multipleDataSeriesJTable.getRowHeight() + 30);
        } else {
            multipleDataSeriesJTable.getColumn("Protein").setMaxWidth(70);
            multipleDataSeriesJTable.getColumn("Change").setMaxWidth(Integer.MAX_VALUE);
        }

        // update the chart type
        if (selectedPlotType.equalsIgnoreCase("Line")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.lineChart);
        } else if (selectedPlotType.equalsIgnoreCase("Bar")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.barChart);
        } else if (selectedPlotType.equalsIgnoreCase("Pie")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.pieChart);
        } else if (selectedPlotType.equalsIgnoreCase("Stack")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.stackedBarChart);
        } else if (selectedPlotType.equalsIgnoreCase("Stack %")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.stackedPercentBarChart);
        } else if (selectedPlotType.equalsIgnoreCase("Area")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.areaChart);
        } else if (selectedPlotType.equalsIgnoreCase("Box")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.boxPlot);
        } else if (selectedPlotType.equalsIgnoreCase("Up/Down")) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.upDownChart);
        }

        // update the chart orientation
        if (verticalPlotMultipleDataSeriesJCheckBox.isSelected()) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotOrientation(PlotOrientation.VERTICAL);
        } else {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotOrientation(PlotOrientation.HORIZONTAL);
        }

        // repaint the table to update the plot
        multipleDataSeriesJTable.revalidate();
        multipleDataSeriesJTable.repaint();
    }//GEN-LAST:event_multipleDataSeriesJComboBoxActionPerformed

    /**
     * Updates the plot orientation.
     * 
     * @param evt
     */
    private void verticalPlotMultipleDataSeriesJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verticalPlotMultipleDataSeriesJCheckBoxActionPerformed
        multipleDataSeriesJComboBoxActionPerformed(null);
    }//GEN-LAST:event_verticalPlotMultipleDataSeriesJCheckBoxActionPerformed

    /**
     * Starts the JSparklines demo.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JSparklinesDemo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gradientPanel;
    private javax.swing.JComboBox multipleDataSeriesJComboBox;
    private javax.swing.JPanel multipleDataSeriesJPanel;
    private javax.swing.JScrollPane multipleDataSeriesJScrollPane;
    private javax.swing.JTable multipleDataSeriesJTable;
    private javax.swing.JComboBox multipleValuesJComboBox;
    private javax.swing.JPanel multipleValuesJPanel;
    private javax.swing.JScrollPane multipleValuesJScrollPane;
    private javax.swing.JTable multipleValuesJTable;
    private javax.swing.JCheckBox showJSparklinesJCheckBox;
    private javax.swing.JPanel singleValuesJPanel;
    private javax.swing.JScrollPane singleValuesJScrollPane;
    private javax.swing.JTable singleValuesJTable;
    private javax.swing.JCheckBox verticalPlotMultipleDataSeriesJCheckBox;
    private javax.swing.JCheckBox verticalPlotMultipleValuesJCheckBox;
    // End of variables declaration//GEN-END:variables

    /**
     * Retrieves the version number set in the pom file.
     *
     * @return the version number of JSparklines
     */
    public String getVersion() {

        java.util.Properties p = new java.util.Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("jsparklines.properties");
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p.getProperty("jsparklines.version");
    }

    /**
     * An non-opaque cell renderer, making the panel behind the table visible.
     */
    public class NonOpaqueCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JComponent c = (JComponent) super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            ((JComponent) c).setOpaque(isSelected);
            return c;
        }
    }

    /**
     * A simple combo box render making sure that the combo boxes looks good
     * with the current gradient background.
     */
    public class ComboBoxListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel c = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            
            ((JComponent) c).setOpaque(isSelected);

            if (isSelected || cellHasFocus) {
                c.setBackground(Color.LIGHT_GRAY);
            }

            c.setHorizontalAlignment(SwingConstants.CENTER);

            return c;
        }
    }
}
