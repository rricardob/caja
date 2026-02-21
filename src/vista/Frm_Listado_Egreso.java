package vista;

import controlador.EgresoController;
import java.awt.BorderLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.SessionManager;
import modelo.Transaccion;
import modelo.TipoTransaccion;
import dao.TipoTransaccionDAO;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import vista.components.PaginationPanel;
import vista.dataTableModel.PaginatedTableModel;
import vista.dataTableModel.TransaccionTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Frm_Listado_Egreso extends javax.swing.JInternalFrame {

        private final EgresoController controller;
        private final SessionManager session;
        private final TransaccionTableModel tableModel;
        private final PaginatedTableModel<Transaccion> paginatedModel;
        private final PaginationPanel paginationPanel;
        private static final Logger LOGGER = Logger.getLogger(Frm_Listado_Egreso.class.getName());

        public Frm_Listado_Egreso() {
                initComponents();
                this.controller = new EgresoController();
                this.session = SessionManager.getInstance();

                // Configuración de JInternalFrame
                setTitle("Listado de Egresos");
                setClosable(true);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);

                // Configuración de Tabla y Paginación
                tableModel = new TransaccionTableModel();
                jTable1.setModel(tableModel);

                paginatedModel = new PaginatedTableModel<>(
                                tableModel,
                                (model, data) -> ((TransaccionTableModel) model).load(data),
                                20);

                paginationPanel = new PaginationPanel();
                configurarPaginacion();

                pnlPaginacion.setLayout(new BorderLayout());
                pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

                // Inicializar filtros y cargar datos
                this.dc_fecha_inicio.setDateFormatString("dd/MM/yyyy");
                this.dc_fecha_fin.setDateFormatString("dd/MM/yyyy");
                this.dc_fecha_inicio.setDate(java.sql.Date.valueOf(LocalDate.now()));
                this.dc_fecha_fin.setDate(java.sql.Date.valueOf(LocalDate.now()));

                setupFilterBehavior();
                setupTableAesthetics();
                cargarTiposTransaccion();
                loadData();
        }

        private void setupTableAesthetics() {
                // Renderizador para la columna Importe (índice 5 en TransaccionTableModel)
                jTable1.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
                        private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                        boolean isSelected, boolean hasFocus, int row, int column) {

                                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
                                                hasFocus, row,
                                                column);

                                if (value instanceof BigDecimal) {
                                        label.setText("- " + formatter.format(value));
                                }

                                label.setHorizontalAlignment(JLabel.RIGHT);

                                if (!isSelected) {
                                        label.setBackground(new Color(255, 235, 235)); // Rojo muy suave
                                        label.setForeground(new Color(150, 0, 0)); // Rojo oscuro
                                } else {
                                        label.setForeground(Color.WHITE);
                                }

                                return label;
                        }
                });
        }

        private void setupFilterBehavior() {
                // Filtros numéricos y decimales
                DocumentFilters.attachNumeric(txt_dni_ruc, 11);
                DocumentFilters.attachDecimal(txt_monto_min, 12);
                DocumentFilters.attachDecimal(txt_monto_max, 12);

                // Efectos de foco y hints
                UIHelpers.attachHintAndFocusColor(txt_dni_ruc, "DNI/RUC: Ingrese 8 u 11 dígitos");
                UIHelpers.attachHintAndFocusColor(txt_monto_min, "Monto mínimo (mayor a 0)");
                UIHelpers.attachHintAndFocusColor(txt_monto_max, "Monto máximo (mayor a monto mínimo)");

                // Placeholders
                UIHelpers.attachPlaceholder(txt_dni_ruc, " DNI / RUC");
                UIHelpers.attachPlaceholder(txt_monto_min, " Monto Min");
                UIHelpers.attachPlaceholder(txt_monto_max, " Monto Max");
        }

        private void cargarTiposTransaccion() {
                SwingWorker<List<TipoTransaccion>, Void> worker = new SwingWorker<List<TipoTransaccion>, Void>() {
                        @Override
                        protected List<TipoTransaccion> doInBackground() throws Exception {
                                try {
                                        TipoTransaccionDAO tipoDao = new TipoTransaccionDAO();
                                        List<String> categorias = Arrays.asList("EGRESO");
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
                                        // Opción nula para "Todos"
                                        TipoTransaccion todos = new TipoTransaccion();
                                        todos.setId_tipo(0);
                                        todos.setDescripcion("--- TODOS ---");
                                        model.addElement(todos);

                                        for (TipoTransaccion t : lista) {
                                                model.addElement(t);
                                        }
                                        cbx_tipo.setModel(model);
                                } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, "Error al poblar combo tipos", ex);
                                }
                        }
                };
                worker.execute();
        }

        private void configurarPaginacion() {
                paginationPanel.onFirst(e -> {
                        paginatedModel.firstPage();
                        actualizarInfoPaginacion();
                });
                paginationPanel.onPrev(e -> {
                        paginatedModel.previousPage();
                        actualizarInfoPaginacion();
                });
                paginationPanel.onNext(e -> {
                        paginatedModel.nextPage();
                        actualizarInfoPaginacion();
                });
                paginationPanel.onLast(e -> {
                        paginatedModel.lastPage();
                        actualizarInfoPaginacion();
                });
                paginationPanel.onPageSize(e -> {
                        paginatedModel.setPageSize(paginationPanel.getSelectedPageSize());
                        actualizarInfoPaginacion();
                });
                paginationPanel.onGoTo(e -> {
                        try {
                                int page = Integer.parseInt(paginationPanel.getGoToText());
                                if (!paginatedModel.goToPage(page)) {
                                        JOptionPane.showMessageDialog(this, "Página inválida.", "Aviso",
                                                        JOptionPane.WARNING_MESSAGE);
                                }
                                actualizarInfoPaginacion();
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Número inválido.", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                });
        }

        private void actualizarInfoPaginacion() {
                paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        }

        private void loadData() {
                try {
                        Date inicio = dc_fecha_inicio.getDate() != null ? new Date(dc_fecha_inicio.getDate().getTime())
                                        : null;
                        Date fin = dc_fecha_fin.getDate() != null ? new Date(dc_fecha_fin.getDate().getTime()) : null;
                        String dniRuc = UIHelpers.getText(txt_dni_ruc).trim();

                        // Validación de longitud DNI/RUC
                        if (!dniRuc.isEmpty()) {
                                if (dniRuc.length() != 8 && dniRuc.length() != 11) {
                                        JOptionPane.showMessageDialog(this,
                                                        "El DNI debe tener 8 dígitos y el RUC 11 dígitos.",
                                                        "Validación",
                                                        JOptionPane.WARNING_MESSAGE);
                                        return;
                                }
                        }

                        TipoTransaccion tipo = (TipoTransaccion) cbx_tipo.getSelectedItem();
                        Long idTipo = (tipo != null && tipo.getId_tipo() > 0) ? tipo.getId_tipo() : null;

                        BigDecimal montoMin = null;
                        if (!UIHelpers.getText(txt_monto_min).trim().isEmpty()) {
                                try {
                                        montoMin = new BigDecimal(
                                                        UIHelpers.getText(txt_monto_min).trim().replace(",", "."));
                                        if (montoMin.compareTo(BigDecimal.ZERO) < 0) {
                                                JOptionPane.showMessageDialog(this,
                                                                "El monto mínimo no puede ser negativo.", "Validación",
                                                                JOptionPane.WARNING_MESSAGE);
                                                return;
                                        }
                                } catch (NumberFormatException e) {
                                }
                        }

                        BigDecimal montoMax = null;
                        if (!UIHelpers.getText(txt_monto_max).trim().isEmpty()) {
                                try {
                                        montoMax = new BigDecimal(
                                                        UIHelpers.getText(txt_monto_max).trim().replace(",", "."));
                                        if (montoMax.compareTo(BigDecimal.ZERO) < 0) {
                                                JOptionPane.showMessageDialog(this,
                                                                "El monto máximo no puede ser negativo.", "Validación",
                                                                JOptionPane.WARNING_MESSAGE);
                                                return;
                                        }
                                } catch (NumberFormatException e) {
                                }
                        }

                        // Validación de rango de montos
                        if (montoMin != null && montoMax != null && montoMin.compareTo(montoMax) > 0) {
                                JOptionPane.showMessageDialog(this,
                                                "El monto mínimo no puede ser mayor al monto máximo.", "Validación",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        List<Transaccion> lista = controller.obtenerHistorialEgresos(inicio, fin, dniRuc, idTipo,
                                        montoMin,
                                        montoMax);
                        paginatedModel.loadAllData(lista);
                        actualizarInfoPaginacion();
                        paginationPanel.enableAll(!lista.isEmpty());
                } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error al cargar egresos", ex);
                        JOptionPane.showMessageDialog(this, "Error al cargar datos.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
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

                panel_filtro2 = new javax.swing.JPanel();
                lbl_fecha2 = new javax.swing.JLabel();
                btn_buscar2 = new javax.swing.JButton();
                dc_fecha_inicio = new com.toedter.calendar.JDateChooser();
                jLabel1 = new javax.swing.JLabel();
                dc_fecha_fin = new com.toedter.calendar.JDateChooser();
                btn_reporte = new javax.swing.JButton();
                jLabel2 = new javax.swing.JLabel();
                txt_dni_ruc = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                cbx_tipo = new javax.swing.JComboBox<>();
                jLabel4 = new javax.swing.JLabel();
                txt_monto_min = new javax.swing.JTextField();
                jLabel5 = new javax.swing.JLabel();
                txt_monto_max = new javax.swing.JTextField();
                jPanel1 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                pnlPaginacion = new javax.swing.JPanel();

                panel_filtro2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
                panel_filtro2.setToolTipText("Filtro");

                lbl_fecha2.setText("Fecha Inicio :");

                btn_buscar2.setText("Buscar");
                btn_buscar2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_buscar2ActionPerformed(evt);
                        }
                });

                jLabel1.setText("Fecha Fin :");

                btn_reporte.setText("Generar Reporte");
                btn_reporte.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btn_reporteActionPerformed(evt);
                        }
                });

                jLabel2.setText("DNI / RUC : ");

                jLabel3.setText("Tipo Egreso :");

                jLabel4.setText("Monto Desde : ");

                jLabel5.setText("Hasta ");

                javax.swing.GroupLayout panel_filtro2Layout = new javax.swing.GroupLayout(panel_filtro2);
                panel_filtro2.setLayout(panel_filtro2Layout);
                panel_filtro2Layout.setHorizontalGroup(
                                panel_filtro2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel_filtro2Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel4)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txt_monto_min,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                125,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lbl_fecha2)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(dc_fecha_inicio,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                121,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(28, 28, 28)
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel1)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(dc_fecha_fin,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                113,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel5)
                                                                                                .addGap(4, 4, 4)
                                                                                                .addComponent(txt_monto_max,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                125,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(28, 28, 28)
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btn_buscar2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                190,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel3)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(cbx_tipo,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                137,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(28, 28, 28)
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel2)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txt_dni_ruc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                123,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(btn_reporte,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                190,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(10, 10, 10)));
                panel_filtro2Layout.setVerticalGroup(
                                panel_filtro2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(panel_filtro2Layout.createSequentialGroup()
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(panel_filtro2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(15, 15, 15)
                                                                                                .addGroup(panel_filtro2Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                .addGroup(panel_filtro2Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                .addComponent(cbx_tipo,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addComponent(txt_dni_ruc,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addComponent(dc_fecha_inicio,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(lbl_fecha2)
                                                                                                                .addComponent(jLabel1)
                                                                                                                .addComponent(dc_fecha_fin,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jLabel3)))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                panel_filtro2Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addContainerGap()
                                                                                                                .addComponent(jLabel2)))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panel_filtro2Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btn_reporte)
                                                                                .addComponent(btn_buscar2)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(txt_monto_min,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel5)
                                                                                .addComponent(txt_monto_max,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(15, 15, 15)));

                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado De Registro - Egresos"));

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null },
                                                { null, null, null, null, null, null }
                                },
                                new String[] {
                                                "Sr.(es)", "DNI o RUC", "Dirección", "Descripción", "Importe ",
                                                "Fecha Registro"
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
                                                                                .addComponent(panel_filtro2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(40, 40, 40)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addComponent(panel_filtro2,
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

        private void btn_buscar2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_buscar2ActionPerformed
                loadData();
        }// GEN-LAST:event_btn_buscar2ActionPerformed

        private void btn_reporteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_reporteActionPerformed
                try {
                        // 1. Obtener los datos actuales de la tabla (ya filtrados y paginados en el UI)
                        List<Transaccion> listaDatos = tableModel.getData();

                        if (listaDatos.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "No hay datos para generar el reporte.", "Aviso",
                                                JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }

                        // 2. Preparar el DataSource
                        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaDatos);

                        // 3. Parámetros
                        Map<String, Object> parameters = new HashMap<>();
                        String fechaDesde = dc_fecha_inicio.getDate() != null
                                        ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(dc_fecha_inicio.getDate())
                                        : "N/A";
                        String fechaHasta = dc_fecha_fin.getDate() != null
                                        ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(dc_fecha_fin.getDate())
                                        : "N/A";

                        parameters.put("FECHA_INICIO", fechaDesde);
                        parameters.put("FECHA_FIN", fechaHasta);

                        // 4. Cargar el reporte compilado (.jasper)
                        InputStream reportStream = getClass().getResourceAsStream("/recursos/rpt_egresos.jasper");

                        if (reportStream == null) {
                                JOptionPane.showMessageDialog(this,
                                                "No se encontró el archivo del reporte (rpt_egresos.jasper) en los recursos.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                        }

                        // 5. Llenar el reporte
                        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, ds);

                        // 6. Mostrar el reporte
                        JasperViewer viewer = new JasperViewer(jasperPrint, false);
                        viewer.setTitle("Visualizador de Reportes - Egresos");
                        viewer.setVisible(true);

                } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error al generar el reporte de egresos", ex);
                        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }// GEN-LAST:event_btn_reporteActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btn_buscar2;
        private javax.swing.JButton btn_reporte;
        private javax.swing.JComboBox<TipoTransaccion> cbx_tipo;
        private com.toedter.calendar.JDateChooser dc_fecha_fin;
        private com.toedter.calendar.JDateChooser dc_fecha_inicio;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JLabel lbl_fecha2;
        private javax.swing.JPanel panel_filtro2;
        private javax.swing.JPanel pnlPaginacion;
        private javax.swing.JTextField txt_dni_ruc;
        private javax.swing.JTextField txt_monto_max;
        private javax.swing.JTextField txt_monto_min;
        // End of variables declaration//GEN-END:variables
}
