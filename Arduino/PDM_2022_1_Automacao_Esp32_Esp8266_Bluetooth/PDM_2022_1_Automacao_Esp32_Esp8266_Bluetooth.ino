/*
Este código de exemplo está no domínio público (ou licenciado CC0, a seu critério.)
By Evandro Copercini - 2018

Este exemplo cria uma ponte entre Bluetooth serial e clássico (SPP)
e também demonstra que o SerialBT tem as mesmas funcionalidades de um Serial normal

Código adpatado por Wanderley Patrício de Sousa Neto em 14/04/2022
*/

#include <BluetoothSerial.h>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error O Bluetooth não está ativado! Por favor,vá ao menuconfig do seu celular e ative-o
#endif

BluetoothSerial SerialBT;
  
int entrada;
byte receivedData;
byte receivedDataBT;

void piscaLed(int pino, int qntd){
for (int i=0;i<qntd;i++){
  digitalWrite(pino, HIGH);
  delay(500); 
  digitalWrite(pino, LOW); 
  delay(500); 
}
}
 
void setup() {

  Serial.begin(115200);
  SerialBT.begin("ESP32_WPSN_v1"); //Nome do Bluetooth
  Serial.println("O dispositivo Bluetooth está pronto para emparelhar"); 

  pinMode(2,OUTPUT);//LED

}

void loop() {
int num = 0; 
//=============================================================
if (Serial.available()){ //usado para monitor serial comum
  entrada = Serial.read();
  Serial.print("Received: ");
  Serial.println(entrada);
    if (entrada == 48) { //zero
      Serial.println("entrei no if");
      piscaLed(2, 6);
  
      SerialBT.print("zero");
    }
    fflush;
    delay(20);
}
//=============================================================
 if (SerialBT.available()) {//usado para monitor serial bluetooth
  entrada = SerialBT.read();
  SerialBT.print("Received: ");
  SerialBT.println(entrada);

  if (entrada == 48) { //zero
        piscaLed(2, 10);
    }

  if (entrada == 49) { //um
        piscaLed(2, 1);
    }
  if (entrada == 50) { //dois
        piscaLed(2, 2);
    }
  if (entrada == 51) { //três
        piscaLed(2, 3);
    }
  if (entrada == 52) { //quatro
        piscaLed(2, 4); 
    }
  if (entrada == 53) { //cinco
        piscaLed(2, 5);
    }
  if (entrada == 54) { //seis
        piscaLed(2, 6); 
    }
  
 fflush;
 delay(20);
  
  }
 //=============================================================
} 
