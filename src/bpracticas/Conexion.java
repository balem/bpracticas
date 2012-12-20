package bpracticas;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    Connection con;
    Statement snt;
    ResultSet rs;
    File f = new File( "conexion.txt" );
    BufferedReader entrada;
    static String server = "";
    static String usr = "";
    static String pass = "";
    static String db = "";
    
    
    
    Conexion(){
        try {
            String[] linea = new String[4];
            entrada = new BufferedReader( new FileReader( f ) );
            int i =0;
            while(entrada.ready()){
                linea[i] = entrada.readLine();
                i++;
            }
            server = linea[0];
            usr = linea[1];
            pass = linea[2];
            db = linea[3];
            String url = "jdbc:mysql://"+server+"/"+db;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url,usr, pass);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public String dateFormat(Date ddate){
            java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String Fecha = dia.format(ddate);
            return Fecha;
        }
    
    public Connection getConnection(){ 
      return con; 
   } 
    
    public String gencod(String sql){
             int cod = 0;
        try {
            snt = con.createStatement();
            rs = snt.executeQuery(sql);
            rs.next() ;
            cod = rs.getInt("newcod");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
            return String.valueOf(cod);
   }
    
     public int buscaCod(String sql){
                int aux = 0;
        try {
            snt = con.createStatement();
            rs = snt.executeQuery(sql);
            if (rs.next()) {
                aux = rs.getInt("id");
            }
        } catch (SQLException ex) {
               Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 return aux;
     }
     
    public int obtenerCircunscripcion(String circunscripcion){
        int aux = 0;
        try {
            String sql = "SELECT id FROM circunscripcion where descripcion like '%"+circunscripcion+"%'";
            snt = con.createStatement();
            rs = snt.executeQuery(sql);
            rs.next();
            aux = rs.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print(aux);
        return aux;
        
    }
    
    
}
