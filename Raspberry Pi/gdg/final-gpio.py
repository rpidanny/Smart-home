#!C:\Python27\python.exe

from xml.dom import minidom
import time
from datetime import date
import sys
import socket
import wiringpi
io = wiringpi.GPIO(wiringpi.GPIO.WPI_MODE_PINS)
io.pinMode(7,io.OUTPUT)
io.digitalWrite(7,io.LOW)
#d = date.today()

#f = open("preference",'r')
#data=f.readline()
#xmldoc = minidom.parse('test.xml')
#group = xmldoc.getElementsByTagName(data)
#group2 = xmldoc.getElementsByTagName('
day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']

TCP_IP = '192.168.73.1'
TCP_PORT = 5015
BUFFER_SIZE = 1

print "Waiting For Client..."
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)
conn, addr = s.accept()

print 'Connection address:', addr

while True:
    #print "count"
    d = date.today()    
    f = open("preference",'r')
    data=f.readline()
    xmldoc = minidom.parse('test.xml')
    group = xmldoc.getElementsByTagName(data)
    #group2 = xmldoc.getElementsByTagName('
    #day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
    daycount=0
    loadsheddingtimea=0
    loadsheddingtimeb=0

    for s in group :
        if(day[daycount]==d.strftime("%A")):
            #print day[daycount]
            #print d.strftime("%A")
            temp=s.attributes['out1'].value.split('-')
            loadsheddingtimea=temp[0]
            temp=s.attributes['out2'].value.split('-')
            loadsheddingtimeb=temp[0]
        daycount=daycount+1
    #print loadsheddingtimea
    #print loadsheddingtimeb


    #print time.strftime("%H:%M:%S")
   # while True:
    if(loadsheddingtimea == time.strftime("%H:%M:%S") or loadsheddingtimeb == time.strftime("%H:%M:%S")):
        print "Turn off everythng"        
        conn.send("S")
        print conn.recv(1)
		if(conn.recv(1)=="Y"):
			io.digitalWrite(7,io.HIGH)
		elif(conn.recv(1)=="N"):
			io.digitalWrite(7,io.LOW)
        time.sleep(2)
        #break
