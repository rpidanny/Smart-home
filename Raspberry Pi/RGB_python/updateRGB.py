#!/usr/bin/env python

#pretty obvious, but you'll want these dependencies to be installed for this script to work
import RPi.GPIO as GPIO, time, os

def cls():
    os.system(['clear','cls'][os.name == 'nt'])

DEBUG = 1

RED = 17
GREEN = 2
BLUE = 3

tempred=0
tempgreen=0
tempblue=0



while 1:
    global tempred
    global tempblue
    global tempgreen
    
    file = open('/usr/lib/cgi-bin/RBGvalues.txt', 'r')

    r = file.readline()
    g = file.readline()
    b = file.readline()
    r=r.rstrip('\n')
    g=g.rstrip('\n')
    b=b.rstrip('\n')
    if(tempred==r):os.system("""echo "23="""+r+"""" > /dev/pi-blaster""")
    if(tempgreen==g):os.system("""echo "24="""+g+"""" > /dev/pi-blaster""")
    if(tempblue==b):os.system("""echo "25="""+b+"""" > /dev/pi-blaster""")
    
    tempred=r
    tempblue=b
    tempgreen=g
    #os.system("""date""")
    #print("""echo "17="""+b+"""" > /dev/pi-blaster""")
    file.close()
    time.sleep(0.25) #uncomment this if you want the polling loop to be slower
