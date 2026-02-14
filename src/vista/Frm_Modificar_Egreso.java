package vista;

import controlador.EgresoController;
import dao.TipoTransaccionDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.SessionManager;
import modelo.TipoTransaccion;
import modelo.Transaccion;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;

public class Frm_Modificar_Egreso extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Modificar_Egreso.class.getName());
    private final EgresoController controller;
    private final SessionManager session;
    private Transaccion transaccionActual;
    private Consumer<Transaccion> onTransaccionUpdated;

    public Frm_Modificar_Egreso(Transaccion transaccion, Consumer<Transaccion> callback) {
        initComponents();
        this.controller = new EgresoController();
        this.session = SessionManager.getInstance();
        this.transaccionActual = transaccion;
        this.onTransaccionUpdated = callback;
        this.setClosable(true);
        this.setTitle("Modificar Egreso");
        configurarFiltros();
        cargarTiposTransaccionEnCombo();
        cargarDatosTransaccion();
    }

    private void configurarFiltros() {
        DocumentFilters.attachDecimal(txtImporte, 20);
        DocumentFilters.attachTextAreaLimit(txtDescripcionArea, 200);
        UIHelpers.attachHintAndFocusColor(txtImporte, "Importe ≥ 0.01. Puede usar coma o punto.");
        UIHelpers.attachHintAndFocusColor(txtDescripcionArea, "Descripción 5-200 caracteres.");
        UIHelpers.attachPlaceholder(txtImporte, " Importe");
        UIHelpers.attachPlaceholder(txtDescripcionArea, " Motivo o Descripción");
        UIHelpers.updatePlaceholderState(txtImporte);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
    }

    private void cargarTiposTransaccionEnCombo() {
        SwingWorker<List<TipoTransaccion>, Void> worker = new SwingWorker<List<TipoTransaccion>, Void>() {
            @Override
            protected List<TipoTransaccion> doInBackground() {
                try {
                    TipoTransaccionDAO tipoDao = new TipoTransaccionDAO();
                    List<String> categorias = java.util.Arrays.asList("EGRESO");
                    return tipoDao.listarPorCategorias(categorias);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar tipos por categoría", ex);
                    return java.util.Collections.emptyList();
                }
            }

            @Override
            protected void done() {
                try {
                    List<TipoTransaccion> lista = get();
                    DefaultComboBoxModel<TipoTransaccion> model = new DefaultComboBoxModel<>();
                    for (TipoTransaccion t : lista) {
                        model.addElement(t);
                    }
                    cbTipoTransaccion.setModel(model);

                    if (transaccionActual != null) {
                        for (int i = 0; i < model.getSize(); i++) {
                            TipoTransaccion tipo = model.getElementAt(i);
                            if ((int) tipo.getId_tipo() == transaccionActual.getId_tipo()) {
                                cbTipoTransaccion.setSelectedItem(tipo);
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al poblar combo tipos", ex);
                }
            }
        };
        worker.execute();
    }

    private void cargarDatosTransaccion() {
        if (transaccionActual == null)
            return;
        if (transaccionActual.getImporte() != null) {
            txtImporte.setText(transaccionActual.getImporte().toString());
        }
        if (transaccionActual.getDescripcion() != null) {
            txtDescripcionArea.setText(transaccionActual.getDescripcion());
        }
        UIHelpers.updatePlaceholderState(txtImporte);
        UIHelpers.updatePlaceholderState(txtDescripcionArea);
    }

    private void guardarCambios() {
        try {
            String importeStr = UIHelpers.getText(txtImporte).trim();
            String descripcion = UIHelpers.getText(txtDescripcionArea).trim();

            TipoTransaccion tipoSeleccionado = (TipoTransaccion) cbTipoTransaccion.getSelectedItem();
            if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal importe = new BigDecimal(importeStr.replace(",", "."));
            transaccionActual.setId_tipo((int) tipoSeleccionado.getId_tipo());
            transaccionActual.setImporte(importe);
            transaccionActual.setDescripcion(descripcion);
            transaccionActual.setId_usuario(session.getIdUsuario());

            boolean actualizado = controller.actualizarTransaccion(transaccionActual);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Egreso actualizado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                transaccionActual.setTipoDescripcion(tipoSeleccionado.getDescripcion());
                if (onTransaccionUpdated != null) {
                    onTransaccionUpdated.accept(transaccionActual);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtImporte = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbTipoTransaccion = new javax.swing.JComboBox<>();
        lblDescripcion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcionArea = new javax.swing.JTextArea();
        lblImporte = new javax.swing.JLabel();

        jButton1.setText("ACEPTAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("CANCELAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Elegir Tipo Transaccion :");

        cbTipoTransaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoTransaccionActionPerformed(evt);
            }
        });

        lblDescripcion.setText("Descripción :");

        txtDescripcionArea.setColumns(20);
        txtDescripcionArea.setRows(5);
        jScrollPane2.setViewportView(txtDescripcionArea);

        lblImporte.setText("Importe :  ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(lblDescripcion)
                                                        .addComponent(lblImporte))
                                                .addGap(20, 20, 20)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jScrollPane2,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 295,
                                                                Short.MAX_VALUE)
                                                        .addComponent(cbTipoTransaccion,
                                                                javax.swing.GroupLayout.Alignment.TRAILING, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtImporte))))
                                .addGap(35, 35, 35)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cbTipoTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addComponent(lblDescripcion)))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 20,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 17,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        guardarCambios();
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }// GEN-LAST:event_jButton2ActionPerformed

    private void cbTipoTransaccionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbTipoTransaccionActionPerformed

    }// GEN-LAST:event_cbTipoTransaccionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<TipoTransaccion> cbTipoTransaccion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblImporte;
    private javax.swing.JTextArea txtDescripcionArea;
    private javax.swing.JTextField txtImporte;
    // End of variables declaration//GEN-END:variables

}
