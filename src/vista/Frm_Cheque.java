package vista;

import java.util.logging.Level;
import java.util.logging.Logger;
import controlador.ChequeController;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.Cheque;
import util.ui.UIHelpers;
import vista.dataTableModel.ChequeTableModel;
import java.util.Collections;
import com.toedter.calendar.JDateChooser;
import util.ui.DocumentFilters;
import javax.swing.text.JTextComponent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import vista.dataTableModel.PaginatedTableModel;
import vista.components.PaginationPanel;
import java.awt.BorderLayout;

public class Frm_Cheque extends javax.swing.JInternalFrame {

        private static final Logger LOGGER = Logger.getLogger(Frm_Cheque.class.getName());
        private final ChequeController controller;
        private ChequeTableModel chequeTableModel;
        private PaginatedTableModel<Cheque> paginatedModel;
        private PaginationPanel paginationPanel;

        public Frm_Cheque() {
                initComponents();
                this.controller = new ChequeController();
                this.setClosable(true);
                this.setTitle("Registro de Cheques");

                chequeTableModel = new ChequeTableModel();
                jTable1.setModel(chequeTableModel);

                // Inicializar paginación
                paginatedModel = new PaginatedTableModel<>(
                                chequeTableModel,
                                (model, data) -> ((ChequeTableModel) model).load(data),
                                20);
                paginationPanel = new PaginationPanel();
                configurarPaginacion();

                pnlPaginacion.setLayout(new BorderLayout());
                pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

                // Acción de doble clic para abrir edición
                jTable1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                                        onEdit();
                                }
                        }
                });

                dc_fecha_emision_cheque.setDate(null);
                UIHelpers.updatePlaceholderState(
                                (JTextComponent) dc_fecha_emision_cheque.getDateEditor().getUiComponent());

                setupValidations();
                cargarChequesEnTabla();
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
                                                                "Página inválida. Rango: 1-"
                                                                                + paginatedModel.getTotalPages(),
                                                                "Advertencia",
                                                                JOptionPane.WARNING_MESSAGE);
                                        }
                                        paginationPanel.clearGoTo();
                                        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                                }
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                });
        }

        private void onEdit() {
                int fila = jTable1.getSelectedRow();
                if (fila == -1) {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar un cheque de la tabla.", "Advertencia",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                Cheque cheque = chequeTableModel.getChequeAt(fila);
                Frm_Modificar_Cheque frmModificar = new Frm_Modificar_Cheque(cheque, (updated) -> {
                        cargarChequesEnTabla();
                });

                this.getDesktopPane().add(frmModificar);
                frmModificar.setVisible(true);
        }

        private void setupValidations() {
                // Validaciones de teclado (Filtros)
                DocumentFilters.attachDecimal(txtSaldoAnterior, 15);
                DocumentFilters.attachNumeric(txtNroCheque, 20);
                DocumentFilters.attachDecimal(txtTotalCheque, 15);

                // Tooltips (Efecto burbuja) y Color de Foco
                UIHelpers.attachHintAndFocusColor(txtSaldoAnterior,
                                "Ingrese el saldo anterior (Solo números y punto/coma)");
                UIHelpers.attachHintAndFocusColor(txtNroCheque, "Ingrese el número de cheque (Solo números)");
                UIHelpers.attachHintAndFocusColor(txtTotalCheque,
                                "Ingrese el importe total del cheque (Solo números y punto/coma)");

                // Placeholders
                UIHelpers.attachPlaceholder(txtSaldoAnterior, " Saldo Anterior");
                UIHelpers.attachPlaceholder(txtNroCheque, " Número de Cheque");
                UIHelpers.attachPlaceholder(txtTotalCheque, " Total Cheque");

                if (dc_fecha_emision_cheque.getDateEditor().getUiComponent() instanceof JTextComponent) {
                        JTextComponent editor = (JTextComponent) dc_fecha_emision_cheque.getDateEditor()
                                        .getUiComponent();
                        UIHelpers.attachHintAndFocusColor(editor, "Seleccione la fecha de emisión del cheque");
                        UIHelpers.attachPlaceholder(editor, " Fecha de Emisión");
                }
        }

        private void cargarChequesEnTabla() {
                SwingWorker<List<Cheque>, Void> worker = new SwingWorker<List<Cheque>, Void>() {
                        @Override
                        protected List<Cheque> doInBackground() throws Exception {
                                try {
                                        return controller.listarCheques();
                                } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, "Error al listar cheques: {0}", ex.toString());
                                        return Collections.emptyList();
                                }
                        }

                        @Override
                        protected void done() {
                                try {
                                        List<Cheque> lista = get();
                                        paginatedModel.loadAllData(lista);
                                        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                                        paginationPanel.enableAll(!lista.isEmpty());
                                } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, "Error al cargar datos en la tabla: {0}",
                                                        ex.toString());
                                        paginationPanel.setInfo("Sin datos");
                                        paginationPanel.enableAll(false);
                                }
                        }
                };
                worker.execute();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jSeparator1 = new javax.swing.JSeparator();
                jPanel1 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                pnlPaginacion = new javax.swing.JPanel();
                panel_registro_cheques = new javax.swing.JPanel();
                lblEmisionCheque = new javax.swing.JLabel();
                lblNroCheque = new javax.swing.JLabel();
                txtNroCheque = new javax.swing.JTextField();
                lblSaldoAnterior = new javax.swing.JLabel();
                txtSaldoAnterior = new javax.swing.JTextField();
                btnGuardar = new javax.swing.JButton();
                lblTotalCheque = new javax.swing.JLabel();
                btnEditar = new javax.swing.JButton();
                btnEliminar = new javax.swing.JButton();
                txtTotalCheque = new javax.swing.JTextField();
                dc_fecha_emision_cheque = new com.toedter.calendar.JDateChooser();

                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado De Registro - Cheques"));

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null }
                                },
                                new String[] {
                                                "ID ", "Saldo Anterior", "Fecha Emisión", "Nro Cheque", "Total Cheque"
                                }));
                jScrollPane1.setViewportView(jTable1);

                javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
                pnlPaginacion.setLayout(pnlPaginacionLayout);
                pnlPaginacionLayout.setHorizontalGroup(
                                pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                pnlPaginacionLayout.setVerticalGroup(
                                pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 40, Short.MAX_VALUE));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane1)
                                                                                .addComponent(pnlPaginacion,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(10, 10, 10)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                207,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(pnlPaginacion,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)));

                panel_registro_cheques
                                .setBorder(javax.swing.BorderFactory
                                                .createTitledBorder("Formulario De Registro - Cheques"));

                lblEmisionCheque.setText("Fecha Emisión Cheque : ");

                lblNroCheque.setText("Nro Cheque : ");

                lblSaldoAnterior.setText("Saldo Anterior :");

                btnGuardar.setText("GUARDAR");
                btnGuardar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnGuardarActionPerformed(evt);
                        }
                });

                lblTotalCheque.setText("Total Cheque : ");

                btnEditar.setText("EDITAR");
                btnEditar.setMaximumSize(new java.awt.Dimension(81, 23));
                btnEditar.setMinimumSize(new java.awt.Dimension(81, 23));
                btnEditar.setPreferredSize(new java.awt.Dimension(81, 23));
                btnEditar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnEditarActionPerformed(evt);
                        }
                });

                btnEliminar.setText("ELIMINAR");
                btnEliminar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnEliminarActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout panel_registro_chequesLayout = new javax.swing.GroupLayout(
                                panel_registro_cheques);
                panel_registro_cheques.setLayout(panel_registro_chequesLayout);
                panel_registro_chequesLayout.setHorizontalGroup(
                                panel_registro_chequesLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel_registro_chequesLayout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addGroup(panel_registro_chequesLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(btnGuardar,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(20, 20, 20)
                                                                                                .addComponent(btnEditar,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(20, 20, 20)
                                                                                                .addComponent(btnEliminar,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(lblTotalCheque)
                                                                                .addGroup(panel_registro_chequesLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(panel_registro_chequesLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblSaldoAnterior)
                                                                                                                .addComponent(lblEmisionCheque)
                                                                                                                .addComponent(lblNroCheque))
                                                                                                .addGap(10, 10, 10)
                                                                                                .addGroup(panel_registro_chequesLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(txtSaldoAnterior)
                                                                                                                .addComponent(dc_fecha_emision_cheque,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addGroup(panel_registro_chequesLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(txtTotalCheque,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                362,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                                                .addComponent(txtNroCheque))))
                                                                .addGap(278, 278, 278)));
                panel_registro_chequesLayout.setVerticalGroup(
                                panel_registro_chequesLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel_registro_chequesLayout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(lblSaldoAnterior)
                                                                                .addComponent(txtSaldoAnterior,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(lblEmisionCheque)
                                                                                .addComponent(dc_fecha_emision_cheque,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(lblNroCheque)
                                                                                .addComponent(txtNroCheque,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                20,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(lblTotalCheque)
                                                                                .addComponent(txtTotalCheque,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_registro_chequesLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnGuardar,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(btnEditar,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnEliminar))
                                                                .addGap(18, 18, 18)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(30, 30, 30)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(panel_registro_cheques,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(50, 50, 50)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(panel_registro_cheques,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuardarActionPerformed
                try {
                        // Usar UIHelpers.getText para manejar placeholders correctamente
                        String nroCheque = UIHelpers.getText(txtNroCheque).trim();
                        java.util.Date fechaEmision = dc_fecha_emision_cheque.getDate();
                        String saldoAnteriorStr = UIHelpers.getText(txtSaldoAnterior).trim();
                        String totalChequeStr = UIHelpers.getText(txtTotalCheque).trim();

                        // Validaciones manuales con mensajes descriptivos
                        if (saldoAnteriorStr.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "El campo 'Saldo Anterior' no puede estar vacío.",
                                                "Validación", JOptionPane.WARNING_MESSAGE);
                                txtSaldoAnterior.requestFocus();
                                return;
                        }
                        if (fechaEmision == null) {
                                JOptionPane.showMessageDialog(this, "Debe seleccionar una 'Fecha de Emisión'.",
                                                "Validación", JOptionPane.WARNING_MESSAGE);
                                dc_fecha_emision_cheque.requestFocus();
                                return;
                        }
                        if (nroCheque.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "El campo 'Número de Cheque' no puede estar vacío.",
                                                "Validación", JOptionPane.WARNING_MESSAGE);
                                txtNroCheque.requestFocus();
                                return;
                        }
                        if (totalChequeStr.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "El campo 'Total Cheque' no puede estar vacío.",
                                                "Validación", JOptionPane.WARNING_MESSAGE);
                                txtTotalCheque.requestFocus();
                                return;
                        }

                        int idCheque = controller.guardarCheque(nroCheque, fechaEmision, saldoAnteriorStr,
                                        totalChequeStr);

                        if (idCheque > 0) {
                                JOptionPane.showMessageDialog(this,
                                                "Cheque registrado correctamente (ID: " + idCheque + ").", "Éxito",
                                                JOptionPane.INFORMATION_MESSAGE);

                                // Limpiar campos y actualizar placeholders
                                txtNroCheque.setText("");
                                txtTotalCheque.setText("");
                                txtSaldoAnterior.setText("");
                                dc_fecha_emision_cheque.setDate(null);

                                UIHelpers.updatePlaceholderState(txtNroCheque);
                                UIHelpers.updatePlaceholderState(txtTotalCheque);
                                UIHelpers.updatePlaceholderState(txtSaldoAnterior);

                                if (dc_fecha_emision_cheque.getDateEditor()
                                                .getUiComponent() instanceof JTextComponent) {
                                        UIHelpers.updatePlaceholderState(
                                                        (JTextComponent) dc_fecha_emision_cheque.getDateEditor()
                                                                        .getUiComponent());
                                }

                                cargarChequesEnTabla();
                        } else {
                                JOptionPane.showMessageDialog(this, "Error al registrar el cheque.", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                } catch (IllegalArgumentException | IllegalStateException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación de Negocio",
                                        JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Excepción al guardar cheque: {0}", e.getMessage());
                        JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }// GEN-LAST:event_btnGuardarActionPerformed

        private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEditarActionPerformed
                onEdit();
        }// GEN-LAST:event_btnEditarActionPerformed

        private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEliminarActionPerformed
                int fila = jTable1.getSelectedRow();
                if (fila == -1) {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar un cheque de la tabla.", "Advertencia",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cheque?",
                                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                        Cheque cheque = chequeTableModel.getChequeAt(fila);
                        if (controller.eliminarCheque(cheque.getId_cheque())) {
                                JOptionPane.showMessageDialog(this, "Cheque eliminado correctamente.", "Éxito",
                                                JOptionPane.INFORMATION_MESSAGE);
                                cargarChequesEnTabla();
                        } else {
                                JOptionPane.showMessageDialog(this, "Error al eliminar el cheque.", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }// GEN-LAST:event_btnEliminarActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btnEditar;
        private javax.swing.JButton btnEliminar;
        private javax.swing.JButton btnGuardar;
        private com.toedter.calendar.JDateChooser dc_fecha_emision_cheque;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JTable jTable1;
        private javax.swing.JLabel lblEmisionCheque;
        private javax.swing.JLabel lblNroCheque;
        private javax.swing.JLabel lblSaldoAnterior;
        private javax.swing.JLabel lblTotalCheque;
        private javax.swing.JPanel panel_registro_cheques;
        private javax.swing.JPanel pnlPaginacion;
        private javax.swing.JTextField txtNroCheque;
        private javax.swing.JTextField txtSaldoAnterior;
        private javax.swing.JTextField txtTotalCheque;
        // End of variables declaration//GEN-END:variables

}
