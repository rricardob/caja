package vista;

import controlador.CajaController;
import controlador.UsuarioController;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.SesionCaja;
import modelo.SessionManager;
import modelo.Usuario;
import util.TextPrompt;

public class login extends javax.swing.JFrame {

    private final UsuarioController usuarioController;
    private final CajaController cajaController;
    private final SessionManager session;

    public login() {
        initComponents();
        this.usuarioController = new UsuarioController();
        this.cajaController = new CajaController();
        this.session = SessionManager.getInstance();
        this.setLocationRelativeTo(null);
        loadPlaceHolders();
        verificarCajaNoCerrada();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_usuario = new javax.swing.JTextField();
        txt_password = new javax.swing.JPasswordField();
        btn_login = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });
        txt_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_passwordKeyPressed(evt);
            }
        });

        btn_login.setBackground(new java.awt.Color(255, 51, 51));
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setText("Iniciar Sesiòn");
        btn_login.setBorder(null);
        btn_login.setBorderPainted(false);
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/logo_2.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        login();
    }//GEN-LAST:event_btn_loginActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void txt_passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_passwordKeyPressed
        if (evt.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_txt_passwordKeyPressed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        try {
            // Establece el Look and Feel de Metal
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_login;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables

    private void loadPlaceHolders() {
        new TextPrompt("Ingresa tu Usuario", this.txt_usuario);
        new TextPrompt("Ingresa tu Contraseña", this.txt_password);
    }

    private void verificarCajaNoCerrada() {
        System.out.println("====VERIFICANDO SI SE DEBE CERRAR CAJA=====");
        boolean estadoCaja = cajaController.puedeAperturarSesion(session.getIdUsuario());
        if (estadoCaja) {
            System.out.println("====CAJA ABIERTA====");
            SesionCaja sesionCaja = this.cajaController.obtenerSesionActivaActual(session.getIdUsuario());

            LocalDateTime horaInicioSesion = sesionCaja.getHoraInicio().toLocalDateTime();
            LocalDateTime ahora = LocalDateTime.now();

            LocalDate fechaInicio = horaInicioSesion.toLocalDate();
            LocalDate fechaActual = ahora.toLocalDate();

            System.out.println("Condición: ¿Es de un día anterior? " + fechaInicio.isBefore(fechaActual));
            if (fechaInicio.isBefore(fechaActual)) { // Si la sesión es de un día anterior
                System.out.println("====CERRANDO CAJA DE UN DIA ANTERIOR====");

                // Definir la hora de cierre al final del día de apertura (23:59:59)
                LocalDateTime fechaHoraCierre = horaInicioSesion
                        .withHour(23)
                        .withMinute(59)
                        .withSecond(59)
                        .withNano(0); // Elimina nanosegundos para precisión

                // Convertir a Timestamp
                Timestamp timestamp = Timestamp.valueOf(fechaHoraCierre);

                this.cajaController.cerrarSesion(sesionCaja.getIdSesion(), timestamp);
            }
        } else {
            System.out.println("====NO HAY CAJA ABIERTA PENDIENTE=====");
        }

    }

    public void login() {

        Usuario user = null;
        btn_login.setBackground(Color.RED);

        if (this.txt_usuario.getText().equals("")) {
            txt_usuario.requestFocusInWindow();

        } else if (this.txt_password.getText().equals("")) {
            txt_password.requestFocusInWindow();
        } else {

            user = usuarioController.login(txt_usuario.getText(), txt_password.getText());
            if (user == null) {
                JOptionPane.showMessageDialog(null, " LOS DATOS SON INCORRECTOS ", "MENSAJE", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                try {

                    menu.usuario = user.getNombre_completo();
                    menu.usuarioId = user.getId_usuario();

                    // Crear diálogo de carga
                    JDialog loadingDialog = new JDialog(this, "Cargando...", true);
                    loadingDialog.setSize(200, 100);
                    loadingDialog.setLocationRelativeTo(this);
                    loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                    JProgressBar progressBar = new JProgressBar();
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Cargando...");
                    progressBar.setStringPainted(true);

                    loadingDialog.add(progressBar);

                    // Usar SwingWorker para manejar el retardo
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            // Simula carga de 3 segundos
                            Thread.sleep(2000);
                            return null;
                        }

                        @Override
                        protected void done() {
                            loadingDialog.dispose();
                            menu menu = new menu();
                            menu.setVisible(true);
                            dispose(); // cerrar login
                        }
                    };

                    worker.execute();
                    loadingDialog.setVisible(true);

                } catch (Exception ex) {
                    System.out.print(ex.getMessage());

                }
            }

        }

    }

}
