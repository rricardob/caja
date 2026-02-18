package vista;

import java.util.logging.Level;
import java.util.logging.Logger;
import controlador.EgresoController;
import modelo.Cliente;
import modelo.Transaccion;
import dao.TipoTransaccionDAO;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import modelo.TipoTransaccion;
import util.validation.ValidationResult;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
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

public class Frm_Egresos extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Egresos.class.getName());
    private final EgresoController controller;
    private Cliente clienteSeleccionado;
    private TransaccionTableModel transaccionTableModel;
    private PaginatedTableModel<Transaccion> paginatedModel;
    private PaginationPanel paginationPanel;

    public Frm_Egresos() {
        initComponents();
        this.controller = new EgresoController();
        this.setClosable(true);
        this.setTitle("Recibo de Egresos");

        // Inicializar el table model y asignarlo
        transaccionTableModel = new TransaccionTableModel();
        jTable1.setModel(transaccionTableModel);

        setupTableAesthetics();

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
                20);
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        // Montar el panel de paginación en el contenedor visual del listado
        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        // Cargar datos en la tabla (fuera del EDT)
        cargarEgresosEnTabla();

        // Cargar datos de tipo_transaccion (Filtrado por EGRESO)
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
        setupFilterBehavior();

        // Actualizar Estado
        UIHelpers.updatePlaceholderState(txtDoc);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
        UIHelpers.updatePlaceholderState(txtImporte);
    }

    private void setupTableAesthetics() {
        // Renderizador para la columna Importe (índice 5 en TransaccionTableModel)
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);

                if (value instanceof BigDecimal) {
                    label.setText("- " + formatter.format(value));
                }

                label.setHorizontalAlignment(JLabel.RIGHT);

                if (!isSelected) {
                    label.setBackground(new Color(255, 235, 235)); // Rojo muy suave
                    label.setForeground(new Color(150, 0, 0)); // Rojo oscuro
                } else {
                    label.setForeground(Color.WHITE);
                }

                return label;
            }
        });
    }

    private void setupFilterBehavior() {
        DocumentFilters.attachNumeric(txtDoc, 11);
        DocumentFilters.attachDecimal(txtImporte, 12);
        DocumentFilters.attachTextAreaLimit(txtDescripcionArea, 200);

        // Hints y placeholders
        UIHelpers.attachHintAndFocusColor(txtDoc, "DNI/RUC: Ingrese 8 u 11 dígitos");
        UIHelpers.attachHintAndFocusColor(txtDescripcionArea, "Descripción (5-200 caracteres).");
        UIHelpers.attachHintAndFocusColor(txtImporte, "Importe (mayor a 0)");

        UIHelpers.attachPlaceholder(txtDoc, " DNI / RUC");
        UIHelpers.attachPlaceholder(txtDescripcionArea, " Motivo o Descripción");
        UIHelpers.attachPlaceholder(txtImporte, " Importe");

        // Actualizar Estado
        UIHelpers.updatePlaceholderState(txtDoc);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
        UIHelpers.updatePlaceholderState(txtImporte);
    }

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
                JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarEgresosEnTabla() {
        SwingWorker<List<Transaccion>, Void> worker = new SwingWorker<List<Transaccion>, Void>() {
            @Override
            protected List<Transaccion> doInBackground() throws Exception {
                try {
                    return controller.listarEgresosPorSesionActiva();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar egresos: {0}", ex.toString());
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

    private void cargarTiposTransaccionEnCombo() {
        SwingWorker<List<TipoTransaccion>, Void> worker = new SwingWorker<List<TipoTransaccion>, Void>() {
            @Override
            protected List<TipoTransaccion> doInBackground() throws Exception {
                try {
                    TipoTransaccionDAO tipoDao = new TipoTransaccionDAO();
                    List<String> categorias = Arrays.asList("EGRESO");
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
                        LOGGER.log(Level.WARNING, "No hay tipos de transacción activos para EGRESO");
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al poblar combo tipos: {0}", ex.toString());
                }
            }
        };
        worker.execute();
    }

    private void onEdit() {
        int viewRow = jTable1.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Registro Para Editar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = jTable1.convertRowIndexToModel(viewRow);

        Transaccion seleccionado = transaccionTableModel.getTransaccionAt(modelRow);
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Registro Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Callback: recarga todo el listado para mantener coherencia
        Consumer<Transaccion> onUpdated = (t) -> {
            cargarEgresosEnTabla();
        };

        // Abrir JInternalFrame de edición (Frm_Modificar_Egreso)
        Frm_Modificar_Egreso frm = new Frm_Modificar_Egreso(seleccionado, onUpdated);
        showInternal(frm);
    }

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();
        panel_registro_egresos = new javax.swing.JPanel();
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado De Registro - Egresos"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null }
                },
                new String[] {
                        "Sr.(es)", "DNI o RUC", "Dirección", "Descripción", "Importe ", "Fecha Registro"
                }));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
                pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        pnlPaginacionLayout.setVerticalGroup(
                pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 40, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(pnlPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)));

        panel_registro_egresos
                .setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario De Registro - Egresos"));

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

        javax.swing.GroupLayout panel_registro_egresosLayout = new javax.swing.GroupLayout(panel_registro_egresos);
        panel_registro_egresos.setLayout(panel_registro_egresosLayout);
        panel_registro_egresosLayout.setHorizontalGroup(
                panel_registro_egresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                panel_registro_egresosLayout.createSequentialGroup()
                                                        .addGroup(panel_registro_egresosLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(panel_registro_egresosLayout
                                                                        .createSequentialGroup()
                                                                        .addGroup(panel_registro_egresosLayout
                                                                                .createParallelGroup(
                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lblDoc)
                                                                                .addComponent(lblSr))
                                                                        .addGap(7, 7, 7))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        panel_registro_egresosLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(lblDireccion)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                                        .addGroup(panel_registro_egresosLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(txtDoc,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(txtSr)
                                                                .addComponent(txtDireccion)))
                                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbTipoTransaccion, 0,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                                .addGroup(panel_registro_egresosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblDescripcion)
                                                        .addComponent(lblImporte))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel_registro_egresosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtImporte)
                                                        .addComponent(jScrollPane2))))
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscarCliente)
                                .addGap(65, 65, 65)
                                .addComponent(lblFecha)
                                .addGap(20, 20, 20)));
        panel_registro_egresosLayout.setVerticalGroup(
                panel_registro_egresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblDoc)
                                        .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscarCliente)
                                        .addComponent(lblFecha))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSr, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSr))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblDireccion))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18,
                                        Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cbTipoTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panel_registro_egresosLayout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(lblDescripcion)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18,
                                        Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 17,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(panel_registro_egresosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2))
                                .addContainerGap(18, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panel_registro_egresos, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(50, 50, 50)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(panel_registro_egresos, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)));

        jPanel1.getAccessibleContext().setAccessibleName("Listado De Registro - Egresos");
        panel_registro_egresos.getAccessibleContext().setAccessibleName("Formulario De Registro - Egresos");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSrActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSrActionPerformed
    }// GEN-LAST:event_txtSrActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBuscarClienteActionPerformed
        String doc = UIHelpers.getText(txtDoc).trim();
        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el DNI o RUC para buscar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Cliente c = controller.buscarClientePorDoc(doc);
            if (c == null) {
                int resp = JOptionPane.showConfirmDialog(this, "Cliente no encontrado. ¿Desea crear uno nuevo?",
                        "Cliente no encontrado", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    vista.Frm_Cliente frmCliente = new vista.Frm_Cliente(creado -> {
                        if (creado != null) {
                            clienteSeleccionado = creado;
                            txtSr.setText(creado.getNombre_completo());
                            txtDireccion.setText(creado.getDireccion());
                            txtDoc.setText(getIdentificadorCliente(creado));
                            UIHelpers.updatePlaceholderState(txtSr);
                            UIHelpers.updatePlaceholderState(txtDireccion);
                            UIHelpers.updatePlaceholderState(txtDoc);
                        }
                    });
                    showInternal(frmCliente);
                }
            } else {
                clienteSeleccionado = c;
                txtSr.setText(c.getNombre_completo());
                txtDireccion.setText(c.getDireccion());
                txtDoc.setText(getIdentificadorCliente(c));
                UIHelpers.updatePlaceholderState(txtSr);
                UIHelpers.updatePlaceholderState(txtDireccion);
                UIHelpers.updatePlaceholderState(txtDoc);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error buscar cliente en egresos: {0}", e.toString());
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuardarActionPerformed
        try {
            String doc = UIHelpers.getText(txtDoc).trim();
            String descripcion = UIHelpers.getText(txtDescripcionArea).trim();
            String importeStr = UIHelpers.getText(txtImporte).trim();

            TipoTransaccion tipoSeleccionado = (TipoTransaccion) cbTipoTransaccion.getSelectedItem();
            if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de egreso.", "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ValidationResult vr = controller.validateEgreso(doc, descripcion, importeStr);
            if (!vr.isOk()) {
                JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (clienteSeleccionado == null && !doc.isEmpty()) {
                clienteSeleccionado = controller.buscarClientePorDoc(doc);
            }
            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "El cliente es requerido.", "Cliente requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal importe = new BigDecimal(importeStr.replace(",", "."));
            int idTrans = controller.guardarEgreso(clienteSeleccionado, importe, descripcion,
                    tipoSeleccionado.getId_tipo());

            if (idTrans > 0) {
                JOptionPane.showMessageDialog(this, "Egreso registrado correctamente (ID: " + idTrans + ").", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                txtDescripcionArea.setText("");
                txtImporte.setText("");
                UIHelpers.updatePlaceholderState(txtDescripcionArea);
                UIHelpers.updatePlaceholderState(txtImporte);
                cargarEgresosEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar egreso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Sesión Requerida", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción guardar egreso: {0}", e.toString());
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnGuardarActionPerformed

    private void cbTipoTransaccionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbTipoTransaccionActionPerformed
    }// GEN-LAST:event_cbTipoTransaccionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        onEdit();
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un egreso para eliminar.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transaccion t = transaccionTableModel.getTransaccionAt(jTable1.convertRowIndexToModel(selectedRow));
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener el egreso seleccionado.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmación detallada (estilo Ingresos)
        String mensaje = String.format(
                "¿Está seguro de eliminar este egreso?\n\nCliente: %s\nImporte: %s\nDescripción: %s",
                t.getNombre_completo(),
                t.getImporte().toString(),
                t.getDescripcion());

        int confirm = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return controller.eliminarTransaccion(t.getId_transaccion());
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(Frm_Egresos.this, "Egreso eliminado correctamente.", "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                            cargarEgresosEnTabla();
                        } else {
                            JOptionPane.showMessageDialog(Frm_Egresos.this, "No se pudo eliminar el egreso.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error eliminar egreso", ex);
                        JOptionPane.showMessageDialog(Frm_Egresos.this, "Error al eliminar: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }// GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<TipoTransaccion> cbTipoTransaccion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDoc;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblImporte;
    private javax.swing.JLabel lblSr;
    private javax.swing.JPanel panel_registro_egresos;
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTextArea txtDescripcionArea;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtImporte;
    private javax.swing.JTextField txtSr;
    // End of variables declaration//GEN-END:variables
}
