
package vista;

import controlador.RegistroBancoController;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import modelo.Banco;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Registro_Banco extends javax.swing.JInternalFrame {
    
    
    private String opcion;
    private final RegistroBancoController bancoController;
    private Consumer<Banco> onBancoCreated;
    private Banco banco;

    public Frm_Registro_Banco() {
        initComponents();
        this.bancoController = new RegistroBancoController();
        setupFieldBehavior();
    }
    
    /**
     * Constructor que acepta callback
     */
    public Frm_Registro_Banco(Consumer<Banco> onBancoCreated) {
        this();
        this.onBancoCreated = onBancoCreated;
    }
    
    
    // Constructor sobrecargado: recibe el parámetro
    public Frm_Registro_Banco(String parametro, Banco banco, Consumer<Banco> onBancoCreated) {
        this(onBancoCreated); // llama al constructor base que ya inicializa todo
        this.opcion = parametro;
        this.banco = banco;
        this.setTitle(opcion);
        this.btn_guardar.setText(opcion);
        if (this.opcion.equals("Editar")) {
            this.txt_nombre_banco.setText(banco.getDescripcion());
            UIHelpers.updatePlaceholderState(txt_nombre_banco);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_guardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txt_nombre_banco = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbx_estado = new javax.swing.JComboBox<>();

        setClosable(true);
        setMaximizable(true);

        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Banco"));

        jLabel1.setText("Nombre Banco");

        jLabel2.setText("Estado");

        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombre_banco, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(cbx_estado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_nombre_banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_guardar)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_guardar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed

        String banco = UIHelpers.getText(txt_nombre_banco).trim();
        String estado = (String) cbx_estado.getSelectedItem();


        if (this.opcion.equals("Guardar")) {
            guardarBanco(banco);
        } else {
            editarBanco(banco, ("Activo".equals(estado)) ? Boolean.TRUE : Boolean.FALSE);
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void guardarBanco(String nombreBanco) {
        boolean flag = true;

        Banco banco = new Banco();
        banco.setDescripcion(nombreBanco);
        banco.setEstado(1);

        if (nombreBanco == null || nombreBanco.isEmpty()) {
            JOptionPane.showMessageDialog(null, " El banco no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }


        if (flag) {

            if (this.bancoController.existeBanco(banco.getDescripcion())) {
                JOptionPane.showMessageDialog(null, " Ya existe el banco", "MENSAJE", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resultado = this.bancoController.registrarBanco(banco);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, " Banco registrado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoCreated.accept(banco);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al registrar el banco ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    
    private void editarBanco(String nombreBanco, boolean estaroBanco) {
        boolean flag = true;
    
        Banco banco = new Banco();
        banco.setDescripcion(nombreBanco);
        banco.setEstado(estaroBanco ? 1 : 0);
        banco.setId_banco(this.banco.getId_banco());

        if (flag) {
            boolean resultado = this.bancoController.actualizarBanco(banco);

            if (resultado) {
                JOptionPane.showMessageDialog(null, " Banco actualizado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onBancoCreated.accept(banco);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al actualizar el banco ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Configuración bloqueo inicial; filtros de longitud/dígitos/símbolos y
     * estado según tipo
     */
    private void setupFieldBehavior() {

        // Filtros alfanum/símbolos para nombre y direccion
        DocumentFilters.attachAlphaNumSymbol(txt_nombre_banco, 150);

        // Tooltips + efecto foco 
        UIHelpers.attachHintAndFocusColor(txt_nombre_banco, "Nombre de Banco: letras, números, espacios y - . / (3-100 caracteres)");

        // Placeholders
        UIHelpers.attachPlaceholder(txt_nombre_banco, " Nombre de Banco");

        // Estado inicial según combo (esto también actualizará los placeholders apropiadamente)
        //updateTipoFields();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardar;
    private javax.swing.JComboBox<String> cbx_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_nombre_banco;
    // End of variables declaration//GEN-END:variables
}
