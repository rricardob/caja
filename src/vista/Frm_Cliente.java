package vista;

import dao.ClienteDAO;
import modelo.Cliente;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 * Frm_Cliente adaptado para creacion de cliente desde Frm_Ingreso.
 * Usa un callback Consumer<Cliente> para notificar al llamador cuando
 * se crea el cliente con exito.
 */
public class Frm_Cliente extends javax.swing.JInternalFrame {
    
    private static final Logger LOGGER = Logger.getLogger(Frm_Cliente.class.getName());
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private Cliente clienteCreado;

    // callback que invocaremos cuando se cree el cliente (puede ser null)
    private Consumer<Cliente> onClienteCreated;
    
    public Frm_Cliente() {
        initComponents();
        configureFrame();
    }
    
    /** Constructor que acepta callback */
    public Frm_Cliente(Consumer<Cliente> onClienteCreated) {
        this();
        this.onClienteCreated = onClienteCreated;
    }
    
    private void configureFrame() {
        this.setClosable(true);
        this.setResizable(false);
        this.setTitle("Registrar Cliente");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDoc))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(45, 45, 45)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(32, 32, 32)
                        .addComponent(txtDireccion)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addContainerGap(95, Short.MAX_VALUE))
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

    // lógica de creación de cliente 
    private void onAceptar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String doc = txtDoc.getText().trim();

        if (nombre.isEmpty() || doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete al menos: Nombre y Doc. Identidad",
                    "Datos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = new Cliente();
        c.setNombre_completo(nombre);
        c.setDireccion(direccion);
        c.setDoc_identidad(doc);

        try {
            Cliente creado = clienteDAO.insertarCliente(c);
            if (creado != null) {
                this.clienteCreado = creado;
                JOptionPane.showMessageDialog(this, "Cliente creado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                LOGGER.log(Level.INFO, "Cliente creado ID: {0}", creado.getId_cliente());

                // Notificar al callback (si existe)
                if (onClienteCreated != null) {
                    try {
                        onClienteCreated.accept(creado);
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Callback onClienteCreated fallo: {0}", ex.getMessage());
                    }
                }

                // cerramos el internal frame
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear cliente. Verifique datos o documento duplicado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción al crear cliente: {0}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al crear cliente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** Opcional: permitir setear el callback despues de crear instancia */
    public void setOnClienteCreated(Consumer<Cliente> onClienteCreated) {
        this.onClienteCreated = onClienteCreated;
    }

    public Cliente getClienteCreado() {
        return clienteCreado;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
