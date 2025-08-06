package vista;

import controlador.CajaController;
import javax.swing.JOptionPane;

public class Frm_Caja extends javax.swing.JInternalFrame {

    private final boolean estadoCaja;
    private String labelEstadoCaja;
    private final CajaController cajaController;

    public Frm_Caja() {
        initComponents();
        this.cajaController = new CajaController();
        this.estadoCaja = cajaController.verificarEstadoCaja();
        activarComponentes();
    }

    private void activarComponentes() {
        if (this.estadoCaja) {
            this.setTitle("Cerrar Caja");
            this.btn_caja.setText("Cerrar Caja");
            this.labelEstadoCaja = "cerrar la caja";
        } else {
            this.setTitle("Abrir caja");
            this.btn_caja.setText("Abrir Caja");
            this.labelEstadoCaja = "abrir la caja";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_caja = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_monto = new javax.swing.JTextField();

        setClosable(true);

        btn_caja.setText("jButton1");
        btn_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cajaActionPerformed(evt);
            }
        });

        jLabel1.setText("Monto:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_caja)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txt_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(btn_caja)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cajaActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea "+this.labelEstadoCaja+"?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

        }
    }//GEN-LAST:event_btn_cajaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_caja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txt_monto;
    // End of variables declaration//GEN-END:variables
}
