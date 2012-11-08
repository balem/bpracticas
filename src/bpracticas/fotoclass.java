/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @web http://jc-mouse.blogspot.com
 * @author mouse
 */
public class fotoclass {
    Conexion con;
  public fotoclass (){
    con = new Conexion();
  } 
      
public void guardarfoto(String nombre, String foto) {
        {
            FileInputStream fis = null;
            try {
                File file = new File(foto);
                fis = new FileInputStream(file);

                PreparedStatement pstm = con.getConnection().prepareStatement("insert into " + 
                        " archivos(nombre, archivo) " + " values(?,?)");
                pstm.setString(1, nombre);                
                pstm.setBinaryStream(2, fis,(int) file.length());
                pstm.execute();
                pstm.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(fotoclass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(fotoclass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
   }
}