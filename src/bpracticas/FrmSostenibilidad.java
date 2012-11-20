/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.sql.SQLException;
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
public class FrmSostenibilidad extends javax.swing.JFrame {
    int respuesta = 0;
    Conexion z = new Conexion();
    
   
    private String[] observacion = new String[6];

    public String[] getObservacion() {
        return observacion;
    }
    
    /**
     * Creates new form FrmInnovacion
     */
    public FrmSostenibilidad() {
        initComponents();
        txtCriterio.setText("Sostenibilidad");
        if(FrmPrincipal.getEvaluar()!= 0){
            txtId.setText(FrmPrincipal.getEvaluar()+"");
            configPractica();
            selecDefecto(true);
        }
        recoCombo(listarRecomendaciones(), cboEcoPresupuesto);
        recoCombo(listarRecomendaciones(), cboEcoPresupuesto);
        recoCombo(listarRecomendaciones(), txtNormaJustifica);
        recoCombo(listarRecomendaciones(), cboAumentPartici);
        recoCombo(listarRecomendaciones(), cboAdapContentGrupo);
        recoCombo(listarRecomendaciones(), cboAdecEspacio);
        recoCombo(listarRecomendaciones(), txtNormaJustifica);
        
    }

    private void selecDefecto(boolean si){
        rdEcoPresuNO.setSelected(si);
        rdbEstructuraNo.setSelected(si);
        rdAumentParticiNo.setSelected(si);
        rdAdaptMetoNo.setSelected(si);
        rdAdapContentGrupoNo.setSelected(si);
        rdNormaJusNO.setSelected(si);
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
            Logger.getLogger(FrmSostenibilidad.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmSostenibilidad.class.getName()).log(Level.SEVERE, null, ex);
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
        
        int novedad =0;
        int promocion =0;
        int norma = 0;
        int meto =0;
        int veng = 0;
        int trans =0;
        
        if(rdEcoPresuSi.isSelected() && !rdEcoPresuNO.isSelected()){
            norma = 1;
        }
        
        if(rdbEstricutraSi.isSelected() && !rdbEstructuraNo.isSelected()){
            novedad = 1;
        }
        
        if(rdNormaJusSi.isSelected() && !rdNormaJusNO.isSelected()){
            promocion = 1;
        }
        
        if(rdAumentParticiSi.isSelected() && !rdAumentParticiNo.isSelected()){
            meto = 1;
        }
        
        if(rdAdapContentGrupoSi.isSelected() && !rdAdapContentGrupoNo.isSelected()){
            veng = 1;
        }
        
        if(rdAdaptMetoSi.isSelected() && !rdAdaptMetoNo.isSelected()){
            trans = 1;
        }
        
        try {
            //insertar evaluacion 1
            int bnreco = saberRecomendacion(cboAntecendentes, 0);
            String sql = "CALL revaluacion("+accion+", 1,"+txtId.getText().toString()+",'"+txtEstructuraPrevia.getText().toString()+"', '"+cargaVariable("Innovacion")[0]+"',"+novedad+", "+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
            
            
            //insertar evaluacion 2
            bnreco = saberRecomendacion(txtNormaJustifica, 0);
            sql = "CALL revaluacion("+accion+", 2,"+txtId.getText().toString()+",'"+txtNormaJusticia.getText().toString()+"', '"+cargaVariable("Innovacion")[1]+"',"+promocion+","+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
            
            
            //insertar evaluación 3
            bnreco = saberRecomendacion(cboEcoPresupuesto, 0);
            sql = "CALL revaluacion("+accion+", 3,"+txtId.getText().toString()+",'"+txtPresuAsignado.getText().toString()+"', '"+cargaVariable("Innovacion")[2]+"',"+norma+","+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
            
            
            //insertar evaluacion 4
            bnreco = saberRecomendacion(cboAumentPartici, 0);
            sql = "CALL revaluacion("+accion+", 4,"+txtId.getText().toString()+",'"+txtAumentPartici.getText().toString()+"', '"+cargaVariable("Innovacion")[3]+"',"+meto+","+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
            
                
            //insertar evaluacion 5
            bnreco = saberRecomendacion(cboAdapContentGrupo, 0);
            sql = "CALL revaluacion("+accion+", 5,"+txtId.getText().toString()+",'"+txtAdapContentGrupo.getText().toString()+"', '"+cargaVariable("Innovacion")[4]+"',"+veng+","+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
            
            
            //insertar evaluación 6
            bnreco = saberRecomendacion(cboAdecEspacio, 0);
            sql = "CALL revaluacion("+accion+", 6,"+txtId.getText().toString()+",'"+txtAdecEspacio.getText().toString()+"', '"+cargaVariable("Innovacion")[5]+"',"+trans+","+bnreco+");";
            System.out.println(sql);
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(FrmSostenibilidad.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmSostenibilidad.class.getName()).log(Level.SEVERE, null, ex);
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

        btGEstructura = new javax.swing.ButtonGroup();
        groupEdiciones = new javax.swing.ButtonGroup();
        groupAntecedentes = new javax.swing.ButtonGroup();
        groupNormaJus = new javax.swing.ButtonGroup();
        groupMarcoJuris = new javax.swing.ButtonGroup();
        grupEstruDefin = new javax.swing.ButtonGroup();
        groupCoheInt = new javax.swing.ButtonGroup();
        groupDisenoEvaluacion = new javax.swing.ButtonGroup();
        groupSistemaSeguimiento = new javax.swing.ButtonGroup();
        groupEcoPresu = new javax.swing.ButtonGroup();
        groupEcoRubro = new javax.swing.ButtonGroup();
        groupDineroDisp = new javax.swing.ButtonGroup();
        groupCosteActividad = new javax.swing.ButtonGroup();
        gruopAumentPartici = new javax.swing.ButtonGroup();
        groupAdapContentGrupo = new javax.swing.ButtonGroup();
        groupAdecEspacio = new javax.swing.ButtonGroup();
        groupAdaptMeto = new javax.swing.ButtonGroup();
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
        txtEstructuraPrevia = new javax.swing.JTextField();
        lblSiete = new javax.swing.JLabel();
        rdbEstricutraSi = new javax.swing.JRadioButton();
        rdbEstructuraNo = new javax.swing.JRadioButton();
        lblIdNoveadd2 = new javax.swing.JLabel();
        cboEstructuraPrevia = new org.jdesktop.swingx.JXComboBox();
        txtEdiciones = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rdEdicionesSi = new javax.swing.JRadioButton();
        rdEdicionesNo = new javax.swing.JRadioButton();
        lblIdNoveadd3 = new javax.swing.JLabel();
        cboEdiciones = new org.jdesktop.swingx.JXComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtAntecedentes = new javax.swing.JTextField();
        rdNovedadSi2 = new javax.swing.JRadioButton();
        rdNovedadNo2 = new javax.swing.JRadioButton();
        cboAntecendentes = new org.jdesktop.swingx.JXComboBox();
        lblIdNoveadd4 = new javax.swing.JLabel();
        pnlPromocion = new javax.swing.JPanel();
        txtNormaJusticia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdNormaJusSi = new javax.swing.JRadioButton();
        rdNormaJusNO = new javax.swing.JRadioButton();
        lblIdPromocion = new javax.swing.JLabel();
        txtNormaJustifica = new org.jdesktop.swingx.JXComboBox();
        rdMarcoJurNo = new javax.swing.JRadioButton();
        rdMarcoJurSi = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        lblIdPromocion1 = new javax.swing.JLabel();
        txtMarcoJuridiccional = new javax.swing.JTextField();
        cboMarcoJuridiccional = new org.jdesktop.swingx.JXComboBox();
        jLabel12 = new javax.swing.JLabel();
        cboPromocion2 = new org.jdesktop.swingx.JXComboBox();
        txtEstrucDifinia = new javax.swing.JTextField();
        rdEstrucDefinNo = new javax.swing.JRadioButton();
        lblIdPromocion2 = new javax.swing.JLabel();
        rdEstrucDefinSi = new javax.swing.JRadioButton();
        cboCoheInter = new org.jdesktop.swingx.JXComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtCoheInterna = new javax.swing.JTextField();
        rdCoheSi = new javax.swing.JRadioButton();
        rdCoheNO = new javax.swing.JRadioButton();
        lblIdPromocion3 = new javax.swing.JLabel();
        lblIdPromocion4 = new javax.swing.JLabel();
        rdDisenoEvaluacionNO = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        rdDisenoEvaluacionSi = new javax.swing.JRadioButton();
        txtDisenoEvaluacion = new javax.swing.JTextField();
        cboDisenoEvaluacion = new org.jdesktop.swingx.JXComboBox();
        txtSistemaSeguimiento = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lblIdPromocion5 = new javax.swing.JLabel();
        rdSistemaSeguimientoNo = new javax.swing.JRadioButton();
        rdSistemaSeguimientoSi = new javax.swing.JRadioButton();
        cboSistemaSeguimiento = new org.jdesktop.swingx.JXComboBox();
        pnlNormalizacion = new javax.swing.JPanel();
        txtPresuAsignado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdEcoPresuSi = new javax.swing.JRadioButton();
        rdEcoPresuNO = new javax.swing.JRadioButton();
        lblIdNormalizacion = new javax.swing.JLabel();
        cboEcoPresupuesto = new org.jdesktop.swingx.JXComboBox();
        jLabel17 = new javax.swing.JLabel();
        lblIdNormalizacion1 = new javax.swing.JLabel();
        rdEcoRubroSi = new javax.swing.JRadioButton();
        rdEcoRubroNo = new javax.swing.JRadioButton();
        txtEcoRubro = new javax.swing.JTextField();
        cboEcoRubro = new org.jdesktop.swingx.JXComboBox();
        txtDineroDisp = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        lblIdNormalizacion2 = new javax.swing.JLabel();
        rdDineroDispSi = new javax.swing.JRadioButton();
        rdDineroDispNo = new javax.swing.JRadioButton();
        cboDineroDisp = new org.jdesktop.swingx.JXComboBox();
        jLabel19 = new javax.swing.JLabel();
        lblIdNormalizacion3 = new javax.swing.JLabel();
        txtCosteActividad = new javax.swing.JTextField();
        cboCosteActividad = new org.jdesktop.swingx.JXComboBox();
        rdCosteActividadSi = new javax.swing.JRadioButton();
        rdCosteActividadNo = new javax.swing.JRadioButton();
        pnlPertinencia = new javax.swing.JPanel();
        txtAumentPartici = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rdAumentParticiSi = new javax.swing.JRadioButton();
        rdAumentParticiNo = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        txtAdapContentGrupo = new javax.swing.JTextField();
        rdAdapContentGrupoSi = new javax.swing.JRadioButton();
        rdAdapContentGrupoNo = new javax.swing.JRadioButton();
        rdAdaptMetoNo = new javax.swing.JRadioButton();
        rdAdaptMetoSi = new javax.swing.JRadioButton();
        txtAdecEspacio = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        lblIdPer4 = new javax.swing.JLabel();
        lblPer5 = new javax.swing.JLabel();
        lblPer6 = new javax.swing.JLabel();
        cboAumentPartici = new org.jdesktop.swingx.JXComboBox();
        cboAdapContentGrupo = new org.jdesktop.swingx.JXComboBox();
        cboAdecEspacio = new org.jdesktop.swingx.JXComboBox();
        rdAdecEspacioNo = new javax.swing.JRadioButton();
        rdAdecEspacioSi = new javax.swing.JRadioButton();
        jLabel20 = new javax.swing.JLabel();
        lblPer7 = new javax.swing.JLabel();
        txtAdaptMeto = new javax.swing.JTextField();
        cboAdaptMeto = new org.jdesktop.swingx.JXComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Evaluación del criterio sostenibilidad");

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

        pnlMantenimientoTiempo.setBorder(javax.swing.BorderFactory.createTitledBorder("Mantenimiento en el tiempo"));

        lblSiete.setText("Inserto en estructura previa");

        btGEstructura.add(rdbEstricutraSi);
        rdbEstricutraSi.setText("Si");
        rdbEstricutraSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbEstricutraSiActionPerformed(evt);
            }
        });

        btGEstructura.add(rdbEstructuraNo);
        rdbEstructuraNo.setText("No");
        rdbEstructuraNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbEstructuraNoActionPerformed(evt);
            }
        });

        lblIdNoveadd2.setText("7");

        cboEstructuraPrevia.setEditable(true);

        jLabel9.setText("Ediciones: Ocasiones en que se ha realizado la práctica anteriormente.");

        groupEdiciones.add(rdEdicionesSi);
        rdEdicionesSi.setText("Si");
        rdEdicionesSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEdicionesSiActionPerformed(evt);
            }
        });

        groupEdiciones.add(rdEdicionesNo);
        rdEdicionesNo.setText("No");
        rdEdicionesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEdicionesNoActionPerformed(evt);
            }
        });

        lblIdNoveadd3.setText("8");

        cboEdiciones.setEditable(true);

        jLabel10.setText("Antecedentes: prácticas anteriores precursoras de la actividad.");

        groupAntecedentes.add(rdNovedadSi2);
        rdNovedadSi2.setText("Si");
        rdNovedadSi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNovedadSi2ActionPerformed(evt);
            }
        });

        groupAntecedentes.add(rdNovedadNo2);
        rdNovedadNo2.setText("No");
        rdNovedadNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNovedadNo2ActionPerformed(evt);
            }
        });

        cboAntecendentes.setEditable(true);

        lblIdNoveadd4.setText("9");

        javax.swing.GroupLayout pnlMantenimientoTiempoLayout = new javax.swing.GroupLayout(pnlMantenimientoTiempo);
        pnlMantenimientoTiempo.setLayout(pnlMantenimientoTiempoLayout);
        pnlMantenimientoTiempoLayout.setHorizontalGroup(
            pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEstructuraPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboEstructuraPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                                .addComponent(lblIdNoveadd2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblSiete)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdbEstricutraSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdbEstructuraNo))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(cboAntecendentes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtAntecedentes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdNovedadSi2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdNovedadNo2))
                    .addComponent(cboEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdEdicionesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEdicionesNo)))
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
                    .addComponent(txtEstructuraPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdbEstricutraSi)
                    .addComponent(rdbEstructuraNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboEstructuraPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdNoveadd3)
                    .addComponent(jLabel9))
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEdicionesSi)
                    .addComponent(rdEdicionesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblIdNoveadd4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAntecedentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdNovedadSi2)
                    .addComponent(rdNovedadNo2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboAntecendentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlPromocion.setBorder(javax.swing.BorderFactory.createTitledBorder("Viabilidad técnica"));

        jLabel5.setText("Existe norma que la justifica.");

        groupNormaJus.add(rdNormaJusSi);
        rdNormaJusSi.setText("Si");

        rdNormaJusNO.setText("No");
        rdNormaJusNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNormaJusNOActionPerformed(evt);
            }
        });

        lblIdPromocion.setText("10");

        txtNormaJustifica.setEditable(true);

        groupMarcoJuris.add(rdMarcoJurNo);
        rdMarcoJurNo.setText("No");
        rdMarcoJurNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMarcoJurNoActionPerformed(evt);
            }
        });

        groupMarcoJuris.add(rdMarcoJurSi);
        rdMarcoJurSi.setText("Si");
        rdMarcoJurSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMarcoJurSiActionPerformed(evt);
            }
        });

        jLabel11.setText("Existe marco jurisdiccional que la  regula");

        lblIdPromocion1.setText("11");

        cboMarcoJuridiccional.setEditable(true);

        jLabel12.setText("Tiene una estructura definida.");

        cboPromocion2.setEditable(true);

        grupEstruDefin.add(rdEstrucDefinNo);
        rdEstrucDefinNo.setText("No");
        rdEstrucDefinNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEstrucDefinNoActionPerformed(evt);
            }
        });

        lblIdPromocion2.setText("12");

        grupEstruDefin.add(rdEstrucDefinSi);
        rdEstrucDefinSi.setText("Si");

        cboCoheInter.setEditable(true);

        jLabel13.setText("Coherencia interna.");

        groupCoheInt.add(rdCoheSi);
        rdCoheSi.setText("Si");

        groupCoheInt.add(rdCoheNO);
        rdCoheNO.setText("No");
        rdCoheNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCoheNOActionPerformed(evt);
            }
        });

        lblIdPromocion3.setText("13");

        lblIdPromocion4.setText("14");

        groupDisenoEvaluacion.add(rdDisenoEvaluacionNO);
        rdDisenoEvaluacionNO.setText("No");
        rdDisenoEvaluacionNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDisenoEvaluacionNOActionPerformed(evt);
            }
        });

        jLabel14.setText("Existe un diseño de evaluación");

        groupDisenoEvaluacion.add(rdDisenoEvaluacionSi);
        rdDisenoEvaluacionSi.setText("Si");

        cboDisenoEvaluacion.setEditable(true);

        jLabel15.setText("Existe un sistema de seguimiento de acuerdos, decisiones");

        lblIdPromocion5.setText("15");

        groupSistemaSeguimiento.add(rdSistemaSeguimientoNo);
        rdSistemaSeguimientoNo.setText("No");
        rdSistemaSeguimientoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSistemaSeguimientoNoActionPerformed(evt);
            }
        });

        groupSistemaSeguimiento.add(rdSistemaSeguimientoSi);
        rdSistemaSeguimientoSi.setText("Si");

        cboSistemaSeguimiento.setEditable(true);

        javax.swing.GroupLayout pnlPromocionLayout = new javax.swing.GroupLayout(pnlPromocion);
        pnlPromocion.setLayout(pnlPromocionLayout);
        pnlPromocionLayout.setHorizontalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtNormaJusticia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdNormaJusSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdNormaJusNO))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdMarcoJurSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdMarcoJurNo))
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
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtEstrucDifinia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdEstrucDefinSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEstrucDefinNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdCoheSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdCoheNO))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtDisenoEvaluacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdDisenoEvaluacionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDisenoEvaluacionNO))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtSistemaSeguimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdSistemaSeguimientoSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdSistemaSeguimientoNo))
                    .addComponent(txtNormaJustifica, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPromocion2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCoheInter, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDisenoEvaluacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSistemaSeguimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
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
                    .addComponent(txtNormaJusticia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdNormaJusSi)
                    .addComponent(rdNormaJusNO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNormaJustifica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdMarcoJurSi)
                    .addComponent(rdMarcoJurNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion2)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstrucDifinia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEstrucDefinSi)
                    .addComponent(rdEstrucDefinNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboPromocion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion3)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdCoheSi)
                    .addComponent(rdCoheNO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCoheInter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion4)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDisenoEvaluacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDisenoEvaluacionSi)
                    .addComponent(rdDisenoEvaluacionNO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDisenoEvaluacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion5)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSistemaSeguimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdSistemaSeguimientoSi)
                    .addComponent(rdSistemaSeguimientoNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboSistemaSeguimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlNormalizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Viabilidad económica"));

        jLabel6.setText("Presupuesto asignado");

        groupEcoPresu.add(rdEcoPresuSi);
        rdEcoPresuSi.setText("Si");

        groupEcoPresu.add(rdEcoPresuNO);
        rdEcoPresuNO.setText("No");
        rdEcoPresuNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEcoPresuNOActionPerformed(evt);
            }
        });

        lblIdNormalizacion.setText("16");

        cboEcoPresupuesto.setEditable(true);

        jLabel17.setText("creación o liberación de rubros.");

        lblIdNormalizacion1.setText("17");

        groupEcoRubro.add(rdEcoRubroSi);
        rdEcoRubroSi.setText("Si");

        groupEcoRubro.add(rdEcoRubroNo);
        rdEcoRubroNo.setText("No");
        rdEcoRubroNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEcoRubroNoActionPerformed(evt);
            }
        });

        cboEcoRubro.setEditable(true);

        jLabel18.setText("(Dinero dispuesto y disponible en la ejecución)");

        lblIdNormalizacion2.setText("18");

        groupDineroDisp.add(rdDineroDispSi);
        rdDineroDispSi.setText("Si");

        groupDineroDisp.add(rdDineroDispNo);
        rdDineroDispNo.setText("No");
        rdDineroDispNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDineroDispNoActionPerformed(evt);
            }
        });

        cboDineroDisp.setEditable(true);

        jLabel19.setText("Coste de la actividad.");

        lblIdNormalizacion3.setText("19");

        cboCosteActividad.setEditable(true);

        groupCosteActividad.add(rdCosteActividadSi);
        rdCosteActividadSi.setText("Si");

        groupCosteActividad.add(rdCosteActividadNo);
        rdCosteActividadNo.setText("No");
        rdCosteActividadNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCosteActividadNoActionPerformed(evt);
            }
        });

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
                            .addComponent(txtPresuAsignado, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEcoPresuSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEcoPresuNO))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboEcoPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboEcoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblIdNormalizacion2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboDineroDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblIdNormalizacion3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtCosteActividad, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboCosteActividad, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblIdNormalizacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEcoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDineroDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(rdDineroDispSi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdDineroDispNo))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(rdCosteActividadSi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdCosteActividadNo))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(rdEcoRubroSi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdEcoRubroNo)))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnlNormalizacionLayout.setVerticalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblIdNormalizacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPresuAsignado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEcoPresuSi)
                    .addComponent(rdEcoPresuNO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEcoPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblIdNormalizacion1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEcoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEcoRubroSi)
                    .addComponent(rdEcoRubroNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEcoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblIdNormalizacion2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDineroDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDineroDispSi)
                    .addComponent(rdDineroDispNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDineroDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(lblIdNormalizacion3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCosteActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdCosteActividadSi)
                    .addComponent(rdCosteActividadNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCosteActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlPertinencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Viabilidad social"));

        jLabel7.setText("Aumento de participantes");

        gruopAumentPartici.add(rdAumentParticiSi);
        rdAumentParticiSi.setText("Si");

        gruopAumentPartici.add(rdAumentParticiNo);
        rdAumentParticiNo.setText("No");
        rdAumentParticiNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAumentParticiNoActionPerformed(evt);
            }
        });

        jLabel8.setText("Adaptación del contenido a grupo social");

        groupAdapContentGrupo.add(rdAdapContentGrupoSi);
        rdAdapContentGrupoSi.setText("Si");

        groupAdapContentGrupo.add(rdAdapContentGrupoNo);
        rdAdapContentGrupoNo.setText("No");
        rdAdapContentGrupoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdapContentGrupoNoActionPerformed(evt);
            }
        });

        groupAdaptMeto.add(rdAdaptMetoNo);
        rdAdaptMetoNo.setText("No");
        rdAdaptMetoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdaptMetoNoActionPerformed(evt);
            }
        });

        groupAdaptMeto.add(rdAdaptMetoSi);
        rdAdaptMetoSi.setText("Si");

        jLabel16.setText("Adecuación del espacio físico.");

        lblIdPer4.setText("20");

        lblPer5.setText("21");

        lblPer6.setText("22");

        cboAumentPartici.setEditable(true);

        cboAdapContentGrupo.setEditable(true);

        cboAdecEspacio.setEditable(true);

        groupAdecEspacio.add(rdAdecEspacioNo);
        rdAdecEspacioNo.setText("No");
        rdAdecEspacioNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdecEspacioNoActionPerformed(evt);
            }
        });

        groupAdecEspacio.add(rdAdecEspacioSi);
        rdAdecEspacioSi.setText("Si");

        jLabel20.setText("Adaptación metodológica al contexto local.");

        lblPer7.setText("23");

        cboAdaptMeto.setEditable(true);

        javax.swing.GroupLayout pnlPertinenciaLayout = new javax.swing.GroupLayout(pnlPertinencia);
        pnlPertinencia.setLayout(pnlPertinenciaLayout);
        pnlPertinenciaLayout.setHorizontalGroup(
            pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addComponent(lblPer5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addComponent(lblPer6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboAdecEspacio, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAdecEspacio, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboAdaptMeto, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPertinenciaLayout.createSequentialGroup()
                                    .addComponent(txtAdaptMeto, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                    .addComponent(rdAdaptMetoSi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rdAdaptMetoNo))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPertinenciaLayout.createSequentialGroup()
                                    .addComponent(lblPer7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPertinenciaLayout.createSequentialGroup()
                            .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtAumentPartici, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                            .addComponent(lblIdPer4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(cboAumentPartici, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(cboAdapContentGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtAdapContentGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                    .addComponent(rdAdapContentGrupoSi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rdAdapContentGrupoNo))
                                .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                    .addComponent(rdAumentParticiSi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rdAumentParticiNo))
                                .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                    .addComponent(rdAdecEspacioSi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rdAdecEspacioNo))))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        pnlPertinenciaLayout.setVerticalGroup(
            pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblIdPer4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAumentPartici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdAumentParticiSi)
                    .addComponent(rdAumentParticiNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAumentPartici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblPer5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdAdapContentGrupoSi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rdAdapContentGrupoNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtAdapContentGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAdapContentGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblPer6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAdecEspacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdAdecEspacioSi)
                    .addComponent(rdAdecEspacioNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAdecEspacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblPer7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAdaptMeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdAdaptMetoSi)
                    .addComponent(rdAdaptMetoNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAdaptMeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMantenimientoTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlPertinencia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlNormalizacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlMantenimientoTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPertinencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PnlCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNGuardarActionPerformed
       imprimirarray();
       if(pregutarEvaluacion("Innovación") == 0){
            abm(1);
       }else{
           int dia = JOptionPane.showConfirmDialog(null, "Confirmación", "Esta práctica ya fue evaluada, desea modificar la evaluación", WIDTH);
           if(dia == JOptionPane.YES_OPTION){
               
           }
       }
    }//GEN-LAST:event_btNGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        selecDefecto(true);
        txtPresuAsignado.setText(null);
        txtEstructuraPrevia.setText(null);
        txtAdecEspacio.setText(null);
        txtAumentPartici.setText(null);
        txtNormaJusticia.setText(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rdSistemaSeguimientoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSistemaSeguimientoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdSistemaSeguimientoNoActionPerformed

    private void rdDisenoEvaluacionNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDisenoEvaluacionNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDisenoEvaluacionNOActionPerformed

    private void rdCoheNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdCoheNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdCoheNOActionPerformed

    private void rdEstrucDefinNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEstrucDefinNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEstrucDefinNoActionPerformed

    private void rdMarcoJurNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMarcoJurNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdMarcoJurNoActionPerformed

    private void rdNormaJusNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNormaJusNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNormaJusNOActionPerformed

    private void rdNovedadNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNovedadNo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNovedadNo2ActionPerformed

    private void rdNovedadSi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNovedadSi2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNovedadSi2ActionPerformed

    private void rdEdicionesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEdicionesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEdicionesNoActionPerformed

    private void rdEdicionesSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEdicionesSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEdicionesSiActionPerformed

    private void rdbEstructuraNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbEstructuraNoActionPerformed
        
    }//GEN-LAST:event_rdbEstructuraNoActionPerformed

    private void rdbEstricutraSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbEstricutraSiActionPerformed
        
    }//GEN-LAST:event_rdbEstricutraSiActionPerformed

    private void rdEcoPresuNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEcoPresuNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEcoPresuNOActionPerformed

    private void rdAdaptMetoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdaptMetoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAdaptMetoNoActionPerformed

    private void rdAdapContentGrupoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdapContentGrupoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAdapContentGrupoNoActionPerformed

    private void rdAumentParticiNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAumentParticiNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAumentParticiNoActionPerformed

    private void rdEcoRubroNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEcoRubroNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEcoRubroNoActionPerformed

    private void rdDineroDispNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDineroDispNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDineroDispNoActionPerformed

    private void rdCosteActividadNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdCosteActividadNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdCosteActividadNoActionPerformed

    private void rdAdecEspacioNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdecEspacioNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAdecEspacioNoActionPerformed

    private void rdMarcoJurSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMarcoJurSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdMarcoJurSiActionPerformed

    private void imprimirarray(){
        for (int i = 0; i < this.observacion.length; i++) {
           System.out.println(cargaVariable("innovacion")[i]); 
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
            java.util.logging.Logger.getLogger(FrmSostenibilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmSostenibilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmSostenibilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmSostenibilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmSostenibilidad().setVisible(true);
              
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlCriterio;
    private javax.swing.ButtonGroup btGEstructura;
    private javax.swing.JButton btNGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalir;
    private org.jdesktop.swingx.JXComboBox cboAdapContentGrupo;
    private org.jdesktop.swingx.JXComboBox cboAdaptMeto;
    private org.jdesktop.swingx.JXComboBox cboAdecEspacio;
    private org.jdesktop.swingx.JXComboBox cboAntecendentes;
    private org.jdesktop.swingx.JXComboBox cboAumentPartici;
    private org.jdesktop.swingx.JXComboBox cboCoheInter;
    private org.jdesktop.swingx.JXComboBox cboCosteActividad;
    private org.jdesktop.swingx.JXComboBox cboDineroDisp;
    private org.jdesktop.swingx.JXComboBox cboDisenoEvaluacion;
    private org.jdesktop.swingx.JXComboBox cboEcoPresupuesto;
    private org.jdesktop.swingx.JXComboBox cboEcoRubro;
    private org.jdesktop.swingx.JXComboBox cboEdiciones;
    private org.jdesktop.swingx.JXComboBox cboEstructuraPrevia;
    private org.jdesktop.swingx.JXComboBox cboMarcoJuridiccional;
    private org.jdesktop.swingx.JXComboBox cboPromocion2;
    private org.jdesktop.swingx.JXComboBox cboSistemaSeguimiento;
    private javax.swing.ButtonGroup groupAdapContentGrupo;
    private javax.swing.ButtonGroup groupAdaptMeto;
    private javax.swing.ButtonGroup groupAdecEspacio;
    private javax.swing.ButtonGroup groupAntecedentes;
    private javax.swing.ButtonGroup groupCoheInt;
    private javax.swing.ButtonGroup groupCosteActividad;
    private javax.swing.ButtonGroup groupDineroDisp;
    private javax.swing.ButtonGroup groupDisenoEvaluacion;
    private javax.swing.ButtonGroup groupEcoPresu;
    private javax.swing.ButtonGroup groupEcoRubro;
    private javax.swing.ButtonGroup groupEdiciones;
    private javax.swing.ButtonGroup groupMarcoJuris;
    private javax.swing.ButtonGroup groupNormaJus;
    private javax.swing.ButtonGroup groupSistemaSeguimiento;
    private javax.swing.ButtonGroup gruopAumentPartici;
    private javax.swing.ButtonGroup grupEstruDefin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCriterio;
    private javax.swing.JLabel lblIdNormalizacion;
    private javax.swing.JLabel lblIdNormalizacion1;
    private javax.swing.JLabel lblIdNormalizacion2;
    private javax.swing.JLabel lblIdNormalizacion3;
    private javax.swing.JLabel lblIdNoveadd2;
    private javax.swing.JLabel lblIdNoveadd3;
    private javax.swing.JLabel lblIdNoveadd4;
    private javax.swing.JLabel lblIdPer4;
    private javax.swing.JLabel lblIdPromocion;
    private javax.swing.JLabel lblIdPromocion1;
    private javax.swing.JLabel lblIdPromocion2;
    private javax.swing.JLabel lblIdPromocion3;
    private javax.swing.JLabel lblIdPromocion4;
    private javax.swing.JLabel lblIdPromocion5;
    private javax.swing.JLabel lblPer5;
    private javax.swing.JLabel lblPer6;
    private javax.swing.JLabel lblPer7;
    private javax.swing.JLabel lblSiete;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlMantenimientoTiempo;
    private javax.swing.JPanel pnlNormalizacion;
    private javax.swing.JPanel pnlPertinencia;
    private javax.swing.JPanel pnlPromocion;
    private javax.swing.JRadioButton rdAdapContentGrupoNo;
    private javax.swing.JRadioButton rdAdapContentGrupoSi;
    private javax.swing.JRadioButton rdAdaptMetoNo;
    private javax.swing.JRadioButton rdAdaptMetoSi;
    private javax.swing.JRadioButton rdAdecEspacioNo;
    private javax.swing.JRadioButton rdAdecEspacioSi;
    private javax.swing.JRadioButton rdAumentParticiNo;
    private javax.swing.JRadioButton rdAumentParticiSi;
    private javax.swing.JRadioButton rdCoheNO;
    private javax.swing.JRadioButton rdCoheSi;
    private javax.swing.JRadioButton rdCosteActividadNo;
    private javax.swing.JRadioButton rdCosteActividadSi;
    private javax.swing.JRadioButton rdDineroDispNo;
    private javax.swing.JRadioButton rdDineroDispSi;
    private javax.swing.JRadioButton rdDisenoEvaluacionNO;
    private javax.swing.JRadioButton rdDisenoEvaluacionSi;
    private javax.swing.JRadioButton rdEcoPresuNO;
    private javax.swing.JRadioButton rdEcoPresuSi;
    private javax.swing.JRadioButton rdEcoRubroNo;
    private javax.swing.JRadioButton rdEcoRubroSi;
    private javax.swing.JRadioButton rdEdicionesNo;
    private javax.swing.JRadioButton rdEdicionesSi;
    private javax.swing.JRadioButton rdEstrucDefinNo;
    private javax.swing.JRadioButton rdEstrucDefinSi;
    private javax.swing.JRadioButton rdMarcoJurNo;
    private javax.swing.JRadioButton rdMarcoJurSi;
    private javax.swing.JRadioButton rdNormaJusNO;
    private javax.swing.JRadioButton rdNormaJusSi;
    private javax.swing.JRadioButton rdNovedadNo2;
    private javax.swing.JRadioButton rdNovedadSi2;
    private javax.swing.JRadioButton rdSistemaSeguimientoNo;
    private javax.swing.JRadioButton rdSistemaSeguimientoSi;
    private javax.swing.JRadioButton rdbEstricutraSi;
    private javax.swing.JRadioButton rdbEstructuraNo;
    private javax.swing.JTextField txtAdapContentGrupo;
    private javax.swing.JTextField txtAdaptMeto;
    private javax.swing.JTextField txtAdecEspacio;
    private javax.swing.JTextField txtAntecedentes;
    private javax.swing.JTextField txtAumentPartici;
    private javax.swing.JTextField txtCoheInterna;
    private javax.swing.JTextField txtCosteActividad;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtDineroDisp;
    private javax.swing.JTextField txtDisenoEvaluacion;
    private javax.swing.JTextField txtEcoRubro;
    private javax.swing.JTextField txtEdiciones;
    private javax.swing.JTextField txtEstrucDifinia;
    private javax.swing.JTextField txtEstructuraPrevia;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMarcoJuridiccional;
    private javax.swing.JTextField txtNormaJusticia;
    private org.jdesktop.swingx.JXComboBox txtNormaJustifica;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextField txtPresuAsignado;
    private javax.swing.JTextField txtSistemaSeguimiento;
    // End of variables declaration//GEN-END:variables
}
