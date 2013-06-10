package utilidades;

import java.util.BitSet; 
import java.io.Serializable;
import java.lang.Math;
/**
 * Clase que sirve como estructura para almacenar y manipular matrices que almacenan bits 
 * @author Alexis
 *
 */
public class BitMatriz implements Serializable {
  
	BitSet[] firma;
	int lamda;
	
	int numInstancias;
	
	/**
	 * Método que crea una instacia de la clase BitMatrix
	 * 
	 * @param numInstancias número de filas que contiene la matriz bínaria
	 * @param lamda número de bits que se almacenará en cada fila de la matriz
	 */
	public BitMatriz( int numInstancias, int lamda ){
		
		
		firma = new BitSet[numInstancias];
		this.numInstancias = numInstancias;
		
		for (int i = 0; i < numInstancias; i++){
			firma[i] = new  BitSet(lamda);
		}
		
		this.lamda =lamda;
	}

	
	
	/**
	 * Método que modifica el valor de un bits a 1, en la posición fila i, columna j
	 * 
	 * @param i número de fila en la que se encuentra el elemento a modificar
	 * @param j número de columna en la que se encuentra el elemento a modificar
	 */
	public void setBit (int i, int j) { 
			firma[i].set(j); 
		}
	
	
	/**
	 * Obtiene elementos de una celda
	 * 
	 * @param i número de fila en la que se encuentra el elemento que se desea obtener
	 * @param j número de columna en la que se encuentra el elemento que se desea obtener
	 * @return el valor del bits de la posición i, j
	 */
	public boolean getBit (int i, int j) { 
			return firma[i].get(j); 
		}
	
	
	/**
	 * Obtiene fila con bits
	 * 
	 * @param i número de fila en la que se encuentra el elemento que se desea obtener
	 * @return matriz binaria para la columna
	 */
	public BitSet getFirma (int i) { 
		return firma[i]; 
		
	}
	
	
	/**
	 * convierte una fila de bits en su representación de entero
	 * 
	 * @param row númer fila que desea transformarse
	 * @return decimal con el valor del binario representado
	 */
	public double RowToNum (int row) { 
		double num;
		int bit;
		num =0;
		for(int i = 0; i< lamda+1;i++){
			if (firma[row].get(i)==true) {
				bit=1;
			} else {
				bit = 0;
			}
			
			num = num + ( bit * (Math.pow(2, i)) );
			
			
			
		}
		
		return num; 
	}
	
	
	
	
	public void imprimir(){
		
		for(int i=0; i<numInstancias; i++){
			
			for(int j=0; j<lamda; j++){
				
				if (this.getBit(i, j)==true){
				System.out.print(1+" ");
				} else {
					System.out.print(0+" ");
				}
			}
			System.out.println();
		}
	}
	
	
	public void imprimirf(int fila){
		
		//for(int i=0; i<filas; i++){
			
			for(int j=0; j<lamda; j++){
				
				if (this.getBit(fila, j)==true){
				System.out.print(1+" ");
				} else {
					System.out.print(0+" ");
				}
			}
			System.out.println();
		//}
	}
	
	

	
}
