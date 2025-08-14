package vista;

import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.SwingUtilities;
import controlador.IngresoController;
import modelo.Cliente;
//import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.Transaccion;
import vista.dataTableModel.TransaccionTableModel;

public class Frm_Ingreso extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Ingreso.class.getName());
    private final IngresoController controller;
    private Cliente clienteSeleccionado;

    // Nuevo: modelo de la tabla
    private TransaccionTableModel transaccionTableModel;

    public Frm_Ingreso() {
        initComponents();
        this.controller = new IngresoController();
        this.setClosable(true);
        this.setTitle("Recibo de Ingresos");
        // Inicializar el table model y asignarlo
        transaccionTableModel = new TransaccionTableModel();
        jTable1.setModel(transaccionTableModel);
        // Cargar datos en la tabla (fuera del EDT)
        cargarIngresosEnTabla();
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblFecha.setText(LocalDate.now().format(fmt));
        } catch (Exception e) {
        }
    }

    /**
     * Carga los ingresos desde la BDD usando IngresoController y los pone en el
     * table model. Se ejecuta en background y actualiza la UI en el EDT.
     */
    private void cargarIngresosEnTabla() {
        // Usamos SwingWorker para no bloquear la UI
        SwingWorker<List<Transaccion>, Void> worker = new SwingWorker<List<Transaccion>, Void>() {
            @Override
            protected List<Transaccion> doInBackground() throws Exception {
                try {
                    return controller.listarIngresosPorSesionActiva();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar ingresos: {0}", ex.toString());
                    return java.util.Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Transaccion> lista = get();
                    transaccionTableModel.load(lista);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al cargar datos en la tabla: {0}", ex.toString());
                }
            }
        };
        worker.execute();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        panel_registro_ingresos = new javax.swing.JPanel();
        lblSr = new javax.swing.JLabel();
        txtSr = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        lblDoc = new javax.swing.JLabel();
        txtDoc = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        btnBuscarCliente = new javax.swing.JButton();
        lblDescripcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblImporte = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        panel_registro_ingresos.setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario De Registro - Ingresos"));

        lblSr.setText("Sr.(es) : ");

        txtSr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSrActionPerformed(evt);
            }
        });

        lblDireccion.setText("Direccion :  ");

        lblDoc.setText("Doc.Identidad : ");

        lblFecha.setText("FECHA : ");

        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        lblDescripcion.setText("Descripción :");

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });

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

        lblImporte.setText("Importe :  ");

        javax.swing.GroupLayout panel_registro_ingresosLayout = new javax.swing.GroupLayout(panel_registro_ingresos);
        panel_registro_ingresos.setLayout(panel_registro_ingresosLayout);
        panel_registro_ingresosLayout.setHorizontalGroup(
            panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDoc)
                            .addComponent(lblSr)
                            .addComponent(lblImporte)
                            .addComponent(lblDireccion)
                            .addComponent(lblDescripcion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSr)
                            .addComponent(txtDoc)
                            .addComponent(txtImporte)))
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnBuscarCliente)
                .addGap(50, 50, 50)
                .addComponent(lblFecha)
                .addGap(50, 50, 50))
        );
        panel_registro_ingresosLayout.setVerticalGroup(
            panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoc)
                    .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente)
                    .addComponent(lblFecha))
                .addGap(18, 18, 18)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSr))
                .addGap(18, 18, 18)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcion))
                .addGap(23, 23, 23)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImporte)
                    .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado De Registro - Ingresos"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Sr.(es)", "Doc.Identidad", "Dirección", "Descripción", "Importe ", "Fecha Registro"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

                // --- NUEVO: obtener la transaccion desde la BDD y añadirla al modelo ---
                SwingWorker<Transaccion, Void> workerAdd = new SwingWorker<Transaccion, Void>() {
                    @Override
                    protected Transaccion doInBackground() throws Exception {
                        try {
                            return controller.obtenerTransaccionPorId(idTrans);
                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, "Error obtenerTransaccionPorId: {0}", ex.toString());
                            return null;
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            Transaccion t = get();
                            if (t != null) {
                                transaccionTableModel.add(t);
                                // Opcional: scroll hacia la nueva fila
                                int row = transaccionTableModel.getRowCount() - 1;
                                if (row >= 0) {
                                    jTable1.scrollRectToVisible(jTable1.getCellRect(row, 0, true));
                                }
                            } else {
                                LOGGER.log(Level.WARNING, "Transaccion recuperada nula para id {0}", idTrans);
                            }
                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, "Error al añadir transaccion al modelo: {0}", ex.toString());
                        }
                    }
                };
                workerAdd.execute();

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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDoc;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblImporte;
    private javax.swing.JLabel lblSr;
    private javax.swing.JPanel panel_registro_ingresos;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtSr;
    // End of variables declaration//GEN-END:variables
}
