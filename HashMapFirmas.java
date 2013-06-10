package utilidades;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HashMapFirmas {

  
	public Map<BitSet, List<Integer>> m;
	
	public HashMapFirmas() {
		// TODO Auto-generated constructor stub
		m = new HashMap<BitSet, List<Integer>>();
	}

	
	public void agregar(BitSet f, Integer indice){
		
		
		
		
		
		List<Integer> l = m.get(f);
		if(l == null){
			
			
			
			l = new ArrayList<Integer>();
			l.add(indice);
			m.put(f, l);
			
		}else{
			
			l.add(indice);
			m.put(f, l);
			
		}
			
			
			
		}
		
		/**
		 * Método que sirve para obtener la lista de adayacencia generada
		 * @return Lista de Adyacencia de firmas e indices
		 */
		public Map<BitSet, List<Integer>> GetMap(){
			return m;
		}
		
		
		/**
		 * Imprime valores de Hasmap
		 */
		public void Mostrar(){
		
		
				Set set = m.entrySet(); 
				// Get an iterator 
				Iterator i = set.iterator(); 
				while(i.hasNext()) { 
				Map.Entry me = (Map.Entry)i.next(); 
				System.out.print(me.getKey() + "-> "); 
				System.out.println(me.getValue()); 
				} 
				
				
			//}
		}
		
		public List<Integer> getList(BitSet f){
			
			return m.get(f);
		}
	
}
