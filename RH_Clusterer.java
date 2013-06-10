package rhc;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import utilidades.BitMatriz;
import utilidades.HashMapFirmas;
import utilidades.HashMapFirmas2;
import utilidades.Objeto;
//import Bisectriz;
//import SimpleMatrix;
import utilidades.BitMatriz;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import org.ejml.simple.*;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




public class RH_Clusterer {
  
	//Variables Globales
	
	private static double lamda;
	 private static String filePath;
	 private static String SOPath;
	 //declaramos dataset
	 private static SimpleMatrix dataset;
	 private static ArrayList ArregloAleatorios;
	 //declaramos matriz con Hiperplanos
	 private static SimpleMatrix H;
	 //declaramos matriz de de bits
	 private static BitMatriz mh;
	 private static Objeto lattice;
	 private static String salvar;
	
public static void main(String[] args) {
        
	
	try{
		
			if(Double.parseDouble(args[0]) == 0 ){
				
				filePath = args[1];
				lamda = Double.parseDouble(args[2]);
				
				readArrf(filePath);
				GenerarAleatorio();
				generaBisectrices();
				generaFirmas();
				
				if (args.length == 4){
					
					SOPath = args[3];
					salvar = "y";
					
				}else{
					salvar = "n";
				}
				
			}else if ( Double.parseDouble(args[0]) == 1) {
			
				filePath = args[1];
			
				//Cargamos objeto 
				readSerializableObject(filePath);
				
				dataset = lattice.getDs();
				H = lattice.getBisect();
				mh = lattice.getFirm();
				
				if (args.length == 4){
					SOPath = args[3];
					salvar = "y";
					
				}else{
					salvar = "n";
				}
				
			}
			
		
		
		filePath = args[1];
		lamda = Double.parseDouble(args[2]);

        }
        catch(Exception exception){
            System.out.println("How to use: PATH_DATASET HYPERPLANES");
            System.exit(0);
        }
	
	
	//Cargamos arff a Matriz


	
	HashMapFirmas hsm = new HashMapFirmas();
	HashMapFirmas2 hsm2 = new HashMapFirmas2();
	
	for(int u = 0; u < dataset.numRows(); u++){
		
		hsm.agregar(mh.getFirma(u), u);
		hsm2.agregar((int)mh.RowToNum(u), u);
		
	}
	
	
	
	
	if(salvar == "y"){
	
		writeSerializableObject(SOPath);
	}
	

	}
	
	







/**
 * Lee un archivo ARFF y lo carga en una Matriz
 * @param ruta,  ruta en la cual se encuentra el arff
 */
	public  static void readArrf( String ruta){
	
		//SimpleMatrix ds;
          Instances data;
		 try {
			   BufferedReader reader = new BufferedReader( new FileReader(ruta));
			   
			   //Cargar Arff
			   data = new Instances(reader);
			   data.setClassIndex(data.numAttributes() - 1);
			   double[] aux = new double[data.numAttributes()-1];
			   dataset = new SimpleMatrix(data.numInstances(), data.numAttributes()-1);
				 int i=0;
					while(i < data.numInstances() ){
					
						aux = data.instance(i).toDoubleArray();
						for(int u=0; u<data.numAttributes()-1; u++){
							//System.out.println(aux[u]);
							dataset.set(i, u, aux[u]);
						}
						i++;
					}
			   reader.close();
				
	     }catch ( IOException e ) {
	    	    System.err.println("Se produjo un error de E/S");
	    	    System.out.println(ruta);
	     } 
	
		

	}
	
	
	public static void GenerarAleatorio(){
		
		ArregloAleatorios = new ArrayList((dataset.numRows()));
		
		for(int i = 1; i <= dataset.numRows() ; i++){
			
			ArregloAleatorios.add(i);
			
		}
			
		//Desordena elementos de arreglo
		Collections.shuffle(ArregloAleatorios);
		
	}
	
	
	
	
	/** Bisectriz método que recibe los puntos a y b, generando una bisectriz perpendicular al trazo entre a y b, 
	 * y que pasa por el punto medio  de dicho trazo
	 * 
	 * @param a matriz del tipo SimpleMatrix que almacena las coordenadas de un punto en el espacio
	 * @param b matriz del tipo SimpleMatrix que almacena las coordenadas de un punto en el espacio
	 * @return matriz del tipo SimpleMatrix con ecuación de bisectriz del tipo a1X1 +...+anXn + C = 0,
	 * que pasa por el punto medio del trazo que une los puntos a y b siendo perpendicular a dicho punto
	 */
	public static SimpleMatrix Bisectriz(SimpleMatrix a, SimpleMatrix b){
	
		//declaramos matriz que contendrá ecuación con bisectriz
		SimpleMatrix bisectriz;
		//instanciamos matriz que contendrá ecuación de bisectriz
		bisectriz = new SimpleMatrix(1, a.numCols()+1);
		

		//insertamos primera parte (A + BX)
		bisectriz.insertIntoThis(0, 0, b.minus(a));
	
	

		//Insertamos coeficiente C
		bisectriz.insertIntoThis(0, a.numCols(), b.mult(b.transpose()).scale(0.5).minus(a.mult(a.transpose()).scale(0.5)).scale(-1));

	
		return bisectriz;
	}
	
	
	public static void generaBisectrices(){
	 H = new SimpleMatrix((int)lamda-1, dataset.numCols()+1);
		
		//Matriz auxiliar
		SimpleMatrix a;
		SimpleMatrix b;
		
		//Bisectriz bisec = new Bisectriz();
		
		
		
		
		
		//Se generan lamda bisectrices
		for(int i = 1; i<lamda; i++){
	

		//Se extrae el primer elemento para generar bisectriz

		a = dataset.extractMatrix(Integer.valueOf(ArregloAleatorios.get(2*(i-1)).toString())-1, Integer.valueOf(ArregloAleatorios.get(2*(i-1)).toString()) , 0, dataset.numCols());

		
		//Se extrae el segundo elemento para generar bisectriz	

		b = dataset.extractMatrix(Integer.valueOf(ArregloAleatorios.get(2*(i-1)+1).toString())-1, Integer.valueOf(ArregloAleatorios.get(2*(i-1)+1).toString()) , 0, dataset.numCols());

		//Se genera bisectriz y se inserta en H
		H.insertIntoThis(i-1, 0, Bisectriz(a, b));
		
		}
		
		
	}
	
	
	
public static double evaluaPosicion(SimpleMatrix recta, SimpleMatrix punto ){
		
		int valor;
		SimpleMatrix c = new SimpleMatrix(punto.numRows(), punto.numCols());
		
		if(recta.extractMatrix(0, 1, 0, recta.numCols()-1).mult(c.transpose()).elementSum()<=-1*recta.get(0, recta.numCols()-1)){
		
			if(recta.extractMatrix(0, 1, 0, recta.numCols()-1).mult(punto.transpose()).elementSum()<=-1*recta.get(0, recta.numCols()-1)){
				valor = 0;
			}else{
				valor = 1;
			}
			
		}else{
			
			if(recta.extractMatrix(0, 1, 0, recta.numCols()-1).mult(punto.transpose()).elementSum()<=-1*recta.get(0, recta.numCols()-1)){
				valor = 1;
			}else{
				valor = 0;
			}
			
		}
		
		
		
	
		return valor;
	}




/**
 * Genera una matriz de bits, en la cual se almacena la posición de 
 * cada elemento i del dataset, en relación al lado de la bisectriz j en la que se encuentre el 
 * elemento i, almacenando un 1 o un cero en la posicion del bit j correspondiente a la bisectriz j
 * 
 * @return posiciones de elementos en relación a bisectricez
 */
public static void generaFirmas( ){
	
	SimpleMatrix recta;
	SimpleMatrix punto;
	
    mh = new BitMatriz(dataset.numRows(), H.numCols());
    
	for(int i = 0; i<dataset.numRows(); i++){
		//System.out.println(H.numRows());

		punto = dataset.extractMatrix(i, i+1, 0, dataset.numCols());
		for(int j = 0; j<H.numRows(); j++){
			
			recta= H.extractMatrix(j, j+1, 0, H.numCols());
			if (evaluaPosicion(recta, punto)==1) {
		
				mh.setBit(i, j);
				
			}
			
		}
	
		
	}
	
	
}


/**
 * Escribe un objeto serializable en un archivo según su ruta
 * @param path ruta donde se desea escribir el archivo
 */
private static void writeSerializableObject(String path){
    ObjectOutputStream out;
    try {
        out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(lattice);
        out.flush();
        out.close();
    } 
    catch (IOException ex) {
        System.out.println("Path "+path+" not found");
    } 
}




/**
 * Lee un objeto serialiable de un archivo según su ruta
 * @param path ruta desde donde se desea leer el archivo
 */
private static void readSerializableObject(String path){
    ObjectInputStream in;
    try {
    	
    	
        in = new ObjectInputStream(new FileInputStream(path));
        lattice =  (Objeto) in.readObject();
        

        in.close();
    } 
    catch (IOException ex) {
       System.out.println("File "+path+" not found. "+"Initializing new lattice");
    } 
    catch (ClassNotFoundException ex) {
       System.out.println("ERROR!");
    } 
}
