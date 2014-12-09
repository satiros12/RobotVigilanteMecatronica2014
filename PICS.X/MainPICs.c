/*
 * File:   main.c
 * Author: stan
 *
 * Created on 21 de noviembre de 2014, 0:33
 */


#include <xc.h>
#include "usart_pic16.h"
#include "SoftwareServo.h"

#pragma config CPD = OFF, BOREN = OFF, IESO = OFF, DEBUG = OFF, FOSC = HS
#pragma config FCMEN = OFF, MCLRE = ON, WDTE = OFF, CP = OFF, LVP = OFF
#pragma config PWRTE = ON, BOR4V = BOR21V, WRT = OFF

#pragma intrinsic(_delay)
#ifndef _XTAL_FREQ
#define _XTAL_FREQ 20000000 // necessary for __delay_us
#endif
          #define ID_PICS 0b01000000
            #define ID_PC 0b00000000
          #define ID_PICM 0b00000000

        #define M_SERVO_X 0b00000000
        #define M_SERVO_Y 0b00100000
  #define M_SERVO_SIGNO_P 0b00000000
  #define M_SERVO_SIGNO_N 0b00010000
    #define M_SERVO_RESET 0b00010000
 #define M_SERVO_VAL_MASK 0b00001111

#define UART_SPEED 9600


char mensaje = 0;
char puerto_b = 0b00000000;
char servo_angle_x, servo_angle_y;
unsigned long int count_timer = 0;
void main(void);
void interrupt interrumptions(void);
void initialize(void);
void interpreteDeMensajes();

/*
 *
 */
void main() {
    initialize();
    while (1) {

        PORTB = puerto_b |= 0b00000001;
        //Servo_setAngle((char) 1,(char) servo_angle_y=(servo_angle_y+30));
        while (count_timer < (unsigned long int) 50) {
            ;
        }
        count_timer = 0;
        if (mensaje == 0) PORTB = puerto_b &= 0b11111101;
        PORTB = puerto_b &= 0b11111110;
        //  Servo_setAngle((char) 1,(char) servo_angle_y=(servo_angle_y-30));
        while (count_timer < (unsigned long int) 50) {
            ;
        }
        count_timer = 0;
        if (mensaje > 0) {
            PORTB = puerto_b |= 0b00000010;
            USARTWriteChar(mensaje);
            mensaje = (char) 0;
        }
        Servo_setAngle((char) 0, (char) servo_angle_x);
        Servo_setAngle((char) 1, (char) servo_angle_y);
        // if(servo_angle_x+5 < 180) Servo_setAngle((char) 0,(char) servo_angle_x=(servo_angle_x+5));
        // else  Servo_setAngle((char) 0,(char) servo_angle_x=0);

        // else  Servo_setAngle((char) 1,(char) servo_angle_y=0);
    }
}

void initialize(void) {
    OSCCON = 0b00001000;

    /*  TRIS
     *  1: Input        0 : Output
     *
     * PORT
     *  1: Vdd output   0: GND Output
     */
    //TRISB0=0b00000000;
    PORTB = 0b00000000;
    TRISA = 0b00000000;
    PORTA = 0b00000000;
    TRISB = 0b00000000;
    PORTB = puerto_b;

    //TMR0 = 256 - 55;
    TMR1 = (unsigned short) 0; // 65536 - 625; // 625;
    T1CON = 0b00110001;
    //PIE1bits.
    //T0CS = (char) 0;
    // OPTION_REGbits.PSA = 0; // assign prescaler to Timer0
    // OPTION_REGbits.PS = 0b000;



    //Initial angle 0º == 128
    servo_angle_x = (char) 90;
    servo_angle_y = (char) 90;

    //Initialize UART
    USARTInit(UART_SPEED);

    //Initialize Servo
    Servo_setupSoftware();
    Servo_setAngle((char) 0, (char) servo_angle_x);
    Servo_setAngle((char) 1, (char) servo_angle_y);
    TMR1IE = 1;

    PEIE = 1;
    ei();
}

void interrupt interrumptions() {
    //USARTHandleRxInt();

    if (TMR1IF == 1) {
        //TMR0 = 256 - 55 + 10;
        //PORTA=0x03;
        TMR1 = (unsigned short) 65536 - 12550;
        //if(SERVO_us5_counter >= 1800) SERVO_us5_counter=0;
        //SERVO_us5_counter++;
        count_timer++;
        Servo_nextStep();
        TMR1IF = 0;
        //T0IF=0;
    }
    //    if (T0IF == 1) {



    //       T0IF = 0;
    //   }

    if (RCIF == 1) {

        mensaje = RCREG;
        if (mensaje != 0) {
           // char tmp = ;
          //  unsigned boolean = ;
            if ((mensaje & ID_PICS) == ID_PICS) {
              //  boolean=;

                if ((mensaje & M_SERVO_Y) == M_SERVO_Y) {
                  //  boolean=;
                    if(mensaje == (ID_PICS | M_SERVO_Y | M_SERVO_SIGNO_N)) servo_angle_y=90;
                    else if ((mensaje & M_SERVO_SIGNO_N) == M_SERVO_SIGNO_N) {
                        if (servo_angle_y - (char) (mensaje & M_SERVO_VAL_MASK) > 0) servo_angle_y = servo_angle_y - (char) (mensaje & M_SERVO_VAL_MASK);
                    } else {
                        if (servo_angle_y + (char) (mensaje & M_SERVO_VAL_MASK) < SERVO_MAX_ANGLE_VAL) servo_angle_y = servo_angle_y + (char) (mensaje & M_SERVO_VAL_MASK);
                    }
                } else {
                    //boolean=;<
                    if(mensaje == (ID_PICS | M_SERVO_X | M_SERVO_SIGNO_N)) servo_angle_x=90;
                    else if ((mensaje & M_SERVO_SIGNO_N) == M_SERVO_SIGNO_N) {
                        if (servo_angle_x - (mensaje & M_SERVO_VAL_MASK) > 0) servo_angle_x = servo_angle_x - (char) (mensaje & M_SERVO_VAL_MASK);
                    } else {
                        if (servo_angle_x + (mensaje & M_SERVO_VAL_MASK) < SERVO_MAX_ANGLE_VAL) servo_angle_x = servo_angle_x + (char) (mensaje & M_SERVO_VAL_MASK);
                    }
                }
                //  if(servo_angle_y + val - (val * 2 * signo) < SERVO_MAX_ANGLE_VAL) servo_angle_y += val - (val * 2 * signo);

            }
        }
        RCIF = 0;
    }
}

void interpreteDeMensajes() {



}