import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import LOG.RobotLogger;


public class SerialComunication {
	
		/*Mensajes de 7 bits, por errores de mensaje incromprensible con mas.
		 * 		[destino] [MENSAJE]
		 *bits:     1	     6
		 *-------------------------------PC >> PIC de Motor-----------------------------
		 *	Mensjae de peticion de estado de servomotores al PIC del Motor:
		 *      En decimal: -0
		 *      En binario: 100000
		 *
		 *	Mensaje de velocidad con PIC del Motor:
		 *      [signo] [valor velocidad]
		 *bits:    1         5 
		 *	signo: 
		 *		(+)	: 0
		 *		(-) : 0
		 *	velocidad : el valor binario de 5 vits.
		 *
		 *	** El mensaje correspondiente a -0, es el mensaje de peticion del valor de los servomotores.
		 *Cuando se envia el mensaje de peticion de estado de servomotres, se esperan 2 mensjaes, uno por servomotor.
		 *----------------------------PC >> PIC de Servomotres----------------------
		 *  Mensaje para mover un servomotor:
		 *  
		 *      [servomotor a mover] [signo] [valor de movimiento]
		 *bits:	         1              1             4
		 *   servomotor a mover:
		 *   	(servomotor x) : 0
		 *   	(servomotor y) : 1
		 *   signo:
		 *   	(+) : 0
		 *   	(-) : 1
		 *   valor de movimiento : se trata de un valor de 4 bits que hace correspondencia a una velocidad de giro del
		 *   servomotor en alguna dirección, donde hay una constante que multiplica al valor.
		 *   	0 -> RESET
		 *   	1 -> angulo(CONSTANTE) 
		 *   	2 -> angulo(2 * CONSTANTE) 
		 *   	3 -> angulo(3 * CONSTANTE)
		 *   	4 -> angulo(4 * CONSTANTE)
		 *   	5 -> angulo(5 * CONSTANTE)
		 *   	6 -> angulo(6 * CONSTANTE)
		 *   	7 -> angulo(7 * CONSTANTE)
		 *   	COSNTANTE : normalmente es 1.
		 *----------------------PIC MOTOR >> PC --------------------------------------------
		 *  Mensajae con el valor de los servomotores:
		 *  ******LOS MENSAJE MENSAJES HACIA EL PC, TIENEN COMO DIRECCIÓN (1)**********
		 *  	[sonar leido] [valor en centimetros]
		 * bits:      1                5
		 *   sonar leido:
		 *   	(delantero) : 0
		 *   	(trasero)   : 1
		 *   valor en centimetros : redondeado en 5 bits (se puede dividir por un multiplo de 2
		 *   
		 */	
	
		public static final char M_ID_DST_PICm= 0b00000000;
		public static final char M_ID_DST_PICs= 0b01000000;
		  public static final char M_ID_SRC_PC= 0b00000000;
		
		
		
		//Mensajes con PIC MOTOR
		public static final Byte M_SIGNO_POSITIVO_MOTOR = 0b00000000;
		public static final Byte M_SIGNO_NEGATIVO_MOTOR = 0b00100000;
		       public static final Byte M_SONAR_REQUEST = 0b00100000;
		
		//Mensajes con PIC SERVOMOTOR
		 public static final Byte M_SIGNO_POSITIVO_SERVO = 0b00000000;
		 public static final Byte M_SIGNO_NEGATIVO_SERVO = 0b00010000;
		              public static final Byte M_SERVO_X = 0b00000000;
		              public static final Byte M_SERVO_Y = 0b00100000;
		          public static final Byte M_SERVO_RESET = 0b00010000;
		      /*public static final Byte M_SERVO_SPEED_1 = 0b00000001;
		        public static final Byte M_SERVO_SPEED_2 = 0b00000010;
		        public static final Byte M_SERVO_SPEED_3 = 0b00000011;
		        public static final Byte M_SERVO_SPEED_4 = 0b00000100;
		        public static final Byte M_SERVO_SPEED_5 = 0b00000101;
		        public static final Byte M_SERVO_SPEED_6 = 0b00000110;
		        public static final Byte M_SERVO_SPEED_7 = 0b00000111;*/
		     public static final Byte M_SERVO_SPEED_MASK = 0b00001111;
		
		//Mensajes del PIC al PC
		              public static final Byte M_SONAR_L = 0b0000000;	 //Right : Derecha
		              public static final Byte M_SONAR_R = 0b0010000;	 //Left : Izquierda
		
		
		
		
		
		private String deviceName;
		private int baudRate, bits, patiy, stopBit;
		private static SerialPort serialPort;
		private static RobotSerialPortReader readListener; //= new SerialPortReader(serialPort);
		
		private static ArrayList<Byte> inputMesageBuffer;
		
		private final static Logger LOGGER = Logger.getLogger(RobotLogger.class.getName());
		
		public SerialComunication(String deviceName, int baudRate, int bits, int stopBit, int parity) throws SerialPortException{
			LOGGER.setLevel(Level.INFO);
			this.inputMesageBuffer = new ArrayList<Byte>();
			this.deviceName = deviceName;
			this.baudRate = baudRate;
			this.bits = bits;
			this.patiy = parity;
			this.stopBit = stopBit;
			this.serialPort = new SerialPort(deviceName); //DEFINIMOS EL PUERTO A USAR
			serialPort.openPort();	//ABRIMOS EL PUERTO
			serialPort.setParams(baudRate, bits, stopBit, parity); //PARAMETROS
			this.readListener = new RobotSerialPortReader(this); //INICIAMOS LA CLASE QUE REACCIONARA ANTE EVENTOS DEL SERIAL
			int mask = SerialPort.MASK_RXCHAR; // + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(readListener);//Add SerialPortEventListener
            LOGGER.info("[STARTED SERIAL COMUNICATION] With " + deviceName + " baud rate " +  baudRate + " message bits " +  bits + " stop bits " +  stopBit + " parity " +  parity);
		}

		public void sendServoReset(byte servomotor) throws SerialPortException {
			byte messege = (byte) (M_ID_DST_PICs | servomotor | M_SERVO_RESET);
			serialPort.writeByte(messege);
			LOGGER.info("[SERVO RESET]" + byteToString(messege));
		}

		public void sendServoAngleIncrement(byte servomotor, byte direccion,
				char velocidad) throws SerialPortException {
			byte messege = (byte) (M_ID_DST_PICs | servomotor | direccion | (velocidad & M_SERVO_SPEED_MASK));
			serialPort.writeByte(messege);
			LOGGER.info("[SERVO ANGLE INCREMENT]" + byteToString(messege));
		}

		public void sendSonarRequest() throws SerialPortException {
			byte messege =(byte) (M_ID_DST_PICm | M_SONAR_REQUEST);
			serialPort.writeByte(messege);
			LOGGER.info("[SONAR REQUEST]" + byteToString(messege));
		}

		public boolean sendMotorSpeed(byte direccion, int velocidad) throws SerialPortException {
			if(velocidad < 256){ 
				byte messege = (byte) (M_ID_DST_PICm | direccion | ((byte) (velocidad) & 0xFF));
				serialPort.writeByte(messege);
				LOGGER.info("[SEND MOTOR SPEED]" + byteToString(messege));
			}
			else{
				LOGGER.severe("[ERROR] wrong speed value " + velocidad);
				return false;
			}
			return true;
		}
		
		private static String byteToString(byte value){
			return String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
		}
		
		private synchronized void addMessageToBuffer(byte message){
			this.inputMesageBuffer.add(message);
		}
		
		private synchronized void removeMessegeFromBuffer(int index){
			this.inputMesageBuffer.remove(index);
		}
		
		public boolean hasNext(byte servo){
			if(inputMesageBuffer.size() > 0){
				for(Byte b : inputMesageBuffer)
					if((b & this.M_ID_DST_PICs) != this.M_ID_DST_PICs){
						if((b & this.M_SONAR_R) == this.M_SONAR_R)
							return (M_SONAR_R.compareTo(servo) == 0);
						else
							return (M_SONAR_L.compareTo(servo) == 0);
					}
					else{
						LOGGER.severe("[MESSEGE UNKNOWN] Deletting message... : " + byteToString(b));
						//inputMesageBuffer.remove(i);
					}
			}else{
				LOGGER.info("[NO MORE MESSAGES IN THE BUFFER]");
			}
					
			LOGGER.info("[NO MORE MESSEGES FOR THIS SERVO] " + byteToString(servo));	
			return false;
		}
		
		public int getNextServoValue(byte servo){
			if(hasNext(servo)){
				for(int i=0; i < inputMesageBuffer.size(); i++)
					if((inputMesageBuffer.get(i) & this.M_ID_DST_PICs) != this.M_ID_DST_PICs){
						if((inputMesageBuffer.get(i) & this.M_SONAR_R) == this.M_SONAR_R){
							if(M_SONAR_R.compareTo(servo) == 0){
								int result = inputMesageBuffer.get(i) & 0b00011111;
								removeMessegeFromBuffer(i);
								return result*8; //Desplazo 3 bits a la izquierda.
							}
						}							
						else{
							if(M_SONAR_L.compareTo(servo) == 0){
								int result = inputMesageBuffer.get(i) & 0b00011111;
								//inputMesageBuffer.remove(i);
								removeMessegeFromBuffer(i);
								return result*8; //Desplazo 3 bits a la izquierda.
							}
						}
					}
					else{
						LOGGER.severe("[MESSEGE UNKNOWN] Deletting message... : " + byteToString(inputMesageBuffer.get(i)));
						//inputMesageBuffer.remove(i);
					}
			}else{
				LOGGER.severe("[NO MESSEGES] Don't have more messages for " + byteToString(servo));
				return -1;
			}
					
			LOGGER.severe("[ERROR MESSEGE NOT FOUND] There must to be a meassage for " + byteToString(servo));
			return -2;
		}
		
		public static void main(String[] args) throws SerialPortException, InterruptedException {
			//Testing for the SerialComunication.ystem.out.println(String.format("%8s", Integer.toBinaryString(a[i] & 0xFF)).replace(' ', '0'));
			
			SerialComunication sc = new SerialComunication("/dev/ttyACM0",SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			
			//Mensajes hacia el PIC del Motor
			//lista de mensajes.
			Thread.sleep(1000);
			for(int i =0; i<5;i++){
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_POSITIVO_SERVO, (char)5);
			Thread.sleep(1000);
			sc.sendServoAngleIncrement(SerialComunication.M_SERVO_X, SerialComunication.M_SIGNO_POSITIVO_SERVO, (char)5);
			//Thread.sleep(1000);
			//System.out.println("Recibido");
			Thread.sleep(1000);
			}
			for(int i =0; i<5;i++){
				sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_NEGATIVO_SERVO, (char)5);
				Thread.sleep(1000);
				sc.sendServoAngleIncrement(SerialComunication.M_SERVO_X, SerialComunication.M_SIGNO_NEGATIVO_SERVO, (char)5);
				//Thread.sleep(1000);
				//System.out.println("Recibido");
				Thread.sleep(2000);
				}
			
			//sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_POSITIVO_SERVO, SerialComunication.M_SERVO_SPEED_7);
			//Thread.sleep(10000);
			//sc.sendServoAngleIncrement(SerialComunication.M_SERVO_X, SerialComunication.M_SIGNO_NEGATIVO_SERVO, SerialComunication.M_SERVO_SPEED_7);
			//Thread.sleep(10000);
			//sc.sendServoAngleIncrement(SerialComunication.M_SERVO_Y, SerialComunication.M_SIGNO_NEGATIVO_SERVO, SerialComunication.M_SERVO_SPEED_7);
			//Thread.sleep(10000);
			System.exit(0);
		}
		
		
		
		class RobotSerialPortReader implements SerialPortEventListener {
			SerialComunication sc;
			
			public RobotSerialPortReader(SerialComunication sc){
				this.sc = sc;
			}
			
	        public synchronized void serialEvent(SerialPortEvent event) {
	            if (event.isRXCHAR()) {//If data is available
	                if (event.getEventValue() > 0) {//Check bytes count in the input buffer
	                    //Read data, if 1 bytes available
	                    try {
	                        sc.addMessageToBuffer(serialPort.readBytes(1)[0]);
	                        LOGGER.info("[NEW MESSEGE] " + byteToString(inputMesageBuffer.get(inputMesageBuffer.size() - 1)));
	                    } catch (SerialPortException ex) {
	                        LOGGER.severe("[SERIAL PORT EXCEPTION] ERROR: " + ex.toString());
	                    }
	                }
	            }
	        }
	    }
}
