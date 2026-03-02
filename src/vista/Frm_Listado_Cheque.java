/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

/**
 *
 * @author AMD Ryzen
 */
import controlador.ChequeController;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.sql.Date;
import modelo.Cheque;
import util.ui.UIHelpers;
import util.ui.DocumentFilters;
import vista.dataTableModel.ChequeTableModel;
import vista.dataTableModel.PaginatedTableModel;
import vista.components.PaginationPanel;
import javax.swing.text.JTextComponent;

public class Frm_Listado_Cheque extends javax.swing.JInternalFrame {

        private static final Logger LOGGER = Logger.getLogger(Frm_Listado_Cheque.class.getName());
        private final ChequeController controller;
        private ChequeTableModel chequeTableModel;
        private PaginatedTableModel<Cheque> paginatedModel;
        private PaginationPanel paginationPanel;

        /**
         * Creates new form Frm_Listado_Cheque
         */
        public Frm_Listado_Cheque() {
                initComponents();
                this.controller = new ChequeController();
                this.setClosable(true);
                this.setTitle("Listado de Cheques");

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

                // Configuración inicial de filtros
                dc_fecha_inicio.setDateFormatString("dd/MM/yyyy");
                dc_fecha_fin.setDateFormatString("dd/MM/yyyy");
                dc_fecha_inicio.setDate(new java.util.Date());
                dc_fecha_fin.setDate(new java.util.Date());

                setupFilterBehavior();
                loadData();
        }

        private void loadData() {
                java.util.Date inicio = dc_fecha_inicio.getDate();
                java.util.Date fin = dc_fecha_fin.getDate();
                String nroCheque = UIHelpers.getText(txtNroCheque).trim();

                // Validación General: Rango de fechas coherente
                if (inicio != null && fin != null && inicio.after(fin)) {
                        JOptionPane.showMessageDialog(this,
                                        "La fecha de inicio no puede ser posterior a la fecha fin.",
                                        "Validación de Fechas",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                SwingWorker<List<Cheque>, Void> worker = new SwingWorker<List<Cheque>, Void>() {
                        @Override
                        protected List<Cheque> doInBackground() throws Exception {
                                try {
                                        return controller.listarCheques(inicio, fin, nroCheque);
                                } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, "Error al filtrar cheques: {0}", ex.toString());
                                        return java.util.Collections.emptyList();
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

        private void setupFilterBehavior() {
                // Validación de Teclado (Obligatorio)
                DocumentFilters.attachNumeric(txtNroCheque, 20);

                // Tooltips (Obligatorio)
                txtNroCheque.setToolTipText("Ingrese el número de cheque a buscar (solo números)");
                dc_fecha_inicio.setToolTipText("Fecha mínima de emisión");
                dc_fecha_fin.setToolTipText("Fecha máxima de emisión");
                btnBuscar.setToolTipText("Ejecutar búsqueda con los filtros aplicados");

                // Hints y placeholders (Obligatorio)
                UIHelpers.attachHintAndFocusColor(txtNroCheque, "Solo números");
                UIHelpers.attachPlaceholder(txtNroCheque, " Número de Cheque");

                // No usar placeholders en filtros de fecha
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
                        paginatedModel.setPageSize(paginationPanel.getSelectedPageSize());
                        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                });
                paginationPanel.onGoTo(e -> {
                        try {
                                int page = Integer.parseInt(paginationPanel.getGoToText());
                                if (!paginatedModel.goToPage(page)) {
                                        JOptionPane.showMessageDialog(this, "Página inválida.", "Aviso",
                                                        JOptionPane.WARNING_MESSAGE);
                                }
                                paginationPanel.setInfo(paginatedModel.getPaginationInfo());
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Número inválido.", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                });
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                pnlPaginacion = new javax.swing.JPanel();
                panelFiltro = new javax.swing.JPanel();
                lblFechaInicio = new javax.swing.JLabel();
                btnBuscar = new javax.swing.JButton();
                dc_fecha_inicio = new com.toedter.calendar.JDateChooser();
                lblFechaFin = new javax.swing.JLabel();
                dc_fecha_fin = new com.toedter.calendar.JDateChooser();
                lblNroCheque = new javax.swing.JLabel();
                txtNroCheque = new javax.swing.JTextField();

                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado De Cheques"));

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null }
                                },
                                new String[] {
                                                "ID", "Saldo Anterior", "Fecha Emisión", "Nro Cheque", "Total Cheque"
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

                panelFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
                panelFiltro.setToolTipText("Filtro");

                lblFechaInicio.setText("Fecha Inicio :");

                btnBuscar.setText("Buscar");
                btnBuscar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnBuscarActionPerformed(evt);
                        }
                });

                lblFechaFin.setText("Fecha Fin :");

                lblNroCheque.setText("Nro Cheque : ");

                javax.swing.GroupLayout panelFiltroLayout = new javax.swing.GroupLayout(panelFiltro);
                panelFiltro.setLayout(panelFiltroLayout);
                panelFiltroLayout.setHorizontalGroup(
                                panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panelFiltroLayout.createSequentialGroup()
                                                                .addGroup(panelFiltroLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(panelFiltroLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(10, 10, 10)
                                                                                                .addComponent(btnBuscar,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                190,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(panelFiltroLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(10, 10, 10)
                                                                                                .addComponent(lblFechaInicio)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(dc_fecha_inicio,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                121,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(40, 40, 40)
                                                                                                .addComponent(lblFechaFin)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(dc_fecha_fin,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                113,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(40, 40, 40)
                                                                                                .addComponent(lblNroCheque)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txtNroCheque,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                175,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(198, Short.MAX_VALUE)));
                panelFiltroLayout.setVerticalGroup(
                                panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panelFiltroLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addGroup(panelFiltroLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(txtNroCheque,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(dc_fecha_inicio,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblFechaInicio)
                                                                                .addComponent(lblFechaFin)
                                                                                .addComponent(dc_fecha_fin,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblNroCheque))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnBuscar,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                27,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(panelFiltro,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(40, 40, 40)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addComponent(panelFiltro,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(40, 40, 40)));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
                loadData();
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btnBuscar;
        private com.toedter.calendar.JDateChooser dc_fecha_fin;
        private com.toedter.calendar.JDateChooser dc_fecha_inicio;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JLabel lblFechaFin;
        private javax.swing.JLabel lblFechaInicio;
        private javax.swing.JLabel lblNroCheque;
        private javax.swing.JPanel panelFiltro;
        private javax.swing.JPanel pnlPaginacion;
        private javax.swing.JTextField txtNroCheque;
        // End of variables declaration//GEN-END:variables
}
