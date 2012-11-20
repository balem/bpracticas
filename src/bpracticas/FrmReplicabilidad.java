/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Daniel San Nicolas
 */
public class FrmReplicabilidad extends javax.swing.JFrame {
    int respuesta = 0;
    Conexion z = new Conexion();

    private String[] observacion = new String[9];
    
    private ArrayList<JComboBox> combos = new ArrayList<JComboBox>();
    


    
    /**
     * Creates new form FrmInnovacion
     */
    public FrmReplicabilidad() {
        initComponents();
        txtCriterio.setText("Replicabilidad");
        if(FrmPrincipal.getEvaluar()!= 0){
            txtId.setText(FrmPrincipal.getEvaluar()+"");
            configPractica();
            selecDefecto(true);
        }
        recoCombo(listarRecomendaciones(), cboSistemtatizacion);
        recoCombo(listarRecomendaciones(), cboDiseno);
        recoCombo(listarRecomendaciones(), cboDifusion);
        recoCombo(listarRecomendaciones(), cboEspacios);
        recoCombo(listarRecomendaciones(), cboAcciones);
        recoCombo(listarRecomendaciones(), cboPracPropicias);
        recoCombo(listarRecomendaciones(), cboIncorporacion);
        recoCombo(listarRecomendaciones(), cboDifGrupos);
        recoCombo(listarRecomendaciones(), cboOtrasPrac);
        
        combos.add(cboSistemtatizacion);
        combos.add(cboDiseno);
        combos.add(cboDifusion);
        combos.add(cboEspacios);
        combos.add(cboAcciones);
        combos.add(cboPracPropicias);
        combos.add(cboIncorporacion);
        combos.add(cboDifGrupos);
        combos.add(cboOtrasPrac);
        

    }

    private void selecDefecto(boolean si){
        rdSistemtatizacionNo.setSelected(si);
        rdDisenoNo.setSelected(si);
        rdDifusionNo.setSelected(si);
        rdEspaciosNo.setSelected(si);
        rdAccionesNo.setSelected(si);
        rdPracPropiciasNo.setSelected(si);
        rdDifGruposNo.setSelected(si);
        rdIncorporacionNo.setSelected(si);
        rdOtrasPracNo.setSelected(si);
       
    }
    
    
    /**
     * @param Este metodo permite preguntar si una evaluación ha sido cargada para luego decidir sobre la acción de insertar o modificar
     * 
     */
    private int pregutarEvaluacion(String factor){
        try {
            
            String sql = "select count(*) as cant from vvarcriterios where factores like '"+factor+"' and practicas = "+txtId.getText().toString();
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            respuesta = Integer.parseInt(z.rs.getString("cant").toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return respuesta;
    }
    
    
    private void configPractica(){
        
        try {
            String sql = "SELECT Titulo FROM bpracticas.practicas where id = "+txtId.getText();
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            txtPractica.setText(z.rs.getString("Titulo"));
        } catch (SQLException ex) {
            Logger.getLogger(FrmReplicabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String[] cargaVariable(String factor){
        try {
            String sql = "SELECT * FROM vvariables where factores like '"+factor+"'";
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            int i = 0;
            while(z.rs.next()){
                this.observacion[i] = z.rs.getString("descripcion").toString();
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmReplicabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.observacion;
    }
    
    private int saberRecomendacion(JComboBox combo, int bnreco){
        for (int i = 0; i < listarRecomendaciones().length; i++) {
                if(listarRecomendaciones()[i].equals(combo.getSelectedItem().toString())){
                    bnreco++;
                }
            }
        
        if(bnreco == 0){
            try {
                z.snt.execute("INSERT INTO recomendaciones (descripcion) values('"+combo.getSelectedItem().toString()+"')");
                z.rs = z.snt.executeQuery("select max(id) as id from recomendaciones");
                z.rs.next();
                bnreco = z.rs.getInt("id");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            }else{
            try {
                z.rs = z.snt.executeQuery("SELECT id FROM recomendaciones where descripcion like '"+combo.getSelectedItem().toString()+"'");
                z.rs.next();
                bnreco = z.rs.getInt("id");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            }
        
        return bnreco;
    }
    
    private void abm(int accion){
        int sistema = 0;
        int diseno = 0;
        int difusion = 0;
        int existe = 0;
        int acciones = 0;
        int practica = 0;
        int incorpora = 0;
        int diferentes = 0;
        int otras = 0;
        
        int[] selecciono = new int[9];
        
        if(rdSistemtatizacionSi.isSelected() && !rdSistemtatizacionNo.isSelected()){
            sistema = 1;
            selecciono[0] = 1;
        }else{
            selecciono[0] = 0;
        }
        
        if(rdDisenoSi.isSelected() && !rdDisenoNo.isSelected()){
            diseno = 1;
        selecciono[1] = 1;
        }else{
            selecciono[1] = 0;
        }
        
        if(rdDifGruposSi.isSelected() && !rdDifGruposNo.isSelected()){
            difusion = 1;
        selecciono[2] = 1;
        }else{
            selecciono[2] = 0;
        }
        
        if(rdEspaciosSi.isSelected() && !rdEspaciosNo.isSelected()){
            existe = 1;
        selecciono[3] = 1;
        }else{
            selecciono[3] = 0;
        }
        
        if(rdAccionesSi.isSelected() && !rdAccionesNo.isSelected()){
            acciones = 1;
        selecciono[4] = 1;
        }else{
            selecciono[4] = 0;
        }
        
        if(rdPracPropiciasSi.isSelected() && !rdPracPropiciasNo.isSelected()){
            practica = 1;
        selecciono[5] = 1;
        }else{
            selecciono[5] = 0;
        }
        
        if(rdincorporacionSi.isSelected() && !rdIncorporacionNo.isSelected()){
            incorpora = 1;
        selecciono[6] = 1;
        }else{
            selecciono[6] = 0;
        }
        
        if(rdDifGruposSi.isSelected() && !rdDifGruposNo.isSelected()){
            diferentes = 1;
        selecciono[7] = 1;
        }else{
            selecciono[7] = 0;
        }
        
        
        if(rdOtrasPracSi.isSelected() && !rdOtrasPracNo.isSelected()){
            otras = 1;
        selecciono[8] = 1;
        }else{
            selecciono[8] = 0;
        }
        
        String[] texto = new String[9];
        
        texto[0] = txtSistemtatizacion.getText().toString();
        texto[1] = txtDiseno.getText().toString();
        texto[2] = txtDifusion.getText().toString();
        texto[3] = txtEspacios.getText().toString();
        texto[4] = txtAcciones.getText().toString();
        texto[5] = txtPracPropicias.getText().toString();
        texto[6] = txtIncorporacion.getText().toString();
        texto[7] = txtDifGrupos.getText().toString();
        texto[8] = txtOtrasPrac.getText().toString();
        
        
       
        try {
            //insertar evaluacion 1
            int bnreco;
            int aux = 24;
            String sql;
            for (int i = 0; i < selecciono.length; i++) {
                bnreco = saberRecomendacion(combos.get(i), 0);
                sql = "CALL revaluacion("+accion+", "+aux+","+txtId.getText().toString()+",'"+texto[i]+"', '"+cargaVariable("Replicabilidad")[i]+"',"+selecciono[i]+", "+bnreco+");";
                System.out.println(sql);
                z.snt = z.con.createStatement();
                z.snt.execute(sql);
                aux++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmReplicabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private String[] listarRecomendaciones(){
        String datos[] = null; 
        try {
            String sql = "SELECT count(id) as id FROM recomendaciones";
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            datos = new String[z.rs.getInt("id")];
            sql = "SELECT * FROM recomendaciones limit 10";
            z.rs = z.snt.executeQuery(sql);
            int i = 0;
            while (z.rs.next()) {            
                datos[i] = z.rs.getString("descripcion");
                i++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmReplicabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return datos;
    }
    
    /**
     * 
     * @param recomendaciones 
     * carga las recomendaciones para Novedad
     */
    
    private void recoCombo(String[] recomendaciones, JComboBox combo){
        combo.setEditable(true); //para poder escribir adentro
        combo.setModel(new javax.swing.DefaultComboBoxModel(recomendaciones));
        combo.setName("cachoBox");
        AutoCompleteDecorator.decorate(combo);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupSistemtatizacion = new javax.swing.ButtonGroup();
        groupAcciones = new javax.swing.ButtonGroup();
        groupOtrasPrac = new javax.swing.ButtonGroup();
        btGPertinencia1 = new javax.swing.ButtonGroup();
        btGPertinencia2 = new javax.swing.ButtonGroup();
        btGPertinencia3 = new javax.swing.ButtonGroup();
        groupDiseno = new javax.swing.ButtonGroup();
        groupDifusion = new javax.swing.ButtonGroup();
        groupEspacios = new javax.swing.ButtonGroup();
        groupPracPropicias = new javax.swing.ButtonGroup();
        groupIncorporacion = new javax.swing.ButtonGroup();
        groupDifGrupos = new javax.swing.ButtonGroup();
        PnlCriterio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtPractica = new javax.swing.JTextField();
        lblCriterio = new javax.swing.JLabel();
        txtCriterio = new javax.swing.JTextField();
        pnlAcciones = new javax.swing.JPanel();
        btNGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        pnlMantenimientoTiempo = new javax.swing.JPanel();
        txtSistemtatizacion = new javax.swing.JTextField();
        lblSiete = new javax.swing.JLabel();
        rdSistemtatizacionSi = new javax.swing.JRadioButton();
        rdSistemtatizacionNo = new javax.swing.JRadioButton();
        lblIdNoveadd2 = new javax.swing.JLabel();
        cboSistemtatizacion = new org.jdesktop.swingx.JXComboBox();
        txtDiseno = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rdDisenoSi = new javax.swing.JRadioButton();
        rdDisenoNo = new javax.swing.JRadioButton();
        lblIdNoveadd3 = new javax.swing.JLabel();
        cboDiseno = new org.jdesktop.swingx.JXComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtDifusion = new javax.swing.JTextField();
        rdDifusionSi = new javax.swing.JRadioButton();
        rdDifusionNo = new javax.swing.JRadioButton();
        cboDifusion = new org.jdesktop.swingx.JXComboBox();
        lblIdNoveadd4 = new javax.swing.JLabel();
        lblIdNoveadd5 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtEspacios = new javax.swing.JTextField();
        cboEspacios = new org.jdesktop.swingx.JXComboBox();
        rdEspaciosSi = new javax.swing.JRadioButton();
        rdEspaciosNo = new javax.swing.JRadioButton();
        pnlPromocion = new javax.swing.JPanel();
        txtAcciones = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdAccionesSi = new javax.swing.JRadioButton();
        rdAccionesNo = new javax.swing.JRadioButton();
        lblIdPromocion = new javax.swing.JLabel();
        cboAcciones = new org.jdesktop.swingx.JXComboBox();
        rdPracPropiciasNo = new javax.swing.JRadioButton();
        rdPracPropiciasSi = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        lblIdPromocion1 = new javax.swing.JLabel();
        txtPracPropicias = new javax.swing.JTextField();
        cboPracPropicias = new org.jdesktop.swingx.JXComboBox();
        jLabel12 = new javax.swing.JLabel();
        cboIncorporacion = new org.jdesktop.swingx.JXComboBox();
        txtIncorporacion = new javax.swing.JTextField();
        rdIncorporacionNo = new javax.swing.JRadioButton();
        lblIdPromocion2 = new javax.swing.JLabel();
        rdincorporacionSi = new javax.swing.JRadioButton();
        cboDifGrupos = new org.jdesktop.swingx.JXComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtDifGrupos = new javax.swing.JTextField();
        rdDifGruposSi = new javax.swing.JRadioButton();
        rdDifGruposNo = new javax.swing.JRadioButton();
        lblIdPromocion3 = new javax.swing.JLabel();
        pnlNormalizacion = new javax.swing.JPanel();
        txtOtrasPrac = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdOtrasPracSi = new javax.swing.JRadioButton();
        rdOtrasPracNo = new javax.swing.JRadioButton();
        lblIdNormalizacion = new javax.swing.JLabel();
        cboOtrasPrac = new org.jdesktop.swingx.JXComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PnlCriterio.setBorder(javax.swing.BorderFactory.createTitledBorder("Criterio"));

        jLabel1.setText("id");

        jLabel2.setText("Práctica");

        txtId.setEnabled(false);

        txtPractica.setEnabled(false);

        lblCriterio.setText("Criterio");

        txtCriterio.setEnabled(false);

        javax.swing.GroupLayout PnlCriterioLayout = new javax.swing.GroupLayout(PnlCriterio);
        PnlCriterio.setLayout(PnlCriterioLayout);
        PnlCriterioLayout.setHorizontalGroup(
            PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCriterioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCriterio))
                .addGap(18, 18, 18)
                .addGroup(PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlCriterioLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 165, Short.MAX_VALUE))
                    .addComponent(txtCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(txtPractica, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                .addContainerGap())
        );
        PnlCriterioLayout.setVerticalGroup(
            PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCriterioLayout.createSequentialGroup()
                .addGroup(PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPractica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlCriterioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCriterio))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pnlAcciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btNGuardar.setText("Guardar");
        btNGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btNGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAccionesLayout.setVerticalGroup(
            pnlAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccionesLayout.createSequentialGroup()
                .addComponent(btNGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pnlMantenimientoTiempo.setBorder(javax.swing.BorderFactory.createTitledBorder("Transferencia"));

        lblSiete.setText("Sistematización de la práctica");

        grupSistemtatizacion.add(rdSistemtatizacionSi);
        rdSistemtatizacionSi.setText("Si");
        rdSistemtatizacionSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSistemtatizacionSiActionPerformed(evt);
            }
        });

        grupSistemtatizacion.add(rdSistemtatizacionNo);
        rdSistemtatizacionNo.setText("No");
        rdSistemtatizacionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSistemtatizacionNoActionPerformed(evt);
            }
        });

        lblIdNoveadd2.setText("24");

        cboSistemtatizacion.setEditable(true);

        jLabel9.setText("Diseño claro adaptable");

        groupDiseno.add(rdDisenoSi);
        rdDisenoSi.setText("Si");
        rdDisenoSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDisenoSiActionPerformed(evt);
            }
        });

        groupDiseno.add(rdDisenoNo);
        rdDisenoNo.setText("No");
        rdDisenoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDisenoNoActionPerformed(evt);
            }
        });

        lblIdNoveadd3.setText("25");

        cboDiseno.setEditable(true);

        jLabel10.setText("Difusión de la práctica.");

        groupDifusion.add(rdDifusionSi);
        rdDifusionSi.setText("Si");
        rdDifusionSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDifusionSiActionPerformed(evt);
            }
        });

        groupDifusion.add(rdDifusionNo);
        rdDifusionNo.setText("No");
        rdDifusionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDifusionNoActionPerformed(evt);
            }
        });

        cboDifusion.setEditable(true);

        lblIdNoveadd4.setText("26");

        lblIdNoveadd5.setText("27");

        jLabel21.setText("Existen espacios de transferencia");

        cboEspacios.setEditable(true);

        groupEspacios.add(rdEspaciosSi);
        rdEspaciosSi.setText("Si");
        rdEspaciosSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEspaciosSiActionPerformed(evt);
            }
        });

        groupEspacios.add(rdEspaciosNo);
        rdEspaciosNo.setText("No");
        rdEspaciosNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEspaciosNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMantenimientoTiempoLayout = new javax.swing.GroupLayout(pnlMantenimientoTiempo);
        pnlMantenimientoTiempo.setLayout(pnlMantenimientoTiempoLayout);
        pnlMantenimientoTiempoLayout.setHorizontalGroup(
            pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSistemtatizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSistemtatizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                                .addComponent(lblIdNoveadd2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblSiete)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdSistemtatizacionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdSistemtatizacionNo))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(cboDifusion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtDifusion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdDifusionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDifusionNo))
                    .addComponent(cboDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdDisenoSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDisenoNo))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtEspacios, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdEspaciosSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEspaciosNo))
                    .addComponent(cboEspacios, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        pnlMantenimientoTiempoLayout.setVerticalGroup(
            pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdNoveadd2)
                    .addComponent(lblSiete))
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSistemtatizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdSistemtatizacionSi)
                    .addComponent(rdSistemtatizacionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboSistemtatizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdNoveadd3)
                    .addComponent(jLabel9))
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDisenoSi)
                    .addComponent(rdDisenoNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblIdNoveadd4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDifusion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDifusionSi)
                    .addComponent(rdDifusionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDifusion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblIdNoveadd5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEspacios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEspaciosSi)
                    .addComponent(rdEspaciosNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboEspacios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlPromocion.setBorder(javax.swing.BorderFactory.createTitledBorder("Sinergia"));

        jLabel5.setText("Acciones iniciadas ");

        groupAcciones.add(rdAccionesSi);
        rdAccionesSi.setText("Si");

        groupAcciones.add(rdAccionesNo);
        rdAccionesNo.setText("No");
        rdAccionesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAccionesNoActionPerformed(evt);
            }
        });

        lblIdPromocion.setText("28");

        cboAcciones.setEditable(true);

        groupPracPropicias.add(rdPracPropiciasNo);
        rdPracPropiciasNo.setText("No");
        rdPracPropiciasNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPracPropiciasNoActionPerformed(evt);
            }
        });

        groupPracPropicias.add(rdPracPropiciasSi);
        rdPracPropiciasSi.setText("Si");

        jLabel11.setText("Prácticas propiciadas ");

        lblIdPromocion1.setText("29");

        cboPracPropicias.setEditable(true);

        jLabel12.setText("Incorporación de otras instituciones");

        cboIncorporacion.setEditable(true);

        groupIncorporacion.add(rdIncorporacionNo);
        rdIncorporacionNo.setText("No");
        rdIncorporacionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdIncorporacionNoActionPerformed(evt);
            }
        });

        lblIdPromocion2.setText("30");

        groupIncorporacion.add(rdincorporacionSi);
        rdincorporacionSi.setText("Si");

        cboDifGrupos.setEditable(true);

        jLabel13.setText("Diferentes grupos sociales incorporados al proceso ");

        groupDifGrupos.add(rdDifGruposSi);
        rdDifGruposSi.setText("Si");

        groupDifGrupos.add(rdDifGruposNo);
        rdDifGruposNo.setText("No");
        rdDifGruposNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDifGruposNoActionPerformed(evt);
            }
        });

        lblIdPromocion3.setText("31");

        javax.swing.GroupLayout pnlPromocionLayout = new javax.swing.GroupLayout(pnlPromocion);
        pnlPromocion.setLayout(pnlPromocionLayout);
        pnlPromocionLayout.setHorizontalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdAccionesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdAccionesNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtPracPropicias, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdPracPropiciasSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdPracPropiciasNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtIncorporacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdincorporacionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdIncorporacionNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtDifGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdDifGruposSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDifGruposNo))
                    .addComponent(cboAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPracPropicias, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIncorporacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDifGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPromocionLayout.setVerticalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdAccionesSi)
                    .addComponent(rdAccionesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPracPropicias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdPracPropiciasSi)
                    .addComponent(rdPracPropiciasNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboPracPropicias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion2)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIncorporacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdincorporacionSi)
                    .addComponent(rdIncorporacionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboIncorporacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion3)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDifGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDifGruposSi)
                    .addComponent(rdDifGruposNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDifGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlNormalizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Convergencia"));

        jLabel6.setText("Otras prácticas coinciden en la misma.");

        groupOtrasPrac.add(rdOtrasPracSi);
        rdOtrasPracSi.setText("Si");

        groupOtrasPrac.add(rdOtrasPracNo);
        rdOtrasPracNo.setText("No");
        rdOtrasPracNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdOtrasPracNoActionPerformed(evt);
            }
        });

        lblIdNormalizacion.setText("32");

        cboOtrasPrac.setEditable(true);

        javax.swing.GroupLayout pnlNormalizacionLayout = new javax.swing.GroupLayout(pnlNormalizacion);
        pnlNormalizacion.setLayout(pnlNormalizacionLayout);
        pnlNormalizacionLayout.setHorizontalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(lblIdNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtOtrasPrac, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdOtrasPracSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdOtrasPracNo))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboOtrasPrac, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        pnlNormalizacionLayout.setVerticalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblIdNormalizacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOtrasPrac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdOtrasPracSi)
                    .addComponent(rdOtrasPracNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboOtrasPrac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlPromocion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlMantenimientoTiempo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlMantenimientoTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PnlCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNGuardarActionPerformed
       imprimirarray();
       if(pregutarEvaluacion("Replicabilidad") == 0){
            abm(1);
       }else{
           int dia = JOptionPane.showConfirmDialog(null, "Confirmación", "Esta práctica ya fue evaluada, desea modificar la evaluación", WIDTH);
           if(dia == JOptionPane.YES_OPTION){
               
           }
       }
    }//GEN-LAST:event_btNGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        selecDefecto(true);
        txtOtrasPrac.setText(null);
        txtSistemtatizacion.setText(null);
        txtAcciones.setText(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rdDifGruposNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDifGruposNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDifGruposNoActionPerformed

    private void rdIncorporacionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdIncorporacionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdIncorporacionNoActionPerformed

    private void rdPracPropiciasNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPracPropiciasNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPracPropiciasNoActionPerformed

    private void rdAccionesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAccionesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAccionesNoActionPerformed

    private void rdDifusionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDifusionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDifusionNoActionPerformed

    private void rdDifusionSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDifusionSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDifusionSiActionPerformed

    private void rdDisenoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDisenoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDisenoNoActionPerformed

    private void rdDisenoSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDisenoSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDisenoSiActionPerformed

    private void rdSistemtatizacionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSistemtatizacionNoActionPerformed
        
    }//GEN-LAST:event_rdSistemtatizacionNoActionPerformed

    private void rdSistemtatizacionSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSistemtatizacionSiActionPerformed
        
    }//GEN-LAST:event_rdSistemtatizacionSiActionPerformed

    private void rdOtrasPracNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdOtrasPracNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdOtrasPracNoActionPerformed

    private void rdEspaciosSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEspaciosSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEspaciosSiActionPerformed

    private void rdEspaciosNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEspaciosNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEspaciosNoActionPerformed

    private void imprimirarray(){
        for (int i = 0; i < this.observacion.length; i++) {
           System.out.println(cargaVariable("Replicabilidad")[i]); 
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
            java.util.logging.Logger.getLogger(FrmReplicabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmReplicabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmReplicabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmReplicabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmReplicabilidad().setVisible(true);
              
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlCriterio;
    private javax.swing.ButtonGroup btGPertinencia1;
    private javax.swing.ButtonGroup btGPertinencia2;
    private javax.swing.ButtonGroup btGPertinencia3;
    private javax.swing.JButton btNGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalir;
    private org.jdesktop.swingx.JXComboBox cboAcciones;
    private org.jdesktop.swingx.JXComboBox cboDifGrupos;
    private org.jdesktop.swingx.JXComboBox cboDifusion;
    private org.jdesktop.swingx.JXComboBox cboDiseno;
    private org.jdesktop.swingx.JXComboBox cboEspacios;
    private org.jdesktop.swingx.JXComboBox cboIncorporacion;
    private org.jdesktop.swingx.JXComboBox cboOtrasPrac;
    private org.jdesktop.swingx.JXComboBox cboPracPropicias;
    private org.jdesktop.swingx.JXComboBox cboSistemtatizacion;
    private javax.swing.ButtonGroup groupAcciones;
    private javax.swing.ButtonGroup groupDifGrupos;
    private javax.swing.ButtonGroup groupDifusion;
    private javax.swing.ButtonGroup groupDiseno;
    private javax.swing.ButtonGroup groupEspacios;
    private javax.swing.ButtonGroup groupIncorporacion;
    private javax.swing.ButtonGroup groupOtrasPrac;
    private javax.swing.ButtonGroup groupPracPropicias;
    private javax.swing.ButtonGroup grupSistemtatizacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCriterio;
    private javax.swing.JLabel lblIdNormalizacion;
    private javax.swing.JLabel lblIdNoveadd2;
    private javax.swing.JLabel lblIdNoveadd3;
    private javax.swing.JLabel lblIdNoveadd4;
    private javax.swing.JLabel lblIdNoveadd5;
    private javax.swing.JLabel lblIdPromocion;
    private javax.swing.JLabel lblIdPromocion1;
    private javax.swing.JLabel lblIdPromocion2;
    private javax.swing.JLabel lblIdPromocion3;
    private javax.swing.JLabel lblSiete;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlMantenimientoTiempo;
    private javax.swing.JPanel pnlNormalizacion;
    private javax.swing.JPanel pnlPromocion;
    private javax.swing.JRadioButton rdAccionesNo;
    private javax.swing.JRadioButton rdAccionesSi;
    private javax.swing.JRadioButton rdDifGruposNo;
    private javax.swing.JRadioButton rdDifGruposSi;
    private javax.swing.JRadioButton rdDifusionNo;
    private javax.swing.JRadioButton rdDifusionSi;
    private javax.swing.JRadioButton rdDisenoNo;
    private javax.swing.JRadioButton rdDisenoSi;
    private javax.swing.JRadioButton rdEspaciosNo;
    private javax.swing.JRadioButton rdEspaciosSi;
    private javax.swing.JRadioButton rdIncorporacionNo;
    private javax.swing.JRadioButton rdOtrasPracNo;
    private javax.swing.JRadioButton rdOtrasPracSi;
    private javax.swing.JRadioButton rdPracPropiciasNo;
    private javax.swing.JRadioButton rdPracPropiciasSi;
    private javax.swing.JRadioButton rdSistemtatizacionNo;
    private javax.swing.JRadioButton rdSistemtatizacionSi;
    private javax.swing.JRadioButton rdincorporacionSi;
    private javax.swing.JTextField txtAcciones;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtDifGrupos;
    private javax.swing.JTextField txtDifusion;
    private javax.swing.JTextField txtDiseno;
    private javax.swing.JTextField txtEspacios;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIncorporacion;
    private javax.swing.JTextField txtOtrasPrac;
    private javax.swing.JTextField txtPracPropicias;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextField txtSistemtatizacion;
    // End of variables declaration//GEN-END:variables
}
