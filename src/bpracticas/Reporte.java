/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Enrique Rodríguez
 */

public class Reporte {
    Conexion z = new Conexion();

    public void iniciar(String sql, String reporte){
        try {
            z.snt = z.con.createStatement();
            
            z.rs = z.snt.executeQuery(sql);
            
            JRResultSetDataSource jrRS = new JRResultSetDataSource(z.rs);
            HashMap parameters = new HashMap();
            
            URL urlMaestro = getClass().getResource(reporte);
            
            JasperReport masterReport = null;
            
            masterReport = (JasperReport) JRLoader.loadObject(urlMaestro);
            JasperPrint masterPrint;
                               
            masterPrint = JasperFillManager.fillReport(masterReport, parameters, jrRS);

            JasperViewer ventana = new JasperViewer(masterPrint, false);
            ventana.setTitle("Vista Previa");
            ventana.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(FrmRPractica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FrmRPractica.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
