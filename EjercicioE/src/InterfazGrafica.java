import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;

import jssc.SerialPort;
import jssc.SerialPortException;


public class InterfazGrafica {
	//Variables del programa
	String dirt=null;
	final int auto = 0;
	final int manual = 1;
	int estadoC=1;
	int estadoR=1;
	int x=0;
	int y=0;
	int posx=0;
	int posy=0;
	int vel=0;
	int velD=0;
	int velI=0;
	//Variables de la interfaz
	Frame f=new Frame();	
	Panel p=new Panel(new GridLayout(1,2));
	Panel p2=new Panel(new GridLayout(2,1));
	Panel p3=new Panel(new GridLayout(1,2));
	Panel p4=new Panel(new GridLayout(4,2));
	Panel p5=new Panel(new GridLayout(3,5));
	Panel p6=new Panel(new GridLayout(3,3));
	Button b=new Button(">");
	Button b2=new Button("<");
	Button b3=new Button(">>");
	Button b4=new Button("<<");
	Button b5=new Button("u");
	Button b6=new Button("d");
	Button b7=new Button("r");
	Button b8=new Button("l");
	Button b9=new Button("A/M");
	Button b10=new Button("Š");
	Label l=new Label("Velocidad");
	Label l2=new Label("Posicion de la camara");
	Label l3=new Label("Estado del robot");
	Label l4=new Label("Estado de la camara");
	Label l5=new Label("");
	Label l6=new Label("");
	Label l7=new Label("");
	Label l8=new Label("");
	Label l9=new Label("");
	Label l51=new Label("");
	Label l61=new Label("");
	Label l71=new Label("");
	Label l81=new Label("");
	Label l91=new Label("");
	Label l52=new Label("");
	Label l62=new Label("");
	Label l72=new Label("");
	Label l82=new Label("");
	Label l92=new Label("");
	TextField t=new TextField(20);
	TextField t2=new TextField(20);
	TextField t3=new TextField(20);
	TextField t4=new TextField(20);
	
	String interfaceCOM = "";
	
	//Stan part
	Frame InterfazSelection_interfaz_com = new Frame();
	Label InterfazSelection_label_interfaz_com=new Label("Interface:");
	TextField InterfazSelection_interfaz_com_text=new TextField(40);
	Button InterfazSelection_Interfaz_com_but=new Button("OK");
	Panel InterfazSelection_interfaz_com_panel=new Panel(new GridLayout(3, 1));
	SerialComunication sc = null;
	
	InterfazGrafica(){
		
	//	sc=new SerialComunication(, baudRate, bits, stopBit, parity)
	//Frame
	f.setSize(800,400);
	f.setTitle("Robot vigilante");
	f.setVisible(true);
	f.setLocation(360,240);	
	
	InterfazSelection_interfaz_com.setSize(100,80);
	InterfazSelection_interfaz_com.setTitle("Robot vigilante");
	InterfazSelection_interfaz_com.setVisible(true);
	InterfazSelection_interfaz_com.setLocation(360,240);	
	//f.set;
	//Paneles
	//Panel de control del robot
	p5.add(l5);
	p5.add(l6);
	p5.add(l7);
	p5.add(l8);
	p5.add(l9);
	
	p5.add(b4);
	p5.add(b2);
	p5.add(b10);
	p5.add(b);
	p5.add(b3);
	
	p5.add(l51);
	p5.add(l61);
	p5.add(l71);
	p5.add(l81);
	p5.add(l91);
	//Panel de control de la camara
	p6.add(l52);
	p6.add(b5);
	p6.add(l62);
	
	p6.add(b8);
	p6.add(b9);
	p6.add(b7);
	
	p6.add(l72);
	p6.add(b6);
	p6.add(l82);
	
	//Panel de estado del robot
	p4.add(l);
	p4.add(t);
	
	p4.add(l2);
	p4.add(t2);
	
	p4.add(l3);
	p4.add(t3);
	
	p4.add(l4);
	p4.add(t4);
	//Panel general
	p3.add(p5);
	p3.add(p6);
	
	p2.add(p4);
	p2.add(p3);
	
	p.add(l92);
	p.add(p2);
	
	f.add(p);
	
	//Panel COM
	InterfazSelection_interfaz_com_panel.add(InterfazSelection_label_interfaz_com);
	InterfazSelection_interfaz_com_panel.add(InterfazSelection_interfaz_com_text);
	InterfazSelection_interfaz_com_panel.add(InterfazSelection_Interfaz_com_but);
	InterfazSelection_interfaz_com.add(InterfazSelection_interfaz_com_panel);
	InterfazSelection_interfaz_com.addWindowListener(new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			InterfazSelection_interfaz_com.dispose();
			
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
	InterfazSelection_Interfaz_com_but.addMouseListener(new ComInterfaceSelected());
	//Eventos
	f.addWindowListener(new VentanaEsquema());
	b5.addMouseListener(new CamaraArriba());
	b8.addMouseListener(new CamaraIzquierda());
	b7.addMouseListener(new CamaraDerecha());
	b6.addMouseListener(new CamaraAbajo());
	b9.addMouseListener(new CamaraAM());
	b4.addMouseListener(new RobotIzquierdaRapido());
	b2.addMouseListener(new RobotIzquierda());
	b10.addMouseListener(new RobotAM());
	b.addMouseListener(new RobotDerecha());
	b3.addMouseListener(new RobotDerechaRapido());
	p.addKeyListener(new Teclas());
	f.setFocusable(true);
	actualizarEstado();
	}
	public class Teclas extends KeyAdapter{
		public void keyReleased(KeyEvent e) { 
			switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				controlCamara(1);
				break;
			case KeyEvent.VK_DOWN:
				controlCamara(2);
				break;
			case KeyEvent.VK_RIGHT:
				controlCamara(3);
				break;
			case KeyEvent.VK_LEFT:
				controlCamara(4);
				break;
			case KeyEvent.VK_C:
				controlCamara(0);
				break;
			case KeyEvent.VK_D:
				controlRobot(1);
				break;
			case KeyEvent.VK_F:
				controlRobot(2);
				break;
			case KeyEvent.VK_S:
				controlRobot(3);
				break;
			case KeyEvent.VK_A:
				controlRobot(4);
				break;
			case KeyEvent.VK_R:
				controlRobot(0);
				break;
			case KeyEvent.VK_X:
				controlCamara(5);
				break;
			case KeyEvent.VK_Y:
				controlCamara(6);
				break;
			}
			//System.out.println("Key : " + e.getKeyCode());
			
			
		}
	}
	public class CamaraArriba extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
				controlCamara(1);
				}
	}
	public class CamaraAbajo extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
			controlCamara(2);
				}
			}
	public class CamaraDerecha extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
			controlCamara(3);
				}
	}
	public class CamaraIzquierda extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
			controlCamara(4);
				}
	}
	public class CamaraAM extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
				controlCamara(0);
				}
		public void mouseEntered (MouseEvent Eventoin){
			if(estadoC==manual){
			Eventoin.getComponent().setBackground(Color.green);}
			if (estadoC==auto){
				Eventoin.getComponent().setBackground(Color.red);}
			}
		public void mouseExited(MouseEvent Eventoin) {
			Eventoin.getComponent().setBackground(Color.white);	
		}
		}	
	public class RobotDerecha extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
			controlRobot(1);
				}
	}
	public class RobotDerechaRapido extends MouseAdapter{
		public void mouseClicked (MouseEvent Eventoin){
			controlRobot(2);
	
				}
			}
	public class RobotIzquierda extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
				controlRobot(3);
				}
	}
	public class RobotIzquierdaRapido extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
				controlRobot(4);
				}
	}
	public class RobotAM extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
			controlRobot(0);
		}
		public void mouseEntered (MouseEvent Eventoin){
			if(estadoR==manual){
				Eventoin.getComponent().setBackground(Color.green);}
			if (estadoR==auto){
				Eventoin.getComponent().setBackground(Color.red);}
		}
		public void mouseExited(MouseEvent Eventoin) {
			Eventoin.getComponent().setBackground(Color.white);	
		}
	}
	
	
	public class ComInterfaceSelected extends MouseAdapter{ 
		public void mouseClicked (MouseEvent Eventoin){
			interfaceCOM = InterfazSelection_interfaz_com_text.getText();
			InterfazSelection_interfaz_com.dispose();
			try {
				sc = new SerialComunication(interfaceCOM,
						SerialPort.BAUDRATE_9600,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				p.requestFocus();
				//key
				}
	}
	
	
	
	
	public void actualizarEstado(){
		t.setText(""+vel);
		t2.setText("X:"+x+" Y:"+y);
		if(estadoR==manual){
		t3.setText("Manual");}
		else t3.setText("Automatico");
		if(estadoC==manual){
			t4.setText("Manual");}
			else t4.setText("Automatico");
	}
	
	public void controlRobot(int i){
		try {
		switch(i){
		case 0:
			//estadoR=(estadoR+1)%2;
			//actualizarEstado();
			sc.sendMotorSpeed(sc.M_SIGNO_NEGATIVO_MOTOR, 0);
			break;
		case 1:
			//Hacer que avance el robot hacia la derecha
			
				
			
			if(estadoR==manual){
			velI-=1;
			velD+=1;
			vel=velD;
			if(velI >= 0){
				if(velI==0) sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velI);
				else sc.sendMotorSpeed(sc.M_SIGNO_NEGATIVO_MOTOR, velI);
			}else
				sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velD);
			}
			actualizarEstado();
			break;
		case 2:
			//Hacer que avance el robot hacia la derecha mas rapido
			if(estadoR==manual){
			velI-=2;
			velD+=2;
			vel=velD;
			if(velI >= 0){
				if(velI==0) sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velI);
				else sc.sendMotorSpeed(sc.M_SIGNO_NEGATIVO_MOTOR, velI);
			}else
				sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velD);
			}
			actualizarEstado();
			break;
		case 3:
			//Hacer que el robot avance hacia la izquierda
			if(estadoR==manual){
			velD-=1;
			velI+=1;
			vel=velI;
			if(velI >= 0){
				if(velI==0) sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velI);
				else sc.sendMotorSpeed(sc.M_SIGNO_NEGATIVO_MOTOR, velI);
			}else
				sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velD);
			}
			actualizarEstado();
			break;
		case 4:
			//Hacer que el robot avance hacia la izquierda mas rapido
			if(estadoR==manual){
			velD-=2;
			velI+=2;
			vel=velI;
			if(velI >= 0){
				if(velI==0) sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velI);
				else sc.sendMotorSpeed(sc.M_SIGNO_NEGATIVO_MOTOR, velI);
			}else
				sc.sendMotorSpeed(sc.M_SIGNO_POSITIVO_MOTOR, velD);
			}
			actualizarEstado();
			break;
		}
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void controlCamara(int i) {
		try{
		switch(i){
		case 0:
			//Cambiar el estado de la Camara
			estadoC=(estadoC+1)%2;
			actualizarEstado();			
			break;
		case 1:
			//Sumar un grado al Eje Y de la camara
			if(estadoC==manual){
			y=y+1;
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_POSITIVO_SERVO, (char) 1);
			//Thread.sleep(10);
			}
				//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		case 2:
			//Restar un grado al Eje Y de la camara
			if(estadoC==manual){
			y=y-1;
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_NEGATIVO_SERVO, (char) 1);
			}
			//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		case 3:
			//Sumar un grado al Eje X de la camara
			if(estadoC==manual){
			x=x+1;
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_X, SerialComunication.M_SIGNO_NEGATIVO_SERVO, (char) 1);
			}
			//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		case 4:
			//Restar un grado al Eje X de la camara
			if(estadoC==manual){
			x=x-1;
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_X, SerialComunication.M_SIGNO_POSITIVO_SERVO, (char) 1);
			}
			//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		case 5:
			//Restar un grado al Eje X de la camara
			if(estadoC==manual){
			x=0;
			sc.sendServoReset(SerialComunication.M_SERVO_X);
			}
			//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		case 6:
			//Restar un grado al Eje X de la camara
			if(estadoC==manual){
			y=0;
			sc.sendServoReset(SerialComunication.M_SERVO_Y);
			}
			//Mandar dicho grado al PIC
			actualizarEstado();
			break;
		}
		}catch(Exception e){
			e.setStackTrace(e.getStackTrace());
		}
	}
	public static void main(String[] args){
		InterfazGrafica g=new InterfazGrafica();
	}

}