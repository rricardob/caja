/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import controlador.BancoCuentaController;
import dao.RegistroBancoDAO;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import modelo.Banco;
import modelo.BancoCuenta;
import util.ViewUtil;
import vista.components.PaginationPanel;
import vista.dataTableModel.BancoCuentaTableModel;
import vista.dataTableModel.PaginatedTableModel;

public class Frm_Listado_Banco_Cuenta extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Listado_Banco_Cuenta.class.getName());

    private BancoCuentaController bancoCuentaController;
    private JDesktopPane desktop;
    private BancoCuenta bancoCuentaEdicion;
    private PaginatedTableModel<BancoCuenta> paginatedModel;
    private PaginationPanel paginationPanel;
    private BancoCuentaTableModel tableModel;

    public Frm_Listado_Banco_Cuenta() {
        initComponents();
        this.bancoCuentaController = new BancoCuentaController();
        cargarBancoEnCombo();
        controlesPaginacion();
    }

    private void controlesPaginacion() {
        this.bancoCuentaController = new BancoCuentaController();

        tableModel = new BancoCuentaTableModel(java.util.Collections.emptyList());
        tbl_cuentas.setModel(tableModel);

        paginatedModel = new PaginatedTableModel<>(
                tableModel,
                (model, data) -> ((BancoCuentaTableModel) model).load(data),
                20
        );
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        loadData(null, null, null, null, null, null);

        desktop = ViewUtil.getDesktopPaneAncestor(this);
        bancoCuentaEdicion = new BancoCuenta();

        tbl_cuentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarBancoCuenta();
                }
            }
        });

        btn_editar.setEnabled(Boolean.FALSE);
        btn_eliminar.setEnabled(Boolean.FALSE);
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
                                "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                    paginationPanel.clearGoTo();
                    paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarBancoEnCombo() {
        SwingWorker<List<Banco>, Void> worker = new SwingWorker<List<Banco>, Void>() {
            @Override
            protected List<Banco> doInBackground() throws Exception {
                try {
                    RegistroBancoDAO bancoDAO = new RegistroBancoDAO();
                    return bancoDAO.obtenerBancos("");
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar bancos: {0}", ex.toString());
                    return java.util.Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<Banco> lista = get();
                    DefaultComboBoxModel<Banco> model = new DefaultComboBoxModel<>();
                    for (Banco t : lista) {
                        model.addElement(t);
                    }
                    cbx_bancos.setModel(model);

                    if (lista.isEmpty()) {
                        LOGGER.log(Level.WARNING, "No haybancos activos");
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al poblar bancos: {0}", ex.toString());
                }
            }
        };
        worker.execute();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dc_fecha_inicio = new com.toedter.calendar.JDateChooser();
        dc_fecha_fin = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        txt_nro_cuenta = new javax.swing.JTextField();
        cbx_estado = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbx_bancos = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txt_descripcion = new javax.swing.JTextField();
        btn_buscar = new javax.swing.JButton();
        btn_limpiar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_cuentas = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();

        setClosable(true);
        setResizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro Cuentas de Banco"));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Fecha Inicio:");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Fecha Fin:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Nùmero de Cuenta:");

        cbx_estado.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Estado:");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Banco:");

        cbx_bancos.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Descripcion;");

        btn_buscar.setText("Buscar");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        btn_limpiar.setText("Limpiar");
        btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(120, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_buscar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_limpiar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbx_bancos)
                        .addComponent(btn_buscar)
                        .addComponent(btn_limpiar)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado Cuentas"));

        tbl_cuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_cuentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_cuentasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_cuentas);

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPaginacionLayout.setVerticalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_editar.setText("Editar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_nuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btn_editar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_eliminar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_editar)
                    .addComponent(btn_eliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed

        // 1. Obtener los valores de los controles
        Banco banco = (Banco) cbx_bancos.getSelectedItem();
        Long idBanco = (banco != null) ? banco.getId_banco() : null;

        String nroCuenta = txt_nro_cuenta.getText().trim();
        String descripcion = txt_descripcion.getText().trim();
        String estadoStr = (String) cbx_estado.getSelectedItem();

        // Convertir estado a Boolean (null = sin filtro)
        Boolean activo = null;
        if (estadoStr != null) {
            if ("Activo".equals(estadoStr)) {
                activo = Boolean.TRUE;
            } else if ("Inactivo".equals(estadoStr)) {
                activo = Boolean.FALSE;
            }
            // si es "Todos" o cualquier otro → se queda en null (sin filtrar por estado)
        }

        // 2. Manejar las fechas → null si no están seleccionadas
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;

        Date dateInicio = dc_fecha_inicio.getDate();
        if (dateInicio != null) {
            fechaDesde = dateInicio.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        Date dateFin = dc_fecha_fin.getDate();
        if (dateFin != null) {
            fechaHasta = dateFin.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        // 3. Llamada al método de carga (puede recibir varios nulls)
        loadData(
                idBanco, // null = sin filtro de banco
                nroCuenta.isEmpty() ? null : nroCuenta,
                descripcion.isEmpty() ? null : descripcion,
                fechaDesde, // null si no se eligió fecha inicio
                fechaHasta, // null si no se eligió fecha fin
                activo // null = sin filtro de estado, true/false = filtra
        );

    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        loadData(null, null, null, null, null, null);
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void tbl_cuentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cuentasMouseClicked
       int filaSeleccionada = tbl_cuentas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object id = tbl_cuentas.getValueAt(filaSeleccionada, 0);
        //Object nombreBanco = tbl_cuentas.getValueAt(filaSeleccionada, 1);
        //Object estado = tbl_cuentas.getValueAt(filaSeleccionada, 2);
        
        BancoCuenta bancoCuentaSelected = this.bancoCuentaController.obtenerBancoCuentaPorId(Long.parseLong(id.toString()));
        
        this.bancoCuentaEdicion.setId_cuenta(bancoCuentaSelected.getId_cuenta());
        this.bancoCuentaEdicion.setId_banco(bancoCuentaSelected.getId_banco());
        this.bancoCuentaEdicion.setDescripcion(bancoCuentaSelected.getDescripcion());
        this.bancoCuentaEdicion.setNro_cuenta(bancoCuentaSelected.getNro_cuenta());
        this.bancoCuentaEdicion.setMoneda(bancoCuentaSelected.getMoneda());
        this.bancoCuentaEdicion.setFecha(bancoCuentaSelected.getFecha());
        this.bancoCuentaEdicion.setEstado(bancoCuentaSelected.getEstado());

        btn_editar.setEnabled(Boolean.TRUE);
        btn_eliminar.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_tbl_cuentasMouseClicked

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        Frm_Registro_Cuenta_Banco frm_Registro_Cuenta_Banco = new Frm_Registro_Cuenta_Banco("Guardar", this.bancoCuentaEdicion, created -> {
            if (created != null) {
                loadData(null, null, null, null, null, null);
            }
        });
        cargarFormularioBancoCuenta(frm_Registro_Cuenta_Banco);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        editarBancoCuenta();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int filaSeleccionada = tbl_cuentas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        eliminarBancoCuenta();
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void loadData(
            Long idBanco,
            String nroCuenta,
            String descripcion,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo
    ) {
        List<BancoCuenta> bancos = this.bancoCuentaController.listarBancoCuenta(idBanco, nroCuenta, descripcion, fechaDesde, fechaHasta, estadoActivo);
        paginatedModel.loadAllData(bancos);
        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        paginationPanel.enableAll(!bancos.isEmpty());
        aplicarEstilosTabla();
    }

    private void aplicarEstilosTabla() {
        TableColumnModel columnModel = tbl_cuentas.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(20);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);

        tbl_cuentas.setAutoCreateRowSorter(true);

    }
    
    private void cargarFormularioBancoCuenta(Frm_Registro_Cuenta_Banco frm_Registro_Banco) {

        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registro_Banco.pack();
            if (frm_Registro_Banco.getWidth() < 300 || frm_Registro_Banco.getHeight() < 100) {
                frm_Registro_Banco.setSize(420, 220);
            }
            desktop.add(frm_Registro_Banco);
            desktop.revalidate();
            desktop.repaint();
            frm_Registro_Banco.setVisible(true);
            desktop.moveToFront(frm_Registro_Banco);
            try {
                frm_Registro_Banco.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registro_Banco.toFront();
        }

    }
    
    private void cargarFormularioUsuario(Frm_Registro_Cuenta_Banco frm_Registro_Banco) {

        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registro_Banco.pack();
            if (frm_Registro_Banco.getWidth() < 300 || frm_Registro_Banco.getHeight() < 100) {
                frm_Registro_Banco.setSize(420, 220);
            }
            desktop.add(frm_Registro_Banco);
            desktop.revalidate();
            desktop.repaint();
            frm_Registro_Banco.setVisible(true);
            desktop.moveToFront(frm_Registro_Banco);
            try {
                frm_Registro_Banco.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registro_Banco.toFront();
        }

    }

    private void eliminarBancoCuenta() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el banco?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            if (this.bancoCuentaController.eliminarBancoCuenta(this.bancoCuentaEdicion.getId_cuenta())) {
                JOptionPane.showMessageDialog(this, "Cuenta de Banco Eliminado Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un problema al eliminar la cuenta banco", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }
        loadData(null, null, null, null, null, null);
    }
    
    private void editarBancoCuenta() {
        Frm_Registro_Cuenta_Banco frm_Registro_Cuenta_Banco = new Frm_Registro_Cuenta_Banco("Editar", this.bancoCuentaEdicion, updated -> {
            if (updated != null) {
                loadData(null, null, null, null, null, null);
            }
        });
        cargarFormularioUsuario(frm_Registro_Cuenta_Banco);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<Banco> cbx_bancos;
    private javax.swing.JComboBox<String> cbx_estado;
    private com.toedter.calendar.JDateChooser dc_fecha_fin;
    private com.toedter.calendar.JDateChooser dc_fecha_inicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTable tbl_cuentas;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_nro_cuenta;
    // End of variables declaration//GEN-END:variables
}
