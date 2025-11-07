package vista;

import controlador.ClienteController;
import java.awt.BorderLayout;
import modelo.Cliente;
import vista.dataTableModel.ClienteTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import vista.components.PaginationPanel;
import vista.dataTableModel.PaginatedTableModel;

public class Frm_Clientes extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Clientes.class.getName());
    private final ClienteController controller = new ClienteController();
    private final ClienteTableModel tableModel = new ClienteTableModel();
    private PaginatedTableModel<Cliente> paginatedModel;
    private PaginationPanel paginationPanel;

    public Frm_Clientes() {

        initComponents();

        this.setClosable(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Gestionar Clientes");

        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DocumentFilters.attachAlphaNumSymbol(txtSearch, 100);
        UIHelpers.attachPlaceholder(txtSearch, " Nombre , DNI o RUC");
        UIHelpers.attachHintAndFocusColor(txtSearch, "Buscar Por Nombre , DNI o RUC");

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        txtSearch.addActionListener(e -> onSearch());

        // Doble clic en la tabla para editar
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                }
            }
        });

        // Paginación 
        paginatedModel = new PaginatedTableModel<>(
                tableModel,
                (model, data) -> ((ClienteTableModel) model).load(data),
                20
        );

        paginationPanel = new PaginationPanel();
        configurarPaginacion();
        pnlPaginacion.setLayout(new BorderLayout());
        pnlPaginacion.add(paginationPanel, BorderLayout.CENTER);

        loadClients();
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

    private void loadClients() {
        try {
            List<Cliente> lista = controller.listarClientes();
            paginatedModel.loadAllData(lista);
            paginationPanel.setInfo(paginatedModel.getPaginationInfo());
            paginationPanel.enableAll(!lista.isEmpty());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar clientes", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar clientes. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
            paginationPanel.setInfo("Sin datos");
            paginationPanel.enableAll(false);
        }
    }

    private void onAdd() {
        Frm_Cliente frm = new Frm_Cliente(creado -> {
            if (creado != null) {
                loadClients();
            }
        });
        showInternal(frm);
    }

    private void onEdit() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Cliente Para Editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        Cliente c = tableModel.getClienteAt(modelRow);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Frm_Cliente frm = new Frm_Cliente(c, updated -> {
            if (updated != null) {
                loadClients();
            }
        });
        showInternal(frm);
    }

    private void onDelete() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione Cliente Para Eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        Cliente c = tableModel.getClienteAt(modelRow);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this,
                "¿ Eliminar Cliente : " + c.getNombre_completo() + " ?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = controller.eliminarCliente(c.getId_cliente());
        if (ok) {
            loadClients();
            JOptionPane.showMessageDialog(this, "Registro Eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El cliente ya está siendo utilizado en otro proceso", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch() {
        String raw = UIHelpers.getText(txtSearch);
        if (raw == null || raw.trim().isEmpty()) {
            loadClients();
            return;
        }
        String qRaw = raw.trim();
        String qLower = qRaw.toLowerCase();

        List<Cliente> all = controller.listarClientes();
        List<Cliente> filtered = new java.util.ArrayList<>();
        for (Cliente c : all) {
            boolean match = false;

            if (c.getNombre_completo() != null && c.getNombre_completo().toLowerCase().contains(qLower)) {
                match = true;
            }

            if (!match && c.getDoc_identidad() != null && c.getDoc_identidad().contains(qRaw)) {
                match = true;
            }

            if (!match && c.getRuc() != null && c.getRuc().contains(qRaw)) {
                match = true;
            }

            if (match) {
                filtered.add(c);
            }
        }
        paginatedModel.loadAllData(filtered);
        paginationPanel.setInfo(paginatedModel.getPaginationInfo());
        paginationPanel.enableAll(!filtered.isEmpty());
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
            JInternalFrame wrapper = frame;
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

        jPanel1 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        pnlPaginacion = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gestion Clientes "));

        btnAdd.setText("Crear");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Editar");

        btnDelete.setText("Eliminar");

        jLabel1.setText("Buscar :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch)))
                .addGap(420, 420, 420))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addGap(10, 10, 10))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado Clientes "));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombres", "DNI / RUC ", "Dirección", "Teléfono"
            }
        ));
        jScrollPane1.setViewportView(table);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pnlPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlPaginacion;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
