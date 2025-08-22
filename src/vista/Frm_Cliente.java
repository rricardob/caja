package vista;

import modelo.Cliente;
import controlador.ClienteController;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import util.ui.DocumentFilters;
import util.ui.UIHelpers;
import util.validation.ValidationResult;

/**
 * Frm_Cliente adaptado para creacion de cliente desde Frm_Ingreso. Usa un
 * callback Consumer<Cliente> para notificar al llamador cuando se crea el
 * cliente con exito.
 */
public class Frm_Cliente extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Cliente.class.getName());
    private final ClienteController clienteController = new controlador.ClienteController();
    private Cliente clienteCreado;

    // callback que invocaremos cuando se cree el cliente (puede ser null)
    private Consumer<Cliente> onClienteCreated;

    public Frm_Cliente() {
        initComponents();
        configureFrame();
        setupFieldBehavior();
    }

    /**
     * Constructor que acepta callback
     */
    public Frm_Cliente(Consumer<Cliente> onClienteCreated) {
        this();
        this.onClienteCreated = onClienteCreated;
    }

    private void configureFrame() {
        this.setClosable(true);
        this.setResizable(false);
        this.setTitle("Registrar Cliente");
    }

    /**
     * Configuración bloqueo inicial; filtros de longitud/dígitos/símbolos y
     * estado según tipo
     */
    private void setupFieldBehavior() {

        // Filtros numéricos
        DocumentFilters.attachNumeric(txtDoc, 8);    // DNI: 8 dígitos
        DocumentFilters.attachNumeric(txtRuc, 11);   // RUC: 11 dígitos
        DocumentFilters.attachNumeric(txtTelefono, 9); // Teléfono: 9 dígitos

        // Filtros alfanum/símbolos para nombre y direccion
        DocumentFilters.attachAlphaNumSymbol(txtNombre, 150);
        DocumentFilters.attachAlphaNumSymbol(txtDireccion, 200);

        // Tooltips + efecto foco 
        UIHelpers.attachHintAndFocusColor(txtNombre, "Nombre: letras, números, espacios y - . / (3-150 caracteres)");
        UIHelpers.attachHintAndFocusColor(txtDoc, "DNI (8 dígitos numéricos)");
        UIHelpers.attachHintAndFocusColor(txtRuc, "RUC (11 dígitos numéricos)");
        UIHelpers.attachHintAndFocusColor(txtDireccion, "Dirección: letras, números, espacios y - . / (10-200 caracteres)");
        UIHelpers.attachHintAndFocusColor(txtTelefono, "Teléfono (9 dígitos numéricos, opcional)");

        // Placeholders
        UIHelpers.attachPlaceholder(txtNombre, " Nombre Completo");
        UIHelpers.attachPlaceholder(txtDoc, " DNI");
        UIHelpers.attachPlaceholder(txtRuc, " RUC");
        UIHelpers.attachPlaceholder(txtDireccion, " Dirección");
        UIHelpers.attachPlaceholder(txtTelefono, " Teléfono");

        // Estado inicial según combo (esto también actualizará los placeholders apropiadamente)
        updateTipoFields();
    }

    /**
     * Bloquea totalmente entrada (teclado/pegar) y foco visual si está
     * deshabilitado
     */
    private void setEditableAndEnabled(JTextField field, boolean enable) {
        field.setEditable(enable);
        field.setEnabled(enable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDoc = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cbTipo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setText("Sr.(es) : ");

        jLabel2.setText("Direccion :  ");

        jLabel3.setText("Doc.Identidad : ");

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

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Persona", "Empresa" }));
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });

        jLabel4.setText("Teléfono :");

        jLabel5.setText("Ruc : ");

        jLabel6.setText("Elige Tipo Cliente :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(txtTelefono))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        onAceptar();
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.clienteCreado = null;
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        updateTipoFields();
    }//GEN-LAST:event_cbTipoActionPerformed

    /**
     * Habilita/bloquea los campos según el tipo elegido en el combo
     */
    private void updateTipoFields() {
        String tipo = (String) cbTipo.getSelectedItem();
        boolean persona = "Persona".equalsIgnoreCase(tipo);

        // Persona -> habilita DNI, bloquea RUC
        setEditableAndEnabled(txtDoc, persona);
        if (!persona) {
            txtDoc.setText("");
        }
        // Forzar re-evaluación del placeholder tras cambiar editable/text
        UIHelpers.updatePlaceholderState(txtDoc);

        // Empresa -> habilita RUC, bloquea DNI
        setEditableAndEnabled(txtRuc, !persona);
        if (persona) {
            txtRuc.setText("");
        }
        // Forzar re-evaluación del placeholder tras cambiar editable/text
        UIHelpers.updatePlaceholderState(txtRuc);
    }

    private void onAceptar() {
        String tipo = (String) cbTipo.getSelectedItem(); // "Persona" o "Empresa"
        // Aseguramos que el campo no utilizado quede vacío y actualizamos placeholders
        if ("Persona".equalsIgnoreCase(tipo)) {
            txtRuc.setText("");
            UIHelpers.updatePlaceholderState(txtRuc);
        } else {
            txtDoc.setText("");
            UIHelpers.updatePlaceholderState(txtDoc);
        }

        String nombre = UIHelpers.getText(txtNombre).trim();
        String direccion = UIHelpers.getText(txtDireccion).trim();
        String doc = UIHelpers.getText(txtDoc).trim();
        String ruc = UIHelpers.getText(txtRuc).trim();
        String telefono = UIHelpers.getText(txtTelefono).trim();

        Cliente c = new Cliente();
        c.setNombre_completo(nombre);
        c.setDireccion(direccion);
        c.setDoc_identidad(doc.isEmpty() ? null : doc);
        c.setRuc(ruc.isEmpty() ? null : ruc);
        c.setTelefono(telefono.isEmpty() ? null : telefono);

        // VALIDAR A TRAVÉS DEL CONTROLLER (no confundir con lanzar excepción)
        ValidationResult vr = clienteController.validateForType(c, tipo);
        if (!vr.isOk()) {
            JOptionPane.showMessageDialog(this, vr.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            LOGGER.log(Level.INFO, "Validación cliente fallida: {0}", vr.getMessage());
            return;
        }

        // Intentar crear cliente (el controller puede seguir lanzando excepciones por problemas inesperados)
        try {
            Cliente creado = clienteController.crearCliente(c, tipo);
            if (creado != null) {
                this.clienteCreado = creado;
                JOptionPane.showMessageDialog(this, "Cliente creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Notificar callback
                if (onClienteCreated != null) {
                    try {
                        onClienteCreated.accept(creado);
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Callback fallo: {0}", ex.getMessage());
                    }
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Mensaje claro de validación desde ClienteValidator (propagado por controller)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Excepción onAceptar: {0}", ex.toString());
        }
    }

    /**
     * Permitir setear el callback despues de crear instancia
     */
    public void setOnClienteCreated(Consumer<Cliente> onClienteCreated) {
        this.onClienteCreated = onClienteCreated;
    }

    public Cliente getClienteCreado() {
        return clienteCreado;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
