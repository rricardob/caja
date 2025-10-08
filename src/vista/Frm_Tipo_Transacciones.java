package vista;

import controlador.TipoTransaccionController;
import modelo.TipoTransaccion;
import modelo.CategoriaTransaccion;

import dao.CategoriaTransaccionDAO;

import vista.dataTableModel.TipoTransaccionTableModel;

import util.ui.DocumentFilters;
import util.ui.UIHelpers;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Frm_Tipo_Transacciones extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Tipo_Transacciones.class.getName());
    private final TipoTransaccionController controller = new TipoTransaccionController();
    private final TipoTransaccionTableModel tableModel = new TipoTransaccionTableModel();
    private final CategoriaTransaccionDAO catDao = new CategoriaTransaccionDAO();
    private Map<Long, String> categoriaMap = new HashMap<>();

    public Frm_Tipo_Transacciones() {
        initComponents();

        this.setClosable(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Gestionar Tipo Transacciones");

        table1.setModel(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DocumentFilters.attachAlphaNumSymbol(txtSearch1, 100);
        UIHelpers.attachPlaceholder(txtSearch1, " Categoría , Descripción o Estado");
        UIHelpers.attachHintAndFocusColor(txtSearch1, "Buscar Por Categoría , Descripción o Estado");

        btnAdd1.addActionListener(e -> onAdd());
        btnEdit1.addActionListener(e -> onEdit());
        btnDelete1.addActionListener(e -> onDelete());
        txtSearch1.addActionListener(e -> onSearch());

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                }
            }
        });

        loadCategorias();
        loadTipos();
    }

    private void loadCategorias() {
        try {
            List<CategoriaTransaccion> cats = catDao.listarTodos();
            Map<Long, String> map = new HashMap<>();
            for (CategoriaTransaccion c : cats) {
                map.put(c.getId_categoria_transacciones(), c.getDescripcion());
            }
            this.categoriaMap = map;
            tableModel.setCategorias(map);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar categorías", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar categorías. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTipos() {
        try {
            List<TipoTransaccion> lista = controller.listarTodos();
            tableModel.load(lista);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar registros", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de transacción. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        Frm_Crear_Tipo_Transacciones frm = new Frm_Crear_Tipo_Transacciones(created -> {
            if (created != null) {
                tableModel.add(created);
                int r = tableModel.getRowCount() - 1;
                if (r >= 0) {
                    table1.setRowSelectionInterval(r, r);
                }
            }
        });
        frm.setCategoriaMap(categoriaMap); // pasar mapa para mostrar nombres y preselección
        showInternal(frm);
    }

    private void onEdit() {
        int viewRow = table1.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Registro Para Editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table1.convertRowIndexToModel(viewRow);
        TipoTransaccion t = tableModel.getTipoAt(modelRow);
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Registro Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Frm_Crear_Tipo_Transacciones frm = new Frm_Crear_Tipo_Transacciones(t, updated -> {
            if (updated != null) {
                // recargar listado entero para coherencia
                loadTipos();
            }
        });
        frm.setCategoriaMap(categoriaMap);
        showInternal(frm);
    }

    private void onDelete() {
        int viewRow = table1.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Registro Para Eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table1.convertRowIndexToModel(viewRow);
        TipoTransaccion t = tableModel.getTipoAt(modelRow);
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Registro Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿ Eliminar Registro : " + t.getDescripcion() + " ?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        boolean ok = controller.eliminar(t.getId_tipo());
        if (ok) {
            tableModel.removeAt(modelRow);
            JOptionPane.showMessageDialog(this, "Registro Eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El registro ya está siendo utilizado en otro proceso", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch() {
        String raw = util.ui.UIHelpers.getText(txtSearch1);
        if (raw == null || raw.trim().isEmpty()) {
            loadTipos();
            return;
        }
        String qLower = raw.trim().toLowerCase();

        Boolean estadoQuery = null;
        if ("activo".equals(qLower)) {
            estadoQuery = Boolean.TRUE;
        } else if ("inactivo".equals(qLower)) {
            estadoQuery = Boolean.FALSE;
        }

        List<TipoTransaccion> all = controller.listarTodos();
        List<TipoTransaccion> filtered = new ArrayList<>();

        for (modelo.TipoTransaccion t : all) {
            if (estadoQuery != null) {

                if (Boolean.TRUE.equals(t.getEstado()) == estadoQuery) {
                    filtered.add(t);
                }
                continue;
            }

            boolean match = false;

            if (t.getDescripcion() != null && t.getDescripcion().toLowerCase().contains(qLower)) {
                match = true;
            }

            if (!match) {
                Long idCat = t.getId_categoria_transacciones();
                String cat = (idCat == null) ? "" : String.valueOf(categoriaMap.getOrDefault(idCat, ""));
                if (cat != null && cat.toLowerCase().contains(qLower)) {
                    match = true;
                }
            }

            if (!match) {
                String estTxt = Boolean.TRUE.equals(t.getEstado()) ? "activo" : "inactivo";
                if (estTxt.startsWith(qLower)) {
                    match = true;
                }
            }

            if (match) {
                filtered.add(t);
            }
        }
        tableModel.load(filtered);
    }

    private void showInternal(JInternalFrame frame) {
        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, this);
        if (desktop == null) {
            desktop = this.getDesktopPane();
        }
        if (desktop != null) {
            desktop.add(frame);
            frame.pack();
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                LOGGER.log(Level.WARNING, "setSelected fallo: {0}", ex.getMessage());
            }
            frame.toFront();
        } else {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dlg = new JDialog(owner, frame.getTitle(), true);
            dlg.getContentPane().add(frame);
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        btnAdd1 = new javax.swing.JButton();
        btnEdit1 = new javax.swing.JButton();
        btnDelete1 = new javax.swing.JButton();
        txtSearch1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Gestion Tipo Transacciones"));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Categoría ", "Descripción", "Estado"
            }
        ));
        jScrollPane2.setViewportView(table1);

        btnAdd1.setText("Crear");

        btnEdit1.setText("Editar");

        btnDelete1.setText("Eliminar");

        jLabel2.setText("Buscar :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSearch1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd1)
                    .addComponent(btnEdit1)
                    .addComponent(btnDelete1))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jPanel2.getAccessibleContext().setAccessibleName("Gestion Tipo Transacciones");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd1;
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnEdit1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table1;
    private javax.swing.JTextField txtSearch1;
    // End of variables declaration//GEN-END:variables
}
