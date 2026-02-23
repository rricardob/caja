package vista;

import controlador.BancoCuentaController;
import controlador.BancoDepositoController;
import controlador.RegistroBancoController;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import modelo.Banco;
import modelo.BancoCuenta;
import modelo.BancoDeposito;
import util.ViewUtil;
import vista.components.PaginationPanel;
import vista.dataTableModel.BancoDepositoTableModel;
import vista.dataTableModel.PaginatedTableModel;

public class Frm_Listado_Banco_Deposito extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Listado_Banco_Deposito.class.getName());

    private final RegistroBancoController bancoController;
    private final BancoCuentaController bancoCuentaController;
    private BancoDepositoController bancoDepositoController;
    private PaginatedTableModel<BancoDeposito> paginatedModel;
    private PaginationPanel paginationPanel;
    private BancoDepositoTableModel tableModel;
    private JDesktopPane desktop;
    private BancoDeposito bancoDepositoEdicion;

    public Frm_Listado_Banco_Deposito() {
        initComponents();
        this.bancoController = new RegistroBancoController();
        this.bancoCuentaController = new BancoCuentaController();
        this.bancoDepositoController = new BancoDepositoController();
        cbx_cuenta.setEnabled(false);
        cargarBancoEnCombo();
        controlesPaginacion();
    }

    private void cargarBancoEnCombo() {

        try {
            DefaultComboBoxModel<Banco> model = new DefaultComboBoxModel<>();
            List<Banco> lista = bancoController.listarBanco("");
            for (Banco t : lista) {
                model.addElement(t);
            }
            cbx_bancos.setModel(model);
            cbx_cuenta.setEnabled(true);

            if (lista.isEmpty()) {
                LOGGER.log(Level.WARNING, "No hay bancos activos");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al poblar bancos: {0}", ex.toString());
        }

    }

    private void cargarBancoCuentaEnCombo() {

        try {

            Banco bancoSeleccionado = (Banco) cbx_bancos.getSelectedItem();

            if (bancoSeleccionado != null) {
                DefaultComboBoxModel<BancoCuenta> model = new DefaultComboBoxModel<>();
                List<BancoCuenta> lista = bancoCuentaController.listarBancoCuenta(bancoSeleccionado.getId_banco(), null, null, null, null, Boolean.TRUE);
                for (BancoCuenta t : lista) {
                    model.addElement(t);
                }
                cbx_cuenta.setEnabled(true);
                cbx_cuenta.setModel(model);

                if (lista.isEmpty()) {
                    cbx_cuenta.setEnabled(false);
                    LOGGER.log(Level.WARNING, "No hay cuentas activos");
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al poblar cuentas: {0}", ex.toString());
        }

    }

    private void controlesPaginacion() {
        this.bancoDepositoController = new BancoDepositoController();

        tableModel = new BancoDepositoTableModel(java.util.Collections.emptyList());
        tbl_depositos.setModel(tableModel);

        paginatedModel = new PaginatedTableModel<>(
                tableModel,
                (model, data) -> ((BancoDepositoTableModel) model).load(data),
                20
        );
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        loadData(null, null, null, null, null);

        desktop = ViewUtil.getDesktopPaneAncestor(this);
        bancoDepositoEdicion = new BancoDeposito();

        tbl_depositos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //editarBanco();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_nro_voucher = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dc_fecha_inicio = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dc_fecha_fin = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        cbx_bancos = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbx_cuenta = new javax.swing.JComboBox<>();
        btn_buscar = new javax.swing.JButton();
        btn_limpiar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbx_estado = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_depositos = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();

        setClosable(true);
        setResizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro Depositos"));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Nro Voucher:");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Fecha Inicio:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Fecha Fin:");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Banco:");

        cbx_bancos.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_bancos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_bancosActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Cuenta:");

        cbx_cuenta.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

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

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Estado:");

        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cbx_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_cuenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_nro_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addComponent(btn_buscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_limpiar)
                        .addGap(0, 282, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbx_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbx_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txt_nro_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_buscar)
                            .addComponent(btn_limpiar))
                        .addContainerGap())))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado Depositos"));

        tbl_depositos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_depositos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_depositosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_depositos);

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPaginacionLayout.setVerticalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlPaginacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_nuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_editar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbx_bancosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_bancosActionPerformed
        cargarBancoCuentaEnCombo();
    }//GEN-LAST:event_cbx_bancosActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        dc_fecha_inicio.setDate(null);
        dc_fecha_fin.setDate(null);
        cbx_bancos.setSelectedIndex(-1);
        cbx_cuenta.setSelectedIndex(-1);
        txt_nro_voucher.setText("");
        loadData(null, null, null, null, null);
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        // 1. Obtener los valores de los controles
        BancoCuenta bancoCuenta = (BancoCuenta) cbx_cuenta.getSelectedItem();
        Long idCuenta = (bancoCuenta != null) ? bancoCuenta.getId_cuenta() : null;

        String nroVoucher = txt_nro_voucher.getText().trim();
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
                idCuenta, // null = sin filtro de banco
                nroVoucher.isEmpty() ? null : nroVoucher,
                fechaDesde, // null si no se eligió fecha inicio
                fechaHasta, // null si no se eligió fecha fin
                activo // null = sin filtro de estado, true/false = filtra
        );
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void tbl_depositosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_depositosMouseClicked
       int filaSeleccionada = tbl_depositos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object id = tbl_depositos.getValueAt(filaSeleccionada, 0);
        //Object nombreBanco = tbl_cuentas.getValueAt(filaSeleccionada, 1);
        //Object estado = tbl_cuentas.getValueAt(filaSeleccionada, 2);
        
        BancoDeposito bancoDepositoSelected = this.bancoDepositoController.obtenerBancoDepositoPorId(Long.parseLong(id.toString()));
        
        this.bancoDepositoEdicion.setId_deposito(bancoDepositoSelected.getId_deposito());
        this.bancoDepositoEdicion.setFecha(bancoDepositoSelected.getFecha());
        this.bancoDepositoEdicion.setNro_voucher(bancoDepositoSelected.getNro_voucher());
        this.bancoDepositoEdicion.setId_cuenta(bancoDepositoSelected.getId_cuenta());
        this.bancoDepositoEdicion.setImporte(bancoDepositoSelected.getImporte());
        this.bancoDepositoEdicion.setId_usuario(bancoDepositoSelected.getId_usuario());
        this.bancoDepositoEdicion.setTerminal(bancoDepositoSelected.getTerminal());
        this.bancoDepositoEdicion.setEstado(bancoDepositoSelected.getEstado());

        btn_editar.setEnabled(Boolean.TRUE);
        btn_eliminar.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_tbl_depositosMouseClicked

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        Frm_Registro_Deposito_Banco frm_Registro_Deposito_Banco = new Frm_Registro_Deposito_Banco("Guardar", this.bancoDepositoEdicion, created -> {
            if (created != null) {
                loadData(null, null, null, null, null);
            }
        });
        cargarFormularioBancoDeposito(frm_Registro_Deposito_Banco);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int filaSeleccionada = tbl_depositos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        eliminarBancoDeposito();
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        editarBancoDeposito();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void loadData(
            Long idCuenta,
            String nroVoucher,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo
    ) {
        List<BancoDeposito> bancos = this.bancoDepositoController.listarBancoDeposito(idCuenta, nroVoucher, fechaDesde, fechaHasta, estadoActivo);
        paginatedModel.loadAllData(bancos);
        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        paginationPanel.enableAll(!bancos.isEmpty());
        aplicarEstilosTabla();
    }

    private void aplicarEstilosTabla() {
        TableColumnModel columnModel = tbl_depositos.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(20);
        columnModel.getColumn(6).setPreferredWidth(20);
        columnModel.getColumn(7).setPreferredWidth(20);
        columnModel.getColumn(8).setPreferredWidth(20);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);

        tbl_depositos.setAutoCreateRowSorter(true);

    }

    private void cargarFormularioBancoDeposito(Frm_Registro_Deposito_Banco frm_Registro_Deposito_Banco) {

        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registro_Deposito_Banco.pack();
            if (frm_Registro_Deposito_Banco.getWidth() < 300 || frm_Registro_Deposito_Banco.getHeight() < 100) {
                frm_Registro_Deposito_Banco.setSize(420, 220);
            }
            desktop.add(frm_Registro_Deposito_Banco);
            desktop.revalidate();
            desktop.repaint();
            frm_Registro_Deposito_Banco.setVisible(true);
            desktop.moveToFront(frm_Registro_Deposito_Banco);
            try {
                frm_Registro_Deposito_Banco.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registro_Deposito_Banco.toFront();
        }

    }

    private void eliminarBancoDeposito() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el Deposito?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            if (this.bancoDepositoController.eliminarBancoDeposito(this.bancoDepositoEdicion.getId_deposito())) {
                JOptionPane.showMessageDialog(this, "Deposito de Banco Eliminado Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un problema al eliminar el deposito banco", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }
        loadData(null, null, null, null, null);
    }

    private void editarBancoDeposito() {
        Frm_Registro_Deposito_Banco frm_Registro_Deposito_Banco = new Frm_Registro_Deposito_Banco("Editar", this.bancoDepositoEdicion, updated -> {
            if (updated != null) {
                loadData(null, null, null, null, null);
            }
        });
        cargarFormularioBancoDeposito(frm_Registro_Deposito_Banco);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<Banco> cbx_bancos;
    private javax.swing.JComboBox<BancoCuenta> cbx_cuenta;
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
    private javax.swing.JTable tbl_depositos;
    private javax.swing.JTextField txt_nro_voucher;
    // End of variables declaration//GEN-END:variables
}
