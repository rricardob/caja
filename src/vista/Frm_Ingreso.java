package vista;

import java.util.logging.Level;
import java.util.logging.Logger;
import controlador.IngresoController;
import modelo.Cliente;
import modelo.Transaccion;
import dao.TipoTransaccionDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.TipoTransaccion;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import util.validation.ValidationResult;
import vista.dataTableModel.TransaccionTableModel;

public class Frm_Ingreso extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Ingreso.class.getName());
    private final IngresoController controller;
    private Cliente clienteSeleccionado;

    // Modelo De La Tabla
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

        // Cargar datos de tipo_transaccion 
        cargarTiposTransaccionEnCombo();

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblFecha.setText(LocalDate.now().format(fmt));
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "No se pudo formatear la fecha: {0}", e.toString());
        }

        // Bloqueamos Sr y Dirección para que no sean editables manualmente
        txtSr.setEditable(false);
        txtDireccion.setEditable(false);
        // sincronizar placeholder state para campos no editables
        UIHelpers.updatePlaceholderState(txtSr);
        UIHelpers.updatePlaceholderState(txtDireccion);

        // Aplicar filtros
        DocumentFilters.attachNumeric(txtDoc, 11);
        DocumentFilters.attachDecimal(txtImporte, 20);
        DocumentFilters.attachTextAreaLimit(txtDescripcionArea, 200);

        // Tooltips + efecto "azul al foco"
        UIHelpers.attachHintAndFocusColor(txtDoc, "Ingrese DNI o RUC (8-11 dígitos).");
        UIHelpers.attachHintAndFocusColor(txtDescripcionArea, "Descripción (5-200 caracteres).");
        UIHelpers.attachHintAndFocusColor(txtImporte, "Importe (>= 1). Puede usar coma o punto decimal.");

        // Placeholders (no se aplican a campos bloqueados)
        UIHelpers.attachPlaceholder(txtDoc, " DNI o RUC");
        UIHelpers.attachPlaceholder(txtDescripcionArea, " Motivo o Descripción");
        UIHelpers.attachPlaceholder(txtImporte, " Importe");

        // Si alguno de los campos tenía texto (programático) ya, actualizar su estado
        UIHelpers.updatePlaceholderState(txtDoc);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
        UIHelpers.updatePlaceholderState(txtImporte);

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

    /**
     * Carga el combo cbTipoTransaccion con tipos activos de categorías INGRESO
     * y REPOSICION.
     */
    private void cargarTiposTransaccionEnCombo() {
        SwingWorker<List<TipoTransaccion>, Void> worker = new SwingWorker<List<TipoTransaccion>, Void>() {
            @Override
            protected List<TipoTransaccion> doInBackground() throws Exception {
                try {
                    TipoTransaccionDAO tipoDao = new TipoTransaccionDAO();
                    List<String> categorias = Arrays.asList("INGRESO", "REPOSICION");
                    return tipoDao.listarPorCategorias(categorias);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar tipos por categoría: {0}", ex.toString());
                    return java.util.Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<TipoTransaccion> lista = get();
                    DefaultComboBoxModel<TipoTransaccion> model = new DefaultComboBoxModel<>();
                    for (TipoTransaccion t : lista) {
                        model.addElement(t);
                    }
                    cbTipoTransaccion.setModel(model);

                    if (lista.isEmpty()) {
                        LOGGER.log(Level.WARNING, "No hay tipos de transacción activos para INGRESO/REPOSICION");
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al poblar combo tipos: {0}", ex.toString());
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
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblImporte = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcionArea = new javax.swing.JTextArea();
        cbTipoTransaccion = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
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

        lblDoc.setText("DNI o RUC :");

        lblFecha.setText("FECHA : ");

        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        lblDescripcion.setText("Descripción :");

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

        txtDescripcionArea.setColumns(20);
        txtDescripcionArea.setRows(5);
        jScrollPane2.setViewportView(txtDescripcionArea);

        cbTipoTransaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoTransaccionActionPerformed(evt);
            }
        });

        jLabel1.setText("Elegir Tipo Transaccion :");

        javax.swing.GroupLayout panel_registro_ingresosLayout = new javax.swing.GroupLayout(panel_registro_ingresos);
        panel_registro_ingresos.setLayout(panel_registro_ingresosLayout);
        panel_registro_ingresosLayout.setHorizontalGroup(
            panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_registro_ingresosLayout.createSequentialGroup()
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDoc)
                                    .addComponent(lblSr))
                                .addGap(7, 7, 7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_registro_ingresosLayout.createSequentialGroup()
                                .addComponent(lblDireccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDoc, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSr)
                            .addComponent(txtDireccion)))
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbTipoTransaccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescripcion)
                            .addComponent(lblImporte))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtImporte)
                            .addComponent(jScrollPane2))))
                .addGap(18, 18, 18)
                .addComponent(btnBuscarCliente)
                .addGap(68, 68, 68)
                .addComponent(lblFecha)
                .addGap(32, 32, 32))
        );
        panel_registro_ingresosLayout.setVerticalGroup(
            panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoc)
                    .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCliente)
                    .addComponent(lblFecha))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSr))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbTipoTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblDescripcion)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
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
                "Sr.(es)", "DNI o RUC", "Dirección", "Descripción", "Importe ", "Fecha Registro"
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSrActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        String doc = UIHelpers.getText(txtDoc).trim();
        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el DNI o RUC para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LOGGER.log(Level.INFO, "Inicio btnBuscarClienteActionPerformed (doc={0})", doc);
            Cliente c = controller.buscarClientePorDoc(doc);
            LOGGER.log(Level.INFO, "Resultado buscarClientePorDoc: {0}", c);

            if (c == null) {
                int resp = JOptionPane.showConfirmDialog(this, "Cliente no encontrado. ¿Desea crear uno nuevo manualmente?", "Cliente no encontrado", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        vista.Frm_Cliente frmCliente = new vista.Frm_Cliente(creado -> {
                            if (creado != null) {
                                clienteSeleccionado = creado;
                                txtSr.setText(creado.getNombre_completo());
                                txtDireccion.setText(creado.getDireccion());
                                txtSr.setEditable(false);
                                txtDireccion.setEditable(false);
                                txtDoc.setText(getIdentificadorCliente(creado));
                                // sincronizar placeholder state después de setText programático
                                UIHelpers.updatePlaceholderState(txtSr);
                                UIHelpers.updatePlaceholderState(txtDireccion);
                                UIHelpers.updatePlaceholderState(txtDoc);
                            }
                        });

                        javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) javax.swing.SwingUtilities.getAncestorOfClass(
                                javax.swing.JDesktopPane.class, this);
                        if (desktop == null) {
                            desktop = this.getDesktopPane();
                        }

                        if (desktop != null) {
                            frmCliente.pack();
                            if (frmCliente.getWidth() < 300 || frmCliente.getHeight() < 100) {
                                frmCliente.setSize(420, 220);
                            }
                            desktop.add(frmCliente);
                            desktop.revalidate();
                            desktop.repaint();
                            frmCliente.setVisible(true);
                            desktop.moveToFront(frmCliente);
                            try {
                                frmCliente.setSelected(true);
                            } catch (java.beans.PropertyVetoException ex) {
                                LOGGER.log(Level.WARNING, "setSelected fallo: {0}", ex.getMessage());
                            }
                            frmCliente.toFront();
                        } else {
                            String msg = "No se encontró JDesktopPane en la jerarquía. Asegúrate de que Frm_Ingreso esté dentro de un JDesktopPane.";
                            LOGGER.log(Level.SEVERE, msg);
                            JOptionPane.showMessageDialog(this, msg, "Error de configuración", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error al abrir Frm_Cliente: {0}", ex.toString());
                        JOptionPane.showMessageDialog(this, "Error al abrir formulario de cliente: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                clienteSeleccionado = c;
                txtSr.setText(c.getNombre_completo());
                txtDireccion.setText(c.getDireccion());
                txtSr.setEditable(false);
                txtDireccion.setEditable(false);
                txtDoc.setText(getIdentificadorCliente(c));
                UIHelpers.updatePlaceholderState(txtSr);
                UIHelpers.updatePlaceholderState(txtDireccion);
                UIHelpers.updatePlaceholderState(txtDoc);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error buscar cliente: {0}", e.toString());
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    /**
     * Retorna el identificador que corresponde: doc_identidad si existe, de lo
     * contrario ruc si existe, o cadena vacía.
     */
    private String getIdentificadorCliente(Cliente c) {
        if (c == null) {
            return "";
        }
        String doc = c.getDoc_identidad();
        if (doc != null && !doc.trim().isEmpty()) {
            return doc;
        }
        String ruc = c.getRuc();
        return ruc != null ? ruc : "";
    }


    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            String nombreCliente = UIHelpers.getText(txtSr).trim();
            String direccion = UIHelpers.getText(txtDireccion).trim();
            String doc = UIHelpers.getText(txtDoc).trim();
            String descripcion = UIHelpers.getText(txtDescripcionArea).trim();
            String importeStr = UIHelpers.getText(txtImporte).trim();

            // Obtener el tipo seleccionado del combo
            TipoTransaccion tipoSeleccionado = (TipoTransaccion) cbTipoTransaccion.getSelectedItem();
            if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de transacción.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validación centralizada
            ValidationResult vr = controller.validateIngreso(doc, descripcion, importeStr);
            if (!vr.isOk()) {
                JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Si cliente no fue cargado, intentar buscarlo por el identificador
            if (clienteSeleccionado == null && !doc.isEmpty()) {
                clienteSeleccionado = controller.buscarClientePorDoc(doc);
            }
            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "El cliente no está registrado. Debes registrar el cliente antes de guardar el ingreso.",
                        "Cliente requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Normalizar importe
            String normalizedImporte = importeStr.replace(",", ".");
            BigDecimal importe = new BigDecimal(normalizedImporte);

            // Pasar el idTipo del objeto seleccionado al controller
            int idTrans = controller.guardarIngreso(clienteSeleccionado, importe, descripcion, tipoSeleccionado.getId_tipo());

            if (idTrans > 0) {
                JOptionPane.showMessageDialog(this, "Ingreso registrado correctamente (ID: " + idTrans + ").",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos y actualizar placeholders
                txtDescripcionArea.setText("");
                txtImporte.setText("");
                UIHelpers.updatePlaceholderState(txtDescripcionArea);
                UIHelpers.updatePlaceholderState(txtImporte);

                LOGGER.log(Level.INFO, "Ingreso registrado ID: {0}, tipoId: {1}, clienteId: {2}, importe: {3}",
                        new Object[]{idTrans, tipoSeleccionado.getId_tipo(), clienteSeleccionado.getId_cliente(), importe});

                // Añadir la transacción recién creada al modelo (manteniendo tu flujo actual)
                SwingWorker<Transaccion, Void> workerAdd = new SwingWorker<Transaccion, Void>() {
                    @Override
                    protected Transaccion doInBackground() throws Exception {
                        return controller.obtenerTransaccionPorId(idTrans);
                    }

                    @Override
                    protected void done() {
                        try {
                            Transaccion t = get();
                            if (t != null) {
                                transaccionTableModel.add(t);
                                int row = transaccionTableModel.getRowCount() - 1;
                                if (row >= 0) {
                                    jTable1.scrollRectToVisible(jTable1.getCellRect(row, 0, true));
                                }
                            }
                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, "Error al añadir transacción al modelo", ex);
                        }
                    }
                };
                workerAdd.execute();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación/Estado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción guardar ingreso: {0}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbTipoTransaccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoTransaccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoTransaccionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<TipoTransaccion> cbTipoTransaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDoc;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblImporte;
    private javax.swing.JLabel lblSr;
    private javax.swing.JPanel panel_registro_ingresos;
    private javax.swing.JTextArea txtDescripcionArea;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtSr;
    // End of variables declaration//GEN-END:variables
}
