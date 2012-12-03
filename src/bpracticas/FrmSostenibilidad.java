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
public class FrmSostenibilidad extends javax.swing.JFrame {
    int respuesta = 0;
    Conexion z = new Conexion();
    private String[] observacion = new String[6];
    private ArrayList<JComboBox> combos = new ArrayList<JComboBox>();
    private int imb =1;

    public int getImb() {
        return imb;
    }

    public void setImb(int imb) {
        this.imb = imb;
    }

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
        
        if(pregutarEvaluacion("sostenibilidad") != 0){
           cargaEvaluacion(txtCriterio.getText().toString(), 17, 7);
           setImb(2);
        }
        
        recoCombo(listarRecomendaciones(), cboEstructuraPrevia);
        recoCombo(listarRecomendaciones(), cboEdiciones);
        recoCombo(listarRecomendaciones(), cboAntecendentes);
        recoCombo(listarRecomendaciones(), cboNormaJusticia);
        recoCombo(listarRecomendaciones(), cboMarcoJuridiccional);
        recoCombo(listarRecomendaciones(), cboEstrucDifinia);
        recoCombo(listarRecomendaciones(), cboCoheInterna);
        recoCombo(listarRecomendaciones(), cboExisteDisenoEva);
        recoCombo(listarRecomendaciones(), cboSistemaSeguimiento);
        recoCombo(listarRecomendaciones(), cboPresuAsignado);
        recoCombo(listarRecomendaciones(), cboEcoRubro);
        recoCombo(listarRecomendaciones(), cboDineroDisp);
        recoCombo(listarRecomendaciones(), cboCosteActividad);
        recoCombo(listarRecomendaciones(), cboAumentPartici);
        recoCombo(listarRecomendaciones(), cboAdapContentGrupo);
        recoCombo(listarRecomendaciones(), cboAdecEspacio);
        recoCombo(listarRecomendaciones(), cboAdaptMeto);
        
        combos.add(cboEstructuraPrevia);
        combos.add(cboEdiciones);
        combos.add(cboAntecendentes);
        combos.add(cboNormaJusticia);
        combos.add(cboMarcoJuridiccional);
        combos.add(cboEstrucDifinia);
        combos.add(cboCoheInterna);
        combos.add(cboExisteDisenoEva);
        combos.add(cboSistemaSeguimiento);
        combos.add(cboPresuAsignado);
        combos.add(cboEcoRubro);
        combos.add(cboDineroDisp);
        combos.add(cboCosteActividad);
        combos.add(cboAumentPartici);
        combos.add(cboAdapContentGrupo);
        combos.add(cboAdecEspacio);
        combos.add(cboAdaptMeto);
    }

    
    
    private void selecDefecto(boolean si){
        rdPresuAsignadoNo.setSelected(si);
        rdEstructuraPreviaNo.setSelected(si);
        rdAumentParticiNo.setSelected(si);
        rdAdaptMetoNo.setSelected(si);
        rdAdapContentGrupoNo.setSelected(si);
        rdNormaJusticiaNo.setSelected(si);
    }
    
    
     /**
     * @param Este metodo permite preguntar si una evaluación ha sido cargada para luego decidir sobre la acción de insertar o modificar
     * 
     */
    private int pregutarEvaluacion(String factor){
        try {
            
            String sql = "SELECT "+factor+" FROM practicas where id = "+txtId.getText().toString();
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            respuesta = Integer.parseInt(z.rs.getString(factor).toString());
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
        int[] selecciono = new int[17];
        String[] texto = new String[17];
        
        if(rdEstructuraPreviaSi.isSelected() && !rdEstructuraPreviaNo.isSelected()){
            selecciono[0] = 1;
        }else{
            selecciono[0] = 0;
        }
        
        if(rdEcoRubroSi.isSelected() && !rdEcoRubroNo.isSelected()){
            selecciono[1] = 1;
        }else{
            selecciono[1] = 0;
        }
        
        if(rdAntecedentesSi.isSelected() && !rdAntecedentesNo.isSelected()){
            selecciono[2] = 1;
        }else{
            selecciono[2] = 0;
        }
        
        if(rdNormaJusticiaSi.isSelected() && !rdNormaJusticiaNo.isSelected()){
            selecciono[3] = 1;
        }else{
            selecciono[3] = 0;
        }
        
        if(rdMarcoJuridiccionalSi.isSelected() && !rdMarcoJuridiccionalNo.isSelected()){
            selecciono[4] = 1;
        }else{
            selecciono[4] = 0;
        }
        
        if(rdEstrucDifiniaSi.isSelected() && !rdEstrucDifiniaNo.isSelected()){
            selecciono[5] = 1;
        }else{
            selecciono[5] = 0;
        }
        
        if(rdCoheInternaSi.isSelected() && !rdCoheInternaNo.isSelected()){
            selecciono[6] = 1;
        }else{
            selecciono[6] = 0;
        }
        
        if(rdExisteDisenoEvaSi.isSelected() && !rdExisteDisenoEvaNo.isSelected()){
            selecciono[7] = 1;
        }else{
            selecciono[7] = 0;
        }
        
        if(rdSistemaSeguimientoSi.isSelected() && !rdSistemaSeguimientoNo.isSelected()){
            selecciono[8] = 1;
        }else{
            selecciono[8] = 0;
        }
        
        if(rdPresuAsignadoSi.isSelected() && !rdPresuAsignadoNo.isSelected()){
            selecciono[9] = 1;
        }else{
            selecciono[9] = 0;
        }
        
        if(rdEcoRubroSi.isSelected() && !rdEcoRubroNo.isSelected()){
            selecciono[10] = 1;
        }else{
            selecciono[10] = 0;
        }
        
        if(rdDineroDispSi.isSelected() && !rdDineroDispNo.isSelected()){
            selecciono[11] = 1;
        }else{
            selecciono[11] = 0;
        }
        
        if(rdCosteActividadSi.isSelected() && !rdCosteActividadNo.isSelected()){
            selecciono[12] = 1;
        }else{
            selecciono[12] = 0;
        }
        
        if(rdAumentParticiSi.isSelected() && !rdAumentParticiNo.isSelected()){
            selecciono[13] = 1;
        }else{
            selecciono[13] = 0;
        }
        
        if(rdAdapContentGrupoSi.isSelected() && !rdAdapContentGrupoNo.isSelected()){
            selecciono[14] = 1;
        }else{
            selecciono[14] = 0;
        }
        
        if(rdAdecEspacioSi.isSelected() && !rdAdecEspacioNo.isSelected()){
            selecciono[15] = 1;
        }else{
            selecciono[15] = 0;
        }
        
        if(rdAdaptMetoSi.isSelected() && !rdAdaptMetoNo.isSelected()){
            selecciono[16] = 1;
        }else{
            selecciono[16] = 0;
        }
        
        texto[0] = txtEstructuraPrevia.getText().toString();
        texto[1] = txtEdiciones.getText().toString();
        texto[2] = txtAntecedentes.getText().toString();
        texto[3] = txtNormaJusticia.getText().toString();
        texto[4] = txtMarcoJuridiccional.getText().toString();
        texto[5] = txtEstrucDifinia.getText().toString();
        texto[6] = txtCoheInterna.getText().toString();
        texto[7] = txtExisteDisenoEva.getText().toString();
        texto[8] = txtSistemaSeguimiento.getText().toString();
        texto[9] = txtPresuAsignado.getText().toString();
        texto[10] = txtEcoRubro.getText().toString();
        texto[11] = txtDineroDisp.getText().toString();
        texto[12] = txtCosteActividad.getText().toString();
        texto[13] = txtAumentPartici.getText().toString();
        texto[14] = txtAdapContentGrupo.getText().toString();
        texto[15] = txtAdecEspacio.getText().toString();
        texto[16] = txtAdaptMeto.getText().toString();
        
        try {
           
            int bnreco;
            int aux = 7;
            String sql;
            for (int i = 0; i < selecciono.length; i++) {
                bnreco = saberRecomendacion(combos.get(i), 0);
                sql = "CALL revaluacion("+accion+", "+aux+","+txtId.getText().toString()+",'"+texto[i]+"', 'variable',"+selecciono[i]+", "+bnreco+");";
                System.out.println(sql);
                z.snt = z.con.createStatement();
                z.snt.execute(sql);
                aux++;
            }
            
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
            sql = "SELECT * FROM recomendaciones";
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
    
    private void cargaEvaluacion(String criterio, int filas, int columnas){
        String[][] arreglo = new String[filas][columnas];
         try {
            
        //cargamos el arreglo    
            String sql = "SELECT * FROM var_pra_fact where factores like '"+criterio+"' and practicas = "+txtId.getText();
            System.out.print(sql);
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            System.out.println();
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    arreglo[i][j] = z.rs.getString(j+1);
                    System.out.print(arreglo[i][j]+" - " );
                }
                z.rs.next();
                System.out.println(" * ");
            }
             
             for (int i = 0; i < filas; i++) {
                 for (int j = 0; j < columnas; j++) {
                     
                 
                switch (Integer.parseInt(arreglo[i][0])){
                    case 7: 
                        txtEstructuraPrevia.setText(arreglo[i][2]);
                        cboEcoRubro.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdEstructuraPreviaSi.setSelected(true);
                            rdEstructuraPreviaNo.setSelected(false);
                        }else{
                            rdEstructuraPreviaSi.setSelected(false);
                            rdEstructuraPreviaNo.setSelected(true);
                        }
                    break;
                    case 8: 
                        txtEdiciones.setText(arreglo[i][2]);
                        cboEdiciones.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdEdicionesSi.setSelected(true);
                            rdEdicionesNo.setSelected(false);
                        }else{
                            rdEdicionesSi.setSelected(false);
                            rdEdicionesNo.setSelected(true);
                        }
                    break;
                    case 9: 
                        txtAntecedentes.setText(arreglo[i][2]);
                        cboAntecendentes.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAntecedentesSi.setSelected(true);
                            rdAntecedentesNo.setSelected(false);
                        }else{
                            rdAntecedentesSi.setSelected(false);
                            rdAntecedentesNo.setSelected(true);
                        }
                    break;
                    case 10: 
                        txtNormaJusticia.setText(arreglo[i][2]);
                        cboNormaJusticia.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdNormaJusticiaSi.setSelected(true);
                            rdNormaJusticiaNo.setSelected(false);
                        }else{
                            rdNormaJusticiaSi.setSelected(false);
                            rdNormaJusticiaNo.setSelected(true);
                        }
                    break;    
                    case 11: 
                        txtMarcoJuridiccional.setText(arreglo[i][2]);
                        cboMarcoJuridiccional.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdMarcoJuridiccionalSi.setSelected(true);
                            rdMarcoJuridiccionalNo.setSelected(false);
                        }else{
                            rdMarcoJuridiccionalSi.setSelected(false);
                            rdMarcoJuridiccionalNo.setSelected(true);
                        }
                    break;
                    case 12: 
                        txtEstrucDifinia.setText(arreglo[i][2]);
                        cboEstrucDifinia.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdEstrucDifiniaSi.setSelected(true);
                            rdEstrucDifiniaNo.setSelected(false);
                        }else{
                            rdEstrucDifiniaSi.setSelected(false);
                            rdEstrucDifiniaNo.setSelected(true);
                        }
                    break; 
                    case 13: 
                        txtCoheInterna.setText(arreglo[i][2]);
                        cboCoheInterna.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdCoheInternaSi.setSelected(true);
                            rdCoheInternaNo.setSelected(false);
                        }else{
                            rdCoheInternaSi.setSelected(false);
                            rdCoheInternaNo.setSelected(true);
                        }
                    break;
                    case 14: 
                        txtExisteDisenoEva.setText(arreglo[i][2]);
                        cboExisteDisenoEva.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdExisteDisenoEvaSi.setSelected(true);
                            rdExisteDisenoEvaNo.setSelected(false);
                        }else{
                            rdExisteDisenoEvaSi.setSelected(false);
                            rdExisteDisenoEvaNo.setSelected(true);
                        }
                    break;
                    case 15: 
                        txtSistemaSeguimiento.setText(arreglo[i][2]);
                        cboSistemaSeguimiento.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdSistemaSeguimientoSi.setSelected(true);
                            rdSistemaSeguimientoNo.setSelected(false);
                        }else{
                            rdSistemaSeguimientoSi.setSelected(false);
                            rdSistemaSeguimientoNo.setSelected(true);
                        }
                    break;
                     case 16: 
                        txtPresuAsignado.setText(arreglo[i][2]);
                        cboPresuAsignado.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPresuAsignadoSi.setSelected(true);
                            rdPresuAsignadoNo.setSelected(false);
                        }else{
                            rdPresuAsignadoSi.setSelected(false);
                            rdPresuAsignadoNo.setSelected(true);
                        }
                    break;     
                    case 17: 
                        txtEcoRubro.setText(arreglo[i][2]);
                        cboEcoRubro.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdEcoRubroSi.setSelected(true);
                            rdEcoRubroNo.setSelected(false);
                        }else{
                            rdEcoRubroSi.setSelected(false);
                            rdEcoRubroNo.setSelected(true);
                        }
                    break;
                    case 18: 
                        txtDineroDisp.setText(arreglo[i][2]);
                        cboDineroDisp.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdDineroDispSi.setSelected(true);
                            rdDineroDispNo.setSelected(false);
                        }else{
                            rdDineroDispSi.setSelected(false);
                            rdDineroDispNo.setSelected(true);
                        }
                    break;
                    case 19: 
                        txtCosteActividad.setText(arreglo[i][2]);
                        cboCosteActividad.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdCosteActividadSi.setSelected(true);
                            rdCosteActividadNo.setSelected(false);
                        }else{
                            rdCosteActividadSi.setSelected(false);
                            rdCosteActividadNo.setSelected(true);
                        }
                    break;
                    case 20: 
                        txtAumentPartici.setText(arreglo[i][2]);
                        cboAumentPartici.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAumentParticiSi.setSelected(true);
                            rdAumentParticiNo.setSelected(false);
                        }else{
                            rdAumentParticiSi.setSelected(false);
                            rdAumentParticiNo.setSelected(true);
                        }
                    break;
                    case 21: 
                        txtAdapContentGrupo.setText(arreglo[i][2]);
                        cboAdapContentGrupo.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAdapContentGrupoSi.setSelected(true);
                            rdAdapContentGrupoNo.setSelected(false);
                        }else{
                            rdAdapContentGrupoSi.setSelected(false);
                            rdAdapContentGrupoNo.setSelected(true);
                        }
                    break;     
                     
                    case 22: 
                        txtAdecEspacio.setText(arreglo[i][2]);
                        cboAdecEspacio.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAdecEspacioSi.setSelected(true);
                            rdAdecEspacioNo.setSelected(false);
                        }else{
                            rdAdecEspacioSi.setSelected(false);
                            rdAdecEspacioNo.setSelected(true);
                        }
                    break;     
                    
                    case 23: 
                        txtAdaptMeto.setText(arreglo[i][2]);
                        cboAdaptMeto.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAdaptMetoSi.setSelected(true);
                            rdAdaptMetoNo.setSelected(false);
                        }else{
                            rdAdaptMetoSi.setSelected(false);
                            rdAdaptMetoNo.setSelected(true);
                        }
                    break;    
                }
                
                }
             }
             
        } catch (SQLException ex) {
            Logger.getLogger(FrmInnovacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


   private void evaluacionHecha(String factor){
        try {
            String sql = "UPDATE practicas SET "+factor+" = 1 WHERE id="+txtId.getText().toString();
            z.snt = z.con.createStatement();
            z.snt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(FrmReplicabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        rdEstructuraPreviaSi = new javax.swing.JRadioButton();
        rdEstructuraPreviaNo = new javax.swing.JRadioButton();
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
        rdAntecedentesSi = new javax.swing.JRadioButton();
        rdAntecedentesNo = new javax.swing.JRadioButton();
        cboAntecendentes = new org.jdesktop.swingx.JXComboBox();
        lblIdNoveadd4 = new javax.swing.JLabel();
        pnlPromocion = new javax.swing.JPanel();
        txtNormaJusticia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdNormaJusticiaSi = new javax.swing.JRadioButton();
        rdNormaJusticiaNo = new javax.swing.JRadioButton();
        lblIdPromocion = new javax.swing.JLabel();
        cboNormaJusticia = new org.jdesktop.swingx.JXComboBox();
        rdMarcoJuridiccionalNo = new javax.swing.JRadioButton();
        rdMarcoJuridiccionalSi = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        lblIdPromocion1 = new javax.swing.JLabel();
        txtMarcoJuridiccional = new javax.swing.JTextField();
        cboMarcoJuridiccional = new org.jdesktop.swingx.JXComboBox();
        jLabel12 = new javax.swing.JLabel();
        cboEstrucDifinia = new org.jdesktop.swingx.JXComboBox();
        txtEstrucDifinia = new javax.swing.JTextField();
        rdEstrucDifiniaNo = new javax.swing.JRadioButton();
        lblIdPromocion2 = new javax.swing.JLabel();
        rdEstrucDifiniaSi = new javax.swing.JRadioButton();
        cboCoheInterna = new org.jdesktop.swingx.JXComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtCoheInterna = new javax.swing.JTextField();
        rdCoheInternaSi = new javax.swing.JRadioButton();
        rdCoheInternaNo = new javax.swing.JRadioButton();
        lblIdPromocion3 = new javax.swing.JLabel();
        lblIdPromocion4 = new javax.swing.JLabel();
        rdExisteDisenoEvaNo = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        rdExisteDisenoEvaSi = new javax.swing.JRadioButton();
        txtExisteDisenoEva = new javax.swing.JTextField();
        cboExisteDisenoEva = new org.jdesktop.swingx.JXComboBox();
        txtSistemaSeguimiento = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lblIdPromocion5 = new javax.swing.JLabel();
        rdSistemaSeguimientoNo = new javax.swing.JRadioButton();
        rdSistemaSeguimientoSi = new javax.swing.JRadioButton();
        cboSistemaSeguimiento = new org.jdesktop.swingx.JXComboBox();
        pnlNormalizacion = new javax.swing.JPanel();
        txtPresuAsignado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdPresuAsignadoSi = new javax.swing.JRadioButton();
        rdPresuAsignadoNo = new javax.swing.JRadioButton();
        lblIdNormalizacion = new javax.swing.JLabel();
        cboPresuAsignado = new org.jdesktop.swingx.JXComboBox();
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

        btGEstructura.add(rdEstructuraPreviaSi);
        rdEstructuraPreviaSi.setText("Si");
        rdEstructuraPreviaSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEstructuraPreviaSiActionPerformed(evt);
            }
        });

        btGEstructura.add(rdEstructuraPreviaNo);
        rdEstructuraPreviaNo.setText("No");
        rdEstructuraPreviaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEstructuraPreviaNoActionPerformed(evt);
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

        groupAntecedentes.add(rdAntecedentesSi);
        rdAntecedentesSi.setText("Si");
        rdAntecedentesSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAntecedentesSiActionPerformed(evt);
            }
        });

        groupAntecedentes.add(rdAntecedentesNo);
        rdAntecedentesNo.setText("No");
        rdAntecedentesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAntecedentesNoActionPerformed(evt);
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
                        .addComponent(rdEstructuraPreviaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEstructuraPreviaNo))
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
                        .addComponent(rdAntecedentesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdAntecedentesNo))
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
                    .addComponent(rdEstructuraPreviaSi)
                    .addComponent(rdEstructuraPreviaNo))
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
                    .addComponent(rdAntecedentesSi)
                    .addComponent(rdAntecedentesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboAntecendentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlPromocion.setBorder(javax.swing.BorderFactory.createTitledBorder("Viabilidad técnica"));

        jLabel5.setText("Existe norma que la justifica.");

        groupNormaJus.add(rdNormaJusticiaSi);
        rdNormaJusticiaSi.setText("Si");

        rdNormaJusticiaNo.setText("No");
        rdNormaJusticiaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNormaJusticiaNoActionPerformed(evt);
            }
        });

        lblIdPromocion.setText("10");

        cboNormaJusticia.setEditable(true);

        groupMarcoJuris.add(rdMarcoJuridiccionalNo);
        rdMarcoJuridiccionalNo.setText("No");
        rdMarcoJuridiccionalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMarcoJuridiccionalNoActionPerformed(evt);
            }
        });

        groupMarcoJuris.add(rdMarcoJuridiccionalSi);
        rdMarcoJuridiccionalSi.setText("Si");
        rdMarcoJuridiccionalSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdMarcoJuridiccionalSiActionPerformed(evt);
            }
        });

        jLabel11.setText("Existe marco jurisdiccional que la  regula");

        lblIdPromocion1.setText("11");

        cboMarcoJuridiccional.setEditable(true);

        jLabel12.setText("Tiene una estructura definida.");

        cboEstrucDifinia.setEditable(true);

        grupEstruDefin.add(rdEstrucDifiniaNo);
        rdEstrucDifiniaNo.setText("No");
        rdEstrucDifiniaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEstrucDifiniaNoActionPerformed(evt);
            }
        });

        lblIdPromocion2.setText("12");

        grupEstruDefin.add(rdEstrucDifiniaSi);
        rdEstrucDifiniaSi.setText("Si");

        cboCoheInterna.setEditable(true);

        jLabel13.setText("Coherencia interna.");

        groupCoheInt.add(rdCoheInternaSi);
        rdCoheInternaSi.setText("Si");

        groupCoheInt.add(rdCoheInternaNo);
        rdCoheInternaNo.setText("No");
        rdCoheInternaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCoheInternaNoActionPerformed(evt);
            }
        });

        lblIdPromocion3.setText("13");

        lblIdPromocion4.setText("14");

        groupDisenoEvaluacion.add(rdExisteDisenoEvaNo);
        rdExisteDisenoEvaNo.setText("No");
        rdExisteDisenoEvaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdExisteDisenoEvaNoActionPerformed(evt);
            }
        });

        jLabel14.setText("Existe un diseño de evaluación");

        groupDisenoEvaluacion.add(rdExisteDisenoEvaSi);
        rdExisteDisenoEvaSi.setText("Si");

        cboExisteDisenoEva.setEditable(true);

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
                        .addComponent(rdNormaJusticiaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdNormaJusticiaNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdMarcoJuridiccionalSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdMarcoJuridiccionalNo))
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
                        .addComponent(rdEstrucDifiniaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdEstrucDifiniaNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdCoheInternaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdCoheInternaNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtExisteDisenoEva, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdExisteDisenoEvaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdExisteDisenoEvaNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtSistemaSeguimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdSistemaSeguimientoSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdSistemaSeguimientoNo))
                    .addComponent(cboNormaJusticia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEstrucDifinia, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboExisteDisenoEva, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(rdNormaJusticiaSi)
                    .addComponent(rdNormaJusticiaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboNormaJusticia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdMarcoJuridiccionalSi)
                    .addComponent(rdMarcoJuridiccionalNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarcoJuridiccional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion2)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstrucDifinia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEstrucDifiniaSi)
                    .addComponent(rdEstrucDifiniaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboEstrucDifinia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion3)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdCoheInternaSi)
                    .addComponent(rdCoheInternaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCoheInterna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion4)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtExisteDisenoEva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdExisteDisenoEvaSi)
                    .addComponent(rdExisteDisenoEvaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboExisteDisenoEva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        groupEcoPresu.add(rdPresuAsignadoSi);
        rdPresuAsignadoSi.setText("Si");

        groupEcoPresu.add(rdPresuAsignadoNo);
        rdPresuAsignadoNo.setText("No");
        rdPresuAsignadoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPresuAsignadoNoActionPerformed(evt);
            }
        });

        lblIdNormalizacion.setText("16");

        cboPresuAsignado.setEditable(true);

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
                        .addComponent(rdPresuAsignadoSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdPresuAsignadoNo))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboPresuAsignado, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(rdPresuAsignadoSi)
                    .addComponent(rdPresuAsignadoNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPresuAsignado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
       abm(getImb());
            evaluacionHecha("sostenibilidad");
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
            this.dispose();
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

    private void rdExisteDisenoEvaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdExisteDisenoEvaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdExisteDisenoEvaNoActionPerformed

    private void rdCoheInternaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdCoheInternaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdCoheInternaNoActionPerformed

    private void rdEstrucDifiniaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEstrucDifiniaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEstrucDifiniaNoActionPerformed

    private void rdMarcoJuridiccionalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMarcoJuridiccionalNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdMarcoJuridiccionalNoActionPerformed

    private void rdNormaJusticiaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNormaJusticiaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNormaJusticiaNoActionPerformed

    private void rdAntecedentesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAntecedentesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAntecedentesNoActionPerformed

    private void rdAntecedentesSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAntecedentesSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAntecedentesSiActionPerformed

    private void rdEdicionesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEdicionesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEdicionesNoActionPerformed

    private void rdEdicionesSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEdicionesSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEdicionesSiActionPerformed

    private void rdEstructuraPreviaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEstructuraPreviaNoActionPerformed
        
    }//GEN-LAST:event_rdEstructuraPreviaNoActionPerformed

    private void rdEstructuraPreviaSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEstructuraPreviaSiActionPerformed
        
    }//GEN-LAST:event_rdEstructuraPreviaSiActionPerformed

    private void rdPresuAsignadoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPresuAsignadoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPresuAsignadoNoActionPerformed

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

    private void rdMarcoJuridiccionalSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdMarcoJuridiccionalSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdMarcoJuridiccionalSiActionPerformed

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
    private org.jdesktop.swingx.JXComboBox cboCoheInterna;
    private org.jdesktop.swingx.JXComboBox cboCosteActividad;
    private org.jdesktop.swingx.JXComboBox cboDineroDisp;
    private org.jdesktop.swingx.JXComboBox cboEcoRubro;
    private org.jdesktop.swingx.JXComboBox cboEdiciones;
    private org.jdesktop.swingx.JXComboBox cboEstrucDifinia;
    private org.jdesktop.swingx.JXComboBox cboEstructuraPrevia;
    private org.jdesktop.swingx.JXComboBox cboExisteDisenoEva;
    private org.jdesktop.swingx.JXComboBox cboMarcoJuridiccional;
    private org.jdesktop.swingx.JXComboBox cboNormaJusticia;
    private org.jdesktop.swingx.JXComboBox cboPresuAsignado;
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
    private javax.swing.JRadioButton rdAntecedentesNo;
    private javax.swing.JRadioButton rdAntecedentesSi;
    private javax.swing.JRadioButton rdAumentParticiNo;
    private javax.swing.JRadioButton rdAumentParticiSi;
    private javax.swing.JRadioButton rdCoheInternaNo;
    private javax.swing.JRadioButton rdCoheInternaSi;
    private javax.swing.JRadioButton rdCosteActividadNo;
    private javax.swing.JRadioButton rdCosteActividadSi;
    private javax.swing.JRadioButton rdDineroDispNo;
    private javax.swing.JRadioButton rdDineroDispSi;
    private javax.swing.JRadioButton rdEcoRubroNo;
    private javax.swing.JRadioButton rdEcoRubroSi;
    private javax.swing.JRadioButton rdEdicionesNo;
    private javax.swing.JRadioButton rdEdicionesSi;
    private javax.swing.JRadioButton rdEstrucDifiniaNo;
    private javax.swing.JRadioButton rdEstrucDifiniaSi;
    private javax.swing.JRadioButton rdEstructuraPreviaNo;
    private javax.swing.JRadioButton rdEstructuraPreviaSi;
    private javax.swing.JRadioButton rdExisteDisenoEvaNo;
    private javax.swing.JRadioButton rdExisteDisenoEvaSi;
    private javax.swing.JRadioButton rdMarcoJuridiccionalNo;
    private javax.swing.JRadioButton rdMarcoJuridiccionalSi;
    private javax.swing.JRadioButton rdNormaJusticiaNo;
    private javax.swing.JRadioButton rdNormaJusticiaSi;
    private javax.swing.JRadioButton rdPresuAsignadoNo;
    private javax.swing.JRadioButton rdPresuAsignadoSi;
    private javax.swing.JRadioButton rdSistemaSeguimientoNo;
    private javax.swing.JRadioButton rdSistemaSeguimientoSi;
    private javax.swing.JTextField txtAdapContentGrupo;
    private javax.swing.JTextField txtAdaptMeto;
    private javax.swing.JTextField txtAdecEspacio;
    private javax.swing.JTextField txtAntecedentes;
    private javax.swing.JTextField txtAumentPartici;
    private javax.swing.JTextField txtCoheInterna;
    private javax.swing.JTextField txtCosteActividad;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtDineroDisp;
    private javax.swing.JTextField txtEcoRubro;
    private javax.swing.JTextField txtEdiciones;
    private javax.swing.JTextField txtEstrucDifinia;
    private javax.swing.JTextField txtEstructuraPrevia;
    private javax.swing.JTextField txtExisteDisenoEva;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMarcoJuridiccional;
    private javax.swing.JTextField txtNormaJusticia;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextField txtPresuAsignado;
    private javax.swing.JTextField txtSistemaSeguimiento;
    // End of variables declaration//GEN-END:variables
}
