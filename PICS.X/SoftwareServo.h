/* 
 * File:   SoftwareServo.h
 * Author: stan
 *
 * Created on 7 de diciembre de 2014, 16:58
 */

#ifndef SOFTWARESERVO_H
#define	SOFTWARESERVO_H

#ifdef	__cplusplus
extern "C" {
#endif
    #include <xc.h>
    #ifndef _XTAL_FREQ
    #define _XTAL_FREQ 20000000 // necessary for __delay_us
    #endif

    #define SERVO_MAX_ANGLE_VAL 128
/*
    #define SERVO_0 0b00000001
    #define SERVO_1 0b00000010
    #define SERVO_2 0b00000100
    #define SERVO_3 0b00001000
    #define SERVO_4 0b00010000
    #define SERVO_5 0b00100000
    #define SERVO_6 0b01000000
    #define SERVO_7 0b10000000
*/
#define SERVO_MAX_SERVOS 2
    char SERVO_pinPosition[SERVO_MAX_SERVOS];

    char SERVO_counterGloblal, SERVO_counterPrecision;

    unsigned SERVO_us5_counter;
    char PORT_SERVOS;

    void Servo_nextStep();
    void Servo_setupSoftware();
    int Servo_setAngle(char servo, char angle);
    void Servo_resetAngle(char servo);

#ifdef	__cplusplus
}
#endif

#endif	/* SOFTWARESERVO_H */

