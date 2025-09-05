package vista;

import controlador.ClienteController;
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

public class Frm_Clientes extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Clientes.class.getName());
    private final ClienteController controller = new ClienteController();
    private final ClienteTableModel tableModel = new ClienteTableModel();

    public Frm_Clientes() {
        initComponents();

        // Mostrar X (cerrable) y título en la barra de la ventana interna
        this.setClosable(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Gestionar Clientes");

        // Asignamos el table model (reemplaza el DefaultModel generado por NetBeans)
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 1) Filtro que sólo permita letras/números/ - . / y espacios
        // 2) Placeholder "Nombre , DNI o RUC"
        // 3) Tooltip "Buscar Por Nombre , DNI o RUC"
        // 4) Focus color (efecto azul suave)
        DocumentFilters.attachAlphaNumSymbol(txtSearch, 100); // límite 100 caracteres
        UIHelpers.attachPlaceholder(txtSearch, " Nombre , DNI o RUC");
        UIHelpers.attachHintAndFocusColor(txtSearch, "Buscar Por Nombre , DNI o RUC");

        // Listeners para botones y búsqueda (Enter en txtSearch)
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        txtSearch.addActionListener(e -> onSearch()); // Enter en el campo realiza búsqueda

        // Doble clic en la tabla para editar
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                }
            }
        });

        // Cargar datos iniciales
        loadClients();
    }

    private void loadClients() {
        try {
            List<Cliente> lista = controller.listarClientes();
            tableModel.load(lista);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar clientes", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar clientes. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        // Abrir Frm_Cliente en modo "nuevo" y actualizar tabla cuando se cree
        Frm_Cliente frm = new Frm_Cliente(created -> {
            if (created != null) {
                tableModel.add(created);
                // opcional: seleccionar la fila nueva
                int r = tableModel.getRowCount() - 1;
                if (r >= 0) {
                    table.setRowSelectionInterval(r, r);
                }
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

        // Reusar Frm_Cliente en modo edición (constructor que recibe Cliente y callback)
        Frm_Cliente frm = new Frm_Cliente(c, updated -> {
            if (updated != null) {
                // refrescar tabla completa para mantener coherencia (sencillo y seguro)
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
            tableModel.removeAt(modelRow);
            JOptionPane.showMessageDialog(this, "Cliente Eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar cliente. Puede haber restricciones en la base.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch() {
        String raw = UIHelpers.getText(txtSearch);
        if (raw == null || raw.trim().isEmpty()) {
            loadClients();
            return;
        }
        String qRaw = raw.trim();          // usado para comparar con doc/ruc (numéricos)
        String qLower = qRaw.toLowerCase(); // usado para comparar nombres (insensible a mayúsculas)

        // Búsqueda en memoria (adecuada si la tabla no es gigantesca). Si tienes muchos clientes, cambia a búsqueda en BD.
        List<Cliente> all = controller.listarClientes();
        List<Cliente> filtered = new java.util.ArrayList<>();
        for (Cliente c : all) {
            boolean match = false;

            // Nombre (case-insensitive)
            if (c.getNombre_completo() != null && c.getNombre_completo().toLowerCase().contains(qLower)) {
                match = true;
            }

            // Doc identidad (comparación con qRaw - no toLower porque son dígitos)
            if (!match && c.getDoc_identidad() != null && c.getDoc_identidad().contains(qRaw)) {
                match = true;
            }

            // RUC
            if (!match && c.getRuc() != null && c.getRuc().contains(qRaw)) {
                match = true;
            }

            if (match) {
                filtered.add(c);
            }
        }
        tableModel.load(filtered);
    }

    private void showInternal(JInternalFrame frame) {
        // agrega el internal frame al JDesktopPane (si existe) o lo muestra como dialog modal
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
            // fallback: mostrar en diálogo modal
            JInternalFrame wrapper = frame;
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dlg = new JDialog(owner, frame.getTitle(), true);
            dlg.getContentPane().add(frame);
            // necesario para que el content sea visible
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gestion Clientes "));

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

        btnAdd.setText("Crear");

        btnEdit.setText("Editar");

        btnDelete.setText("Eliminar");

        jLabel1.setText("Buscar :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
