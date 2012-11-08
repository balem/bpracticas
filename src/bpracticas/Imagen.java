/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
 
/**
 *
 * @author Maframaran
 */
public class Imagen extends JFrame {
 
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Imagen","jpg","png");
    private JFileChooser buscador = new JFileChooser();
 
    private JPanel contenedor;
 
    JButton examinar = new JButton("Examinar...");
    JButton guardar = new JButton("Guardar");
    JPanel visualizador = new JPanel();
 
    private ImageIcon imagen;
    private Graphics graficador;
    public Imagen(){
        contenedor = (JPanel) getContentPane();
 
        contenedor.setLayout(null);
 
        examinar.setBounds(20, 20, 120, 20);
        contenedor.add(examinar);
 
        guardar.setBounds(150, 20, 120, 20);
        contenedor.add(guardar);
 
        visualizador.setBounds(20, 60, 100, 100);
        contenedor.add(visualizador);
 
        examinar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                int result = buscador.showOpenDialog(null);
 
                if (result == JFileChooser.APPROVE_OPTION){
                    // aqui pintas la imagen
                }
            }
        });
 
        guardar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                // aqui guardas la imagen
            }
        });
 
        setSize(300,300);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
 
    public void paint(Graphics g){
        graficador = g;
    }
 
    public static void main(String arg[]){
        new Imagen();
    }
}
