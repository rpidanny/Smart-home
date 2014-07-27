import webiopi
import datetime

GPIO = webiopi.GPIO


HOUR_ON  = 8  # Turn Light ON at 08:00
HOUR_OFF = 18 # Turn Light OFF at 18:00

# setup function is automatically called at WebIOPi startup
def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(17, GPIO.OUT)
    GPIO.setFunction(27, GPIO.OUT)
    GPIO.setFunction(22, GPIO.OUT)
    GPIO.setFunction(10, GPIO.OUT)
    GPIO.setFunction(9, GPIO.OUT)
    GPIO.setFunction(11,GPIO.OUT)
    GPIO.setFunction(14, GPIO.OUT)
    GPIO.setFunction(15, GPIO.OUT)
    GPIO.setFunction(18,GPIO.OUT)
    GPIO.setFunction(8, GPIO.OUT)
    GPIO.setFunction(7,GPIO.OUT)
    # retrieve current datetime
    now = datetime.datetime.now()

    # test if we are between ON time and tun the light ON
    if ((now.hour >= HOUR_ON) and (now.hour < HOUR_OFF)):
        GPIO.digitalWrite(10, GPIO.HIGH)

# loop function is repeatedly called by WebIOPi 
def loop():

    # retrieve current datetime

    now = datetime.datetime.now()

    # toggle light ON all days at the correct time
    if ((now.hour == HOUR_ON) and (now.minute == 0) and (now.second == 0)):
        if (GPIO.digitalRead(10) == GPIO.LOW):
            GPIO.digitalWrite(10, GPIO.HIGH)

    # toggle light OFF
    if ((now.hour == HOUR_OFF) and (now.minute == 0) and (now.second == 0)):
        if (GPIO.digitalRead(10) == GPIO.HIGH):
            GPIO.digitalWrite(10, GPIO.LOW)

    # gives CPU some time before looping again
    webiopi.sleep(1)

# destroy function is called at WebIOPi shutdown
def destroy():
    GPIO.digitalWrite(17, GPIO.LOW)
    GPIO.digitalWrite(27, GPIO.LOW)
    GPIO.digitalWrite(22, GPIO.LOW)
    GPIO.digitalWrite(10, GPIO.LOW)
    GPIO.digitalWrite(9, GPIO.LOW)
    GPIO.digitalWrite(11, GPIO.LOW)
    GPIO.digitalWrite(14, GPIO.LOW)
    GPIO.digitalWrite(15, GPIO.LOW)
    GPIO.digitalWrite(18, GPIO.LOW)
    GPIO.digitalWrite(8, GPIO.LOW)
    GPIO.digitalWrite(7, GPIO.LOW)
