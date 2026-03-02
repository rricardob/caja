package vista;

import controlador.ChequeController;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import modelo.Cheque;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Modificar_Cheque extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Modificar_Cheque.class.getName());
    private final ChequeController controller;
    private Cheque chequeActual;
    private Consumer<Cheque> onChequeUpdated;

    public Frm_Modificar_Cheque(Cheque cheque, Consumer<Cheque> callback) {
        initComponents();
        this.controller = new ChequeController();
        this.chequeActual = cheque;
        this.onChequeUpdated = callback;
        this.setClosable(true);
        this.setTitle("Modificar Cheque");

        setupValidations();
        cargarDatosCheque();
    }

    private void setupValidations() {
        // Filtros de teclado
        DocumentFilters.attachDecimal(txtSaldoAnterior, 15);
        DocumentFilters.attachNumeric(txtNroCheque, 20);
        DocumentFilters.attachDecimal(txtTotalCheque, 15);

        // Tooltips y Placeholders
        UIHelpers.attachHintAndFocusColor(txtSaldoAnterior, "Saldo Anterior (Solo números)");
        UIHelpers.attachHintAndFocusColor(txtNroCheque, "Número de Cheque (Solo números)");
        UIHelpers.attachHintAndFocusColor(txtTotalCheque, "Total Cheque (Solo números)");

        UIHelpers.attachPlaceholder(txtSaldoAnterior, " Saldo Anterior");
        UIHelpers.attachPlaceholder(txtNroCheque, " Número de Cheque");
        UIHelpers.attachPlaceholder(txtTotalCheque, " Total Cheque");

        // No usar placeholder en fecha
    }

    private void cargarDatosCheque() {
        if (chequeActual == null)
            return;

        txtSaldoAnterior.setText(chequeActual.getSaldo_anterior().toString());
        txtNroCheque.setText(chequeActual.getNro_cheque());
        txtTotalCheque.setText(chequeActual.getTotal_cheque().toString());
        dcDechaEmisionCheque.setDate(chequeActual.getFecha_emision());

        UIHelpers.updatePlaceholderState(txtSaldoAnterior);
        UIHelpers.updatePlaceholderState(txtNroCheque);
        UIHelpers.updatePlaceholderState(txtTotalCheque);

        // No actualizar placeholder en fecha
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSaldoAnterior = new javax.swing.JLabel();
        lblFechaEmisionCheque = new javax.swing.JLabel();
        lblNroCheque = new javax.swing.JLabel();
        lblTotalCheque = new javax.swing.JLabel();
        txtSaldoAnterior = new javax.swing.JTextField();
        txtNroCheque = new javax.swing.JTextField();
        txtTotalCheque = new javax.swing.JTextField();
        dcDechaEmisionCheque = new com.toedter.calendar.JDateChooser();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        lblSaldoAnterior.setText("Saldo Anterior : ");

        lblFechaEmisionCheque.setText("Fecha Emisión Cheque :");

        lblNroCheque.setText("Nro Cheque : ");

        lblTotalCheque.setText("Total Cheque : ");

        btnAceptar.setText("ACEPTAR ");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnAceptar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnCancelar))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblSaldoAnterior)
                                                        .addComponent(lblNroCheque)
                                                        .addComponent(lblTotalCheque)
                                                        .addComponent(lblFechaEmisionCheque))
                                                .addGap(30, 30, 30)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(txtSaldoAnterior)
                                                        .addComponent(txtNroCheque)
                                                        .addComponent(txtTotalCheque)
                                                        .addComponent(dcDechaEmisionCheque,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 290,
                                                                Short.MAX_VALUE))))
                                .addGap(30, 30, 30)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblSaldoAnterior)
                                        .addComponent(txtSaldoAnterior, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFechaEmisionCheque)
                                        .addComponent(dcDechaEmisionCheque, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblNroCheque)
                                        .addComponent(txtNroCheque, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblTotalCheque)
                                        .addComponent(txtTotalCheque, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, 39,
                                                Short.MAX_VALUE)
                                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nroCheque = UIHelpers.getText(txtNroCheque).trim();
            java.util.Date fechaEmision = UIHelpers.getDate(dcDechaEmisionCheque);
            String saldoAnteriorStr = UIHelpers.getText(txtSaldoAnterior).trim();
            String totalChequeStr = UIHelpers.getText(txtTotalCheque).trim();

            if (saldoAnteriorStr.isEmpty() || fechaEmision == null || nroCheque.isEmpty() || totalChequeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            chequeActual.setNro_cheque(nroCheque);
            chequeActual.setFecha_emision(new java.sql.Date(fechaEmision.getTime()));
            chequeActual.setSaldo_anterior(new BigDecimal(saldoAnteriorStr.replace(",", ".")));
            chequeActual.setTotal_cheque(new BigDecimal(totalChequeStr.replace(",", ".")));

            if (controller.actualizarCheque(chequeActual)) {
                JOptionPane.showMessageDialog(this, "Cheque actualizado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                if (onChequeUpdated != null) {
                    onChequeUpdated.accept(chequeActual);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el cheque.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cheque: {0}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private com.toedter.calendar.JDateChooser dcDechaEmisionCheque;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lblFechaEmisionCheque;
    private javax.swing.JLabel lblNroCheque;
    private javax.swing.JLabel lblSaldoAnterior;
    private javax.swing.JLabel lblTotalCheque;
    private javax.swing.JTextField txtNroCheque;
    private javax.swing.JTextField txtSaldoAnterior;
    private javax.swing.JTextField txtTotalCheque;
    // End of variables declaration//GEN-END:variables
}
