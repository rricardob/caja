package vista;

import controlador.UsuarioController;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import modelo.Usuario;
import util.ViewUtil;
import vista.dataTableModel.PasswordCellRenderer;
import vista.dataTableModel.UsuarioTableModel;

public class Frm_Listado_Usuarios extends javax.swing.JInternalFrame {

    private final UsuarioController usuarioController;
    private JDesktopPane desktop;
    private Usuario usuarioEdicion;

    public Frm_Listado_Usuarios() {
        this.usuarioController = new UsuarioController();
        initComponents();
        loadData("");
        desktop = ViewUtil.getDesktopPaneAncestor(this);
        usuarioEdicion = new Usuario();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_usuarios = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txt_nombre_usuario = new javax.swing.JTextField();
        btn_nuevo_usuario = new javax.swing.JButton();
        btn_eliminar_usuario = new javax.swing.JButton();
        btn_editar_usuario = new javax.swing.JButton();

        setClosable(true);

        tbl_usuarios.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_usuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_usuarios);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar por Nombre Usuario"));

        txt_nombre_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nombre_usuarioKeyPressed(evt);
            }
        });

        btn_nuevo_usuario.setText("Nuevo");
        btn_nuevo_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_usuarioActionPerformed(evt);
            }
        });

        btn_eliminar_usuario.setText("Eliminar");
        btn_eliminar_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_usuarioActionPerformed(evt);
            }
        });

        btn_editar_usuario.setText("Editar");
        btn_editar_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_usuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_nombre_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btn_nuevo_usuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_editar_usuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_eliminar_usuario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevo_usuario)
                    .addComponent(btn_eliminar_usuario)
                    .addComponent(btn_editar_usuario))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombre_usuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombre_usuarioKeyPressed
        if (evt.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            loadData(txt_nombre_usuario.getText());
        }
    }//GEN-LAST:event_txt_nombre_usuarioKeyPressed

    private void btn_nuevo_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_usuarioActionPerformed
        Frm_Registrar_Usuario frm_Registrar_Usuario = new Frm_Registrar_Usuario("Guardar", this.usuarioEdicion);
        cargarFormularioUsuario(frm_Registrar_Usuario);
    }//GEN-LAST:event_btn_nuevo_usuarioActionPerformed

    private void btn_editar_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_usuarioActionPerformed
        Frm_Registrar_Usuario frm_Registrar_Usuario = new Frm_Registrar_Usuario("Editar", this.usuarioEdicion);
        cargarFormularioUsuario(frm_Registrar_Usuario);
    }//GEN-LAST:event_btn_editar_usuarioActionPerformed

    private void tbl_usuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_usuariosMouseClicked
        int filaSeleccionada = tbl_usuarios.getSelectedRow(); // Obtiene el índice de la fila (o -1 si no hay selección)

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return; // Sale si no hay fila
        }

        // Captura el valor de la columna 0 (ej. ID). Cambia el índice por el que quieras (0-based)
        Object id = tbl_usuarios.getValueAt(filaSeleccionada, 0); // Puede ser String, Integer, etc.
        Object nombreUsuario = tbl_usuarios.getValueAt(filaSeleccionada, 1); // Puede ser String, Integer, etc.
        Object nombreCompleto = tbl_usuarios.getValueAt(filaSeleccionada, 2); // Puede ser String, Integer, etc.
        Object clave = tbl_usuarios.getValueAt(filaSeleccionada, 4);
        this.usuarioEdicion.setId_usuario(Integer.parseInt(id.toString()));
        this.usuarioEdicion.setNombre_usuario(nombreUsuario.toString());
        this.usuarioEdicion.setNombre_completo(nombreCompleto.toString());
        this.usuarioEdicion.setClave_usuario(clave.toString());
    }//GEN-LAST:event_tbl_usuariosMouseClicked

    private void btn_eliminar_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_usuarioActionPerformed
        int filaSeleccionada = tbl_usuarios.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return; // Sale si no hay fila
        }

        eliminarUsuario();

    }//GEN-LAST:event_btn_eliminar_usuarioActionPerformed

    private void loadData(String nombreCompleto) {
        List<Usuario> usuarios = this.usuarioController.obtenerUsuarios(nombreCompleto);
        tbl_usuarios.setModel(new UsuarioTableModel(usuarios));
        aplicarEstilosTabla();
    }

    private void aplicarEstilosTabla() {
        TableColumnModel columnModel = tbl_usuarios.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(140);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(90);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        tbl_usuarios.setAutoCreateRowSorter(true);
        
        // Configurar el renderer personalizado para la columna "Clave" (índice 4)
        tbl_usuarios.getColumnModel().getColumn(4).setCellRenderer(new PasswordCellRenderer());
    }

    private void cargarFormularioUsuario(Frm_Registrar_Usuario frm_Registrar_Usuario) {

        javax.swing.JDesktopPane desktop = (javax.swing.JDesktopPane) javax.swing.SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registrar_Usuario.pack();
            if (frm_Registrar_Usuario.getWidth() < 300 || frm_Registrar_Usuario.getHeight() < 100) {
                frm_Registrar_Usuario.setSize(420, 220);
            }
            desktop.add(frm_Registrar_Usuario);
            desktop.revalidate();
            desktop.repaint();
            frm_Registrar_Usuario.setVisible(true);
            desktop.moveToFront(frm_Registrar_Usuario);
            try {
                frm_Registrar_Usuario.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registrar_Usuario.toFront();
        }
    }

    private void eliminarUsuario() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el usuario?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            if (this.usuarioController.eliminarUsuario(this.usuarioEdicion.getId_usuario())) {
                JOptionPane.showMessageDialog(this, "Usuario Eliminado Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un problema al eliminar al usuario", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }
        loadData("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_editar_usuario;
    private javax.swing.JButton btn_eliminar_usuario;
    private javax.swing.JButton btn_nuevo_usuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_usuarios;
    private javax.swing.JTextField txt_nombre_usuario;
    // End of variables declaration//GEN-END:variables
}
