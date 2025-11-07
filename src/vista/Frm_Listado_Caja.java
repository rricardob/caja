package vista;

import controlador.CajaController;
import dao.UsuarioDAO;
import java.awt.BorderLayout;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import modelo.SesionCaja;
import modelo.SessionManager;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.Constantes;
import util.DateUtil;
import vista.components.PaginationPanel;
import vista.dataTableModel.PaginatedTableModel;
import vista.dataTableModel.SesionCajaTableModel;

public class Frm_Listado_Caja extends javax.swing.JInternalFrame {

    private final CajaController cajaController;
    private final SessionManager session;
    private final UsuarioDAO usuarioDAO;
    private Date fechaInicio = Date.valueOf(LocalDate.now());
    private Date fechaFin = Date.valueOf(LocalDate.now());
    private static final Logger LOGGER = Logger.getLogger(Frm_Listado_Caja.class.getName());
    private SesionCajaTableModel tableModel;
    private PaginatedTableModel<SesionCaja> paginatedModel;
    private PaginationPanel paginationPanel;

    public Frm_Listado_Caja() {
        initComponents();
        this.cajaController = new CajaController();
        this.session = SessionManager.getInstance();
        this.usuarioDAO = new UsuarioDAO();

        tableModel = new SesionCajaTableModel(java.util.Collections.emptyList(), usuarioDAO);
        tb_sesiones_caja.setModel(tableModel);

        paginatedModel = new PaginatedTableModel<>(
                tableModel,
                (model, data) -> ((SesionCajaTableModel) model).load(data),
                20
        );
        paginationPanel = new PaginationPanel();
        configurarPaginacion();

        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        loadData(Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));

        this.dc_fecha_inicio.setDateFormatString("dd/MM/yyyy");
        this.dc_fecha_fin.setDateFormatString("dd/MM/yyyy");
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

        panel_filtro = new javax.swing.JPanel();
        lbl_fecha = new javax.swing.JLabel();
        btn_buscar = new javax.swing.JButton();
        dc_fecha_inicio = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        dc_fecha_fin = new com.toedter.calendar.JDateChooser();
        btn_reporte = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_sesiones_caja = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();

        setClosable(true);

        panel_filtro.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
        panel_filtro.setToolTipText("Filtro");

        lbl_fecha.setText("Fecha Inicio:");

        btn_buscar.setText("Buscar");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        jLabel1.setText("Fecha Fin:");

        btn_reporte.setText("Reporte");
        btn_reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_filtroLayout = new javax.swing.GroupLayout(panel_filtro);
        panel_filtro.setLayout(panel_filtroLayout);
        panel_filtroLayout.setHorizontalGroup(
            panel_filtroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_filtroLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lbl_fecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_buscar)
                .addGap(34, 34, 34)
                .addComponent(btn_reporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        panel_filtroLayout.setVerticalGroup(
            panel_filtroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_filtroLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panel_filtroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_filtroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_buscar)
                        .addComponent(btn_reporte))
                    .addComponent(dc_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fecha)
                    .addComponent(jLabel1)
                    .addComponent(dc_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Caja"));

        tb_sesiones_caja.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tb_sesiones_caja);

        pnlPaginacion.setPreferredSize(new java.awt.Dimension(0, 40));

        javax.swing.GroupLayout pnlPaginacionLayout = new javax.swing.GroupLayout(pnlPaginacion);
        pnlPaginacion.setLayout(pnlPaginacionLayout);
        pnlPaginacionLayout.setHorizontalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 778, Short.MAX_VALUE)
        );
        pnlPaginacionLayout.setVerticalGroup(
            pnlPaginacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1)
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_filtro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panel_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed

        if (dc_fecha_inicio.getDate() != null) {
            fechaInicio = new Date(dc_fecha_inicio.getDate().getTime());
        }
        if (dc_fecha_fin.getDate() != null) {
            fechaFin = new Date(dc_fecha_fin.getDate().getTime());
        }
        loadData(fechaInicio, fechaFin);

    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reporteActionPerformed

        reporte2();
        /*try {
            List<SesionCaja> sesionCajas = this.cajaController.obtenerHistorial(this.session.getIdUsuario(), fechaInicio, fechaFin);

            InputStream is = PrintService.class.getClassLoader().getResourceAsStream("rpt_ingreso_egreso.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("DATE",  fechaFin);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                    new JRBeanCollectionDataSource(sesionCajas));
            File outDir = new File("D:/myJasperReports");
            outDir.mkdirs();
            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/ricardo/Descargas/employeeReport.pdf");
            System.out.println("PDF report done!");

        } catch (JRException ex) {
            System.out.println("Error:\n" + ex.getLocalizedMessage());
        }*/

    }//GEN-LAST:event_btn_reporteActionPerformed

    public void reporte() {
        try {

            // Obtener el JRXML como un InputStream desde el classpath
            InputStream jrxmlStream = getClass().getResourceAsStream("/recursos/rpt_ingresos_egresos.jrxml");

            if (jrxmlStream == null) {
                System.err.println("Error: El archivo del reporte no se encontró en el classpath.");
                return;
            }

            // Compilar el reporte desde el InputStream
            JasperReport report = JasperCompileManager.compileReport(jrxmlStream);

            JasperCompileManager.compileReportToFile("src/recursos/rpt_ingresos_egresos.jrxml",
                    "src/recursos/rpt_ingresos_egresos.jasper");

            List<SesionCaja> sesionCajas = this.cajaController.obtenerHistorial(this.session.getIdUsuario(), fechaInicio, fechaFin);

            JRTableModelDataSource dataSource = new JRTableModelDataSource(new SesionCajaTableModel(sesionCajas, usuarioDAO));

            JasperPrint print = JasperFillManager.fillReport(report, null, dataSource);

            JasperViewer view1 = new JasperViewer(print, false);
            view1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view1.setVisible(true);

        } catch (JRException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void loadData(Date fechaInicio, Date fechaFin) {
        try {
            List<SesionCaja> sesionCajas = this.cajaController.obtenerHistorial(this.session.getIdUsuario(), fechaInicio, fechaFin);
            paginatedModel.loadAllData(sesionCajas);
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
            paginationPanel.enableAll(!sesionCajas.isEmpty());

            // Mantener estilos de tabla
            aplicarEstilosTabla();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar datos de caja", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar datos de caja. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
            paginationPanel.setInfo("Sin datos");
            paginationPanel.enableAll(false);
        }
    }

    private void aplicarEstilosTabla() {
        TableColumnModel columnModel = tb_sesiones_caja.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(140);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(70);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        columnModel.getColumn(4).setCellRenderer(rightRenderer);
        columnModel.getColumn(5).setCellRenderer(rightRenderer);

        tb_sesiones_caja.setAutoCreateRowSorter(true);
    }

    public void reporte2() {
        try {
            // Tu código para obtener datos y parámetros (sesionCajas, param)
            // ...
            List<SesionCaja> sesionCajas = this.cajaController.obtenerHistorial(this.session.getIdUsuario(), fechaInicio, fechaFin);
            if (sesionCajas == null || sesionCajas.size() == 0) {
                JOptionPane.showMessageDialog(this, "No Hay registros para generar el PDF");
            }

            Map<String, Object> parameters = new HashMap<String, Object>();
            String[] fechas = DateUtil.obtenerFechaFormateadaTituloReporte(fechaInicio, fechaFin);
            parameters.put("FECHA_INICIAL", fechas[0]);
            parameters.put("FECHA_FINAL", fechas[1]);
            parameters.put("RUTA_RECURSOS", Constantes.RUTA_RECURSOS);

            // Obtener el archivo JRXML como un InputStream
            InputStream jrxmlStream = this.getClass().getResourceAsStream("/recursos/rpt_ingresos_egresos.jrxml");

            if (jrxmlStream == null) {
                System.err.println("Error: El archivo del reporte JRXML no se encontró. Verifica la ruta.");
                return;
            }

            // Compilar el JRXML en un objeto JasperReport en memoria
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            //
            JREmptyDataSource dataSource = new JREmptyDataSource();

            // Llenar el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Mostrar el reporte
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            System.err.println("Error al generar el reporte: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_reporte;
    private com.toedter.calendar.JDateChooser dc_fecha_fin;
    private com.toedter.calendar.JDateChooser dc_fecha_inicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JPanel panel_filtro;
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTable tb_sesiones_caja;
    // End of variables declaration//GEN-END:variables
}
