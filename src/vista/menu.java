package vista;

import controlador.CajaController;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import modelo.SesionCaja;
import modelo.SessionManager;
import util.ViewUtil;

public class menu extends javax.swing.JFrame {

    public static String usuario;
    public static int usuarioId;
    private final SessionManager session;
    private final CajaController cajaController;
    private boolean estadoCaja;

    public menu() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        lbl_username.setVisible(false);
        lbl_username.setText("Usuario: " + usuario);
        this.session = SessionManager.getInstance();
        this.cajaController = new CajaController();
        configurarMenu();
        verificarSesionACtiva();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jToolBar1 = new javax.swing.JToolBar();
        lbl_username = new javax.swing.JLabel();
        menu_bar = new javax.swing.JMenuBar();
        menu_1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menu_2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1073, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
        );

        jToolBar1.setRollover(true);

        lbl_username.setText("xxxxxxxx");
        jToolBar1.add(lbl_username);

        menu_1.setText("Sistema");

        jMenuItem1.setText("Clientes");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menu_1.add(jMenuItem1);

        jMenuItem2.setText("Tipos de Servicios");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menu_1.add(jMenuItem2);

        jMenuItem3.setText("Servicios");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menu_1.add(jMenuItem3);

        menu_bar.add(menu_1);

        menu_2.setText("Reportes");

        jMenuItem4.setText("Por Tipo");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        menu_2.add(jMenuItem4);

        menu_bar.add(menu_2);

        setJMenuBar(menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(desktop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Frm_Listado_Caja ingreso = new Frm_Listado_Caja();
        ingreso.pack();
        desktop.add(ingreso);
        ingreso.setVisible(true);
        ViewUtil.centerScreen(desktop, ingreso);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        /*Frm_Servicio servicio = new Frm_Servicio();
        servicio.pack();
        desktop.add(servicio);
        servicio.setVisible(true);
        int x = (desktop.getWidth() / 2) - servicio.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - servicio.getHeight() /2;
        if (servicio.isShowing()) {
            servicio.setLocation(x, y);
        }*/
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        /*FrmTipoServicio frmTipoServicio = new FrmTipoServicio();
        frmTipoServicio.pack();
        desktop.add(frmTipoServicio);
        frmTipoServicio.setVisible(true);
        int x = (desktop.getWidth() / 2) - frmTipoServicio.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - frmTipoServicio.getHeight() /2;
        if (frmTipoServicio.isShowing()) {
            frmTipoServicio.setLocation(x, y);
        }*/
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        /*Frm_Main_Venta frm_Main_Venta = new Frm_Main_Venta();
        frm_Main_Venta.pack();
        frm_Main_Venta.setearUsuario(nombre, apellido, usuarioId);
        desktop.add(frm_Main_Venta);
        frm_Main_Venta.setVisible(true);
  
        int x = (desktop.getWidth() / 2) - frm_Main_Venta.getWidth() /2;
        int y = (desktop.getHeight()/ 2) - frm_Main_Venta.getHeight() /2;
        if (frm_Main_Venta.isShowing()) {
            frm_Main_Venta.setLocation(x, y);
        }*/
    }//GEN-LAST:event_jMenuItem4ActionPerformed

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
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu().setVisible(true);
            }
        });
    }

    /**
     * Configura el menú basado en los permisos del usuario logueado
     */
    private void configurarMenu() {

        // Limpiar menú existente
        menu_bar.removeAll();

        // Menú Archivo (siempre visible)
        menu_1.removeAll();
        menu_2.removeAll();

        // Verificar si hay sesión activa
        if (!session.sesionActiva()) {
            menu_bar.revalidate();
            menu_bar.repaint();
            return;
        }

        // <-- AÑADE ESTA LÍNEA para que aparezca Sistema -> Clientes
        configurarItemsMenuClientes();

        // Configurar menús basados en permisos
        configurarMenuTransacciones();
        configurarMenuReportes();
        //configurarMenuAdministracion();

        // Mostrar información del usuario en la barra de título
        setTitle("Sistema de Gestión - Usuario: " + session.getNombreUsuario()
                + " (" + session.getNombreRol() + ")");

        menu_bar.revalidate();
        menu_bar.repaint();

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menu_1.add(itemSalir);
        menu_bar.add(menu_1);
    }

    private void configurarMenuTransacciones() {
        if (session.tienePermiso("transacciones")) {
            configurarItemsMenuCaja();
            configurarItemsMenuIngresos();
            configurarItemsMenuEgresos();
            configurarItemsMenuReposiciones();
            //JMenuItem caja = new JMenuItem("Caja");
            //JMenuItem ingreso = new JMenuItem("Ingresos");
            //JMenuItem reposicion = new JMenuItem("Reposiciones");
            //caja.addActionListener(e -> abrirCaja());

            //JMenuItem egreso = new JMenuItem("Egresos");
            //itemHistorial.addActionListener(e -> abrirHistorialTransacciones());
            //menu_1.add(caja);
            //menu_1.add(ingreso);
            //menu_1.add(reposicion);
            //menu_1.add(egreso);
            menu_bar.add(menu_1);
        }
    }

    private void configurarMenuReportes() {
        if (session.tienePermiso("reportes")) {
            menu_2.removeAll();

            JMenuItem itemReporteGeneral = new JMenuItem("Reporte General");
            //itemReporteGeneral.addActionListener(e -> );

            menu_2.add(itemReporteGeneral);
            menu_bar.add(menu_2);
        }
    }

    private void configurarItemsMenuCaja() {
        JMenu caja = new JMenu("Caja");
        JMenuItem listadoCaja = new JMenuItem("Listado de Caja");
        JMenuItem aperturaCaja = new JMenuItem("Apertura de Caja");
        JMenuItem cierreCaja = new JMenuItem("Cierre de Caja");

        listadoCaja.addActionListener(e -> abrirCaja());
        aperturaCaja.addActionListener(e -> abrirAperturaCaja());
        cierreCaja.addActionListener(e -> abrirCierreCaja());

        caja.add(listadoCaja);
        caja.add(aperturaCaja);
        caja.add(cierreCaja);

        menu_1.add(caja);
    }

    private void configurarItemsMenuClientes() {
        JMenu clientes = new JMenu("Clientes");
        JMenuItem registroClientes = new JMenuItem("Registro Clientes");

        // Al hacer click abrir Frm_Cliente
        registroClientes.addActionListener(e -> abrirCliente());

        clientes.add(registroClientes);
        menu_1.add(clientes);
    }

    private void configurarItemsMenuIngresos() {
        JMenu ingresos = new JMenu("Ingresos");
        JMenuItem registroIngresos = new JMenuItem("Registro Ingresos");
        registroIngresos.addActionListener(e -> abrirIngreso());
        ingresos.add(registroIngresos);
        menu_1.add(ingresos);
    }

    private void configurarItemsMenuEgresos() {
        JMenu egresos = new JMenu("Egresos");

        menu_1.add(egresos);
    }

    private void configurarItemsMenuReposiciones() {
        JMenu reposiciones = new JMenu("Reposiciones");

        menu_1.add(reposiciones);
    }

    private void abrirCaja() {
        Frm_Listado_Caja frm_caja = new Frm_Listado_Caja();
        frm_caja.pack();
        desktop.add(frm_caja);
        frm_caja.setVisible(true);
        ViewUtil.centerScreen(desktop, frm_caja);
    }

    private void abrirIngreso() {
        Frm_Ingreso frm_ingreso = new Frm_Ingreso();
        frm_ingreso.pack();
        desktop.add(frm_ingreso);
        frm_ingreso.setVisible(true);
        ViewUtil.centerScreen(desktop, frm_ingreso);
    }

    private void abrirCliente() {
        // buscar si ya existe una instancia abierta de Frm_Cliente
        for (javax.swing.JInternalFrame f : desktop.getAllFrames()) {
            if (f instanceof Frm_Cliente) {
                try {
                    f.setIcon(false);
                    f.setSelected(true);
                    f.toFront();
                } catch (PropertyVetoException ex) {
                    ex.getCause();
                }
                return;
            }
        }

        // si no existe, la creamos
        Frm_Cliente frm = new Frm_Cliente();
        frm.pack();
        desktop.add(frm);
        frm.setVisible(true);
        ViewUtil.centerScreen(desktop, frm);
    }

    private void abrirAperturaCaja() {
        this.estadoCaja = cajaController.puedeAperturarSesion(session.getIdUsuario());
        if (this.estadoCaja) {
            JOptionPane.showMessageDialog(this, "La caja ya se encuentra aperturada!!!");
            return;
        }
        Frm_Apertura_Caja apertura_Caja = new Frm_Apertura_Caja();
        apertura_Caja.pack();
        desktop.add(apertura_Caja);
        apertura_Caja.setVisible(true);
        ViewUtil.centerScreen(desktop, apertura_Caja);
    }

    private void abrirCierreCaja() {
        this.estadoCaja = cajaController.puedeAperturarSesion(session.getIdUsuario());
        if (!this.estadoCaja) {
            JOptionPane.showMessageDialog(this, "La caja no se encuentra aperturada!!!");
            return;
        }

        Frm_Cierre_caja cierre_caja = new Frm_Cierre_caja();
        cierre_caja.pack();
        desktop.add(cierre_caja);
        cierre_caja.setVisible(true);
        ViewUtil.centerScreen(desktop, cierre_caja);
    }

    private void verificarSesionACtiva() {
        SesionCaja sesionCaja = this.cajaController.obtenerSesionActivaActual(session.getIdUsuario());
        if (sesionCaja == null) {
            System.out.println("Todo ok, no hay sesiones de caja activa");
        } else {
            if (session.getIdSesionCaja() == null) {
                session.setIdSesionCaja(sesionCaja.getIdSesion());
                System.out.println("Seteando ID sesion caja " + sesionCaja.getIdSesion());
            }
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbl_username;
    private javax.swing.JMenu menu_1;
    private javax.swing.JMenu menu_2;
    private javax.swing.JMenuBar menu_bar;
    // End of variables declaration//GEN-END:variables

}
