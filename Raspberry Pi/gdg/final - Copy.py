#!C:\Python27\python.exe

from xml.dom import minidom
import time
from datetime import date
import sys
import socket


#d = date.today()

#f = open("preference",'r')
#data=f.readline()
#xmldoc = minidom.parse('test.xml')
#group = xmldoc.getElementsByTagName(data)
#group2 = xmldoc.getElementsByTagName('
day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']

TCP_IP = '192.168.73.1'
TCP_PORT = 5017
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
    #delay=int(f.readline())
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
	#h1,m1,s1=loadsheddinga.split(':')
	#h1=int(h1)-1
	#m1=int(m1)-int(delay)
	
	#h2,m2,s2=loadsheddingb.split(':')
	#h2=int(h2)-1
	#m2=int(m2)-int(delay)
	
	
    if(loadsheddingtimea == time.strftime("%H:%M:%S") or loadsheddingtimeb == time.strftime("%H:%M:%S")):
        print "Turn off everythng"        
        conn.send("S")
        #print conn.recv(1)
        if(conn.recv(1)=='Y'):
            print "LIghts On"
        if(conn.recv(1)=='N'):
            print "LIghts Off"
        time.sleep(2)
        #break
