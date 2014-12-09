#include "SoftwareServo.h"
/*
 *  Inicializao las variables globales corespondientes a este modulo.
 */
void Servo_setupSoftware(){
    int i = 0;
    PORT_SERVOS=(char) 0;
    PORTA = (char) 0;
    //SERVO_counterGloblal=0;
   // SERVO_counterPrecision = 0;
    for(i=0; i < SERVO_MAX_SERVOS; i++)
        SERVO_pinPosition[i]=0;
}

/*
 Esta función tiene que ser llamada en el loop principal.
 *  -- Falla en el sentido de un sleep
 */
    void Servo_nextStep(){
       // TMR0IE = (char) 1;
       // TMR0 = 256 -
        
    char current_angle= (char) 0;
   // PORTA= 0b00000000;
  //  __delay_us(1000);
    PORTA = PORT_SERVOS = (char) 0xFF; //0b00000011; ///0b00000011;
    __delay_us(1000);
    
    for(current_angle = 0;  current_angle < 180; current_angle ++){
        if(SERVO_pinPosition[0] == current_angle)
            PORTA= PORT_SERVOS  &= ((char)1 ^ (char) 0xFF);
        if(SERVO_pinPosition[1] == current_angle)
            PORTA = PORT_SERVOS  &= ((char)2 ^ (char) 0xFF);
        //else
        __delay_us(5);
    }
    PORTA =  PORT_SERVOS =  (char) 0x00; //0b00000000;
   // __delay_us(17000);

        /*
        PORTA = PORT_SERVOS=0xFF;
        __delay_us(1000);
        for(unsigned current_angle = 0;  current_angle < 180; current_angle ++){
           // for(char i = 1, j=1; i < SERVO_MAX_SERVOS; i++, j*=2)
          //       if(SERVO_pinPosition[i] == current_angle) {PORT_SERVOS  |=  j;}
         //        else PORT_SERVOS &= (j ^ 0xFF);
            
           // PORTA = PORT_SERVOS;
            __delay_us(5);
          }
           PORTA = (char) 0;
         */

/*
        if(SERVO_us5_counter > 90  && SERVO_us5_counter <= 270){
          //for(char i = 0; i < SERVO_MAX_SERVOS; i++)
              if(SERVO_pinPosition[0] == SERVO_us5_counter - 90) PORT_SERVOS  &= (1 ^ 0xFF);
              if(SERVO_pinPosition[1] == SERVO_us5_counter - 90) PORT_SERVOS  &= (2 ^ 0xFF);
          PORTA = PORT_SERVOS;
        }else if(SERVO_us5_counter == 90) PORT_SERVOS=0xFF;
        else{
          PORTA = (char) 0;
        }*/
/*
        if(SERVO_counterGloblal == 0){
            
         SERVO_counterPrecision=0;
         PORTA= (char) 0;
         if(SERVO_us5_counter / 200 >= SERVO_counterGloblal+1 ) SERVO_counterGloblal++;

        }
        else if(SERVO_counterGloblal == 1 && SERVO_counterPrecision < SERVO_MAX_ANGLE_VAL){

            for(char i = 0; i < SERVO_MAX_SERVOS; i++)
                if(SERVO_pinPosition[i] == SERVO_counterPrecision) {PORT_SERVOS  |=  i;}
                else PORT_SERVOS &= (i ^ 0xFF);
            PORTA = PORT_SERVOS;
            SERVO_counterPrecision++;

            if(SERVO_counterPrecision == SERVO_MAX_ANGLE_VAL/2) SERVO_counterGloblal++;

        }else if(SERVO_counterGloblal < 20){

             PORTA= (char) 0;
             SERVO_counterPrecision=0;
            if(SERVO_us5_counter / 200 >= SERVO_counterGloblal+1 ) SERVO_counterGloblal++;
      
        }else{

            PORTA= (char) 0;
            SERVO_us5_counter=0;
            SERVO_counterPrecision=0;
            SERVO_counterGloblal=0;

        }
*/
    }

    int Servo_setAngle(char servo, char angle){
        if(angle < SERVO_MAX_ANGLE_VAL && servo < SERVO_MAX_SERVOS){
            SERVO_pinPosition[servo] = angle;
            return 1;
        }
        return 0;
    }
    void Servo_resetAngle(char servo){
        SERVO_pinPosition[servo] = SERVO_MAX_ANGLE_VAL+1;   //Invierto servo y lo quito con un andf del array.
    }