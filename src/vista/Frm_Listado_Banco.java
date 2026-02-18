
package vista;

import controlador.RegistroBancoController;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import modelo.Banco;
import util.ViewUtil;
import vista.components.PaginationPanel;
import vista.dataTableModel.BancoTableModel;
import vista.dataTableModel.PaginatedTableModel;

public class Frm_Listado_Banco extends javax.swing.JInternalFrame {

    private final RegistroBancoController bancoController;
    private JDesktopPane desktop;
    private Banco bancoEdicion;
    private PaginatedTableModel<Banco> paginatedModel;
    private PaginationPanel paginationPanel;
    private BancoTableModel tableModel;

    public Frm_Listado_Banco() {
        initComponents();
        this.bancoController = new RegistroBancoController();
        
        tableModel = new BancoTableModel(java.util.Collections.emptyList());
        tbl_bancos.setModel(tableModel);

        paginatedModel = new PaginatedTableModel<>(
                tableModel,
                (model, data) -> ((BancoTableModel) model).load(data),
                20
        );
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        loadData("");

        desktop = ViewUtil.getDesktopPaneAncestor(this);
        bancoEdicion = new Banco();

        tbl_bancos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarBanco();
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

        btn_nuevo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txt_nombre_banco = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_editar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_bancos = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();

        setClosable(true);
        setResizable(true);

        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Banco"));

        txt_nombre_banco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nombre_bancoKeyPressed(evt);
            }
        });

        jLabel1.setText("Nombre de Banco");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_nombre_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre_banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(18, Short.MAX_VALUE))
        );

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado Bancos"));

        tbl_bancos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_bancos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bancosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_bancos);

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPaginacionLayout.setVerticalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 58, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_editar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_eliminar)
                        .addGap(0, 12, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_nuevo)
                            .addComponent(btn_editar)
                            .addComponent(btn_eliminar))
                        .addGap(31, 31, 31)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        Frm_Registro_Banco frm_Registro_Banco = new Frm_Registro_Banco("Guardar", this.bancoEdicion, created -> {
            if (created != null) {
                loadData("");
            }
        });
        cargarFormularioBanco(frm_Registro_Banco);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int filaSeleccionada = tbl_bancos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        eliminarBanco();
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void tbl_bancosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bancosMouseClicked
       int filaSeleccionada = tbl_bancos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object id = tbl_bancos.getValueAt(filaSeleccionada, 0);
        Object nombreBanco = tbl_bancos.getValueAt(filaSeleccionada, 1);
        Object estado = tbl_bancos.getValueAt(filaSeleccionada, 2);
        this.bancoEdicion.setId_banco(Integer.parseInt(id.toString()));
        this.bancoEdicion.setDescripcion(nombreBanco.toString());
        this.bancoEdicion.setEstado("Activo".equals(estado.toString()) ? 1 : 0);

        btn_editar.setEnabled(Boolean.TRUE);
        btn_eliminar.setEnabled(Boolean.TRUE);
    }//GEN-LAST:event_tbl_bancosMouseClicked

    private void txt_nombre_bancoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombre_bancoKeyPressed
        if (evt.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            loadData(txt_nombre_banco.getText());
        }
    }//GEN-LAST:event_txt_nombre_bancoKeyPressed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        editarBanco();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void cargarFormularioBanco(Frm_Registro_Banco frm_Registro_Banco) {

        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registro_Banco.pack();
            if (frm_Registro_Banco.getWidth() < 300 || frm_Registro_Banco.getHeight() < 100) {
                frm_Registro_Banco.setSize(420, 220);
            }
            desktop.add(frm_Registro_Banco);
            desktop.revalidate();
            desktop.repaint();
            frm_Registro_Banco.setVisible(true);
            desktop.moveToFront(frm_Registro_Banco);
            try {
                frm_Registro_Banco.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registro_Banco.toFront();
        }

    }
    
    private void loadData(String nombreCompleto) {
        List<Banco> bancos = this.bancoController.listarBanco(nombreCompleto);
        paginatedModel.loadAllData(bancos);
        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        paginationPanel.enableAll(!bancos.isEmpty());
        aplicarEstilosTabla();
    }
    
    private void aplicarEstilosTabla() {
        TableColumnModel columnModel = tbl_bancos.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(20);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);

        tbl_bancos.setAutoCreateRowSorter(true);

    }

    private void cargarFormularioUsuario(Frm_Registro_Banco frm_Registro_Banco) {

        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(
                javax.swing.JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }

        if (desktop != null) {
            frm_Registro_Banco.pack();
            if (frm_Registro_Banco.getWidth() < 300 || frm_Registro_Banco.getHeight() < 100) {
                frm_Registro_Banco.setSize(420, 220);
            }
            desktop.add(frm_Registro_Banco);
            desktop.revalidate();
            desktop.repaint();
            frm_Registro_Banco.setVisible(true);
            desktop.moveToFront(frm_Registro_Banco);
            try {
                frm_Registro_Banco.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                System.err.println("Error " + ex.getMessage());
            }
            frm_Registro_Banco.toFront();
        }

    }

    private void eliminarBanco() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el banco?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            if (this.bancoController.eliminarBanco(this.bancoEdicion.getId_banco())) {
                JOptionPane.showMessageDialog(this, "Banco Eliminado Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un problema al eliminar el banco", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }
        loadData("");
    }
    
    private void editarBanco() {
        Frm_Registro_Banco frm_Registro_Banco = new Frm_Registro_Banco("Editar", this.bancoEdicion, updated -> {
            if (updated != null) {
                loadData("");
            }
        });
        cargarFormularioUsuario(frm_Registro_Banco);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTable tbl_bancos;
    private javax.swing.JTextField txt_nombre_banco;
    // End of variables declaration//GEN-END:variables
}
