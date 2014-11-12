import java.util.*;
public class Lista {
	public List<Persona> L;
	public ListIterator<Persona> LI;
	public Persona p=null;
	
	Lista(){
		L =new LinkedList<Persona>();
		LI= L.listIterator();
	}
	public boolean aniadir(Persona p){
		return L.add(p);
	}
	public boolean existe(String dni){
		LI = L.listIterator();
		boolean res= false;
		while(LI.hasNext()){
			p=LI.next();
			if(dni.equals(p.getDNI()))
			{
				LI.remove();
				res=true;
			}
	}
		return res;
	}
	public boolean borrar(Persona k){
		boolean resul= false;
		if(existe(k.getDNI()))
			resul=true;
		return resul;
	}
	
	public Persona consultar(String dni){
		LI = L.listIterator();
		Persona k = null;
		while(LI.hasNext()){
			p=LI.next();
			if(dni.equals(p.getDNI()))
			{
				k=p;
				break;
			}
		}
		return k;
	}
	void imprimirlista(){
		LI = L.listIterator();
		while(LI.hasNext()){
			System.out.println(LI.next());
		}
	}
	int getTam(){
		return L.size();
	}
	public Persona obtener(int i){
		return L.get(i);
	}
	}

