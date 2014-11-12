import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class InterfazGrafica {
	String dirt=null;
	Frame f=new Frame();
	Frame f2=new Frame();
	Frame f3=new Frame();
	Panel p=new Panel(new GridLayout(11,1));
	Panel p2=new Panel(new GridLayout(1,2));
	Panel p3=new Panel(new GridLayout(1,2));
	Panel p4=new Panel(new GridLayout(1,3));
	Panel p5=new Panel(new GridLayout(1,2));
	Panel p6=new Panel(new GridLayout(1,2));
	Panel p7=new Panel(new GridLayout(1,2));
	Panel p8=new Panel(new GridLayout(1,2));
	Panel p9=new Panel(new GridLayout(1,3));
	Panel p10=new Panel(new GridLayout(3,1));
	Button b=new Button("Añadir");
	Button b2=new Button("Eliminar");
	Button b3=new Button(">>Buscar");
	Button b4=new Button("Salir");
	Label l=new Label("Datos del contacto");
	Label l2=new Label("Opciones:");
	Label l3=new Label("Buscar");
	Label l4=new Label("Nombre");
	Label l5=new Label("Apellidos");
	Label l6=new Label("Telefono");
	Label l7=new Label("DNI");
	Label l8=new Label("DNI del contacto");
	Label l9=new Label("--------------------------------------------------------------------------------------------------");
	Label l10=new Label("");
	Label l11=new Label("");
	Label l12=new Label("Creado por: Christian Ayuso Thomas",Label.CENTER);
	Label l13=new Label("Version 1.0",Label.CENTER);
	Label l14=new Label("Agradecimientos: Juan Bermúdez y Rubén Cerrillo",Label.CENTER);
	Color c =new Color(30,117,175);
	TextField t=new TextField(20);
	TextField t2=new TextField(20);
	TextField t3=new TextField(20);
	TextField t4=new TextField(20);
	TextField t5=new TextField(20);
	MenuBar menu=new MenuBar();
	Menu archivo=new Menu("Archivo");
	Menu ayuda=new Menu("Ayuda");
	MenuItem cargar= new MenuItem("Cargar");
	MenuItem guardar= new MenuItem("Guardar");
	MenuItem creador =new MenuItem("Acerca de...");
	MenuItem salir=new MenuItem("Salir");
	Dialog crea =new Dialog(f,"Acerca de...",true);
	Dialog carg =new Dialog(f,"Cargar",true);
	Dialog guard =new Dialog(f,"Guardar",true);
	FileDialog car=new FileDialog(f,"Cargar",FileDialog.LOAD);
	Lista a=new Lista();
	InterfazGrafica(){
	//Frame
	f.setSize(400,400);
	f.setTitle("Agenda Electronica");
	f.setVisible(true);
	f.setLocation(360,240);	
	//Dialogos
	crea.setSize(300,200);
	crea.setTitle("Acerca de...");
	crea.setLocation(420,340);	
	//Menu
	f.setMenuBar(menu);
	menu.add(archivo);
	menu.add(ayuda);
	archivo.add(cargar);
	archivo.add(guardar);
	archivo.addSeparator();
	archivo.add(salir);
	ayuda.add(creador);
	//Paneles
	p10.add(l12);
	p10.add(l13);
	p10.add(l14);
	crea.add(p10);
	p2.add(l4);
	p2.add(t);
	p5.add(l5);
	p5.add(t2);
	p6.add(l6);
	p6.add(t3);
	p7.add(l7);
	p7.add(t4);
	p8.add(b);
	p8.add(b2);
	p4.add(l8);
	p4.add(t5);
	p4.add(b3);
	l.setBackground(c);
	p.add(l);
	p.add(p2);
	p.add(p5);
	p.add(p6);
	p.add(p7);
	l2.setBackground(c);
	p.add(l2);
	p.add(p8);
	l3.setBackground(c);
	p.add(l3);
	p.add(p4);
	p.add(l9);
	p9.add(l10);
	p9.add(l11);
	p9.add(b4);
	p.add(p9);
	f.add(p);
	//Eventos
	f.addWindowListener(new VentanaEsquema());
	b.addMouseListener(new RatonEsquema());
	b2.addMouseListener(new RatonEsquema2());
	b3.addMouseListener(new RatonEsquema3());
	b4.addMouseListener(new RatonEsquema5());
	salir.addActionListener(new eventaco());
	cargar.addActionListener(new eventaco());
	guardar.addActionListener(new eventaco());
	creador.addActionListener(new eventaco());
	crea.addWindowListener(new RatonEsquema6());
	}
	public void CargarFichero(String s){
		String line;
		try{
			File fich= new File(s);
			FileReader fer=new FileReader(fich);
			BufferedReader buf=new BufferedReader(fer);
			while(!(line=buf.readLine()).equals("--")){
				Persona p=new Persona();
				p.setNombre(line);
				line=buf.readLine();
				p.setApellido(line);
				line=buf.readLine();
				p.settelefono(line);
				line=buf.readLine();
				p.setDNI(line);
				a.aniadir(p);
			}
			fer.close();
		}
		catch(Exception e){
			System.out.println("Error en el fichero");
		}
	}
	public void GuardarFichero(String s){
		try{
			FileWriter fich=new FileWriter(s);
			PrintWriter pri= new PrintWriter(fich);
			for(int i=0;i<a.getTam();i++){
				pri.println(a.obtener(i).getNombre());
				pri.println(a.obtener(i).getApellido());
				pri.println(a.obtener(i).gettelefono());
				pri.println(a.obtener(i).getDNI());
		}
			pri.println("--");
			fich.close();
		}
		catch(Exception e){
			System.out.println("Error en el fichero");
		}
	}
	public class RatonEsquema extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
				String nom=t.getText();
				String ape=t2.getText();
				String tel=t3.getText();
				String dni=t4.getText();
				Persona p=new Persona(dni,nom,ape,tel);
				if(a.aniadir(p))
				{
					a.imprimirlista();
				}
				}
		public void mouseEntered (MouseEvent Eventoin){
			Eventoin.getComponent().setBackground(Color.green);
		}
		public void mouseExited(MouseEvent Eventoin) {
			Eventoin.getComponent().setBackground(Color.white);	
		}
	}
	public class RatonEsquema2 extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
			String dni=t4.getText();
			Persona k=new Persona(dni);
				if(a.borrar(k)){
					System.out.println("La persona ha sido borrada");
					Panel p3=new Panel(new GridLayout(2,1));
					Panel p2=new Panel(new GridLayout(1,3));
					Label l=new Label("       La persona ha sido borrada");
					Label l2=new Label("");
					Label l3=new Label("");
					Button b=new Button("Salir");
					f2.setSize(200,100);
					f2.setVisible(true);
					f2.setLocation(480,600);	
					p3.add(l);
					p2.add(l2);
					p2.add(b);
					p2.add(l3);
					p3.add(p2);
					f2.add(p3);
					b.addMouseListener(new RatonEsquema4());
				}
			}
		public void mouseEntered (MouseEvent Eventoin){
			Eventoin.getComponent().setBackground(Color.green);
		}
		public void mouseExited(MouseEvent Eventoin) {
			Eventoin.getComponent().setBackground(Color.white);	
		}
	}
	public class RatonEsquema3 extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
			String d = t5.getText();
				Persona k=a.consultar(d);
				if(k!=null){
				t.setText(k.getNombre());
				t2.setText(k.getApellido());
				t3.setText(k.gettelefono());
				t4.setText(k.getDNI());}
				else {
					Panel p3=new Panel(new GridLayout(2,1));
					Panel p2=new Panel(new GridLayout(1,3));
					Label l=new Label("La persona no ha sido encontrada");
					Label l2=new Label("");
					Label l3=new Label("");
					Button b=new Button("Salir");
					f2.setSize(200,100);
					f2.setVisible(true);
					f2.setLocation(480,600);	
					p3.add(l);
					p2.add(l2);
					p2.add(b);
					p2.add(l3);
					p3.add(p2);
					f2.add(p3);
					b.addMouseListener(new RatonEsquema4());
				}
			}
		public void mouseEntered (MouseEvent Eventoin){
			Eventoin.getComponent().setBackground(Color.green);
		}
		public void mouseExited(MouseEvent Eventoin) {
			Eventoin.getComponent().setBackground(Color.white);	
		}
	}
	public class RatonEsquema4 extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
		f2.setVisible(false);
		}
	}
	public class RatonEsquema5 extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
		System.exit(0);
		}
	}
	public class eventaco implements ActionListener{
		public void actionPerformed(ActionEvent i){
			if(i.getActionCommand().equals("Cargar")){
				car.setVisible(true);
				dirt=(car.getDirectory()+car.getFile());
				System.out.println(dirt);
				CargarFichero(dirt);
			}
			if(i.getActionCommand().equals("Guardar")){
				if(dirt!=null){
					GuardarFichero(dirt);
					System.out.println("Fichero aparcao");
				}
				else System.out.println("No se ha cargao nada");
			}
			if(i.getActionCommand().equals("Acerca de...")){
				crea.setVisible(true);
			}
			if(i.getActionCommand().equals("Salir")){
				System.exit(0);
			}
		}
	}
	public class RatonEsquema6 extends WindowAdapter{
			public void windowClosing(WindowEvent Eventoin) {
				crea.setVisible(false);
		}
		}

	public static void main(String[] args){
		InterfazGrafica g=new InterfazGrafica();
	}

}
