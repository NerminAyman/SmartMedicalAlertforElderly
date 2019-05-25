#include <SD.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <TimeLib.h>
#define TIME_HEADER  "T"   // Header tag for serial time sync message
#define TIME_REQUEST  7    // ASCII bell character requests a time sync message

Adafruit_MLX90614 mlx = Adafruit_MLX90614();
File myFile;
int pinCS = 53; 
double alpha=0.75;
int period=20;
double refresh=0.0;  
const int buttonPin = 7;
bool flag = false;
int buttonState = 0;
int i = 0;

void setup() {
  Serial.begin(9600);
  setSyncProvider(requestSync);
  pinMode(A0,INPUT);
  mlx.begin();
  pinMode(pinCS, OUTPUT);
  pinMode(buttonPin, INPUT);
  
  // SD Card Initialization
  if (SD.begin())
  {
    //Serial.println("SD card is ready to use.");
  } else
  {
   // Serial.println("SD card initialization failed");
    return;
  }
  myFile = SD.open("vitals.csv", FILE_WRITE);

  // if the file opened okay, write to it:
  if (myFile) {
    String initials = "Heart beat rate , Tempretaure , Time, Date";
    myFile.println(initials);
    // close the file
    myFile.close(); 
  }
}

void loop() {
  String reading = "";
  
  //Pulse Sensor Reading
  static double oldValue=0;
  static double oldrefresh=0; 
  int beat=analogRead(A0);  //define sensor pin to AO
  double value=alpha*oldValue+(0-alpha)*beat;
  refresh=value-oldValue;     
  int pulseValue = beat/10;
  reading = String(pulseValue);
  reading += ",";
 // Serial.print(" Heart Monitor = "); // text to be displayed on the LCD when boot up
  //Serial.print(pulseValue);
  oldValue=value;
  oldrefresh=refresh;

 // Serial.print(",");
  
  //Temperature Sensor Reading
  float tempValue = mlx.readObjectTempC();
  reading += String(tempValue);
  reading += "*C";
  //Serial.print("Temperature = ");
  //Serial.println(tempValue);
  reading += ",";

  //TimeStamp display
  if(Serial.available()){
    processSyncMessage();
  }
  
  if (timeStatus()!= timeNotSet) {
    reading += digitalClockDisplay(); 
  }
  
  // Create/Open file
  myFile = SD.open("vitals.csv", FILE_WRITE);
  // if the file opened okay, write to it:
  if (myFile) {
    // Write to file
    myFile.println(reading);
    // close the file
    myFile.close(); 
    //Serial.println("Done.");
  }
  // if the file didn't open, print an error:
  else {
    //Serial.println("error opening vitals.txt");
  }

  
  // read the state of the pushbutton value:
  buttonState = digitalRead(buttonPin);
   // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
  if (buttonState == HIGH) {
    flag = true;
  } else {
  }
  
  if(flag == true){
     Serial.println("0,0.0");
     i++;
  }else{
    Serial.print(pulseValue);
    Serial.print(",");
    Serial.println(tempValue);
  }
  if(i == 3){
    flag =false;
    i = 0;
  }
  delay(10000);
}


void processSyncMessage() {
  unsigned long pctime;
  const unsigned long DEFAULT_TIME = 1554864900; // Jan 1 2013

  if(Serial.find(TIME_HEADER)) {
     pctime = Serial.parseInt();
     if( pctime >= DEFAULT_TIME) { // check the integer is a valid time (greater than Jan 1 2013)
       setTime(pctime); // Sync Arduino clock to the time received on the serial port
     }
  }
}

time_t requestSync()
{
  Serial.write(TIME_REQUEST);  
  return 0; // the time will be sent later in response to serial mesg
}

String digitalClockDisplay(){
  // digital clock display of the time
  String timeStamp = "";
  timeStamp += hour();
  timeStamp += ":";
  if(minute() < 10)
    timeStamp += "0";
  timeStamp += minute();
  timeStamp += ":";
  if(minute() < 10)
    timeStamp += "0";
  timeStamp += second();
  timeStamp += " ,";
  timeStamp += "";
  timeStamp += day();
  timeStamp += "/";
  timeStamp += month();
  timeStamp += "/";
  timeStamp += year();
  
  return timeStamp;
}
