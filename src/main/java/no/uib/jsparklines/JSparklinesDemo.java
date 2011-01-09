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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
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
     * The color to use for the positive values.
     */
    private Color positiveColor = new Color(251, 51, 51);
    /**
     * The color to use for the negative values.
     */
    private Color negativeColor = new Color(51, 51, 251);
    /**
     * The color to use for the neutral values.
     */
    private Color neutralColor = new Color(110, 196, 97);

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
        singleValuesJTable.getColumn("Fold Change").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, -5.0, 5.0, negativeColor, positiveColor));
        singleValuesJTable.getColumn("Peptides").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 80.0, neutralColor));
        singleValuesJTable.getColumn("Coverage").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, neutralColor));


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
        multipleDataSeriesJTable.getColumn("Change").setCellRenderer(new JSparklinesTableCellRenderer(JSparklinesTableCellRenderer.PlotType.barChart, PlotOrientation.VERTICAL, 0.0, maxValue));
    }

    /**
     * Setup some additional GUI properties.
     */
    private void setAdditionalGuiProperties () {

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

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, Color.DARK_GRAY, "Dataset A");

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

            for (int i = 0; i < NUMBER_OF_VALUES; i++) {
                dataA.add(new Double(random.nextInt(MAX_VALUE)));
                dataB.add(new Double(random.nextInt(MAX_VALUE)));
            }

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, Color.BLUE, "Dataset A");
            JSparklinesDataSeries sparklineDataseriesB = new JSparklinesDataSeries(dataB, Color.RED, "Dataset B");

            // add to dataset
            ArrayList<JSparklinesDataSeries> sparkLineDataSeriesAll = new ArrayList<JSparklinesDataSeries>();
            sparkLineDataSeriesAll.add(sparklineDataseriesA);
            sparkLineDataSeriesAll.add(sparklineDataseriesB);

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

        buttonGroupMultipleValues = new javax.swing.ButtonGroup();
        buttonGroupMultipleDataSeries = new javax.swing.ButtonGroup();
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
        lineChartMultipleValuesJRadioButton = new javax.swing.JRadioButton();
        barChartMultipleValuesJRadioButton = new javax.swing.JRadioButton();
        multipleDataSeriesJPanel = new javax.swing.JPanel();
        multipleDataSeriesJScrollPane = new javax.swing.JScrollPane();
        multipleDataSeriesJTable = new javax.swing.JTable();
        lineChartMultipleDataSeriesJRadioButton = new javax.swing.JRadioButton();
        barChartMultipleDataSeriesJRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSparklines Demo");
        setResizable(false);

        gradientPanel.setOpaque(false);

        singleValuesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Single Values"));
        singleValuesJPanel.setOpaque(false);

        showJSparklinesJCheckBox.setSelected(true);
        showJSparklinesJCheckBox.setText("Show Sparklines");
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
                    .addComponent(singleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
        singleValuesJPanelLayout.setVerticalGroup(
            singleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, singleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(singleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
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

        buttonGroupMultipleValues.add(lineChartMultipleValuesJRadioButton);
        lineChartMultipleValuesJRadioButton.setSelected(true);
        lineChartMultipleValuesJRadioButton.setText("Line");
        lineChartMultipleValuesJRadioButton.setOpaque(false);
        lineChartMultipleValuesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineChartMultipleValuesJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroupMultipleValues.add(barChartMultipleValuesJRadioButton);
        barChartMultipleValuesJRadioButton.setText("Bar");
        barChartMultipleValuesJRadioButton.setOpaque(false);
        barChartMultipleValuesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barChartMultipleValuesJRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout multipleValuesJPanelLayout = new javax.swing.GroupLayout(multipleValuesJPanel);
        multipleValuesJPanel.setLayout(multipleValuesJPanelLayout);
        multipleValuesJPanelLayout.setHorizontalGroup(
            multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(multipleValuesJPanelLayout.createSequentialGroup()
                        .addComponent(lineChartMultipleValuesJRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barChartMultipleValuesJRadioButton))
                    .addComponent(multipleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );

        multipleValuesJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {barChartMultipleValuesJRadioButton, lineChartMultipleValuesJRadioButton});

        multipleValuesJPanelLayout.setVerticalGroup(
            multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleValuesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(multipleValuesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(multipleValuesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barChartMultipleValuesJRadioButton)
                    .addComponent(lineChartMultipleValuesJRadioButton)))
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

        buttonGroupMultipleDataSeries.add(lineChartMultipleDataSeriesJRadioButton);
        lineChartMultipleDataSeriesJRadioButton.setText("Line");
        lineChartMultipleDataSeriesJRadioButton.setOpaque(false);
        lineChartMultipleDataSeriesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineChartMultipleDataSeriesJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroupMultipleDataSeries.add(barChartMultipleDataSeriesJRadioButton);
        barChartMultipleDataSeriesJRadioButton.setSelected(true);
        barChartMultipleDataSeriesJRadioButton.setText("Bar");
        barChartMultipleDataSeriesJRadioButton.setOpaque(false);
        barChartMultipleDataSeriesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barChartMultipleDataSeriesJRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout multipleDataSeriesJPanelLayout = new javax.swing.GroupLayout(multipleDataSeriesJPanel);
        multipleDataSeriesJPanel.setLayout(multipleDataSeriesJPanelLayout);
        multipleDataSeriesJPanelLayout.setHorizontalGroup(
            multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, multipleDataSeriesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(multipleDataSeriesJPanelLayout.createSequentialGroup()
                        .addComponent(lineChartMultipleDataSeriesJRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barChartMultipleDataSeriesJRadioButton))
                    .addComponent(multipleDataSeriesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
        multipleDataSeriesJPanelLayout.setVerticalGroup(
            multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(multipleDataSeriesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(multipleDataSeriesJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(multipleDataSeriesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barChartMultipleDataSeriesJRadioButton)
                    .addComponent(lineChartMultipleDataSeriesJRadioButton)))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gradientPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {multipleDataSeriesJPanel, multipleValuesJPanel, singleValuesJPanel});

        gradientPanelLayout.setVerticalGroup(
            gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(multipleDataSeriesJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, gradientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(multipleValuesJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(singleValuesJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private void lineChartMultipleValuesJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineChartMultipleValuesJRadioButtonActionPerformed

        if (lineChartMultipleValuesJRadioButton.isSelected()) {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.lineChart);
        } else {
            ((JSparklinesTableCellRenderer) multipleValuesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.barChart);
        }

        multipleValuesJTable.revalidate();
        multipleValuesJTable.repaint();
    }//GEN-LAST:event_lineChartMultipleValuesJRadioButtonActionPerformed

    /**
     * Updates the plot type.
     *
     * @param evt
     */
    private void barChartMultipleValuesJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barChartMultipleValuesJRadioButtonActionPerformed
        lineChartMultipleValuesJRadioButtonActionPerformed(null);
    }//GEN-LAST:event_barChartMultipleValuesJRadioButtonActionPerformed

    /**
     * Updates the plot type.
     *
     * @param evt
     */
    private void lineChartMultipleDataSeriesJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineChartMultipleDataSeriesJRadioButtonActionPerformed
        if (lineChartMultipleDataSeriesJRadioButton.isSelected()) {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.lineChart);
        } else {
            ((JSparklinesTableCellRenderer) multipleDataSeriesJTable.getColumn("Change").getCellRenderer()).setPlotType(JSparklinesTableCellRenderer.PlotType.barChart);
        }

        multipleDataSeriesJTable.revalidate();
        multipleDataSeriesJTable.repaint();
    }//GEN-LAST:event_lineChartMultipleDataSeriesJRadioButtonActionPerformed

    /**
     * Updates the plot type.
     *
     * @param evt
     */
    private void barChartMultipleDataSeriesJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barChartMultipleDataSeriesJRadioButtonActionPerformed
        lineChartMultipleDataSeriesJRadioButtonActionPerformed(null);
    }//GEN-LAST:event_barChartMultipleDataSeriesJRadioButtonActionPerformed

    /**
     * Starts the demo.
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
    private javax.swing.JRadioButton barChartMultipleDataSeriesJRadioButton;
    private javax.swing.JRadioButton barChartMultipleValuesJRadioButton;
    private javax.swing.ButtonGroup buttonGroupMultipleDataSeries;
    private javax.swing.ButtonGroup buttonGroupMultipleValues;
    private javax.swing.JPanel gradientPanel;
    private javax.swing.JRadioButton lineChartMultipleDataSeriesJRadioButton;
    private javax.swing.JRadioButton lineChartMultipleValuesJRadioButton;
    private javax.swing.JPanel multipleDataSeriesJPanel;
    private javax.swing.JScrollPane multipleDataSeriesJScrollPane;
    private javax.swing.JTable multipleDataSeriesJTable;
    private javax.swing.JPanel multipleValuesJPanel;
    private javax.swing.JScrollPane multipleValuesJScrollPane;
    private javax.swing.JTable multipleValuesJTable;
    private javax.swing.JCheckBox showJSparklinesJCheckBox;
    private javax.swing.JPanel singleValuesJPanel;
    private javax.swing.JScrollPane singleValuesJScrollPane;
    private javax.swing.JTable singleValuesJTable;
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
}
