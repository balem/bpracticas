/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrmProveedores.java
 *
 * Created on 10/03/2012, 07:36:04 PM
 */
package bpracticas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author bala
 */
public class FrmLocalidad extends javax.swing.JFrame {
    DefaultTableModel modelo = new DefaultTableModel();
     Conexion z = new Conexion();
     int flag = 0;

    /** Creates new form FrmProveedores */
    public FrmLocalidad() {
        initComponents();
        confGrilla();
        cargaGilla();
        circunscripciones();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    //método para activar botones
    private void activarTexto(boolean si){
        txtDescripcion.setEnabled(si);
    }
    
   private void circunscripciones(){
        try {
            String sql = "SELECT * FROM circunscripciones order by id ";
                    z.snt = z.con.createStatement();
                    z.rs = z.snt.executeQuery(sql);
                    cboCircunscripcion.removeAllItems();
                    while (z.rs.next()) {
                        cboCircunscripcion.addItem(z.rs.getString("descripcion"));
        }
        } catch (SQLException ex) {
            Logger.getLogger(FrmPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    private void activarBotones(boolean si){
        btnAgregar.setEnabled(!si);
        btnModificar.setEnabled(!si);
        btnGuardar.setEnabled(si);
        btnCancelar.setEnabled(si);
        btnBorrar.setEnabled(!si);
    }
    
    private void limpiarTexto(){
        txtCodigo.setText(null);
        txtDescripcion.setText(null);
    }
    
   private void confGrilla(){
       modelo.addColumn("Id");
       modelo.addColumn("Localidad");
       modelo.addColumn("Circunscripción");
   }
    
    private void cargaGilla(){
        try {
            String sql = "SELECT localidades.id As id, localidades.nombre AS localidad, circunscripciones.descripcion AS circunscripcion"+
                    " FROM circunscripciones circunscripciones INNER JOIN localidades localidades ON circunscripciones.id "+
                    "= localidades.circunscripciones";
            String[] datos = new String[3];
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            while(z.rs.next()){
                datos[0] = z.rs.getString(1);
                datos[1] = z.rs.getString(2);
                datos[2] = z.rs.getString(3);
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmLocalidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void limpiarGrilla() {   
         while (modelo.getRowCount() > 0)
             modelo.removeRow(0);
     }
    
    private void verDatos(){
        int fila = tabla.getSelectedRow();
        txtCodigo.setText(modelo.getValueAt(fila, 0).toString());
        txtDescripcion.setText(modelo.getValueAt(fila, 1).toString());
        cboCircunscripcion.setSelectedItem(modelo.getValueAt(fila, 2));
    }
    
    private void insertar(){
        
        try {
            String sql = "INSERT localidades (nombre, circunscripciones) VALUES ('"+txtDescripcion.getText().toString()+"', "+
                    z.buscaCod("SELECT * FROM bpracticas.circunscripciones where descripcion like '"+cboCircunscripcion.getSelectedItem()+"'") +")";
            z.snt = z.con.createStatement();
            z.snt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(FrmLocalidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void modificar(){
        int cir = z.buscaCod("SELECT * FROM circunscripciones where descripcion like '"+cboCircunscripcion.getSelectedItem()+"'");
        try {
            String sql ="UPDATE localidades SET nombre = '"+
                    txtDescripcion.getText()+
                    "', circunscripciones = "+cir+
                    " WHERE id = "+
                    txtCodigo.getText();
            System.out.print(sql);
            
             z.snt = z.con.createStatement();
             z.snt.executeUpdate(sql);
             
        } catch (SQLException ex) {
            Logger.getLogger(FrmLocalidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void borrar(){
        try {
            String sql = "DELETE FROM localidades WHERE id = "+txtCodigo.getText();
            z.snt = z.con.createStatement();
            z.snt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(FrmLocalidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean  validar(){
        if(txtDescripcion.getText().length() > 0)
            return true;
        else
            return false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlProveedor = new javax.swing.JPanel();
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        lblCircunscripcion = new javax.swing.JLabel();
        cboCircunscripcion = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlProveedor.setBorder(javax.swing.BorderFactory.createTitledBorder("Localidad"));

        lblCodigo.setText("Código");

        lblNombre.setText("Descripcion");

        txtCodigo.setEnabled(false);

        txtDescripcion.setEnabled(false);

        lblCircunscripcion.setText("Circunscripción");

        cboCircunscripcion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlProveedorLayout = new javax.swing.GroupLayout(pnlProveedor);
        pnlProveedor.setLayout(pnlProveedorLayout);
        pnlProveedorLayout.setHorizontalGroup(
            pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProveedorLayout.createSequentialGroup()
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlProveedorLayout.createSequentialGroup()
                .addGroup(pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlProveedorLayout.createSequentialGroup()
                        .addComponent(lblCodigo)
                        .addGap(63, 63, 63)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlProveedorLayout.createSequentialGroup()
                        .addComponent(lblCircunscripcion)
                        .addGap(25, 25, 25)
                        .addComponent(cboCircunscripcion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlProveedorLayout.setVerticalGroup(
            pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProveedorLayout.createSequentialGroup()
                .addGroup(pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlProveedorLayout.createSequentialGroup()
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombre)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblCodigo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCircunscripcion)
                    .addComponent(cboCircunscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBorrar.setText("Borrar");
        btnBorrar.setEnabled(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar)
                    .addComponent(btnModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnBorrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalir)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Registros"));

        tabla.setModel(modelo);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        activarTexto(true);
        activarBotones(true);
        limpiarTexto();
        flag = 1;
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        activarBotones(false);
        activarTexto(false);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
        limpiarTexto();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        verDatos();
        activarBotones(false);
        btnAgregar.setEnabled(false);
        btnCancelar.setEnabled(true);
    }//GEN-LAST:event_tablaMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        activarBotones(true);
        activarTexto(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
        flag = 2;
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if(validar()){
        activarBotones(false);
        activarTexto(false);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
        if(flag ==1)
            insertar();
        if(flag == 2)
            modificar();
        limpiarGrilla();
        cargaGilla();}
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        int msj= JOptionPane.showConfirmDialog(null, "¿Realmente desea borrar el registro?", "Borrar", JOptionPane.YES_NO_OPTION);
            if(msj==JOptionPane.YES_OPTION){
            borrar();
            limpiarGrilla();
            cargaGilla();        
            activarBotones(false);
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboCircunscripcion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCircunscripcion;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JPanel pnlProveedor;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
