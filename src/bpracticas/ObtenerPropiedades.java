/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ObtenerPropiedades {

    public static String getPropiedades(String archivoIn, String nombrePropiedad)
            throws FileNotFoundException, IOException, PropertyException {

        Properties prop;
        InputStream in;
        String propiedad = "";

        in = ObtenerPropiedades.obtenerArchivo(archivoIn);

        prop = new Properties();

        prop.load(in);
        if (!nombrePropiedad.trim().equals("")) {
            propiedad = prop.getProperty(nombrePropiedad);
            if (propiedad == null) {
                throw new PropertyException("Propiedad \"" + nombrePropiedad + "\" no fue encontrada.");
            }
        } else {
            throw new PropertyException("Falta el nombre de la propiedad.");
        }


        return propiedad;
    }

    public static InputStream obtenerArchivo(String archivoIn) throws FileNotFoundException {
        InputStream in = new FileInputStream(archivoIn);
        return in;
    }
}
