from xml.dom import minidom
import time
from datetime import date
import webiopi

GPIO = webiopi.GPIO

LIGHT = 17 # GPIO pin using BCM numbering
FAN = 27
TV = 22
WashingMachine = 10
Microwave = 9
DishWasher = 11


day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
loadsheddingtimea=0
loadsheddingtimeaON=0
loadsheddingtimeb=0
loadsheddingtimebON=0

def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(LIGHT, GPIO.OUT)
    GPIO.setFunction(TV, GPIO.OUT)
    GPIO.setFunction(FAN, GPIO.OUT)
    GPIO.setFunction(WashingMachine, GPIO.OUT)
    GPIO.setFunction(Microwave, GPIO.OUT)
    GPIO.setFunction(DishWasher,GPIO.OUT)
    
    global loadsheddingtimea,loadsheddingtimeaON,loadsheddingtimeb,loadsheddingtimebON
    # retrieve current datetime
    #now = datetime.datetime.now()

    # test if we are between ON time and tun the light ON
    d = date.today()    
    f = open("preference",'r')
    data=f.readline().rstrip('\n')
    #print data
    f.close()
    xmldoc = minidom.parse(r"test.xml")
    #print xmldoc
    group = xmldoc.getElementsByTagName(data)
    #print group
    daycount=0


    for s in group :
     if(day[daycount]==d.strftime("%A")):
	        temp=s.attributes['out1'].value.split('-')
	        loadsheddingtimea=temp[0]
	        loadsheddingtimeaON=temp[1]
	        temp=s.attributes['out2'].value.split('-')
	        loadsheddingtimeb=temp[0]
	        loadsheddingtimebON=temp[1]
     daycount=daycount+1
	
    if(loadsheddingtimeaON >= time.strftime("%H:%M:%S") or loadsheddingtimebON >= time.strftime("%H:%M:%S")):
     GPIO.digitalWrite(LIGHT, GPIO.HIGH)
         
	
	
        
        
def loop():
	if(loadsheddingtimea == time.strftime("%H:%M:%S") or loadsheddingtimeb == time.strftime("%H:%M:%S")):
		if (GPIO.digitalRead(LIGHT) == GPIO.HIGH):
			GPIO.digitalWrite(LIGHT, GPIO.LOW)
			time.sleep(2)

	if(loadsheddingtimeaON == time.strftime("%H:%M:%S") or loadsheddingtimebON == time.strftime("%H:%M:%S")):
		if (GPIO.digitalRead(LIGHT) == GPIO.LOW):
			GPIO.digitalWrite(LIGHT, GPIO.HIGH)
			time.sleep(2)
	webiopi.sleep(1)
		
def destroy():
    GPIO.digitalWrite(LIGHT, GPIO.LOW)
    GPIO.digitalWrite(FAN, GPIO.LOW)
    GPIO.digitalWrite(TV, GPIO.LOW)
