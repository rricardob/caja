package vista;

import controlador.UsuarioController;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import modelo.Usuario;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Registrar_Usuario extends javax.swing.JInternalFrame {

    private final UsuarioController usuarioController;
    private String opcion;
    private Usuario usuario;
    private Consumer<Usuario> onUsuarioCreated; // callback que invocaremos cuando se cree el cliente (puede ser null)

    public Frm_Registrar_Usuario() {
        initComponents();
        this.usuarioController = new UsuarioController();
        setupFieldBehavior();
    }

    /**
     * Constructor que acepta callback
     */
    public Frm_Registrar_Usuario(Consumer<Usuario> onUsuarioCreated) {
        this();
        this.onUsuarioCreated = onUsuarioCreated;
    }

    // Constructor sobrecargado: recibe el parámetro
    public Frm_Registrar_Usuario(String parametro, Usuario usuario, Consumer<Usuario> onUsuarioCreated) {
        this(onUsuarioCreated); // llama al constructor base que ya inicializa todo
        this.opcion = parametro;
        this.usuario = usuario;
        this.setTitle(opcion);
        this.btn_guardar.setText(opcion);
        if (this.opcion.equals("Editar")) {
            this.txt_nombre_usuario.setText(usuario.getNombre_usuario());
            this.txt_nombre_completo.setText(usuario.getNombre_completo());
            this.txt_password.setText(usuario.getClave_usuario());

            UIHelpers.updatePlaceholderState(txt_nombre_usuario);
            UIHelpers.updatePlaceholderState(txt_nombre_completo);
            UIHelpers.updatePlaceholderState(txt_password);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_nombre_usuario = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        txt_nombre_completo = new javax.swing.JTextField();
        btn_guardar = new javax.swing.JButton();

        setClosable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro Usuario"));

        jLabel1.setText("Nombre Usuario:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Contraseña:");

        jLabel3.setText("Nombre Completo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombre_usuario)
                    .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(txt_nombre_completo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_nombre_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txt_nombre_completo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed

        String user = UIHelpers.getText(txt_nombre_usuario).trim();
        String clave = UIHelpers.getText(txt_password).trim();
        String nombreCompleto = UIHelpers.getText(txt_nombre_completo).trim();

        if (this.opcion.equals("Guardar")) {
            guardarUsuario(user, clave, nombreCompleto);
        } else {
            editarUsuario(user, clave, nombreCompleto);
        }

    }//GEN-LAST:event_btn_guardarActionPerformed

    private void guardarUsuario(String usuario, String clave, String nombreCompleto) {
        boolean flag = true;

        Usuario user = new Usuario();
        user.setNombre_usuario(usuario);
        user.setClave_usuario(clave);
        user.setNombre_completo(nombreCompleto);

        if (usuario == null || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, " El usuario no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (clave == null || clave.isEmpty()) {
            JOptionPane.showMessageDialog(null, " La clave no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (nombreCompleto == null || nombreCompleto.isEmpty()) {
            JOptionPane.showMessageDialog(null, " El nombre no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (flag) {

            if (this.usuarioController.existeUsuario(user.getNombre_usuario())) {
                JOptionPane.showMessageDialog(null, " Ya existe el nombre de usuario", "MENSAJE", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resultado = this.usuarioController.registrarUsuario(user);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, " Usuario registrado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onUsuarioCreated.accept(user);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al registrar el usuario ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void editarUsuario(String usuario, String clave, String nombreCompleto) {
        boolean flag = true;
        /*if (usuario == null || usuario.equals("")) {
            JOptionPane.showMessageDialog(null, " El usuario no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (clave == null || clave.equals("")) {
            JOptionPane.showMessageDialog(null, " La clave no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }

        if (nombreCompleto == null || nombreCompleto.equals("")) {
            JOptionPane.showMessageDialog(null, " El nombre no puede ser vacio ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            flag = false;
            return;
        }*/

        Usuario user = new Usuario();
        user.setNombre_usuario(usuario);
        user.setClave_usuario(clave);
        user.setNombre_completo(nombreCompleto);
        user.setId_usuario(this.usuario.getId_usuario());

        if (flag) {
            boolean resultado = this.usuarioController.actualizarUsuario(user);

            if (resultado) {
                JOptionPane.showMessageDialog(null, " Usuario actualizado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                onUsuarioCreated.accept(user);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al actualizar el usuario ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Configuración bloqueo inicial; filtros de longitud/dígitos/símbolos y
     * estado según tipo
     */
    private void setupFieldBehavior() {

        // Filtros alfanum/símbolos para nombre y direccion
        DocumentFilters.attachAlphaNumSymbol(txt_nombre_completo, 150);
        DocumentFilters.attachAlphaNumSymbol(txt_nombre_usuario, 150);
        DocumentFilters.attachAlphaNumSymbol(txt_password, 50);

        // Tooltips + efecto foco 
        UIHelpers.attachHintAndFocusColor(txt_nombre_completo, "Nombre Completo: letras, números, espacios y - . / (3-150 caracteres)");
        UIHelpers.attachHintAndFocusColor(txt_nombre_usuario, "Nombre de Usuario: letras, números, espacios y - . / (3-150 caracteres)");
        UIHelpers.attachHintAndFocusColor(txt_password, "Contraseña: letras, números, espacios y - . / (3-50 caracteres)");

        // Placeholders
        UIHelpers.attachPlaceholder(txt_nombre_completo, " Nombre Completo");
        UIHelpers.attachPlaceholder(txt_nombre_usuario, " Nombre Usuario");
        UIHelpers.attachPlaceholder(txt_password, " Contraseña");

        // Estado inicial según combo (esto también actualizará los placeholders apropiadamente)
        //updateTipoFields();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_nombre_completo;
    private javax.swing.JTextField txt_nombre_usuario;
    private javax.swing.JPasswordField txt_password;
    // End of variables declaration//GEN-END:variables
}
