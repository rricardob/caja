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
import vista.dataTableModel.PaginatedTableModel;
import vista.components.PaginationPanel;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Collections;
import java.util.function.Consumer;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class Frm_Ingreso extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Ingreso.class.getName());
    private final IngresoController controller;
    private Cliente clienteSeleccionado;
    private TransaccionTableModel transaccionTableModel;
    private PaginatedTableModel<Transaccion> paginatedModel;
    private PaginationPanel paginationPanel;

    public Frm_Ingreso() {

        initComponents();
        this.controller = new IngresoController();
        this.setClosable(true);
        this.setTitle("Recibo de Ingresos");

        // Inicializar el table model y asignarlo
        transaccionTableModel = new TransaccionTableModel();
        jTable1.setModel(transaccionTableModel);

        // Acción de doble clic para abrir edición 
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    onEdit();
                }
            }
        });

        // Inicializar paginación (wrapper + panel) y conectar eventos
        paginatedModel = new PaginatedTableModel<>(
                transaccionTableModel,
                (model, data) -> ((TransaccionTableModel) model).load(data),
                20
        );
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        // Montar el panel de paginación en el contenedor visual del listado
        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        // Cargar datos en la tabla (fuera del EDT)
        cargarIngresosEnTabla();

        // Cargar datos de tipo_transaccion 
        cargarTiposTransaccionEnCombo();

        // Seteamos Fecha 
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblFecha.setText(LocalDate.now().format(fmt));
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "No se pudo formatear la fecha: {0}", e.toString());
        }

        // Bloqueamos Sr y Dirección para que no sean editables manualmente
        txtSr.setEditable(false);
        txtDireccion.setEditable(false);

        // Sincronizar placeholder state para campos no editables
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

        // Actualizar Estado 
        UIHelpers.updatePlaceholderState(txtDoc);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
        UIHelpers.updatePlaceholderState(txtImporte);

    }

    // Paginación : conecta panel -> wrapper
    private void configurarPaginacion() {
        paginationPanel.onFirst(e -> {
            paginatedModel.firstPage();
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        });
        paginationPanel.onPrev(e -> {
            paginatedModel.previousPage();
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        });
        paginationPanel.onNext(e -> {
            paginatedModel.nextPage();
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        });
        paginationPanel.onLast(e -> {
            paginatedModel.lastPage();
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        });
        paginationPanel.onPageSize(e -> {
            int newSize = paginationPanel.getSelectedPageSize();
            paginatedModel.setPageSize(newSize);
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        });
        paginationPanel.onGoTo(e -> {
            try {
                String pageText = paginationPanel.getGoToText();
                if (!pageText.isEmpty()) {
                    int page = Integer.parseInt(pageText);
                    boolean ok = paginatedModel.goToPage(page);
                    if (!ok) {
                        JOptionPane.showMessageDialog(this,
                                "Página inválida. Rango: 1-" + paginatedModel.getTotalPages(),
                                "Advertencia",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    paginationPanel.clearGoTo();
                    paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Carga los ingresos desde la BDD usando IngresoController y los pone en el
     * table model.
     */
    private void cargarIngresosEnTabla() {
        SwingWorker<List<Transaccion>, Void> worker = new SwingWorker<List<Transaccion>, Void>() {
            @Override
            protected List<Transaccion> doInBackground() throws Exception {
                try {
                    return controller.listarIngresosPorSesionActiva();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar ingresos: {0}", ex.toString());
                    return Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Transaccion> lista = get();
                    paginatedModel.loadAllData(lista); 
                    paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                    paginationPanel.enableAll(!lista.isEmpty());
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al cargar datos en la tabla: {0}", ex.toString());
                    paginationPanel.setInfo("Sin datos");
                    paginationPanel.enableAll(false);
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
        lblImporte = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcionArea = new javax.swing.JTextArea();
        cbTipoTransaccion = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();

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

        jButton1.setText("EDITAR");
        jButton1.setMaximumSize(new java.awt.Dimension(81, 23));
        jButton1.setMinimumSize(new java.awt.Dimension(81, 23));
        jButton1.setPreferredSize(new java.awt.Dimension(81, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("ELIMINAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_registro_ingresosLayout = new javax.swing.GroupLayout(panel_registro_ingresos);
        panel_registro_ingresos.setLayout(panel_registro_ingresosLayout);
        panel_registro_ingresosLayout.setHorizontalGroup(
            panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registro_ingresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_registro_ingresosLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(65, 65, 65)
                .addComponent(lblFecha)
                .addGap(20, 20, 20))
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
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
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

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPaginacionLayout.setVerticalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(panel_registro_ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSrActionPerformed

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
                                // sincronizar placeholder state después de setText
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

    // Guardar Ingreso 
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

                cargarIngresosEnTabla();
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

    // Edición de ingresos por medio de acción doble click a través del TableModel 
    private void onEdit() {
        int viewRow = jTable1.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Registro Para Editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Si tienes sorter/filters activos, convierte a índice de modelo como en tipos
        int modelRow = jTable1.convertRowIndexToModel(viewRow);

        // Obtener la transacción desde tu TransaccionTableModel
        Transaccion seleccionado = transaccionTableModel.getTransaccionAt(modelRow);
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Registro Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Callback: recarga todo el listado para mantener coherencia como en tipos
        Consumer<Transaccion> onUpdated = (t) -> {
            cargarIngresosEnTabla();
        };

        // Abrir JInternalFrame de edición (Frm_Modificar_Ingreso)
        Frm_Modificar_Ingreso frm = new Frm_Modificar_Ingreso(seleccionado, onUpdated);
        showInternal(frm);
    }

    //  Helper de apertura dentro del DesktopPane (idéntico a tipos)
    private void showInternal(JInternalFrame frame) {
        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }
        if (desktop != null) {
            desktop.add(frame);
            frame.pack();
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (PropertyVetoException ex) {
                LOGGER.log(java.util.logging.Level.WARNING, "setSelected fallo: {0}", ex.getMessage());
            }
            frame.toFront();
        } else {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dlg = new JDialog(owner, frame.getTitle(), true);
            dlg.getContentPane().add(frame.getContentPane());
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
        }
    }

    private void cbTipoTransaccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoTransaccionActionPerformed

    }//GEN-LAST:event_cbTipoTransaccionActionPerformed

    // Edición por medio del Botón 
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        onEdit();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Eliminar por medio del botón 
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un ingreso para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transaccion t = transaccionTableModel.getTransaccionAt(selectedRow);
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ingreso seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmación
        String mensaje = String.format("¿Está seguro de eliminar este ingreso?\n\nCliente: %s\nImporte: %s\nDescripción: %s",
                t.getNombre_completo(),
                t.getImporte().toString(),
                t.getDescripcion());

        int confirm = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Eliminar en segundo plano
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return controller.eliminarTransaccion(t.getId_transaccion());
                }

                @Override
                protected void done() {
                    try {
                        boolean eliminado = get();
                        if (eliminado) {
                            JOptionPane.showMessageDialog(Frm_Ingreso.this, "Ingreso eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            cargarIngresosEnTabla();
                        } else {
                            JOptionPane.showMessageDialog(Frm_Ingreso.this, "No se pudo eliminar el ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error al eliminar ingreso", ex);
                        JOptionPane.showMessageDialog(Frm_Ingreso.this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<TipoTransaccion> cbTipoTransaccion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTextArea txtDescripcionArea;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtSr;
    // End of variables declaration//GEN-END:variables
}
