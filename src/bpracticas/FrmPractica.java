/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Enrique Rodríguez
 */
public class FrmPractica extends javax.swing.JFrame {
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Texto","pdf","doc", "docx","odt","xls","xlsx");
    private JFileChooser buscador = new JFileChooser();
    int flaLo = 0;
    Conexion z = new Conexion();
    private int action = 1;
    /**
     * Creates new form frmPractica
     */
    public FrmPractica() {
        initComponents();
        circunscripciones();
        metodologia();
        habilitarTab(false);
        saltoLinea();
        modificar(FrmPrincipal.getModificaras());
    }
    
    private void habilitarTab(boolean si){
        tabContenedor.setEnabledAt(1, si);
        tabContenedor.setEnabledAt(2, si);
    }
    
   private String[] busLoc(String cir){
       String[] locali = null;
        try {
            String buscarCir = "SELECT localidades FROM vlocalidades where circunscripciones like '"+cir+"'";
            String countLocal = "SELECT count(*) as cant FROM vlocalidades where circunscripciones like '%"+cir+"%'";
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(countLocal);
            z.rs.next();
            locali = new String[Integer.parseInt(z.rs.getString("cant"))];
            
            
            z.rs = z.snt.executeQuery(buscarCir);
            int i =0;
            while(z.rs.next()){
                locali[i] = z.rs.getString("localidades");
                i++;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return locali;
   }
    
    private void modificar(int id){
        if(id != 0){
            try {
                String sql = "SELECT id, titulo, inicio, fin, tema, descripcion, conclusiones, acuerdos, metodologias, localidades, circunscripciones, disponible  FROM vpractica where disponible = 1 and id = "+id;
                z.snt = z.con.createStatement();
                z.rs = z.snt.executeQuery(sql);
                while(z.rs.next()){
                    String local;
                    String buscarCir = "SELECT * FROM circunscripciones where descripcion like '"+z.rs.getString("circunscripciones") +"'";
                    local = z.rs.getString("localidades").toString();
                    txtId.setText(z.rs.getString("id").toString());
                    txtTitulo.setText(z.rs.getString("titulo").toString());
                    cboInicio.setDate(z.rs.getDate("inicio"));
                    cboFin.setDate(z.rs.getDate("fin"));
                    txtTema.setText(z.rs.getString("tema").toString());
                    txtDescripcion.setText(z.rs.getString("descripcion").toString());
                    txtConclusion.setText(z.rs.getString("conclusiones").toString());
                    txtAcuerdos.setText(z.rs.getString("acuerdos").toString());
                    cboMetodologia.setSelectedIndex(z.buscaCod("SELECT id from metodologias where descripcion like '"+z.rs.getString("metodologias")+"'") -1 );
                    cboCircunscripciones.setSelectedIndex(z.buscaCod(buscarCir) -1);
                    System.out.println(local);                    
                    cboLocalidad.setSelectedItem(local);
//                    for (int i = 0; i < buscarCir.length(); i++) {
//                        if(busLoc(cboCircunscripciones.getSelectedItem().toString())[i].equals(local)){
//                            cboLocalidad.setSelectedItem(local);
//                        }
//                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(FrmPractica.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            habilitarTab(true);
            action = 2;
        }
        
    }
    
    
    private void saltoLinea(){
        txtConclusion.setLineWrap(true);
        txtDescripcion.setLineWrap(true);
        txtAcuerdos.setLineWrap(true);
    }
    
    public int escribir(String nombre, String rutaArchivo) {
        InputStream entrada = null;

        PreparedStatement pst = null;
        int ingresados = 0;
        try {
            File archivo;
            String insert;

            z.con.setAutoCommit(false);
            

            insert = "Insert into documentos (nombre, documentos, fecha) values(?,?, NOW())";

            pst = (PreparedStatement) z.con.prepareStatement(insert);

            archivo = new File(rutaArchivo);

            entrada = new FileInputStream(archivo);

            pst.setString(1, nombre.toUpperCase());
            pst.setBinaryStream(2, entrada, (int) archivo.length());

            ingresados = pst.executeUpdate();
            z.con.commit();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return ingresados;
    }
/**
     * 
     * @param nombreArchivoBuscar
     * @param nombreArchivoSalida
     * @return 
     */
    public boolean obtenerArchivo(String nombreArchivoBuscar, String nombreArchivoSalida) {
        InputStream salida = null;
        try {
            PreparedStatement pst;
            Blob blob;
            FileOutputStream archivoSalida;
            String select;

            byte[] arreglo;
            int byteLeidos = 0;

            z.con.setAutoCommit(false);
            
            select = "select documentos from documentos WHERE nombre=?";
            pst = (PreparedStatement) z.con.prepareStatement(select);
            pst.setString(1, nombreArchivoBuscar);

            z.rs = pst.executeQuery();

            if (z.rs != null) {
                z.rs.next();
                blob = (Blob) z.rs.getBlob(1);
                salida = blob.getBinaryStream();

                arreglo = new byte[2048];

                archivoSalida = new FileOutputStream(nombreArchivoSalida);

                while ((byteLeidos = salida.read(arreglo)) > 0) {
                    archivoSalida.write(arreglo, 0, byteLeidos);
                }
                return true;
            } else {
                return false;
            }

        } catch (IOException ex) {
            Logger.getLogger(Datosblob.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Datosblob.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Datosblob.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tabContenedor = new javax.swing.JTabbedPane();
        pnlIdentidad = new javax.swing.JPanel();
        txtTitulo = new javax.swing.JTextField();
        lblTituo = new javax.swing.JLabel();
        lblInicio = new javax.swing.JLabel();
        lblFin = new javax.swing.JLabel();
        lblTema = new javax.swing.JLabel();
        txtTema = new javax.swing.JTextField();
        lblLocalidad = new javax.swing.JLabel();
        cboLocalidad = new javax.swing.JComboBox();
        lblLocalidad1 = new javax.swing.JLabel();
        cboCircunscripciones = new javax.swing.JComboBox();
        lblTituo1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        cboInicio = new org.freixas.jcalendar.JCalendarCombo();
        cboFin = new org.freixas.jcalendar.JCalendarCombo();
        pnlDesarrollo = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        lblMetodologia = new javax.swing.JLabel();
        cboMetodologia = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        btnXAutor = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnlConclusion = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtConclusion = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtAcuerdos = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        btnDocumentos = new javax.swing.JButton();
        pnlAcciones = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlIdentidad.setBorder(javax.swing.BorderFactory.createTitledBorder("Identificación de la práctica"));

        txtTitulo.setToolTipText("Escriba el título que se le dío a la Actividad");
        txtTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTituloActionPerformed(evt);
            }
        });

        lblTituo.setText("Título");

        lblInicio.setText("Fecha de Inicio");

        lblFin.setText("Fecha de fin");

        lblTema.setText("Tema");

        txtTema.setToolTipText("Escriba el Tema, ejemplo Acceso a la Información de personas con discapacidad");

        lblLocalidad.setText("Localidad");

        cboLocalidad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblLocalidad1.setText("Circunscripción");

        cboCircunscripciones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCircunscripciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCircunscripcionesActionPerformed(evt);
            }
        });

        lblTituo1.setText("ID");

        txtId.setToolTipText("Escriba el título que se le dío a la Actividad");
        txtId.setEnabled(false);
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlIdentidadLayout = new javax.swing.GroupLayout(pnlIdentidad);
        pnlIdentidad.setLayout(pnlIdentidadLayout);
        pnlIdentidadLayout.setHorizontalGroup(
            pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIdentidadLayout.createSequentialGroup()
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblInicio)
                        .addComponent(lblTituo)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTema)
                            .addComponent(lblLocalidad)
                            .addComponent(lblLocalidad1))
                        .addComponent(lblTituo1))
                    .addComponent(lblFin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtTema)
                        .addComponent(cboLocalidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboCircunscripciones, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboInicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(454, 454, 454))
        );
        pnlIdentidadLayout.setVerticalGroup(
            pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIdentidadLayout.createSequentialGroup()
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTituo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTituo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIdentidadLayout.createSequentialGroup()
                        .addComponent(lblInicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFin))
                    .addGroup(pnlIdentidadLayout.createSequentialGroup()
                        .addComponent(cboInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocalidad1)
                    .addComponent(cboCircunscripciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocalidad)
                    .addComponent(cboLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIdentidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTema)
                    .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(311, 311, 311))
        );

        tabContenedor.addTab("Identificación", pnlIdentidad);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Breve descripción de la actividad"));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.setToolTipText("Breve descripción de la actividad");
        jScrollPane2.setViewportView(txtDescripcion);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        lblMetodologia.setText("Metodología");

        cboMetodologia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Autores"));

        btnXAutor.setText("Autores");
        btnXAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXAutorActionPerformed(evt);
            }
        });

        jLabel1.setText("Añadir y Editar Autores de esta practica");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnXAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnXAutor))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDesarrolloLayout = new javax.swing.GroupLayout(pnlDesarrollo);
        pnlDesarrollo.setLayout(pnlDesarrolloLayout);
        pnlDesarrolloLayout.setHorizontalGroup(
            pnlDesarrolloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDesarrolloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDesarrolloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDesarrolloLayout.createSequentialGroup()
                        .addComponent(lblMetodologia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboMetodologia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        pnlDesarrolloLayout.setVerticalGroup(
            pnlDesarrolloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDesarrolloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlDesarrolloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMetodologia)
                    .addComponent(cboMetodologia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        tabContenedor.addTab("Desarrollo", pnlDesarrollo);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Conclusión"));

        txtConclusion.setColumns(20);
        txtConclusion.setRows(5);
        jScrollPane5.setViewportView(txtConclusion);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Acuerdos tomados"));

        txtAcuerdos.setColumns(20);
        txtAcuerdos.setRows(5);
        jScrollPane6.setViewportView(txtAcuerdos);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
        );

        jLabel4.setText("Documentos Generados");

        btnDocumentos.setText("Documentos");
        btnDocumentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConclusionLayout = new javax.swing.GroupLayout(pnlConclusion);
        pnlConclusion.setLayout(pnlConclusionLayout);
        pnlConclusionLayout.setHorizontalGroup(
            pnlConclusionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConclusionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConclusionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlConclusionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDocumentos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlConclusionLayout.setVerticalGroup(
            pnlConclusionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConclusionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDocumentos)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        tabContenedor.addTab("Conclusión", pnlConclusion);

        pnlAcciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btnAgregar.setText("Agregar");
        btnAgregar.setEnabled(false);

        btnModificar.setText("Modificar");
        btnModificar.setEnabled(false);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBorrar.setText("Borrar");
        btnBorrar.setEnabled(false);

        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAccionesLayout = new javax.swing.GroupLayout(pnlAcciones);
        pnlAcciones.setLayout(pnlAccionesLayout);
        pnlAccionesLayout.setHorizontalGroup(
            pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionesLayout.createSequentialGroup()
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAccionesLayout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAccionesLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAccionesLayout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        pnlAccionesLayout.setVerticalGroup(
            pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar)
                    .addComponent(btnModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnBorrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void txtTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTituloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTituloActionPerformed

    
    
    private void cboCircunscripcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCircunscripcionesActionPerformed
        flaLo = 1;
        localidad();
    }//GEN-LAST:event_cboCircunscripcionesActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        abm(action);
        action = 2;
        habilitarTab(true);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnXAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXAutorActionPerformed
        int xa = Integer.parseInt(txtId.getText().toString());
        FrmArea autor = new FrmArea(this, true, xa);
        autor.setVisible(true);
    }//GEN-LAST:event_btnXAutorActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentosActionPerformed
        FrmArchivo archivo = new FrmArchivo(this, true, Integer.parseInt(this.txtId.getText().toString()));
        archivo.setVisible(true);
    }//GEN-LAST:event_btnDocumentosActionPerformed

    
    private void abm(int accion){
        try {
            int id = 1;
            String fechIni = z.dateFormat(cboInicio.getDate()).toString();
            String fechFin = z.dateFormat(cboFin.getDate()).toString();
            int meto = z.buscaCod("SELECT id FROM metodologias where descripcion like '%"+cboMetodologia.getSelectedItem().toString()+"%'");
            int loca = z.buscaCod("SELECT * FROM localidades where nombre like '%"+cboLocalidad.getSelectedItem().toString()+"%'");
            if(accion == 2){
                id = Integer.parseInt(txtId.getText().toString());
            }
            String sql = "CALL rpractica ("+accion+", "+id+", '"+txtTitulo.getText().toString()+"', '"+
                    fechIni+"', '"+fechFin+"', '"+txtTema.getText().toString()+"', '"+meto+"', '"+txtDescripcion.getText().toString()+"', '"+
                    txtConclusion.getText().toString()+"', '"+txtAcuerdos.getText().toString()+"', "+loca+")";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.executeUpdate(sql);
            z.rs = z.snt.executeQuery("select max(id) as id from practicas limit 1;");
            z.rs.next();
            txtId.setText(z.rs.getString("id"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void circunscripciones(){
        try {
            String sql = "SELECT * FROM circunscripciones order by id ";
                    z.snt = z.con.createStatement();
                    z.rs = z.snt.executeQuery(sql);
                    cboCircunscripciones.removeAllItems();
                    while (z.rs.next()) {
                        cboCircunscripciones.addItem(z.rs.getString("descripcion"));
        }
        } catch (SQLException ex) {
            Logger.getLogger(FrmPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    
    
    private void localidad(){
        String buscar;
        buscar = "SELECT * FROM circunscripciones where descripcion like '%"+cboCircunscripciones.getSelectedItem()+"%'";
        
        System.out.print(buscar);        
        try {
            Conexion z = new Conexion();
            String sql = "SELECT * FROM localidades where circunscripciones = "+z.buscaCod(buscar) +" order by nombre ";
                    z.snt = z.con.createStatement();
                    z.rs = z.snt.executeQuery(sql);
                    cboLocalidad.removeAllItems();
                    while (z.rs.next()) {
                        cboLocalidad.addItem(z.rs.getString("nombre"));
        }
        } catch (SQLException ex) {
            Logger.getLogger(FrmPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    private void metodologia(){
        try {
            Conexion z = new Conexion();
            String sql = "SELECT * FROM metodologias";
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            cboMetodologia.removeAllItems();
            while (z.rs.next()) {
                cboMetodologia.addItem(z.rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
        
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
            java.util.logging.Logger.getLogger(FrmPractica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPractica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPractica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPractica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
                    new FrmPractica().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDocumentos;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnXAutor;
    private javax.swing.JComboBox cboCircunscripciones;
    private org.freixas.jcalendar.JCalendarCombo cboFin;
    private org.freixas.jcalendar.JCalendarCombo cboInicio;
    private javax.swing.JComboBox cboLocalidad;
    private javax.swing.JComboBox cboMetodologia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFin;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblLocalidad;
    private javax.swing.JLabel lblLocalidad1;
    private javax.swing.JLabel lblMetodologia;
    private javax.swing.JLabel lblTema;
    private javax.swing.JLabel lblTituo;
    private javax.swing.JLabel lblTituo1;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlConclusion;
    private javax.swing.JPanel pnlDesarrollo;
    private javax.swing.JPanel pnlIdentidad;
    private javax.swing.JTabbedPane tabContenedor;
    private javax.swing.JTextArea txtAcuerdos;
    private javax.swing.JTextArea txtConclusion;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtTema;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
