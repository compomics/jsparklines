package no.uib.jsparklines;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
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
        singleValuesJTable.getTableHeader().setReorderingAllowed(false);
        multipleValuesJTable.setRowHeight(30);
        multipleDataSeriesJTable.setRowHeight(30);
        multipleValuesJTable.getColumn("Protein").setMaxWidth(70);
        multipleDataSeriesJTable.getColumn("Protein").setMaxWidth(70);
        setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/line_plot.GIF")));
        this.setTitle("JSparklines " + getVersion() + " - Demo");

        // add data to the single values example
        addDataSingleValues();

        // set the JSparklines renderers for the columns containing numbers in the first example table
        // NB: JSparklines with single values are editable, so the columns can be 'editable' in the JTable
        singleValuesJTable.getColumn("Fold Change").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, -5.0, 5.0, negativeColor, positiveColor));
        singleValuesJTable.getColumn("Peptides").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 80.0, neutralColor));
        singleValuesJTable.getColumn("Coverage").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, neutralColor));


        // add data to the multiple values example
        double maxValue = 10;
        addDataMultipleValues(maxValue);

        // set the JSparklines renderers
        // NB: JSparklines with multiple values are NOT editable, so remember to set the columns as 'not editable' in the JTable
        multipleValuesJTable.getColumn("Change").setCellRenderer(new JSparklinesTableCellRenderer(JSparklinesTableCellRenderer.PlotType.lineChart, PlotOrientation.VERTICAL, 0.0, maxValue));


        // add data to the multiple data series example
        addDataMultipleDataSeries(maxValue);

        // set the JSparklines renderers
        // NB: JSparklines with multiple values are NOT editable, so remember to set the columns as 'not editable' in the JTable
        multipleDataSeriesJTable.getColumn("Change").setCellRenderer(new JSparklinesTableCellRenderer(JSparklinesTableCellRenderer.PlotType.barChart, PlotOrientation.VERTICAL, 0.0, maxValue));
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

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, Color.DARK_GRAY);

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

            JSparklinesDataSeries sparklineDataseriesA = new JSparklinesDataSeries(dataA, Color.BLUE);
            JSparklinesDataSeries sparklineDataseriesB = new JSparklinesDataSeries(dataB, Color.RED);

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
        jPanel1 = new javax.swing.JPanel();
        showJSparklinesJCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        singleValuesJTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        multipleValuesJTable = new javax.swing.JTable();
        lineChartMultipleValuesJRadioButton = new javax.swing.JRadioButton();
        barChartMultipleValuesJRadioButton = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        multipleDataSeriesJTable = new javax.swing.JTable();
        lineChartMultipleDataSeriesJRadioButton = new javax.swing.JRadioButton();
        barChartMultipleDataSeriesJRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSparklines Demo");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Single Values"));

        showJSparklinesJCheckBox.setSelected(true);
        showJSparklinesJCheckBox.setText("Show Sparklines");
        showJSparklinesJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showJSparklinesJCheckBoxActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(singleValuesJTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showJSparklinesJCheckBox)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showJSparklinesJCheckBox))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Values"));

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
        jScrollPane2.setViewportView(multipleValuesJTable);

        buttonGroupMultipleValues.add(lineChartMultipleValuesJRadioButton);
        lineChartMultipleValuesJRadioButton.setSelected(true);
        lineChartMultipleValuesJRadioButton.setText("Line");
        lineChartMultipleValuesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineChartMultipleValuesJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroupMultipleValues.add(barChartMultipleValuesJRadioButton);
        barChartMultipleValuesJRadioButton.setText("Bar");
        barChartMultipleValuesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barChartMultipleValuesJRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lineChartMultipleValuesJRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barChartMultipleValuesJRadioButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {barChartMultipleValuesJRadioButton, lineChartMultipleValuesJRadioButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barChartMultipleValuesJRadioButton)
                    .addComponent(lineChartMultipleValuesJRadioButton)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple Data Series"));

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
        jScrollPane3.setViewportView(multipleDataSeriesJTable);

        buttonGroupMultipleDataSeries.add(lineChartMultipleDataSeriesJRadioButton);
        lineChartMultipleDataSeriesJRadioButton.setText("Line");
        lineChartMultipleDataSeriesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineChartMultipleDataSeriesJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroupMultipleDataSeries.add(barChartMultipleDataSeriesJRadioButton);
        barChartMultipleDataSeriesJRadioButton.setSelected(true);
        barChartMultipleDataSeriesJRadioButton.setText("Bar");
        barChartMultipleDataSeriesJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barChartMultipleDataSeriesJRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lineChartMultipleDataSeriesJRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barChartMultipleDataSeriesJRadioButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barChartMultipleDataSeriesJRadioButton)
                    .addComponent(lineChartMultipleDataSeriesJRadioButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel1, jPanel2, jPanel3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton lineChartMultipleDataSeriesJRadioButton;
    private javax.swing.JRadioButton lineChartMultipleValuesJRadioButton;
    private javax.swing.JTable multipleDataSeriesJTable;
    private javax.swing.JTable multipleValuesJTable;
    private javax.swing.JCheckBox showJSparklinesJCheckBox;
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
}
