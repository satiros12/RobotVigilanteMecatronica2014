
public class Persona {
private String DNI;
private String telefono;
private String nombre;
private String apellido;
public String getDNI () {return DNI;}
public String getNombre () {return nombre;}
public String getApellido() {return apellido;}
public String gettelefono () {return telefono;}
public void setDNI (String DNI){this.DNI=DNI;}
public void setNombre (String nombre){this.nombre=nombre;}
public void setApellido (String apellido){this.apellido=apellido;}
public void settelefono (String telefono){this.telefono=telefono;}
Persona(){
	DNI=null;
	telefono=null;
	nombre=null;
	apellido=null;
}
Persona(String DNI){
	setDNI(DNI);
}
Persona(String DNI,String nombre,String apellido,String telefono){
	setDNI(DNI);
	settelefono(telefono);
	setApellido(apellido);
	setNombre(nombre);
}
public boolean equals (Persona p){return this.getDNI().equals(p.getDNI());}
public int compareTo(Persona p){
	return Integer.parseInt(p.getDNI())-Integer.parseInt(this.getDNI());
}
}
