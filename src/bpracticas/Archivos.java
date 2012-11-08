/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ariel
 */
public class Archivos extends Conexion {
/**
     * 
     * @param nombre
     * @param rutaArchivo
     * @return 
     */
    Conexion z = new Conexion();
    public int escribir(String nombre, String rutaArchivo) {
        InputStream entrada = null;

        PreparedStatement pst = null;
        int ingresados = 0;
        try {
            File archivo;
            String insert;

            z.con.setAutoCommit(false);
            

            insert = "Insert into documentos values(?,?)";

            pst = z.con.prepareStatement(insert);

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
            ResultSet rs;
            Blob blob;
            FileOutputStream archivoSalida;
            String select;

            byte[] arreglo;
            int byteLeidos = 0;

            z.con.setAutoCommit(false);
            
            select = "select documentos from documentos WHERE nombre=?";
            pst = z.con.prepareStatement(select);
            pst.setString(1, nombreArchivoBuscar);

            z.rs = pst.executeQuery();

            if (z.rs != null) {
                z.rs.next();
                blob = z.rs.getBlob(1);
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
}
