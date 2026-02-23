package vista;

import controlador.BancoCuentaController;
import controlador.BancoDepositoController;
import controlador.RegistroBancoController;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo.Banco;
import modelo.BancoCuenta;
import modelo.BancoDeposito;
import modelo.SessionManager;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Registro_Deposito_Banco extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Registro_Deposito_Banco.class.getName());

    private String opcion;
    private final BancoDepositoController bancoDepositoController;
    private final BancoCuentaController bancoCuentaController;
    private final RegistroBancoController bancoController;
    private Consumer<BancoDeposito> onBancoDepositoCreated;
    private BancoDeposito bancoDeposito;
    private final SessionManager session;

    public Frm_Registro_Deposito_Banco() {
        initComponents();
        this.bancoDepositoController = new BancoDepositoController();
        this.bancoController = new RegistroBancoController();
        this.bancoCuentaController = new BancoCuentaController();
        this.session = SessionManager.getInstance();
        cargarBancoEnCombo();
        setupFieldBehavior();
    }

    public Frm_Registro_Deposito_Banco(Consumer<BancoDeposito> onBancoDepositoCreated) {
        this();
        this.onBancoDepositoCreated = onBancoDepositoCreated;
    }

    public Frm_Registro_Deposito_Banco(String parametro, BancoDeposito bancoDeposito, Consumer<BancoDeposito> onBancoDepositoCreated) {
        this(onBancoDepositoCreated); // llama al constructor base que ya inicializa todo
        this.opcion = parametro;
        this.bancoDeposito = bancoDeposito;
        this.setTitle(opcion);
        this.btn_guardar.setText(opcion);
        if (this.opcion.equals("Editar")) {
            //this.cbx_bancos.setSelectedItem(this.bancoCuenta.getId_banco());
            BancoCuenta bc = this.bancoCuentaController.obtenerBancoCuentaPorId(this.bancoDeposito.getId_cuenta());
            selectBancoById(bc.getId_banco());
            selectCuentaById(bc.getId_cuenta());
            this.txt_nro_voucher.setText(this.bancoDeposito.getNro_voucher());
            this.txt_importe.setText(this.bancoDeposito.getImporte() + "");
            this.cbx_estado.setSelectedItem(this.bancoDeposito.getEstado() == 1 ? "Activo" : "Inactivo");
            UIHelpers.updatePlaceholderState(txt_nro_voucher);
            UIHelpers.updatePlaceholderState(txt_importe);
        }
    }

    private void cargarBancoEnCombo() {

        try {
            DefaultComboBoxModel<Banco> model = new DefaultComboBoxModel<>();
            List<Banco> lista = bancoController.listarBanco("");
            for (Banco t : lista) {
                model.addElement(t);
            }
            cbx_bancos.setModel(model);

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
                cbx_cuentas.setEnabled(true);
                cbx_cuentas.setModel(model);

                if (lista.isEmpty()) {
                    cbx_cuentas.setEnabled(false);
                    LOGGER.log(Level.WARNING, "No hay cuentas activos");
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al poblar cuentas: {0}", ex.toString());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_nro_voucher = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbx_bancos = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbx_cuentas = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_importe = new javax.swing.JTextField();
        cbx_estado = new javax.swing.JComboBox<>();
        btn_guardar = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Deposito"));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Nro Voucher:");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Banco:");

        cbx_bancos.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_bancos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_bancosActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Cuenta:");

        cbx_cuentas.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Importe");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Estado:");

        cbx_estado.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_guardar)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_nro_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_bancos, 0, 435, Short.MAX_VALUE)
                            .addComponent(cbx_cuentas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_importe, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_nro_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbx_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbx_cuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbx_bancosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_bancosActionPerformed
        cargarBancoCuentaEnCombo();
    }//GEN-LAST:event_cbx_bancosActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        Banco idBanco = (Banco) cbx_bancos.getSelectedItem();
        String nroVoucher = UIHelpers.getText(txt_nro_voucher).trim();
        BancoCuenta idCuenta = (BancoCuenta) cbx_cuentas.getSelectedItem();
        String importe = UIHelpers.getText(txt_importe).trim();
        //usuario
        //terminal
        String estado = (String) cbx_estado.getSelectedItem();

        if (this.opcion.equals("Guardar")) {
            guardarBancoDeposito(idCuenta.getId_cuenta(), nroVoucher, Double.parseDouble(importe), this.session.getIdUsuario(), this.session.getNombreUsuario(), estado.equals("Activo") ? 1 : 0);
        } else {
            editarBancoDeposito(idCuenta.getId_cuenta(), nroVoucher, this.session.getNombreUsuario(), this.session.getIdUsuario(), Double.parseDouble(importe), estado.equals("Activo") ? 1 : 0);
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void selectBancoById(Long idBanco) {
        if (idBanco == null) {
            return;
        }
        ComboBoxModel<Banco> model = cbx_bancos.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Banco item = model.getElementAt(i);
            if (item != null && idBanco.equals(item.getId_banco())) {
                cbx_bancos.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectCuentaById(Long idCuenta) {
        if (idCuenta == null) {
            return;
        }
        ComboBoxModel<BancoCuenta> model = cbx_cuentas.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            BancoCuenta item = model.getElementAt(i);
            if (item != null && idCuenta.equals(item.getId_banco())) {
                cbx_cuentas.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardarBancoDeposito(long idCuenta, String nroVoucher, Double importe, int idUsuario, String terminal, int estado) {
        boolean flag = true;

        BancoDeposito bancoDesposito = new BancoDeposito();
        bancoDesposito.setId_cuenta(idCuenta);
        bancoDesposito.setNro_voucher(nroVoucher);
        bancoDesposito.setImporte(importe);
        bancoDesposito.setId_usuario(idUsuario);
        bancoDesposito.setTerminal(terminal);
        bancoDesposito.setEstado(estado);

        if (idCuenta == 0L) {
            JOptionPane.showMessageDialog(null, " La cuenta de Banco no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (nroVoucher == null || nroVoucher.isEmpty()) {
            JOptionPane.showMessageDialog(null, " El numero de voucher no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (importe == null || importe <= 0) {
            JOptionPane.showMessageDialog(null, " El importe del deposito no puede ser vacio o menor a cero ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (flag) {

            if (this.bancoDepositoController.existeBancoDeposito(bancoDesposito.getNro_voucher())) {
                JOptionPane.showMessageDialog(null, " Ya existe el voucher", "MENSAJE", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resultado = this.bancoDepositoController.registrarBancoDeposito(bancoDesposito);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, " Deposito registrado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoDepositoCreated.accept(bancoDesposito);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al registrar el deposito ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void editarBancoDeposito(long idCuenta, String nroVoucher, String terminal, int idUsuario, double importe, int estado) {
        boolean flag = true;

        BancoDeposito bancoDeposito = new BancoDeposito();
        bancoDeposito.setId_cuenta(idCuenta);
        bancoDeposito.setNro_voucher(nroVoucher);
        bancoDeposito.setTerminal(terminal);
        bancoDeposito.setId_usuario(idUsuario);
        bancoDeposito.setImporte(importe);
        bancoDeposito.setId_deposito(this.bancoDeposito.getId_deposito());

        if (flag) {
            boolean resultado = this.bancoDepositoController.actualizarBancoDeposito(bancoDeposito);

            if (resultado) {
                JOptionPane.showMessageDialog(null, "Deposito de Banco actualizado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoDepositoCreated.accept(bancoDeposito);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al actualizar el deposito de Banco ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void setupFieldBehavior() {

        // Filtros alfanum/símbolos para nombre y direccion
        DocumentFilters.attachAlphaNumSymbol(txt_nro_voucher, 30);
        DocumentFilters.attachDecimal(txt_importe, 20);
        //DocumentFilters.attachAlphaNumSymbol(txt_importe, 30);

        // Tooltips + efecto foco 
        UIHelpers.attachHintAndFocusColor(txt_nro_voucher, "Nùmero de Voucher: números y guiones (3-20 caracteres)");
        UIHelpers.attachHintAndFocusColor(txt_importe, "Importe (>= 1). Puede usar coma o punto decimal.");

        // Placeholders
        UIHelpers.attachPlaceholder(txt_nro_voucher, " Nùmero de Voucher");
        UIHelpers.attachPlaceholder(txt_importe, " Importe");

        // Estado inicial según combo (esto también actualizará los placeholders apropiadamente)
        //updateTipoFields();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox<Banco> cbx_bancos;
    private javax.swing.JComboBox<BancoCuenta> cbx_cuentas;
    private javax.swing.JComboBox<String> cbx_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_importe;
    private javax.swing.JTextField txt_nro_voucher;
    // End of variables declaration//GEN-END:variables
}
