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
public class FrmParticipacion extends javax.swing.JFrame {
    int respuesta = 0;
    Conexion z = new Conexion();
    private String[] observacion = new String[13];

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
    public FrmParticipacion() {
        initComponents();
        txtCriterio.setText("Participcación");
        if(FrmPrincipal.getEvaluar()!= 0){
            txtId.setText(FrmPrincipal.getEvaluar()+"");
            configPractica();
            selecDefecto(true);
        }
        
        if(pregutarEvaluacion("participacion") != 0){
           cargaEvaluacion(txtCriterio.getText().toString(), 13, 7);
           setImb(2);
        }
        
        recoCombo(listarRecomendaciones(), cboInteres);
        recoCombo(listarRecomendaciones(), cboSatisfaccion);
        recoCombo(listarRecomendaciones(), cboDemanda);
        recoCombo(listarRecomendaciones(), cboIdentificacion);
        recoCombo(listarRecomendaciones(), cboParticipantes);
        recoCombo(listarRecomendaciones(), cboAdecuancion);
        recoCombo(listarRecomendaciones(), cboAdeParticipantes);
        recoCombo(listarRecomendaciones(), cboOrganizacion);
        recoCombo(listarRecomendaciones(), cboOtras);
        recoCombo(listarRecomendaciones(), cboDiversidad);
        recoCombo(listarRecomendaciones(), cboCoordinaciones);
        recoCombo(listarRecomendaciones(), cboPartActiva);
        recoCombo(listarRecomendaciones(), cboEstrPart);
        
        combos.add(cboInteres);
        combos.add(cboSatisfaccion);
        combos.add(cboDemanda);
        combos.add(cboIdentificacion);
        combos.add(cboParticipantes);
        combos.add(cboAdecuancion);
        combos.add(cboAdeParticipantes);
        combos.add(cboOrganizacion);
        combos.add(cboOtras);
        combos.add(cboDiversidad);
        combos.add(cboCoordinaciones);
        combos.add(cboPartActiva);
        combos.add(cboEstrPart);

    }

    private void selecDefecto(boolean si){
        
        rdInteresNo.setSelected(si);
        rdSatisfaccionNo.setSelected(si);
        rdEstrPartNo.setSelected(si);
        rdIdentificacionNo.setSelected(si);
        rdParticipantesNo.setSelected(si);
        rdAdecuancionNo.setSelected(si);     
        rdOrganizacionNo.setSelected(si);
        rdDiversidadNo.setSelected(si);
        rdCoordinacionesNo.setSelected(si);
        rdPartActivaNo.setSelected(si);
        
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
            Logger.getLogger(FrmParticipacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmParticipacion.class.getName()).log(Level.SEVERE, null, ex);
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
        int[] selecciono = new int[13];
        String[] texto = new String[13];
        
        if(rdInteresSi.isSelected() && !rdInteresNo.isSelected()){
            selecciono[0] = 1;
        }else{
            selecciono[0] = 0;
        }
        
        if(rdSatisfaccionSi.isSelected() && !rdSatisfaccionNo.isSelected()){
            selecciono[1] = 1;
        }else{
            selecciono[1] = 0;
        }
        
        if(rdDemandaSi.isSelected() && !rdDemandaNo.isSelected()){
            selecciono[2] = 1;
        }else{
            selecciono[2] = 0;
        }
        
        if(rdIdentificacionSi.isSelected() && !rdIdentificacionNo.isSelected()){
            selecciono[3] = 1;
        }else{
            selecciono[3] = 0;
        }
        
        if(rdParticipantesSi.isSelected() && !rdParticipantesNo.isSelected()){
            selecciono[4] = 1;
        }else{
            selecciono[4] = 0;
        }
       
        if(rdAdecuancionSi.isSelected() && !rdAdecuancionNo.isSelected()){
            selecciono[5] = 1;
        }else{
            selecciono[5] = 0;
        }
        
        if(AdeParticipantesSi.isSelected() && !rdAdeParticipantesNo.isSelected()){
            selecciono[6] = 1;
        }else{
            selecciono[6] = 0;
        }
        
        if(rdOrganizacionSi.isSelected() && !rdOrganizacionNo.isSelected()){
            selecciono[7] = 1;
        }else{
            selecciono[7] = 0;
        }
        
        if(rdOtrasSi.isSelected() && !rdOtrasNo.isSelected()){
            selecciono[8] = 1;
        }else{
            selecciono[8] = 0;
        }
        
        
        if(rdDiversidadSi.isSelected() && !rdDiversidadNo.isSelected()){
            selecciono[9] = 1;
        }else{
            selecciono[9] = 0;
        }
        
        if(rdCoordinacionesSi.isSelected() && !rdCoordinacionesNo.isSelected()){
            selecciono[10] = 1;
        }else{
            selecciono[10] = 0;
        }
        
        if(rdPartActivaSi.isSelected() && !rdPartActivaNo.isSelected()){
            selecciono[11] = 1;
        }else{
            selecciono[11] = 0;
        }
        
        if(rdEstrPartSi.isSelected() && !rdEstrPartNo.isSelected()){
            selecciono[12] = 1;
        }else{
            selecciono[12] = 0;
        }
        
        texto[0] = txtInterez.getText().toString();
        texto[1] = txtSatisfaccion.getText().toString();
        texto[2] = txtDemanda.getText().toString();
        texto[3] = txtIdentificacion.getText().toString();
        texto[4] = txtParticipantes.getText().toString();
        texto[5] = txtAdecuancion.getText().toString();
        texto[6] = txtAdeParticipantes.getText().toString();
        texto[7] = txtOrganizacion.getText().toString();
        texto[8] = txtOtras.getText().toString();
        texto[9] = txtDiversidad.getText().toString();
        texto[10] = txtCoordinaciones.getText().toString();
        texto[11] = txtPartActiva.getText().toString();
        texto[12] = txtEstrPart.getText().toString();
        
        
        
        try {
            int bnreco;
            int aux = 33;
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
            Logger.getLogger(FrmParticipacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmParticipacion.class.getName()).log(Level.SEVERE, null, ex);
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
                    case 33: 
                        txtInterez.setText(arreglo[i][2]);
                        cboInteres.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdInteresSi.setSelected(true);
                            rdInteresNo.setSelected(false);
                        }else{
                            rdInteresSi.setSelected(false);
                            rdInteresNo.setSelected(true);
                        }
                    break;
                    case 34: 
                        txtSatisfaccion.setText(arreglo[i][2]);
                        cboSatisfaccion.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdSatisfaccionSi.setSelected(true);
                            rdSatisfaccionNo.setSelected(false);
                        }else{
                            rdSatisfaccionSi.setSelected(false);
                            rdSatisfaccionNo.setSelected(true);
                        }
                    break;
                    case 35: 
                        txtDemanda.setText(arreglo[i][2]);
                        cboDemanda.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdDemandaSi.setSelected(true);
                            rdDemandaNo.setSelected(false);
                        }else{
                            rdDemandaSi.setSelected(false);
                            rdDemandaNo.setSelected(true);
                        }
                    break;
                    case 36: 
                        txtIdentificacion.setText(arreglo[i][2]);
                        cboIdentificacion.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdIdentificacionSi.setSelected(true);
                            rdIdentificacionNo.setSelected(false);
                        }else{
                            rdIdentificacionSi.setSelected(false);
                            rdIdentificacionNo.setSelected(true);
                        }
                    break;    
                    case 37: 
                        txtParticipantes.setText(arreglo[i][2]);
                        cboParticipantes.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdParticipantesSi.setSelected(true);
                            rdParticipantesNo.setSelected(false);
                        }else{
                            rdParticipantesSi.setSelected(false);
                            rdParticipantesNo.setSelected(true);
                        }
                    break;
                    case 38: 
                        txtAdecuancion.setText(arreglo[i][2]);
                        cboAdecuancion.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdAdecuancionSi.setSelected(true);
                            rdAdecuancionNo.setSelected(false);
                        }else{
                            rdAdecuancionSi.setSelected(false);
                            rdAdecuancionNo.setSelected(true);
                        }
                    break; 
                    case 39: 
                        txtAdeParticipantes.setText(arreglo[i][2]);
                        cboAdeParticipantes.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            AdeParticipantesSi.setSelected(true);
                            rdAdeParticipantesNo.setSelected(false);
                        }else{
                            AdeParticipantesSi.setSelected(false);
                            rdAdeParticipantesNo.setSelected(true);
                        }
                    break;
                    case 40: 
                        txtOrganizacion.setText(arreglo[i][2]);
                        cboOrganizacion.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdOrganizacionSi.setSelected(true);
                            rdOrganizacionNo.setSelected(false);
                        }else{
                            rdOrganizacionSi.setSelected(false);
                            rdOrganizacionNo.setSelected(true);
                        }
                    break;
                    case 41: 
                        txtOtras.setText(arreglo[i][2]);
                        cboOtras.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdOtrasSi.setSelected(true);
                            rdOtrasNo.setSelected(false);
                        }else{
                            rdOtrasSi.setSelected(false);
                            rdOtrasNo.setSelected(true);
                        }
                    break;
                    case 42: 
                        txtDiversidad.setText(arreglo[i][2]);
                        cboDiversidad.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdDiversidadSi.setSelected(true);
                            rdDiversidadNo.setSelected(false);
                        }else{
                            rdDiversidadSi.setSelected(false);
                            rdDiversidadNo.setSelected(true);
                        }
                    break;
                            
                    case 43: 
                        txtCoordinaciones.setText(arreglo[i][2]);
                        cboCoordinaciones.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdCoordinacionesSi.setSelected(true);
                            rdCoordinacionesNo.setSelected(false);
                        }else{
                            rdCoordinacionesSi.setSelected(false);
                            rdCoordinacionesNo.setSelected(true);
                        }
                    break; 
                                
                    case 44: 
                        txtPartActiva.setText(arreglo[i][2]);
                        cboPartActiva.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPartActivaSi.setSelected(true);
                            rdPartActivaNo.setSelected(false);
                        }else{
                            rdPartActivaSi.setSelected(false);
                            rdPartActivaNo.setSelected(true);
                        }
                    break; 
                                    
                   case 45: 
                        txtEstrPart.setText(arreglo[i][2]);
                        cboEstrPart.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdEstrPartSi.setSelected(true);
                            rdEstrPartNo.setSelected(false);
                        }else{
                            rdEstrPartSi.setSelected(false);
                            rdEstrPartNo.setSelected(true);
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
            Logger.getLogger(FrmParticipacion.class.getName()).log(Level.SEVERE, null, ex);
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

        groupInteres = new javax.swing.ButtonGroup();
        groupParticipantes = new javax.swing.ButtonGroup();
        groupCoordinaciones = new javax.swing.ButtonGroup();
        btGPertinencia1 = new javax.swing.ButtonGroup();
        btGPertinencia2 = new javax.swing.ButtonGroup();
        btGPertinencia3 = new javax.swing.ButtonGroup();
        groupSatisfaccion = new javax.swing.ButtonGroup();
        gropuAntecedentes = new javax.swing.ButtonGroup();
        groupIdentificacion = new javax.swing.ButtonGroup();
        groupAdecuancion = new javax.swing.ButtonGroup();
        groupAdeParticipantes = new javax.swing.ButtonGroup();
        groupOrganizacion = new javax.swing.ButtonGroup();
        groupDiversidad = new javax.swing.ButtonGroup();
        groupPartActiva = new javax.swing.ButtonGroup();
        groupEstrPart = new javax.swing.ButtonGroup();
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
        txtInterez = new javax.swing.JTextField();
        lblSiete = new javax.swing.JLabel();
        rdInteresSi = new javax.swing.JRadioButton();
        rdInteresNo = new javax.swing.JRadioButton();
        lblIdNoveadd2 = new javax.swing.JLabel();
        cboInteres = new org.jdesktop.swingx.JXComboBox();
        txtSatisfaccion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rdSatisfaccionSi = new javax.swing.JRadioButton();
        rdSatisfaccionNo = new javax.swing.JRadioButton();
        lblIdNoveadd3 = new javax.swing.JLabel();
        cboSatisfaccion = new org.jdesktop.swingx.JXComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtDemanda = new javax.swing.JTextField();
        rdDemandaSi = new javax.swing.JRadioButton();
        rdDemandaNo = new javax.swing.JRadioButton();
        cboDemanda = new org.jdesktop.swingx.JXComboBox();
        lblIdNoveadd4 = new javax.swing.JLabel();
        lblIdNoveadd5 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtIdentificacion = new javax.swing.JTextField();
        rdIdentificacionSi = new javax.swing.JRadioButton();
        cboIdentificacion = new org.jdesktop.swingx.JXComboBox();
        rdIdentificacionNo = new javax.swing.JRadioButton();
        pnlPromocion = new javax.swing.JPanel();
        txtParticipantes = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdParticipantesSi = new javax.swing.JRadioButton();
        rdParticipantesNo = new javax.swing.JRadioButton();
        lblIdPromocion = new javax.swing.JLabel();
        cboParticipantes = new org.jdesktop.swingx.JXComboBox();
        rdAdecuancionNo = new javax.swing.JRadioButton();
        rdAdecuancionSi = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        lblIdPromocion1 = new javax.swing.JLabel();
        txtAdecuancion = new javax.swing.JTextField();
        cboAdecuancion = new org.jdesktop.swingx.JXComboBox();
        jLabel12 = new javax.swing.JLabel();
        cboAdeParticipantes = new org.jdesktop.swingx.JXComboBox();
        txtAdeParticipantes = new javax.swing.JTextField();
        rdAdeParticipantesNo = new javax.swing.JRadioButton();
        lblIdPromocion2 = new javax.swing.JLabel();
        AdeParticipantesSi = new javax.swing.JRadioButton();
        cboOrganizacion = new org.jdesktop.swingx.JXComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtOrganizacion = new javax.swing.JTextField();
        rdOrganizacionSi = new javax.swing.JRadioButton();
        rdOrganizacionNo = new javax.swing.JRadioButton();
        lblIdPromocion3 = new javax.swing.JLabel();
        lblIdPromocion4 = new javax.swing.JLabel();
        rdOtrasNo = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        rdOtrasSi = new javax.swing.JRadioButton();
        txtOtras = new javax.swing.JTextField();
        cboOtras = new org.jdesktop.swingx.JXComboBox();
        txtDiversidad = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lblIdPromocion5 = new javax.swing.JLabel();
        rdDiversidadNo = new javax.swing.JRadioButton();
        rdDiversidadSi = new javax.swing.JRadioButton();
        cboDiversidad = new org.jdesktop.swingx.JXComboBox();
        pnlNormalizacion = new javax.swing.JPanel();
        txtCoordinaciones = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdCoordinacionesSi = new javax.swing.JRadioButton();
        rdCoordinacionesNo = new javax.swing.JRadioButton();
        lblIdNormalizacion = new javax.swing.JLabel();
        cboCoordinaciones = new org.jdesktop.swingx.JXComboBox();
        jLabel17 = new javax.swing.JLabel();
        lblIdNormalizacion1 = new javax.swing.JLabel();
        rdPartActivaSi = new javax.swing.JRadioButton();
        rdPartActivaNo = new javax.swing.JRadioButton();
        txtPartActiva = new javax.swing.JTextField();
        cboPartActiva = new org.jdesktop.swingx.JXComboBox();
        txtEstrPart = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        lblIdNormalizacion2 = new javax.swing.JLabel();
        rdEstrPartSi = new javax.swing.JRadioButton();
        rdEstrPartNo = new javax.swing.JRadioButton();
        cboEstrPart = new org.jdesktop.swingx.JXComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Evaluación del criterio Participación");

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

        pnlMantenimientoTiempo.setBorder(javax.swing.BorderFactory.createTitledBorder("Receptividad"));

        lblSiete.setText("Interés en la práctica.");

        groupInteres.add(rdInteresSi);
        rdInteresSi.setText("Si");
        rdInteresSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdInteresSiActionPerformed(evt);
            }
        });

        groupInteres.add(rdInteresNo);
        rdInteresNo.setText("No");
        rdInteresNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdInteresNoActionPerformed(evt);
            }
        });

        lblIdNoveadd2.setText("33");

        cboInteres.setEditable(true);

        jLabel9.setText("Satisfacción de la ciudadanía");

        groupSatisfaccion.add(rdSatisfaccionSi);
        rdSatisfaccionSi.setText("Si");
        rdSatisfaccionSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSatisfaccionSiActionPerformed(evt);
            }
        });

        groupSatisfaccion.add(rdSatisfaccionNo);
        rdSatisfaccionNo.setText("No");
        rdSatisfaccionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdSatisfaccionNoActionPerformed(evt);
            }
        });

        lblIdNoveadd3.setText("34");

        cboSatisfaccion.setEditable(true);

        jLabel10.setText("Existe demanda de la actividad");

        gropuAntecedentes.add(rdDemandaSi);
        rdDemandaSi.setText("Si");
        rdDemandaSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDemandaSiActionPerformed(evt);
            }
        });

        gropuAntecedentes.add(rdDemandaNo);
        rdDemandaNo.setText("No");
        rdDemandaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDemandaNoActionPerformed(evt);
            }
        });

        cboDemanda.setEditable(true);

        lblIdNoveadd4.setText("35");

        lblIdNoveadd5.setText("36");

        jLabel21.setText("Hay identificación de técnicos y ciudadanos con la práctica");

        groupIdentificacion.add(rdIdentificacionSi);
        rdIdentificacionSi.setText("Si");
        rdIdentificacionSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdIdentificacionSiActionPerformed(evt);
            }
        });

        cboIdentificacion.setEditable(true);

        groupIdentificacion.add(rdIdentificacionNo);
        rdIdentificacionNo.setText("No");
        rdIdentificacionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdIdentificacionNoActionPerformed(evt);
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
                            .addComponent(txtInterez, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                                .addComponent(lblIdNoveadd2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblSiete)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdInteresSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdInteresNo))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addComponent(cboDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdDemandaSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDemandaNo))
                    .addComponent(cboSatisfaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtSatisfaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdSatisfaccionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdSatisfaccionNo))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addGroup(pnlMantenimientoTiempoLayout.createSequentialGroup()
                        .addComponent(txtIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rdIdentificacionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdIdentificacionNo))
                    .addComponent(cboIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtInterez, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdInteresSi)
                    .addComponent(rdInteresNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboInteres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdNoveadd3)
                    .addComponent(jLabel9))
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSatisfaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdSatisfaccionSi)
                    .addComponent(rdSatisfaccionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboSatisfaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblIdNoveadd4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDemandaSi)
                    .addComponent(rdDemandaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblIdNoveadd5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMantenimientoTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdIdentificacionSi)
                    .addComponent(rdIdentificacionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlPromocion.setBorder(javax.swing.BorderFactory.createTitledBorder("Asistencia"));

        jLabel5.setText("Nº de participantes.");

        groupParticipantes.add(rdParticipantesSi);
        rdParticipantesSi.setText("Si");

        groupParticipantes.add(rdParticipantesNo);
        rdParticipantesNo.setText("No");
        rdParticipantesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdParticipantesNoActionPerformed(evt);
            }
        });

        lblIdPromocion.setText("37");

        cboParticipantes.setEditable(true);

        groupAdecuancion.add(rdAdecuancionNo);
        rdAdecuancionNo.setText("No");
        rdAdecuancionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdecuancionNoActionPerformed(evt);
            }
        });

        groupAdecuancion.add(rdAdecuancionSi);
        rdAdecuancionSi.setText("Si");

        jLabel11.setText("adecuación del nº de participantes a la actividad.");

        lblIdPromocion1.setText("38");

        cboAdecuancion.setEditable(true);

        jLabel12.setText("adecuación de participantes con objetivos.");

        cboAdeParticipantes.setEditable(true);

        groupAdeParticipantes.add(rdAdeParticipantesNo);
        rdAdeParticipantesNo.setText("No");
        rdAdeParticipantesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdeParticipantesNoActionPerformed(evt);
            }
        });

        lblIdPromocion2.setText("39");

        groupAdeParticipantes.add(AdeParticipantesSi);
        AdeParticipantesSi.setText("Si");

        cboOrganizacion.setEditable(true);

        jLabel13.setText("organizaciones de ámbito justicia");

        groupParticipantes.add(rdOrganizacionSi);
        rdOrganizacionSi.setText("Si");

        groupParticipantes.add(rdOrganizacionNo);
        rdOrganizacionNo.setText("No");
        rdOrganizacionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdOrganizacionNoActionPerformed(evt);
            }
        });

        lblIdPromocion3.setText("40");

        lblIdPromocion4.setText("41");

        groupOrganizacion.add(rdOtrasNo);
        rdOtrasNo.setText("No");
        rdOtrasNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdOtrasNoActionPerformed(evt);
            }
        });

        jLabel14.setText("Otras Organizaciones sociales.");

        groupOrganizacion.add(rdOtrasSi);
        rdOtrasSi.setText("Si");

        cboOtras.setEditable(true);

        jLabel15.setText("Diversidad de grupos y sectores.");

        lblIdPromocion5.setText("42");

        groupDiversidad.add(rdDiversidadNo);
        rdDiversidadNo.setText("No");
        rdDiversidadNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdDiversidadNoActionPerformed(evt);
            }
        });

        groupDiversidad.add(rdDiversidadSi);
        rdDiversidadSi.setText("Si");

        cboDiversidad.setEditable(true);

        javax.swing.GroupLayout pnlPromocionLayout = new javax.swing.GroupLayout(pnlPromocion);
        pnlPromocion.setLayout(pnlPromocionLayout);
        pnlPromocionLayout.setHorizontalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdParticipantesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdParticipantesNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtAdecuancion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdAdecuancionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdAdecuancionNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromocionLayout.createSequentialGroup()
                                .addComponent(lblIdPromocion1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(txtAdeParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(AdeParticipantesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdAdeParticipantesNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtOrganizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdOrganizacionSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdOrganizacionNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtOtras, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdOtrasSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdOtrasNo))
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addComponent(txtDiversidad, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(rdDiversidadSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdDiversidadNo))
                    .addComponent(cboParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAdecuancion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAdeParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboOrganizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboOtras, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDiversidad, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(txtParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdParticipantesSi)
                    .addComponent(rdParticipantesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAdecuancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdAdecuancionSi)
                    .addComponent(rdAdecuancionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboAdecuancion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion2)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAdeParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AdeParticipantesSi)
                    .addComponent(rdAdeParticipantesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboAdeParticipantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion3)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOrganizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdOrganizacionSi)
                    .addComponent(rdOrganizacionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboOrganizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion4)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdOtrasSi)
                    .addComponent(rdOtrasNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboOtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPromocion5)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiversidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdDiversidadSi)
                    .addComponent(rdDiversidadNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboDiversidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlNormalizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Implicación"));

        jLabel6.setText("Coordinaciones implementadas ");

        groupCoordinaciones.add(rdCoordinacionesSi);
        rdCoordinacionesSi.setText("Si");

        groupCoordinaciones.add(rdCoordinacionesNo);
        rdCoordinacionesNo.setText("No");
        rdCoordinacionesNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCoordinacionesNoActionPerformed(evt);
            }
        });

        lblIdNormalizacion.setText("43");

        cboCoordinaciones.setEditable(true);

        jLabel17.setText("Participación activa de diferentes actores en todas las fases del proyecto.");

        lblIdNormalizacion1.setText("44");

        groupPartActiva.add(rdPartActivaSi);
        rdPartActivaSi.setText("Si");

        groupPartActiva.add(rdPartActivaNo);
        rdPartActivaNo.setText("No");
        rdPartActivaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPartActivaNoActionPerformed(evt);
            }
        });

        cboPartActiva.setEditable(true);

        jLabel18.setText("Estructuras de participación.");

        lblIdNormalizacion2.setText("45");

        groupEstrPart.add(rdEstrPartSi);
        rdEstrPartSi.setText("Si");

        groupEstrPart.add(rdEstrPartNo);
        rdEstrPartNo.setText("No");
        rdEstrPartNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdEstrPartNoActionPerformed(evt);
            }
        });

        cboEstrPart.setEditable(true);

        javax.swing.GroupLayout pnlNormalizacionLayout = new javax.swing.GroupLayout(pnlNormalizacion);
        pnlNormalizacion.setLayout(pnlNormalizacionLayout);
        pnlNormalizacionLayout.setHorizontalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblIdNormalizacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(lblIdNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCoordinaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdCoordinacionesSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdCoordinacionesNo))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboPartActiva, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblIdNormalizacion2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(cboEstrPart, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPartActiva, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstrPart, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(rdEstrPartSi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdEstrPartNo))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlNormalizacionLayout.createSequentialGroup()
                                .addComponent(rdPartActivaSi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdPartActivaNo))))
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboCoordinaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlNormalizacionLayout.setVerticalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblIdNormalizacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCoordinaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdCoordinacionesSi)
                    .addComponent(rdCoordinacionesNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCoordinaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblIdNormalizacion1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPartActiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdPartActivaSi)
                    .addComponent(rdPartActivaNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPartActiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblIdNormalizacion2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstrPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdEstrPartSi)
                    .addComponent(rdEstrPartNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEstrPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlPromocion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pnlMantenimientoTiempo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlNormalizacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(51, Short.MAX_VALUE))
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
            abm(getImb());
            evaluacionHecha("participacion");
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
            this.dispose();
    }//GEN-LAST:event_btNGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        selecDefecto(true);
        txtCoordinaciones.setText(null);
        txtInterez.setText(null);
       
        txtParticipantes.setText(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rdDiversidadNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDiversidadNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDiversidadNoActionPerformed

    private void rdOtrasNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdOtrasNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdOtrasNoActionPerformed

    private void rdOrganizacionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdOrganizacionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdOrganizacionNoActionPerformed

    private void rdAdeParticipantesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdeParticipantesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAdeParticipantesNoActionPerformed

    private void rdAdecuancionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdecuancionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdAdecuancionNoActionPerformed

    private void rdParticipantesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdParticipantesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdParticipantesNoActionPerformed

    private void rdDemandaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDemandaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDemandaNoActionPerformed

    private void rdDemandaSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdDemandaSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdDemandaSiActionPerformed

    private void rdSatisfaccionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSatisfaccionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdSatisfaccionNoActionPerformed

    private void rdSatisfaccionSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdSatisfaccionSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdSatisfaccionSiActionPerformed

    private void rdInteresNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdInteresNoActionPerformed

    }//GEN-LAST:event_rdInteresNoActionPerformed

    private void rdInteresSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdInteresSiActionPerformed

    }//GEN-LAST:event_rdInteresSiActionPerformed

    private void rdCoordinacionesNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdCoordinacionesNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdCoordinacionesNoActionPerformed

    private void rdPartActivaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPartActivaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPartActivaNoActionPerformed

    private void rdEstrPartNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdEstrPartNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdEstrPartNoActionPerformed

    private void rdIdentificacionSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdIdentificacionSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdIdentificacionSiActionPerformed

    private void rdIdentificacionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdIdentificacionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdIdentificacionNoActionPerformed

    private void imprimirarray(){
        for (int i = 0; i < this.observacion.length; i++) {
           System.out.println(cargaVariable("Participacion")[i]); 
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
            java.util.logging.Logger.getLogger(FrmParticipacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmParticipacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmParticipacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmParticipacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmParticipacion().setVisible(true);
              
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AdeParticipantesSi;
    private javax.swing.JPanel PnlCriterio;
    private javax.swing.ButtonGroup btGPertinencia1;
    private javax.swing.ButtonGroup btGPertinencia2;
    private javax.swing.ButtonGroup btGPertinencia3;
    private javax.swing.JButton btNGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalir;
    private org.jdesktop.swingx.JXComboBox cboAdeParticipantes;
    private org.jdesktop.swingx.JXComboBox cboAdecuancion;
    private org.jdesktop.swingx.JXComboBox cboCoordinaciones;
    private org.jdesktop.swingx.JXComboBox cboDemanda;
    private org.jdesktop.swingx.JXComboBox cboDiversidad;
    private org.jdesktop.swingx.JXComboBox cboEstrPart;
    private org.jdesktop.swingx.JXComboBox cboIdentificacion;
    private org.jdesktop.swingx.JXComboBox cboInteres;
    private org.jdesktop.swingx.JXComboBox cboOrganizacion;
    private org.jdesktop.swingx.JXComboBox cboOtras;
    private org.jdesktop.swingx.JXComboBox cboPartActiva;
    private org.jdesktop.swingx.JXComboBox cboParticipantes;
    private org.jdesktop.swingx.JXComboBox cboSatisfaccion;
    private javax.swing.ButtonGroup gropuAntecedentes;
    private javax.swing.ButtonGroup groupAdeParticipantes;
    private javax.swing.ButtonGroup groupAdecuancion;
    private javax.swing.ButtonGroup groupCoordinaciones;
    private javax.swing.ButtonGroup groupDiversidad;
    private javax.swing.ButtonGroup groupEstrPart;
    private javax.swing.ButtonGroup groupIdentificacion;
    private javax.swing.ButtonGroup groupInteres;
    private javax.swing.ButtonGroup groupOrganizacion;
    private javax.swing.ButtonGroup groupPartActiva;
    private javax.swing.ButtonGroup groupParticipantes;
    private javax.swing.ButtonGroup groupSatisfaccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCriterio;
    private javax.swing.JLabel lblIdNormalizacion;
    private javax.swing.JLabel lblIdNormalizacion1;
    private javax.swing.JLabel lblIdNormalizacion2;
    private javax.swing.JLabel lblIdNoveadd2;
    private javax.swing.JLabel lblIdNoveadd3;
    private javax.swing.JLabel lblIdNoveadd4;
    private javax.swing.JLabel lblIdNoveadd5;
    private javax.swing.JLabel lblIdPromocion;
    private javax.swing.JLabel lblIdPromocion1;
    private javax.swing.JLabel lblIdPromocion2;
    private javax.swing.JLabel lblIdPromocion3;
    private javax.swing.JLabel lblIdPromocion4;
    private javax.swing.JLabel lblIdPromocion5;
    private javax.swing.JLabel lblSiete;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlMantenimientoTiempo;
    private javax.swing.JPanel pnlNormalizacion;
    private javax.swing.JPanel pnlPromocion;
    private javax.swing.JRadioButton rdAdeParticipantesNo;
    private javax.swing.JRadioButton rdAdecuancionNo;
    private javax.swing.JRadioButton rdAdecuancionSi;
    private javax.swing.JRadioButton rdCoordinacionesNo;
    private javax.swing.JRadioButton rdCoordinacionesSi;
    private javax.swing.JRadioButton rdDemandaNo;
    private javax.swing.JRadioButton rdDemandaSi;
    private javax.swing.JRadioButton rdDiversidadNo;
    private javax.swing.JRadioButton rdDiversidadSi;
    private javax.swing.JRadioButton rdEstrPartNo;
    private javax.swing.JRadioButton rdEstrPartSi;
    private javax.swing.JRadioButton rdIdentificacionNo;
    private javax.swing.JRadioButton rdIdentificacionSi;
    private javax.swing.JRadioButton rdInteresNo;
    private javax.swing.JRadioButton rdInteresSi;
    private javax.swing.JRadioButton rdOrganizacionNo;
    private javax.swing.JRadioButton rdOrganizacionSi;
    private javax.swing.JRadioButton rdOtrasNo;
    private javax.swing.JRadioButton rdOtrasSi;
    private javax.swing.JRadioButton rdPartActivaNo;
    private javax.swing.JRadioButton rdPartActivaSi;
    private javax.swing.JRadioButton rdParticipantesNo;
    private javax.swing.JRadioButton rdParticipantesSi;
    private javax.swing.JRadioButton rdSatisfaccionNo;
    private javax.swing.JRadioButton rdSatisfaccionSi;
    private javax.swing.JTextField txtAdeParticipantes;
    private javax.swing.JTextField txtAdecuancion;
    private javax.swing.JTextField txtCoordinaciones;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtDemanda;
    private javax.swing.JTextField txtDiversidad;
    private javax.swing.JTextField txtEstrPart;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdentificacion;
    private javax.swing.JTextField txtInterez;
    private javax.swing.JTextField txtOrganizacion;
    private javax.swing.JTextField txtOtras;
    private javax.swing.JTextField txtPartActiva;
    private javax.swing.JTextField txtParticipantes;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextField txtSatisfaccion;
    // End of variables declaration//GEN-END:variables
}
