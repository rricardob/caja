package vista;

import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.SwingUtilities;
import controlador.IngresoController;
import modelo.Cliente;
//import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class Frm_Ingreso extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Ingreso.class.getName());
    private final IngresoController controller;
    private Cliente clienteSeleccionado;

    public Frm_Ingreso() {
        initComponents();
        this.controller = new IngresoController();
        this.setClosable(true);
        this.setTitle("Recibo de Ingresos");
        // Opcional: mostrar fecha en lblFecha (si lo agregas en el Designer)
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblFecha.setText(LocalDate.now().format(fmt));
        } catch (Exception e) {
            // lblFecha podría no existir todavía; ignora si no está
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        lblSr = new javax.swing.JLabel();
        txtSr = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        lblDoc = new javax.swing.JLabel();
        txtDoc = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        lblDescripcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        lblImporte = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        lblSr.setText("Sr.(es) : ");

        txtSr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSrActionPerformed(evt);
            }
        });

        lblFecha.setText("Fecha : ");

        lblDireccion.setText("Direccion :  ");

        lblDoc.setText("Doc.Identidad : ");

        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        lblDescripcion.setText("DESCRIPCION ");

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });

        lblImporte.setText("IMPORTE");

        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
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
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblDescripcion)
                                .addComponent(btnBuscarCliente))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSr)
                                    .addComponent(lblDireccion))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSr, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(txtDireccion))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDoc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblImporte)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtDoc))
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFecha)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtImporte, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                        .addGap(32, 32, 32))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSr)
                    .addComponent(txtSr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoc)
                    .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescripcion)
                    .addComponent(lblImporte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSrActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        String doc = txtDoc.getText().trim();
        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el documento de identidad para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LOGGER.log(Level.INFO, "Inicio btnBuscarClienteActionPerformed (doc={0})", doc);
            Cliente c = controller.buscarClientePorDoc(doc);
            LOGGER.log(Level.INFO, "Resultado buscarClientePorDoc: {0}", c);

            if (c == null) {
                int resp = JOptionPane.showConfirmDialog(this, "Cliente no encontrado. ¿Desea crear uno nuevo manualmente?", "Cliente no encontrado", JOptionPane.YES_NO_OPTION);
                LOGGER.log(Level.INFO, "Respuesta JOptionPane: {0}", resp);
                if (resp == JOptionPane.YES_OPTION) {

                    try {
                        LOGGER.log(Level.INFO, "Preparando abrir Frm_Cliente desde Frm_Ingreso (doc={0})", doc);

                        // Crear el internal frame con callback
                        vista.Frm_Cliente frmCliente = new vista.Frm_Cliente(creado -> {
                            LOGGER.log(Level.INFO, "Callback onClienteCreated invoked with: {0}", creado);
                            if (creado != null) {
                                clienteSeleccionado = creado;
                                txtSr.setText(creado.getNombre_completo());
                                txtDireccion.setText(creado.getDireccion());
                                txtSr.setEditable(false);
                                txtDireccion.setEditable(false);
                                txtDoc.setText(creado.getDoc_identidad());
                                LOGGER.log(Level.INFO, "Cliente creado y seteado en Frm_Ingreso: {0}", creado.getId_cliente());
                            }
                        });

                        // Buscar el JDesktopPane robustamente
                        javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) javax.swing.SwingUtilities.getAncestorOfClass(
                                javax.swing.JDesktopPane.class, this);
                        if (desktop == null) {
                            desktop = this.getDesktopPane();
                        }
                        LOGGER.log(Level.INFO, "JDesktopPane encontrado => {0}", desktop);

                        if (desktop != null) {
                            // Asegurar tamaño y visibilidad
                            frmCliente.pack();
                            if (frmCliente.getWidth() < 300 || frmCliente.getHeight() < 100) {
                                frmCliente.setSize(420, 220);
                            }

                            // Añadir y forzar revalidar/repaint
                            desktop.add(frmCliente);
                            desktop.revalidate();
                            desktop.repaint();

                            // Mostrar y forzar foco/orden
                            frmCliente.setVisible(true);
                            // mover al frente del desktop
                            desktop.moveToFront(frmCliente);
                            try {
                                frmCliente.setSelected(true);
                            } catch (java.beans.PropertyVetoException ex) {
                                LOGGER.log(Level.WARNING, "setSelected fallo: {0}", ex.getMessage());
                            }
                            frmCliente.toFront();

                            LOGGER.log(Level.INFO, "Frm_Cliente añadido y visible en JDesktopPane");
                        } else {
                            String msg = "No se encontró JDesktopPane en la jerarquía. Asegúrate de que Frm_Ingreso esté dentro de un JDesktopPane.";
                            LOGGER.log(Level.SEVERE, msg);
                            JOptionPane.showMessageDialog(this, msg, "Error de configuración", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error al abrir Frm_Cliente: {0}", ex.toString());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error al abrir formulario de cliente: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } // end YES_OPTION
            } else {
                clienteSeleccionado = c;
                txtSr.setText(c.getNombre_completo());
                txtDireccion.setText(c.getDireccion());
                txtSr.setEditable(false);
                txtDireccion.setEditable(false);
                LOGGER.log(Level.INFO, "Cliente encontrado: {0}", c.getId_cliente());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error buscar cliente: {0}", e.toString());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            String nombreCliente = txtSr.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String doc = txtDoc.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String importeStr = txtImporte.getText().trim();

            if (nombreCliente.isEmpty() || importeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete al menos: Sr. e Importe.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.math.BigDecimal importe;
            try {
                importe = new java.math.BigDecimal(importeStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Importe inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.WARNING, "Importe inválido: {0}", importeStr);
                return;
            }

            if (importe.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El importe debe ser mayor que cero.", "Importe inválido", JOptionPane.WARNING_MESSAGE);
                LOGGER.log(Level.WARNING, "Intento de guardar con importe <= 0: {0}", importe);
                return;
            }

            if (clienteSeleccionado == null && !doc.isEmpty()) {
                clienteSeleccionado = controller.buscarClientePorDoc(doc);
            }

            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "El cliente no está registrado. Debes registrar el cliente antes de guardar el ingreso.", "Cliente requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idTrans = controller.guardarIngreso(clienteSeleccionado, importe, descripcion);
            if (idTrans > 0) {
                JOptionPane.showMessageDialog(this, "Ingreso registrado correctamente (ID: " + idTrans + ").", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtDescripcion.setText("");
                txtImporte.setText("");
                LOGGER.log(Level.INFO, "Ingreso registrado ID: {0} clienteId: {1} importe: {2}", new Object[]{idTrans, clienteSeleccionado.getId_cliente(), importe});
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar ingreso. Verifique que exista una sesión de caja abierta.", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.SEVERE, "Error al registrar ingreso. clienteId: {0} importe: {1}", new Object[]{clienteSeleccionado != null ? clienteSeleccionado.getId_cliente() : -1, importe});
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción guardar ingreso: {0}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDoc;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblImporte;
    private javax.swing.JLabel lblSr;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtSr;
    // End of variables declaration//GEN-END:variables
}
