/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Daniel San Nicolas
 */
public class FrmVariables extends javax.swing.JFrame implements ChangeListener{
    Conexion z = new Conexion();
    private int critriosFlag = 0;
    /**
     * Creates new form FrmVariables
     */
    public FrmVariables() {
        initComponents();
        cargarFactore();
        cargarCriterios();
        if(FrmPrincipal.getEvaluar()!= 0){
            txtId.setText(FrmPrincipal.getEvaluar()+"");
            mostrarPractica();
            innovacionCheck();
        }
    }
    
    private void innovacionCheck(){
        JCheckBox novedad = new JCheckBox("Novedad");
        novedad.setVisible(true);
        novedad.add(pnlCriterios);
        add(novedad);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlVariable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtVariable = new javax.swing.JTextArea();
        pnlCriterio = new javax.swing.JPanel();
        lblFactor = new javax.swing.JLabel();
        cboFactor = new javax.swing.JComboBox();
        lblCriterio = new javax.swing.JLabel();
        cboCriterio = new javax.swing.JComboBox();
        lblID = new javax.swing.JLabel();
        lblPractica = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtPractica = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        pnlCriterios = new javax.swing.JPanel();
        jpnaprueba = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlVariable.setBorder(javax.swing.BorderFactory.createTitledBorder("Indicador"));

        txtVariable.setColumns(20);
        txtVariable.setRows(5);
        txtVariable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVariableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtVariable);

        javax.swing.GroupLayout pnlVariableLayout = new javax.swing.GroupLayout(pnlVariable);
        pnlVariable.setLayout(pnlVariableLayout);
        pnlVariableLayout.setHorizontalGroup(
            pnlVariableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlVariableLayout.setVerticalGroup(
            pnlVariableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );

        pnlCriterio.setBorder(javax.swing.BorderFactory.createTitledBorder("Criterios"));

        lblFactor.setText("Factor");

        cboFactor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboFactor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboFactorMouseClicked(evt);
            }
        });
        cboFactor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboFactorItemStateChanged(evt);
            }
        });
        cboFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFactorActionPerformed(evt);
            }
        });

        lblCriterio.setText("Criterio");

        cboCriterio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblID.setText("ID");

        lblPractica.setText("Practica");

        txtId.setEnabled(false);

        txtPractica.setEnabled(false);

        javax.swing.GroupLayout pnlCriterioLayout = new javax.swing.GroupLayout(pnlCriterio);
        pnlCriterio.setLayout(pnlCriterioLayout);
        pnlCriterioLayout.setHorizontalGroup(
            pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCriterioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFactor)
                    .addComponent(lblCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblID)
                    .addComponent(txtId))
                .addGap(33, 33, 33)
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboFactor, 0, 233, Short.MAX_VALUE)
                    .addComponent(cboCriterio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPractica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPractica))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCriterioLayout.setVerticalGroup(
            pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCriterioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFactor)
                    .addComponent(cboFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCriterio)
                    .addComponent(cboCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblID)
                    .addComponent(lblPractica))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPractica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btnGuardar.setText("Guardar");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCriterios.setBorder(javax.swing.BorderFactory.createTitledBorder("Criterios"));

        javax.swing.GroupLayout jpnapruebaLayout = new javax.swing.GroupLayout(jpnaprueba);
        jpnaprueba.setLayout(jpnapruebaLayout);
        jpnapruebaLayout.setHorizontalGroup(
            jpnapruebaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpnapruebaLayout.setVerticalGroup(
            jpnapruebaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlCriteriosLayout = new javax.swing.GroupLayout(pnlCriterios);
        pnlCriterios.setLayout(pnlCriteriosLayout);
        pnlCriteriosLayout.setHorizontalGroup(
            pnlCriteriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCriteriosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpnaprueba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
        );
        pnlCriteriosLayout.setVerticalGroup(
            pnlCriteriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCriteriosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jpnaprueba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlCriterios, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlVariable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCriterios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlVariable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cargarFactore(){
        try {
            String sql = "SELECT descripcion FROM factores;";
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            cboFactor.removeAllItems();
            while(z.rs.next()){
                cboFactor.addItem(z.rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmVariables.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.critriosFlag = 1;
    }
    
    private void activarBotones(boolean si){
        btnCancelar.setEnabled(si);
        btnGuardar.setEnabled(si);
        btnSalir.setEnabled(!si);
    }
    
    private void mostrarPractica(){
        try {
            String sql = "Select Titulo from practicas where id = "+FrmPrincipal.getEvaluar();
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            while(z.rs.next()){
                txtPractica.setText(z.rs.getString("Titulo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmVariables.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertarVariable(){
        try {
            String sql = "CALL rvariable(1, '"+z.buscaCod("SELECT id FROM criterios where nombre like '"+cboCriterio.getSelectedItem()+"'") 
                    +"', '"+txtVariable.getText().toString()+"', "+txtId.getText().toString()+")";
            z.snt = z.con.createStatement();
            System.out.println(sql);
            z.snt.execute(sql);
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmVariables.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void cargarCriterios(){
        String buscar = "SELECT id FROM factores where descripcion like '"+cboFactor.getSelectedItem().toString()+"'";
        
        try {
            String sql = "SELECT nombre FROM criterios where factor = "+z.buscaCod(buscar);
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            cboCriterio.removeAllItems();
            while(z.rs.next()){
                cboCriterio.addItem(z.rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmVariables.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        insertarVariable();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cboFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFactorActionPerformed
        
    }//GEN-LAST:event_cboFactorActionPerformed

    private void cboFactorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboFactorMouseClicked
        
    }//GEN-LAST:event_cboFactorMouseClicked

    private void cboFactorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboFactorItemStateChanged
        if(critriosFlag != 0){
            cargarCriterios();
        }
        
    }//GEN-LAST:event_cboFactorItemStateChanged

    private void txtVariableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVariableKeyReleased
        if(txtVariable.getText().length() > 3){
            activarBotones(true);
        }else{
            activarBotones(false);
        }
    }//GEN-LAST:event_txtVariableKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmVariables.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmVariables.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmVariables.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmVariables.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmVariables().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboCriterio;
    private javax.swing.JComboBox cboFactor;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpnaprueba;
    private javax.swing.JLabel lblCriterio;
    private javax.swing.JLabel lblFactor;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblPractica;
    private javax.swing.JPanel pnlCriterio;
    private javax.swing.JPanel pnlCriterios;
    private javax.swing.JPanel pnlVariable;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextArea txtVariable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
