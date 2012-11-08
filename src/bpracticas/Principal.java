package bpracticas;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTable table_exportar = null;
	public static modelo modelo_expor = new modelo();
	public static DefaultTableModel modelo_impor;
	/**********************************/
	public String nombres[]={"Luis","Sirilio","Shishuka","Octavio","Selino","Chano","Nicolasa"};  //  @jve:decl-index=0:
	public String Apellidos[]={"Segobia","Becerra","Estevez","Carnero","Mora","Zambrano","Rivas"};  //  @jve:decl-index=0:
	public String deudas[]={"1200","5000","300","800","450","750","3500"};  //  @jve:decl-index=0:
	public String estados[]={"activo","inactivo","activo","activo","inactivo","activo","inactivo"}; 
	Object[]fila = new String[4];
	/*******************************/
	private JButton bto_exportar = null;
	private JScrollPane jScrollPane1 = null;
	private JTable table_importar = null;
	private JButton bto_importar = null; 
	private JFileChooser FileChooser=new JFileChooser(); 
	/***************************/
	 Vector columna = new Vector();  //  @jve:decl-index=0:
	 Vector filas = new  Vector();  //  @jve:decl-index=0:
	 static int tabla_ancho = 0; // set the tableWidth 
	 static int tabla_alto = 0; // set the tableHeight

	 
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(4, 4, 604, 138));
			jScrollPane.setViewportView(getTable_exportar());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes table_exportar	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTable_exportar() {
		if (table_exportar == null) {
			table_exportar = new JTable(modelo_expor);
			modelo_expor.addColumn("NOMBRE");
			modelo_expor.addColumn("APELLIDO");
			modelo_expor.addColumn("DEUDA");
			modelo_expor.addColumn("ESTADO");
		    
		    TableColumn  columnart= table_exportar.getColumn("NOMBRE");
			columnart.setPreferredWidth(200);
			columnart.setMinWidth(200);
			columnart.setMaxWidth(200);
		    
		    
		    TableColumn  columnart1= table_exportar.getColumn("APELLIDO");
			columnart1.setPreferredWidth(200);
			columnart1.setMinWidth(200);
			columnart1.setMaxWidth(200);
			
			TableColumn  columnart2= table_exportar.getColumn("DEUDA");
		    columnart2.setPreferredWidth(100);
			columnart2.setMinWidth(100);
			columnart2.setMaxWidth(100);
			
			TableColumn  columnart3= table_exportar.getColumn("ESTADO");
		    columnart3.setPreferredWidth(100);
			columnart3.setMinWidth(100);
			columnart3.setMaxWidth(100);
			
			for(int cua=0; cua<nombres.length; cua++)
			{
				
				fila[0]=nombres[cua];
				fila[1]=Apellidos[cua];
				fila[2]=deudas[cua];
				fila[3]=estados[cua];
				modelo_expor.insertRow(0, fila);
				
			}
			
		}
		return table_exportar;
	}

	/**
	 * This method initializes bto_exportar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBto_exportar() {
		if (bto_exportar == null) {
			bto_exportar = new JButton();
			bto_exportar.setBounds(new Rectangle(609, 5, 103, 64));
			bto_exportar.setIcon(new ImageIcon(getClass().getResource("/images/nuevo.png")));
			bto_exportar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(e.getSource()==bto_exportar)
					{
						 try {
							 /******************AGREGO EL JTABLA A UN ARRAYLIST***************************/
					            List<JTable> tb = new ArrayList<JTable>();
					            tb.add(table_exportar);
					            //-------------------
					            exportar_excel excelExporter = new exportar_excel(tb, new File("DATOS_EXPORTADOS.xls"));
					            if (excelExporter.export()) {
					                JOptionPane.showMessageDialog(null, "DATOS EXPORTADOS CON EXITO!");
					            }
					        } catch (Exception ex) {
					            ex.printStackTrace();
					        }
						  llama_excel();
					}
					
				}
			});
		}
		return bto_exportar;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(new Rectangle(4, 160, 602, 149));
			jScrollPane1.setViewportView(getTable_importar());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes table_importar	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTable_importar() {
		if (table_importar == null) {
			table_importar = new JTable();
			modelo_impor = new DefaultTableModel(filas, columna);
			table_importar.setModel(modelo_impor);
			table_importar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
			table_importar.setEnabled(false); 
			table_importar.setRowHeight(25); 
			table_importar.setRowMargin(4); 
			tabla_ancho = modelo_impor.getColumnCount() * 150; 
			tabla_alto = modelo_impor.getRowCount() * 25; 
			table_importar.setPreferredSize(new Dimension( tabla_ancho, tabla_alto));

			
			
		}
		return table_importar;
	}

	/**
	 * This method initializes bto_importar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBto_importar() {
		if (bto_importar == null) {
			bto_importar = new JButton();
			bto_importar.setBounds(new Rectangle(606, 160, 107, 77));
			bto_importar.setIcon(new ImageIcon(getClass().getResource("/images/nuevo_gray.png")));
			bto_importar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(e.getSource()==bto_importar)
					{
					  FileChooser.showDialog(null, "Importar Hoja ");
					  File file = FileChooser.getSelectedFile();
					  if(!file.getName().endsWith("xls")){
						  
						JOptionPane.showMessageDialog(null, "Seleccione un archivo excel...", "Error",JOptionPane.ERROR_MESSAGE); 
					  } 
					  else 
					  { 
						  CreameTablaRapidito(file);
						  modelo_impor = new DefaultTableModel(filas, columna);
						 tabla_ancho = modelo_impor.getColumnCount() * 150; 
						  tabla_alto = modelo_impor.getRowCount() * 25; 
					      table_importar.setPreferredSize(new Dimension( tabla_ancho, tabla_alto));
					      table_importar.setModel(modelo_impor); 
				      }

						
						
					}
					
					
				}
			});
		}
		return bto_importar;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Principal thisClass = new Principal();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Principal() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(741, 368);
		this.setContentPane(getJContentPane());
		this.setLocationRelativeTo(null);
		this.setTitle("Importar_Exportar datos a excel con java-------by javaface-elblogdelprogramador.com");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getBto_exportar(), null);
			jContentPane.add(getJScrollPane1(), null);
			jContentPane.add(getBto_importar(), null);
		}
		return jContentPane;
	}
	/*********************METODOS Y FUNCIONES**********************/
  public void CreameTablaRapidito(File file) {
		
		Workbook workbook = null;
		
		try { 
		       try {
		       workbook = Workbook.getWorkbook(file);
		       
	    } catch (IOException ex) { 
	       Logger.getLogger( Principal.class. getName()).log(Level.SEVERE, null, ex);
	    } 	
	      
	      Sheet sheet = workbook.getSheet(0); 
	      columna.clear(); 
	      
		   for (int i = 0; i < sheet.getColumns(); i++) { 
			     Cell cell1 = sheet.getCell(i, 0);
			     columna.add(cell1.getContents()); 
			} 
		   filas.clear(); 
     
		    for (int j = 1; j < sheet.getRows(); j++) {
		    	
		         Vector d = new Vector(); 
		         
			    for (int i = 0; i < sheet.getColumns(); i++) {
			    	
			            Cell cell = sheet.getCell(i, j); 
			            d.add(cell.getContents());
			     }
		          d.add("\n");
		          filas.add(d); 
             } 
    
    
     }

	  catch (BiffException e) { 
		e.printStackTrace(); 
	 }
  }


/********************************/
	 
	/********************CLASE MODELO***********************/
	public static class modelo  extends DefaultTableModel
	{
		public boolean isCellEditable(int row, int column)
		{
			if (column==1)

				return false;
				return false;
		}
	   @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columna)
	   {
		if(columna==3) return Integer.class;
		if(columna==3) return Integer.class;
		return Object.class;
	   }
	   
    }
     /**************LLAMAR AL ARCHIVO CREADO***************/
	 public void llama_excel()
	 {
		 try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+"DATOS_EXPORTADOS.xls");
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	 /*******************************/
	
	

	
}  //  @jve:decl-index=0:visual-constraint="10,10"