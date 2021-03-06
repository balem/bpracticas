/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Daniel San Nicolas
 */
public class FrmInnovacion extends javax.swing.JFrame {
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
    
    /**
     * Creates new form FrmInnovacion
     */
    public FrmInnovacion() {
        initComponents();
        txtCriterio.setText("Innovación");
        if(FrmPrincipal.getEvaluar()!= 0){
            txtId.setText(FrmPrincipal.getEvaluar()+"");
            configPractica();
            selecDefecto(true);
        }
        
        if(pregutarEvaluacion("innovacion") != 0){
           cargaEvaluacion(txtCriterio.getText().toString(), 6, 7);
           setImb(2);
        }
        recoCombo(listarRecomendaciones(), cboNovedad);
        recoCombo(listarRecomendaciones(), cboNorma);
        recoCombo(listarRecomendaciones(), cboPromocion);
        recoCombo(listarRecomendaciones(), cboPer4);
        recoCombo(listarRecomendaciones(), cboPer5);
        recoCombo(listarRecomendaciones(), cboPer6);
        
        combos.add(cboNovedad);
        combos.add(cboNorma);
        combos.add(cboPromocion);
        combos.add(cboPer4);
        combos.add(cboPer5);
        combos.add(cboPer6);

    }

    private void selecDefecto(boolean si){
        rdNormaNo.setSelected(si);
        rdNovedadNo.setSelected(si);
        rdPerMetoNo.setSelected(si);
        rdPerTransNO.setSelected(si);
        rdPerValGenNO.setSelected(si);
        rdPromocionNO.setSelected(si);
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
            String sql = "SELECT Titulo FROM practicas where id = "+txtId.getText();
            z.snt = z.con.createStatement();
            z.rs = z.snt.executeQuery(sql);
            z.rs.next();
            txtPractica.setText(z.rs.getString("Titulo"));
        } catch (SQLException ex) {
            Logger.getLogger(FrmInnovacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmInnovacion.class.getName()).log(Level.SEVERE, null, ex);
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
        
        int[] selecciono = new int[6];
        String[] texto = new String[6];
        
        if(rdNormaSi.isSelected() && !rdNormaNo.isSelected()){
              selecciono[0] = 1;
        }else{
            selecciono[0] = 0;
        }
        
        if(rdNovedadSi.isSelected() && !rdNovedadNo.isSelected()){
        selecciono[1] = 1;
        }else{
            selecciono[1] = 0;
        }
        
        if(rdPromocionSI.isSelected() && !rdPromocionNO.isSelected()){
        selecciono[2] = 1;
        }else{
            selecciono[2] = 0;
        }
        
        if(rdPerMetoSi.isSelected() && !rdPerMetoNo.isSelected()){
            selecciono[3] = 1;
        }else{
            selecciono[3] = 0;
        }
        
        if(rdPerValGenSi.isSelected() && !rdPerValGenNO.isSelected()){
            selecciono[4] = 1;
        }else{
            selecciono[4] = 0;
        }
        
        if(rdPerTransSi.isSelected() && !rdPerTransNO.isSelected()){
            selecciono[5] = 1;
        }else{
            selecciono[5] = 0;
        }
        
        texto[0] = txtNormalizacion.getText().toString();
        texto[1] = txtNovedadCreatividad.getText().toString();
        texto[2] = txtPromocion.getText().toString();
        texto[3] = txtPertinenciaMeto.getText().toString();
        texto[4] = txtPertinenciaVgen.getText().toString();
        texto[5] = txtPertienciaTrans.getText().toString();
        
        try {
            int bnreco;
            int aux = 1;
            String sql;
            for (int i = 0; i < selecciono.length; i++) {
                bnreco = saberRecomendacion(combos.get(i), 0);
                sql = "CALL revaluacion("+accion+", "+aux+","+txtId.getText().toString()+",'"+texto[i]+"', 'variable' ,"+selecciono[i]+", "+bnreco+");";
                System.out.println(sql);
                z.snt = z.con.createStatement();
                z.snt.execute(sql);
                aux++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmInnovacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmInnovacion.class.getName()).log(Level.SEVERE, null, ex);
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
                    case 1: 
                        txtNovedadCreatividad.setText(arreglo[i][2]);
                        cboNovedad.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdNovedadSi.setSelected(true);
                            rdNovedadNo.setSelected(false);
                        }else{
                            rdNovedadSi.setSelected(false);
                            rdNovedadNo.setSelected(true);
                        }
                    break;
                    case 2: 
                        txtPromocion.setText(arreglo[i][2]);
                        cboPromocion.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPromocionSI.setSelected(true);
                            rdPromocionNO.setSelected(false);
                        }else{
                            rdPromocionSI.setSelected(false);
                            rdPromocionNO.setSelected(true);
                        }
                    break;
                    case 3: 
                        txtNormalizacion.setText(arreglo[i][2]);
                        cboNorma.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdNormaSi.setSelected(true);
                            rdNormaNo.setSelected(false);
                        }else{
                            rdNormaSi.setSelected(false);
                            rdNormaNo.setSelected(true);
                        }
                    break;
                    case 4: 
                        txtPertinenciaMeto.setText(arreglo[i][2]);
                        cboPer4.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPerMetoSi.setSelected(true);
                            rdPerMetoNo.setSelected(false);
                        }else{
                            rdPerMetoSi.setSelected(false);
                            rdPerMetoNo.setSelected(true);
                        }
                    break;    
                    case 5: 
                        txtPertinenciaVgen.setText(arreglo[i][2]);
                        cboPer5.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPerValGenSi.setSelected(true);
                            rdPerValGenNO.setSelected(false);
                        }else{
                            rdPerValGenSi.setSelected(false);
                            rdPerValGenNO.setSelected(true);
                        }
                    break;
                    case 6: 
                        txtPertienciaTrans.setText(arreglo[i][2]);
                        cboPer6.setSelectedItem(arreglo[i][5]);
                        if(Integer.parseInt(arreglo[i][4]) == 1){
                            rdPerTransSi.setSelected(true);
                            rdPerTransNO.setSelected(false);
                        }else{
                            rdPerTransSi.setSelected(false);
                            rdPerTransNO.setSelected(true);
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

        btGNovedad = new javax.swing.ButtonGroup();
        btGPromocion = new javax.swing.ButtonGroup();
        btGNormalizacion = new javax.swing.ButtonGroup();
        btGPertinencia1 = new javax.swing.ButtonGroup();
        btGPertinencia2 = new javax.swing.ButtonGroup();
        btGPertinencia3 = new javax.swing.ButtonGroup();
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
        pnlNovead = new javax.swing.JPanel();
        txtNovedadCreatividad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rdNovedadSi = new javax.swing.JRadioButton();
        rdNovedadNo = new javax.swing.JRadioButton();
        lblIdNoveadd = new javax.swing.JLabel();
        cboNovedad = new org.jdesktop.swingx.JXComboBox();
        pnlPromocion = new javax.swing.JPanel();
        txtPromocion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rdPromocionSI = new javax.swing.JRadioButton();
        rdPromocionNO = new javax.swing.JRadioButton();
        lblIdPromocion = new javax.swing.JLabel();
        cboPromocion = new org.jdesktop.swingx.JXComboBox();
        pnlNormalizacion = new javax.swing.JPanel();
        txtNormalizacion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdNormaSi = new javax.swing.JRadioButton();
        rdNormaNo = new javax.swing.JRadioButton();
        lblIdNormalizacion = new javax.swing.JLabel();
        cboNorma = new org.jdesktop.swingx.JXComboBox();
        pnlPertinencia = new javax.swing.JPanel();
        txtPertinenciaMeto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdPerMetoSi = new javax.swing.JRadioButton();
        rdPerMetoNo = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        txtPertinenciaVgen = new javax.swing.JTextField();
        rdPerValGenSi = new javax.swing.JRadioButton();
        rdPerValGenNO = new javax.swing.JRadioButton();
        rdPerTransNO = new javax.swing.JRadioButton();
        rdPerTransSi = new javax.swing.JRadioButton();
        txtPertienciaTrans = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lblIdPer4 = new javax.swing.JLabel();
        lblPer5 = new javax.swing.JLabel();
        lblPer6 = new javax.swing.JLabel();
        cboPer4 = new org.jdesktop.swingx.JXComboBox();
        cboPer5 = new org.jdesktop.swingx.JXComboBox();
        cboPer6 = new org.jdesktop.swingx.JXComboBox();

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(btnSalir))
        );

        pnlNovead.setBorder(javax.swing.BorderFactory.createTitledBorder("Novedad"));

        jLabel3.setText("¿Se detecto cierto grado de creatividad aplicada a la práctica?");

        btGNovedad.add(rdNovedadSi);
        rdNovedadSi.setText("Si");
        rdNovedadSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNovedadSiActionPerformed(evt);
            }
        });

        btGNovedad.add(rdNovedadNo);
        rdNovedadNo.setText("No");
        rdNovedadNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNovedadNoActionPerformed(evt);
            }
        });

        lblIdNoveadd.setText("1");

        cboNovedad.setEditable(true);

        javax.swing.GroupLayout pnlNoveadLayout = new javax.swing.GroupLayout(pnlNovead);
        pnlNovead.setLayout(pnlNoveadLayout);
        pnlNoveadLayout.setHorizontalGroup(
            pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNoveadLayout.createSequentialGroup()
                .addGroup(pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNovedadCreatividad, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlNoveadLayout.createSequentialGroup()
                        .addComponent(lblIdNoveadd)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(rdNovedadSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdNovedadNo))
                    .addComponent(cboNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNoveadLayout.setVerticalGroup(
            pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNoveadLayout.createSequentialGroup()
                .addGroup(pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdNovedadSi)
                        .addComponent(rdNovedadNo))
                    .addGroup(pnlNoveadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblIdNoveadd)))
                .addGap(8, 8, 8)
                .addComponent(txtNovedadCreatividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pnlPromocion.setBorder(javax.swing.BorderFactory.createTitledBorder("Promoción"));

        jLabel4.setText("¿Se implementaron estarategias de promoción?");

        btGPromocion.add(rdPromocionSI);
        rdPromocionSI.setText("Si");

        btGPromocion.add(rdPromocionNO);
        rdPromocionNO.setText("No");
        rdPromocionNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPromocionNOActionPerformed(evt);
            }
        });

        lblIdPromocion.setText("2");

        cboPromocion.setEditable(true);

        javax.swing.GroupLayout pnlPromocionLayout = new javax.swing.GroupLayout(pnlPromocion);
        pnlPromocion.setLayout(pnlPromocionLayout);
        pnlPromocionLayout.setHorizontalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPromocionLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblIdPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdPromocionSI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdPromocionNO))
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addComponent(cboPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlPromocionLayout.setVerticalGroup(
            pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromocionLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdPromocionSI)
                    .addComponent(rdPromocionNO)
                    .addComponent(lblIdPromocion)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlNormalizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Normalización"));

        jLabel5.setText("¿Forma parte de la planificación del servicio?");

        btGNormalizacion.add(rdNormaSi);
        rdNormaSi.setText("Si");

        btGNormalizacion.add(rdNormaNo);
        rdNormaNo.setText("No");
        rdNormaNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNormaNoActionPerformed(evt);
            }
        });

        lblIdNormalizacion.setText("3");

        cboNorma.setEditable(true);

        javax.swing.GroupLayout pnlNormalizacionLayout = new javax.swing.GroupLayout(pnlNormalizacion);
        pnlNormalizacion.setLayout(pnlNormalizacionLayout);
        pnlNormalizacionLayout.setHorizontalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                        .addComponent(txtNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNormalizacionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblIdNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)))
                .addComponent(rdNormaSi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdNormaNo)
                .addContainerGap())
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addComponent(cboNorma, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlNormalizacionLayout.setVerticalGroup(
            pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNormalizacionLayout.createSequentialGroup()
                .addGroup(pnlNormalizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(rdNormaSi)
                    .addComponent(rdNormaNo)
                    .addComponent(lblIdNormalizacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNorma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlPertinencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Pertinencia"));

        jLabel6.setText("adecuación de la metodología al objetivo");

        btGPertinencia1.add(rdPerMetoSi);
        rdPerMetoSi.setText("Si");

        btGPertinencia1.add(rdPerMetoNo);
        rdPerMetoNo.setText("No");
        rdPerMetoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPerMetoNoActionPerformed(evt);
            }
        });

        jLabel7.setText("Valor generado u objetivos cubiertos");

        btGPertinencia2.add(rdPerValGenSi);
        rdPerValGenSi.setText("Si");

        btGPertinencia2.add(rdPerValGenNO);
        rdPerValGenNO.setText("No");
        rdPerValGenNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPerValGenNOActionPerformed(evt);
            }
        });

        btGPertinencia3.add(rdPerTransNO);
        rdPerTransNO.setText("No");
        rdPerTransNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPerTransNOActionPerformed(evt);
            }
        });

        btGPertinencia3.add(rdPerTransSi);
        rdPerTransSi.setText("Si");

        jLabel8.setText("Valores en transparencia generados, Objetivos cubiertos");

        lblIdPer4.setText("4");

        lblPer5.setText("5");

        lblPer6.setText("6");

        cboPer4.setEditable(true);

        cboPer5.setEditable(true);

        cboPer6.setEditable(true);

        javax.swing.GroupLayout pnlPertinenciaLayout = new javax.swing.GroupLayout(pnlPertinencia);
        pnlPertinencia.setLayout(pnlPertinenciaLayout);
        pnlPertinenciaLayout.setHorizontalGroup(
            pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPertinenciaMeto, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                                .addComponent(lblIdPer4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboPer4, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cboPer5, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPertinenciaVgen, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPertinenciaLayout.createSequentialGroup()
                            .addComponent(rdPerMetoSi)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rdPerMetoNo))
                        .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                            .addComponent(rdPerTransSi)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rdPerTransNO)))
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addComponent(rdPerValGenSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdPerValGenNO)))
                .addGap(30, 30, 30))
            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addComponent(lblPer5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addComponent(lblPer6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPertienciaTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPer6, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlPertinenciaLayout.setVerticalGroup(
            pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lblIdPer4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPertinenciaMeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboPer4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblPer5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPertinenciaVgen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboPer5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(lblPer6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPertienciaTrans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboPer6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPertinenciaLayout.createSequentialGroup()
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdPerMetoSi)
                            .addComponent(rdPerMetoNo))
                        .addGap(46, 46, 46)
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdPerValGenSi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rdPerValGenNO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(48, 48, 48)
                        .addGroup(pnlPertinenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdPerTransSi)
                            .addComponent(rdPerTransNO))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlPromocion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlNovead, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(PnlCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlNormalizacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPertinencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PnlCriterio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNovead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPertinencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void rdNovedadNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNovedadNoActionPerformed
       
    }//GEN-LAST:event_rdNovedadNoActionPerformed

    private void rdPromocionNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPromocionNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPromocionNOActionPerformed

    private void rdPerMetoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPerMetoNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPerMetoNoActionPerformed

    private void rdPerValGenNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPerValGenNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPerValGenNOActionPerformed

    private void rdPerTransNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdPerTransNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdPerTransNOActionPerformed

    private void rdNovedadSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNovedadSiActionPerformed
        
    }//GEN-LAST:event_rdNovedadSiActionPerformed

    private void btNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNGuardarActionPerformed
       abm(getImb());
            evaluacionHecha("innovacion");
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
            this.dispose();
    }//GEN-LAST:event_btNGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        selecDefecto(true);
        txtNormalizacion.setText(null);
        txtNovedadCreatividad.setText(null);
        txtPertienciaTrans.setText(null);
        txtPertinenciaMeto.setText(null);
        txtPromocion.setText(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rdNormaNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNormaNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNormaNoActionPerformed
    
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
            java.util.logging.Logger.getLogger(FrmInnovacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmInnovacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmInnovacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmInnovacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FrmInnovacion().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlCriterio;
    private javax.swing.ButtonGroup btGNormalizacion;
    private javax.swing.ButtonGroup btGNovedad;
    private javax.swing.ButtonGroup btGPertinencia1;
    private javax.swing.ButtonGroup btGPertinencia2;
    private javax.swing.ButtonGroup btGPertinencia3;
    private javax.swing.ButtonGroup btGPromocion;
    private javax.swing.JButton btNGuardar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnSalir;
    private org.jdesktop.swingx.JXComboBox cboNorma;
    private org.jdesktop.swingx.JXComboBox cboNovedad;
    private org.jdesktop.swingx.JXComboBox cboPer4;
    private org.jdesktop.swingx.JXComboBox cboPer5;
    private org.jdesktop.swingx.JXComboBox cboPer6;
    private org.jdesktop.swingx.JXComboBox cboPromocion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblCriterio;
    private javax.swing.JLabel lblIdNormalizacion;
    private javax.swing.JLabel lblIdNoveadd;
    private javax.swing.JLabel lblIdPer4;
    private javax.swing.JLabel lblIdPromocion;
    private javax.swing.JLabel lblPer5;
    private javax.swing.JLabel lblPer6;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlNormalizacion;
    private javax.swing.JPanel pnlNovead;
    private javax.swing.JPanel pnlPertinencia;
    private javax.swing.JPanel pnlPromocion;
    private javax.swing.JRadioButton rdNormaNo;
    private javax.swing.JRadioButton rdNormaSi;
    private javax.swing.JRadioButton rdNovedadNo;
    private javax.swing.JRadioButton rdNovedadSi;
    private javax.swing.JRadioButton rdPerMetoNo;
    private javax.swing.JRadioButton rdPerMetoSi;
    private javax.swing.JRadioButton rdPerTransNO;
    private javax.swing.JRadioButton rdPerTransSi;
    private javax.swing.JRadioButton rdPerValGenNO;
    private javax.swing.JRadioButton rdPerValGenSi;
    private javax.swing.JRadioButton rdPromocionNO;
    private javax.swing.JRadioButton rdPromocionSI;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNormalizacion;
    private javax.swing.JTextField txtNovedadCreatividad;
    private javax.swing.JTextField txtPertienciaTrans;
    private javax.swing.JTextField txtPertinenciaMeto;
    private javax.swing.JTextField txtPertinenciaVgen;
    private javax.swing.JTextField txtPractica;
    private javax.swing.JTextField txtPromocion;
    // End of variables declaration//GEN-END:variables
}
