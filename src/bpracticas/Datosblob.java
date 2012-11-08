/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import bpracticas.Archivos;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author ariel
 */
public class Datosblob {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Archivos archivos = new Archivos();
        //archivos.rutaPropiedades = "datos.properties";
        
        String res = JOptionPane.showInputDialog(null, "Introduzca\n- 1 para guardar.\n- 2 para extraer.");
        int opcion = Integer.parseInt(res.trim());
        
        if (opcion == 1) {
            JFileChooser elegir = new JFileChooser();
            int valor = elegir.showDialog(null, "Seleccionar");

            if (valor == JFileChooser.APPROVE_OPTION) {
                File file = elegir.getSelectedFile();
                String sali = file.getAbsolutePath();
                System.out.println(sali);
                int resultado = archivos.escribir(file.getName(), file.getAbsolutePath());
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Archivo Insertado :)");
                } else {
                    JOptionPane.showMessageDialog(null, "No se insertó :(");
                }
            }
        } else if (opcion == 2) {
            archivos.obtenerArchivo("LOGO.PNG", "/home/arielb/Documentos/libros/pdf4.pdf");
        }

    }
}
