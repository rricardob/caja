package vista;

import controlador.ClienteController;
import controlador.IngresoController;
import controlador.ReposicionController;
import dao.TipoTransaccionDAO;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.Cliente;
import modelo.Reposicion;
import modelo.TipoTransaccion;
import util.Constantes;
import util.DateUtil;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import util.validation.ValidationResult;

public class Frm_Reposicion_Agregar extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Reposicion_Agregar.class.getName());
    private final ReposicionController reposicionController;
    private final ClienteController clienteController;
    private final IngresoController ingresoController;

    public Frm_Reposicion_Agregar() {
        initComponents();
        this.reposicionController = new ReposicionController();
        this.clienteController = new ClienteController();
        this.ingresoController = new IngresoController();
        this.lbl_fecha.setText(DateUtil.obtenerFechaActual());
        aplicarEstilosInputs();
        cargarTiposTransaccionEnCombo();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_importe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_descripcion = new javax.swing.JTextArea();
        cbTipoTransaccion = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btn_aceptar = new javax.swing.JButton();

        setClosable(true);
        setTitle("Reposicion de Caja");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Reposicion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Descripcion:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Importe:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Fecha:");

        lbl_fecha.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lbl_fecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_fecha.setText("29/08/2025");
        lbl_fecha.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        txt_descripcion.setColumns(20);
        txt_descripcion.setLineWrap(true);
        txt_descripcion.setRows(5);
        jScrollPane1.setViewportView(txt_descripcion);

        jLabel4.setText("Tipo de Transaccion");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_importe, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                    .addComponent(cbTipoTransaccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbl_fecha))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbTipoTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        btn_aceptar.setText("Aceptar");
        btn_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_aceptar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_aceptar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptarActionPerformed

        // Validación centralizada a través del controller (homogeneidad)
        String importeStr = UIHelpers.getText(txt_importe).trim();
        String descripcion = UIHelpers.getText(txt_descripcion).trim();
        ValidationResult vr = this.reposicionController.validateReposicion(descripcion, importeStr);
        if (!vr.isOk()) {
            JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = this.clienteController.buscarPorNombre(Constantes.USUARIO_REPOSICION);
        if (Objects.isNull(cliente)) {
            JOptionPane.showMessageDialog(this, "No esta cargado el cliente de reposicion", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        String normalizedImporte = importeStr.replace(',', '.');
        BigDecimal importe = new BigDecimal(normalizedImporte);
        
        TipoTransaccion tipoSeleccionado = (TipoTransaccion) cbTipoTransaccion.getSelectedItem();
        if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de transacción.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
        int idTrans = this.ingresoController.guardarIngreso(cliente, importe, descripcion, tipoSeleccionado.getId_tipo());
        if (idTrans > 0) {
            //JOptionPane.showMessageDialog(this, "Ingreso registrado correctamente (ID: " + idTrans + ").", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txt_descripcion.setText("");
            txt_importe.setText("");
            UIHelpers.updatePlaceholderState(txt_descripcion);
            UIHelpers.updatePlaceholderState(txt_importe);

            LOGGER.log(Level.INFO, "Ingreso registrado ID: {0} clienteId: {1} importe: {2}", new Object[]{idTrans, cliente.getId_cliente(), importe});
            Reposicion reposicion = this.reposicionController.crearReposicion(idTrans);
            if (Objects.isNull(reposicion)) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la reposicion", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Reposicion registrado correctamente (ID: " + reposicion.getId_reposicionamiento() + ").", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        }

    }//GEN-LAST:event_btn_aceptarActionPerformed

    private void aplicarEstilosInputs() {
        UIHelpers.updatePlaceholderState(txt_descripcion);
        DocumentFilters.attachTextAreaLimit(txt_descripcion, 200);
        UIHelpers.attachHintAndFocusColor(txt_descripcion, "Descripción (5-200 caracteres).");
        UIHelpers.attachPlaceholder(txt_descripcion, "Motivo o Descripción");
        UIHelpers.updatePlaceholderState(txt_descripcion);

        UIHelpers.attachPlaceholder(txt_importe, "Importe");
        UIHelpers.attachHintAndFocusColor(txt_importe, "Importe (>= 1). Puede usar coma o punto decimal.");
        DocumentFilters.attachDecimal(txt_importe, 20);
        UIHelpers.updatePlaceholderState(txt_importe);
    }
    
    /**
     * Carga el combo cbTipoTransaccion con tipos activos de categorías INGRESO
     * y REPOSICION.
     */
    private void cargarTiposTransaccionEnCombo() {
        SwingWorker<List<TipoTransaccion>, Void> worker = new SwingWorker<List<TipoTransaccion>, Void>() {
            @Override
            protected List<TipoTransaccion> doInBackground() throws Exception {
                try {
                    TipoTransaccionDAO tipoDao = new TipoTransaccionDAO();
                    List<String> categorias = Arrays.asList("REPOSICION");
                    return tipoDao.listarPorCategorias(categorias);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al listar tipos por categoría: {0}", ex.toString());
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

                    if (lista.isEmpty()) {
                        LOGGER.log(Level.WARNING, "No hay tipos de transacción activos para INGRESO/REPOSICION");
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al poblar combo tipos: {0}", ex.toString());
                }
            }
        };
        worker.execute();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_aceptar;
    private javax.swing.JComboBox<TipoTransaccion> cbTipoTransaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JTextArea txt_descripcion;
    private javax.swing.JTextField txt_importe;
    // End of variables declaration//GEN-END:variables
}
