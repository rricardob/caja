package vista;

import controlador.BancoCuentaController;
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
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Registro_Cuenta_Banco extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Registro_Cuenta_Banco.class.getName());

    private String opcion;
    private final BancoCuentaController bancoCuentaController;
    private final RegistroBancoController bancoController;
    private Consumer<BancoCuenta> onBancoCuentaCreated;
    private BancoCuenta bancoCuenta;

    public Frm_Registro_Cuenta_Banco() {
        initComponents();
        this.bancoCuentaController = new BancoCuentaController();
        this.bancoController = new RegistroBancoController();
        cargarBancoEnCombo();
        setupFieldBehavior();
    }

    public Frm_Registro_Cuenta_Banco(Consumer<BancoCuenta> onBancoCuentaCreated) {
        this();
        this.onBancoCuentaCreated = onBancoCuentaCreated;
    }

    public Frm_Registro_Cuenta_Banco(String parametro, BancoCuenta bancoCuenta, Consumer<BancoCuenta> onBancoCuentaCreated) {
        this(onBancoCuentaCreated); // llama al constructor base que ya inicializa todo
        this.opcion = parametro;
        this.bancoCuenta = bancoCuenta;
        this.setTitle(opcion);
        this.btn_guardar.setText(opcion);
        if (this.opcion.equals("Editar")) {
            //this.cbx_bancos.setSelectedItem(this.bancoCuenta.getId_banco());
            selectBancoCuentaById(this.bancoCuenta.getId_banco());
            this.txt_nro_cuenta.setText(this.bancoCuenta.getNro_cuenta());
            this.txt_descripcion.setText(this.bancoCuenta.getDescripcion());
            this.cbx_moneda.setSelectedItem(this.bancoCuenta.getMoneda().equals("PEN") ? "Soles" : "Dolares");
            this.cbx_estado.setSelectedItem(this.bancoCuenta.getEstado() == 1 ? "Activo" : "Inactivo");
            UIHelpers.updatePlaceholderState(txt_nro_cuenta);
            UIHelpers.updatePlaceholderState(txt_descripcion);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbx_bancos = new javax.swing.JComboBox<>();
        btn_guardar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_nro_cuenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_descripcion = new javax.swing.JTextField();
        cbx_moneda = new javax.swing.JComboBox<>();
        cbx_estado = new javax.swing.JComboBox<>();

        setClosable(true);
        setMaximizable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Cuenta Banco"));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Banco:");

        cbx_bancos.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Nro Cuenta:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Descripcion:");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Moneda:");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Estado:");

        cbx_moneda.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_moneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Soles", "Dolares" }));

        cbx_estado.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(183, 183, 183)
                                .addComponent(btn_guardar))
                            .addComponent(cbx_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbx_bancos))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbx_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar))
                .addContainerGap())
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

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        Banco idBanco = (Banco) cbx_bancos.getSelectedItem();
        String nroCuenta = UIHelpers.getText(txt_nro_cuenta).trim();
        String descripcion = UIHelpers.getText(txt_descripcion).trim();
        String moneda = (String) cbx_moneda.getSelectedItem();
        String estado = (String) cbx_estado.getSelectedItem();

        if (this.opcion.equals("Guardar")) {
            guardarBancoCuenta(idBanco.getId_banco(), nroCuenta, descripcion, moneda, estado.equals("Activo") ? 1 : 0);
        } else {
            editarBancoCuenta(idBanco.getId_banco(), nroCuenta, descripcion, moneda, estado.equals("Activo") ? 1 : 0);
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void guardarBancoCuenta(long idBanco, String nroCuenta, String descripcion, String moneda, int estado) {
        boolean flag = true;

        BancoCuenta bancoCuenta = new BancoCuenta();
        bancoCuenta.setId_banco(idBanco);
        bancoCuenta.setNro_cuenta(nroCuenta);
        bancoCuenta.setDescripcion(descripcion);
        bancoCuenta.setMoneda(moneda);
        bancoCuenta.setEstado(estado);

        if (idBanco == 0L) {
            JOptionPane.showMessageDialog(null, " El banco no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (nroCuenta == null || nroCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(null, " El numero de cuenta no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (flag) {

            if (this.bancoCuentaController.existeBancoCuenta(bancoCuenta.getNro_cuenta())) {
                JOptionPane.showMessageDialog(null, " Ya existe la cuenta de banco", "MENSAJE", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resultado = this.bancoCuentaController.registrarBancoCuenta(bancoCuenta);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, " Cuenta de Banco registrado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoCuentaCreated.accept(bancoCuenta);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al registrar la Cuenta banco ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void editarBancoCuenta(long idBanco, String nroCuenta, String descripcion, String moneda, int estado) {
        boolean flag = true;

        BancoCuenta bancoCuenta = new BancoCuenta();
        bancoCuenta.setId_banco(idBanco);
        bancoCuenta.setNro_cuenta(nroCuenta);
        bancoCuenta.setDescripcion(descripcion);
        bancoCuenta.setMoneda(moneda);
        bancoCuenta.setEstado(estado);
        bancoCuenta.setId_cuenta(this.bancoCuenta.getId_cuenta());

        if (flag) {
            boolean resultado = this.bancoCuentaController.actualizarBancoCuenta(bancoCuenta);

            if (resultado) {
                JOptionPane.showMessageDialog(null, "Cuenta de Banco actualizado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoCuentaCreated.accept(bancoCuenta);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al actualizar la Cuenta de Banco ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void setupFieldBehavior() {

        // Filtros alfanum/símbolos para nombre y direccion
        DocumentFilters.attachAlphaNumSymbol(txt_nro_cuenta, 20);
        DocumentFilters.attachAlphaNumSymbol(txt_descripcion, 150);

        // Tooltips + efecto foco 
        UIHelpers.attachHintAndFocusColor(txt_nro_cuenta, "Nombre de Banco: letras, números, espacios y - . / (3-20 caracteres)");
        UIHelpers.attachHintAndFocusColor(txt_descripcion, "Descripcion: letras, números, espacios y - . / (3-150 caracteres)");

        // Placeholders
        UIHelpers.attachPlaceholder(txt_nro_cuenta, " Nombre de Banco");
        UIHelpers.attachPlaceholder(txt_descripcion, " Descripcion");

        // Estado inicial según combo (esto también actualizará los placeholders apropiadamente)
        //updateTipoFields();
    }

    private void selectBancoCuentaById(Long idBanco) {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox<Banco> cbx_bancos;
    private javax.swing.JComboBox<String> cbx_estado;
    private javax.swing.JComboBox<String> cbx_moneda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_nro_cuenta;
    // End of variables declaration//GEN-END:variables
}
