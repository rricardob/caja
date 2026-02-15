package vista;

import controlador.CajaController;
import modelo.SesionCaja;
import modelo.SessionManager;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
        menu_3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
                desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1073, Short.MAX_VALUE));
        desktopLayout.setVerticalGroup(
                desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 728, Short.MAX_VALUE));

        jToolBar1.setRollover(true);

        lbl_username.setText("xxxxxxxx");
        jToolBar1.add(lbl_username);

        menu_1.setText("Mantenimiento");

        jMenuItem1.setText("Clientes");
        menu_1.add(jMenuItem1);

        jMenuItem2.setText("Tipos de Servicios");
        menu_1.add(jMenuItem2);

        jMenuItem3.setText("Servicios");
        menu_1.add(jMenuItem3);

        menu_bar.add(menu_1);

        menu_2.setText("Reportes");

        jMenuItem4.setText("Por Tipo");
        menu_2.add(jMenuItem4);

        menu_bar.add(menu_2);

        menu_3.setText("Operaciones");
        menu_bar.add(menu_3);

        setJMenuBar(menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(desktop)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(desktop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
        // </editor-fold>
        // </editor-fold>

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
        menu_3.removeAll();

        // Verificar si hay sesión activa
        if (!session.sesionActiva()) {
            menu_bar.revalidate();
            menu_bar.repaint();
            return;
        }

        // Configurar menús basados en permisos
        configurarMenuTransacciones();
        configurarMenuReportes();

        // Mostrar información del usuario en la barra de título
        setTitle("Sistema de Gestión - Usuario: " + session.getNombreUsuario() + " (" + session.getNombreRol() + ")");

        menu_bar.revalidate();
        menu_bar.repaint();
        // menu_bar.add(menu_1);
    }

    private void configurarMenuTransacciones() {
        if (session.tienePermiso("transacciones")) {
            configurarItemsMenuIngresos();
            configurarItemsMenuEgresos();
            configurarItemsMenuReposiciones();
            configurarItemsMenuClientes();
            configurarItemsMenuCaja();
            configurarItemsMenuTipoTransacciones();
            configurarMenuUsuarios();
            menu_bar.add(menu_1);
            menu_bar.add(menu_3);
        }
    }

    private void configurarItemsMenuTipoTransacciones() {
        javax.swing.JMenuItem gestionarTipos = new javax.swing.JMenuItem("Gestionar Tipo Transacciones");
        gestionarTipos.addActionListener(e -> abrirGestionTipoTransacciones());
        menu_1.add(gestionarTipos);
    }

    private void abrirGestionTipoTransacciones() {
        for (javax.swing.JInternalFrame f : desktop.getAllFrames()) {
            if (f instanceof Frm_Tipo_Transacciones) {
                try {
                    f.setIcon(false);
                    f.setSelected(true);
                    f.toFront();
                } catch (java.beans.PropertyVetoException ex) {
                    System.err.println("No se pudo seleccionar Frm_Tipo_Transacciones: " + ex.getMessage());
                }
                return;
            }
        }
        Frm_Tipo_Transacciones frm = new Frm_Tipo_Transacciones();
        frm.pack();
        desktop.add(frm);
        frm.setVisible(true);
        util.ViewUtil.centerScreen(desktop, frm);
    }

    private void configurarMenuUsuarios() {
        JMenuItem listadoUsuarios = new JMenuItem("Listado de Usuarios");

        listadoUsuarios.addActionListener(e -> abrirUsuario());

        menu_3.add(listadoUsuarios);
    }

    private void ConfigurarMenuItemSalir(JMenu menu) {
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menu.add(itemSalir);
    }

    private void configurarMenuReportes() {
        if (session.tienePermiso("reportes")) {
            menu_2.removeAll();

            JMenuItem itemReporteGeneral = new JMenuItem("Reporte General");
            ConfigurarMenuItemSalir(menu_2);
            // itemReporteGeneral.addActionListener(e -> );

            menu_2.add(itemReporteGeneral);
            menu_bar.add(menu_2);
        }
    }

    private void configurarItemsMenuCaja() {
        JMenuItem listadoCaja = new JMenuItem("Listado de Caja");
        JMenuItem aperturaCaja = new JMenuItem("Apertura de Caja");
        JMenuItem cierreCaja = new JMenuItem("Cierre de Caja");

        listadoCaja.addActionListener(e -> abrirCaja());
        aperturaCaja.addActionListener(e -> abrirAperturaCaja());
        cierreCaja.addActionListener(e -> abrirCierreCaja());

        menu_3.add(listadoCaja);
        menu_3.add(aperturaCaja);
        menu_3.add(cierreCaja);

    }

    private void configurarItemsMenuClientes() {
        JMenuItem gestionarEstudiantes = new JMenuItem("Gestionar Clientes");
        gestionarEstudiantes.addActionListener(e -> abrirGestionClientes());
        menu_1.add(gestionarEstudiantes);
    }

    private void configurarItemsMenuIngresos() {
        JMenuItem registroIngresos = new JMenuItem("Registro Ingresos");
        registroIngresos.addActionListener(e -> abrirIngreso());
        menu_3.add(registroIngresos);

        JMenuItem listadoIngresos = new JMenuItem("Listado de Ingresos");
        listadoIngresos.addActionListener(e -> abrirListadoIngresos());
        menu_3.add(listadoIngresos);
    }

    private void configurarItemsMenuEgresos() {
        JMenu egresos = new JMenu("Egresos");

        // menu_1.add(egresos);
        JMenuItem registroEgresos = new JMenuItem("Registro Egresos");
        registroEgresos.addActionListener(e -> abrirEgreso());
        menu_3.add(registroEgresos);
    }

    private void configurarItemsMenuReposiciones() {
        JMenu reposiciones = new JMenu("Reposiciones");
        JMenuItem registroReposicion = new JMenuItem("Registro Reposicion");
        registroReposicion.addActionListener(e -> abrirReposicionAgregar());
        JMenuItem listadoReposicion = new JMenuItem("Listado Reposicion");
        listadoReposicion.addActionListener(e -> abrirReposicionListado());
        reposiciones.add(registroReposicion);
        reposiciones.add(listadoReposicion);
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

    private void abrirListadoIngresos() {
        // Verificar si ya está abierto
        for (javax.swing.JInternalFrame f : desktop.getAllFrames()) {
            if (f instanceof Frm_Listado_Ingreso) {
                try {
                    f.setIcon(false);
                    f.setSelected(true);
                    f.toFront();
                } catch (java.beans.PropertyVetoException ex) {
                    System.err.println("Error al enfocar Frm_Listado_Ingreso: " + ex.getMessage());
                }
                return;
            }
        }
        Frm_Listado_Ingreso frm = new Frm_Listado_Ingreso();
        frm.pack();
        desktop.add(frm);
        frm.setVisible(true);
        ViewUtil.centerScreen(desktop, frm);
    }

    private void abrirEgreso() {
        Frm_Egresos frm_egreso = new Frm_Egresos();
        frm_egreso.pack();
        desktop.add(frm_egreso);
        frm_egreso.setVisible(true);
        ViewUtil.centerScreen(desktop, frm_egreso);
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

    private void abrirGestionClientes() {
        // buscamos si ya existe instancia abierta
        for (javax.swing.JInternalFrame f : desktop.getAllFrames()) {
            if (f instanceof Frm_Clientes) {
                try {
                    f.setIcon(false);
                    f.setSelected(true);
                    f.toFront();
                } catch (PropertyVetoException ex) {
                    // no hacemos nada crítico, solo logueamos
                    System.err.println("No se pudo seleccionar Frm_Clientes: " + ex.getMessage());
                }
                return;
            }
        }

        // si no existe, la creamos y la mostramos
        Frm_Clientes frm = new Frm_Clientes();
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

    private void abrirReposicionAgregar() {
        this.estadoCaja = cajaController.puedeAperturarSesion(session.getIdUsuario());
        if (!this.estadoCaja) {
            JOptionPane.showMessageDialog(this, "La caja no se encuentra aperturada!!!");
            return;
        }
        Frm_Reposicion_Agregar reposicion = new Frm_Reposicion_Agregar();
        reposicion.pack();
        desktop.add(reposicion);
        reposicion.setVisible(true);
        ViewUtil.centerScreen(desktop, reposicion);
    }

    private void abrirReposicionListado() {
        Frm_Listado_Reposicion frm_Listado_Reposicion = new Frm_Listado_Reposicion();
        frm_Listado_Reposicion.pack();
        desktop.add(frm_Listado_Reposicion);
        frm_Listado_Reposicion.setVisible(true);
        ViewUtil.centerScreen(desktop, frm_Listado_Reposicion);
    }

    private void abrirUsuario() {
        Frm_Listado_Usuarios frm_usuarios = new Frm_Listado_Usuarios();
        frm_usuarios.pack();
        desktop.add(frm_usuarios);
        frm_usuarios.setVisible(true);
        ViewUtil.centerScreen(desktop, frm_usuarios);
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
    private javax.swing.JMenu menu_3;
    private javax.swing.JMenuBar menu_bar;
    // End of variables declaration//GEN-END:variables

}
