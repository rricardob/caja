package vista;

import controlador.TipoTransaccionController;

import modelo.TipoTransaccion;
import modelo.CategoriaTransaccion;

import dao.CategoriaTransaccionDAO;

import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import util.validation.ValidationResult;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Frm_Crear_Tipo_Transacciones extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Crear_Tipo_Transacciones.class.getName());

    private final TipoTransaccionController controller = new TipoTransaccionController();
    private final CategoriaTransaccionDAO catDao = new CategoriaTransaccionDAO();

    private Consumer<TipoTransaccion> onSaved;
    private boolean editMode = false;
    private TipoTransaccion editing = null;

    private Map<Long, String> categoriaMap = new HashMap<>();

    private void setupFieldBehavior() {
        // Límite de 100 chars en descripción; validación exacta la hará el Validator
        DocumentFilters.attachTextAreaLimit(jTextArea1, 100);
        DocumentFilters.attachAlphaNumSymbol(jTextArea1, 100);
        UIHelpers.attachHintAndFocusColor(jTextArea1, "Descripción (5-100 caracteres, letras, números, espacios y - . /)");
        UIHelpers.attachPlaceholder(jTextArea1, " Descripción");
        chkActivo.setSelected(true); // por defecto activo
        setupEstadoCheckBoxes();
        applyEstadoMode();
    }

    public Frm_Crear_Tipo_Transacciones() {
        initComponents();
        configureFrame();
        setupFieldBehavior();
        loadCategorias();
    }

    public Frm_Crear_Tipo_Transacciones(Consumer<TipoTransaccion> onSaved) {
        this();
        this.onSaved = onSaved;
    }

    public Frm_Crear_Tipo_Transacciones(TipoTransaccion existing, Consumer<TipoTransaccion> onSaved) {
        this(onSaved);
        if (existing != null) {
            this.editMode = true;
            this.editing = existing;
            populateForEdit(existing);
            applyEstadoMode();
        }
    }

    public void setOnSaved(Consumer<TipoTransaccion> onSaved) {
        this.onSaved = onSaved;
    }

    public void setCategoriaMap(Map<Long, String> map) {
        this.categoriaMap = (map == null) ? new HashMap<>() : new HashMap<>(map);
    }

    private void configureFrame() {
        this.setClosable(true);
        this.setResizable(false);
        this.setTitle(editMode ? "Modificar Tipo Transacción" : "Crear Tipo Transacción");
    }

    private void setupEstadoCheckBoxes() {
        chkActivo.addActionListener(e -> {
            if (chkActivo.isSelected()) {
                chkInactivo.setSelected(false);
            }
        });
        chkInactivo.addActionListener(e -> {
            if (chkInactivo.isSelected()) {
                chkActivo.setSelected(false);
            }
        });
    }

    private void applyEstadoMode() {
        if (!editMode) {
            // Creación: Activo fijo y no interactivo
            chkActivo.setSelected(true);
            chkActivo.setEnabled(false);
            chkInactivo.setSelected(false);
            chkInactivo.setEnabled(false);
        } else {
            // Edición: ambos habilitados
            chkActivo.setEnabled(true);
            chkInactivo.setEnabled(true);
            boolean active = (editing != null) && Boolean.TRUE.equals(editing.getEstado());
            chkActivo.setSelected(active);
            chkInactivo.setSelected(!active);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cbTipo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        chkActivo = new javax.swing.JCheckBox();
        chkInactivo = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();

        jLabel6.setText("Elige Categoria :");

        jLabel1.setText("Descripción :");

        btnAceptar.setText("ACEPTAR");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        chkActivo.setText("Activo ");

        chkInactivo.setText("Inactivo");

        jLabel2.setText("Estado : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkActivo)
                                .addGap(18, 18, 18)
                                .addComponent(chkInactivo))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkActivo)
                    .addComponent(chkInactivo)
                    .addComponent(jLabel2))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadCategorias() {
        try {
            DefaultComboBoxModel<CategoriaTransaccion> model = new DefaultComboBoxModel<>();
            List<CategoriaTransaccion> lista = catDao.listarTodos();
            for (CategoriaTransaccion c : lista) {
                model.addElement(c);
            }
            cbTipo.setModel(model);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar categorías", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar categorías. Revisa los logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectCategoriaById(Long idCat) {
        if (idCat == null) {
            return;
        }
        ComboBoxModel<CategoriaTransaccion> model = cbTipo.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            CategoriaTransaccion item = model.getElementAt(i);
            if (item != null && idCat.equals(item.getId_categoria_transacciones())) {
                cbTipo.setSelectedIndex(i);
                break;
            }
        }
    }


    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        String desc = UIHelpers.getText(jTextArea1).trim();
        CategoriaTransaccion sel = (CategoriaTransaccion) cbTipo.getSelectedItem();
        Long idCat = (sel == null) ? null : sel.getId_categoria_transacciones();
        Boolean estado = chkActivo.isSelected();

        TipoTransaccion t = new TipoTransaccion();
        t.setDescripcion(desc);
        t.setId_categoria_transacciones(idCat);
        t.setEstado(estado);

        try {
            if (!editMode) {
                ValidationResult vr = controller.validate(t);
                if (!vr.isOk()) {
                    JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                TipoTransaccion creado = controller.crear(t);
                if (creado != null) {
                    JOptionPane.showMessageDialog(this, "Tipo Transacción creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    if (onSaved != null) {
                        onSaved.accept(creado);
                    }
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo crear el tipo transacción.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (editing == null || editing.getId_tipo() <= 0) {
                    JOptionPane.showMessageDialog(this, "Registro inválido para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                t.setId_tipo(editing.getId_tipo());
                ValidationResult vr = controller.validate(t, t.getId_tipo());
                if (!vr.isOk()) {
                    JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean ok = controller.actualizar(t);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Datos Actualizados Correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    if (onSaved != null) {
                        onSaved.accept(t);
                    }
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No Se Pudo Actualizar Los Datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar tipo de transacción: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Excepción btnAceptar: {0}", ex.toString());
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed

    }//GEN-LAST:event_cbTipoActionPerformed

    private void populateForEdit(TipoTransaccion t) {
        if (t == null) {
            return;
        }
        chkActivo.setSelected(Boolean.TRUE.equals(t.getEstado()));
        chkInactivo.setSelected(!Boolean.TRUE.equals(t.getEstado()));
        jTextArea1.setText(t.getDescripcion() == null ? "" : t.getDescripcion());
        UIHelpers.updatePlaceholderState(jTextArea1);
        selectCategoriaById(t.getId_categoria_transacciones());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<CategoriaTransaccion> cbTipo;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JCheckBox chkInactivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
