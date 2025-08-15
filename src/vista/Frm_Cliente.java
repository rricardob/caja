package vista;

import dao.ClienteDAO;
import modelo.Cliente;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

/**
 * Frm_Cliente adaptado para creacion de cliente desde Frm_Ingreso. Usa un
 * callback Consumer<Cliente> para notificar al llamador cuando se crea el
 * cliente con exito.
 */
public class Frm_Cliente extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(Frm_Cliente.class.getName());
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final controlador.ClienteController clienteController = new controlador.ClienteController();
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
     * Configura bloqueo inicial, filtros de longitud/dígitos y estado según
     * tipo
     */
    private void setupFieldBehavior() {
        // Aplica filtros de sólo dígitos y longitud máxima
        setDigitLimit(txtDoc, 8);   // DNI: 8 dígitos
        setDigitLimit(txtRuc, 11);  // RUC: 11 dígitos

        // Tooltips informativos (opcionales)
        txtDoc.setToolTipText("DNI (8 dígitos numéricos)");
        txtRuc.setToolTipText("RUC (11 dígitos numéricos)");

        // Estado inicial: habilita según selección actual del combo
        // Opción mínima: si el combo ya dice "Persona", habilita DNI y bloquea RUC.
        // Si quieres ambos bloqueados hasta que el usuario elija, mira la Variante B más abajo.
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

    /**
     * Aplica un filtro de solo dígitos y longitud máxima al JTextField
     */
    private void setDigitLimit(JTextField field, int maxLen) {
        DocumentFilter filter = new NumericLengthFilter(maxLen);
        ((AbstractDocument) field.getDocument()).setDocumentFilter(filter);
    }

    /**
     * Filtro simple: sólo dígitos y máximo N caracteres (funciona con teclear y
     * pegar)
     */
    private static class NumericLengthFilter extends DocumentFilter {

        private final int maxLen;

        NumericLengthFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws javax.swing.text.BadLocationException {
            if (string == null) {
                return;
            }
            String filtered = filterDigits(string);
            if (filtered.isEmpty()) {
                return;
            }
            if ((fb.getDocument().getLength() + filtered.length()) <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                throws javax.swing.text.BadLocationException {
            // Permitir explicitly reemplazos vacíos (p.ej. setText(""))
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            if (text.isEmpty()) {
                // Si reemplazan por cadena vacía, aplicar el replace (permite borrar)
                super.replace(fb, offset, length, text, attrs);
                return;
            }

            String filtered = filterDigits(text);
            if (filtered.isEmpty()) {
                // El texto no contiene dígitos: no insertamos nada
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen - length + filtered.length();
            if (newLen <= maxLen) {
                super.replace(fb, offset, length, filtered, attrs);
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
                throws javax.swing.text.BadLocationException {
            // Permitir siempre eliminar (borrado)
            super.remove(fb, offset, length);
        }

        private String filterDigits(String in) {
            StringBuilder sb = new StringBuilder(in.length());
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
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

        setEditableAndEnabled(txtDoc, persona);
        if (!persona) {
            txtDoc.setText("");
        }

        setEditableAndEnabled(txtRuc, !persona);
        if (persona) {
            txtRuc.setText("");
        }
    }

    private void onAceptar() {

        String tipo = (String) cbTipo.getSelectedItem(); // "Persona" o "Empresa"
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String doc = txtDoc.getText().trim();
        String ruc = txtRuc.getText().trim();
        String telefono = txtTelefono.getText().trim();

        Cliente c = new Cliente();
        c.setNombre_completo(nombre);
        c.setDireccion(direccion);
        c.setDoc_identidad(doc.isEmpty() ? null : doc);
        c.setRuc(ruc.isEmpty() ? null : ruc);
        c.setTelefono(telefono.isEmpty() ? null : telefono);

        try {
            Cliente creado = clienteController.crearCliente(c, tipo);
            if (creado != null) {
                this.clienteCreado = creado;
                JOptionPane.showMessageDialog(this, "Cliente creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Notificar callback
                if (onClienteCreated != null) {
                    onClienteCreated.accept(creado);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Mensaje claro de validación desde clienteController.validarCliente
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
