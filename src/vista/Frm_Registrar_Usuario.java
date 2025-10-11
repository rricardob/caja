package vista;

import controlador.UsuarioController;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class Frm_Registrar_Usuario extends javax.swing.JInternalFrame {

    private final UsuarioController usuarioController;
    private String opcion;
    private Usuario usuario;

    public Frm_Registrar_Usuario() {
        initComponents();
        this.usuarioController = new UsuarioController();
    }

    // Constructor sobrecargado: recibe el parámetro
    public Frm_Registrar_Usuario(String parametro, Usuario usuario) {
        this(); // Llama al constructor por defecto para inicializar la UI
        this.opcion = parametro; // Guarda el parámetro
        this.usuario = usuario;
        this.setTitle(opcion);
        this.btn_guardar.setText(opcion);
        //System.out.println("parametro: " + this.opcion + " usuario: " + this.usuario.toString());
        if (this.opcion.equals("Editar")) {
            this.txt_nombre_usuario.setText(usuario.getNombre_usuario());
            this.txt_nombre_completo.setText(usuario.getNombre_completo());
            this.txt_password.setText(usuario.getClave_usuario());
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
        String usuario = txt_nombre_usuario.getText();
        String clave = txt_password.getText();
        String nombreCompleto = txt_nombre_completo.getText();

        if (this.opcion.equals("Guardar")) {
            guardarUsuario(usuario, clave, nombreCompleto);
        } else {
            editarUsuario(usuario, clave, nombreCompleto);
        }

    }//GEN-LAST:event_btn_guardarActionPerformed

    private void guardarUsuario(String usuario, String clave, String nombreCompleto) {
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

        if (flag) {
            int resultado = this.usuarioController.registrarUsuario(user);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, " Usuario registrado correctamente ", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
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
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, " Hubo un error al actualizar el usuario ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
            }
        }

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
