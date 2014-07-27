#!C:\Python27\python.exe

from xml.dom import minidom
import time
from datetime import date
import sys
import socket
import select
import threading

#d = date.today()

#f = open("preference",'r')
#data=f.readline()
#xmldoc = minidom.parse('test.xml')
#group = xmldoc.getElementsByTagName(data)
#group2 = xmldoc.getElementsByTagName('
day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']

class Server(threading.Thread): 

    def __init__(self, host, port ):
        threading.Thread.__init__(self)
        self.host = host
        self.port = port
        self.socket = None
        self.threadlist = []
        
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.socket.bind((self.host, self.port))
            self.socket.listen(5)
            #server information
            self.host = self.socket.getsockname()[0]
            self.port = self.socket.getsockname()[1]
            print 'listening for connections at:', str(self.socket.getsockname())

        except socket.error as error:
            print error
            sys.exit(1)
#             return

    def run(self):
        inputs = [self.socket, sys.stdin]
        run = True
        while run:
            inputready, outputready, exceptready = select.select(inputs, [], [])
            for s in inputready:
                if s == self.socket:
                    handle = ClientThread(self.socket.accept())
                    handle.setDaemon(True)
                    handle.start()
                    self.threadlist.append(handle)
                    print self.threadlist
                    
                elif s == sys.stdin:
                    if sys.stdin.readline():
                        run = False
                        sys.exit(1)
        
        for handle in self.threadlist:
            handle.join()    
            print "handle join"            
            
        self.socket.close()
        print "socket closing inside Run"
        sys.exit(1)
    
class ClientThread(threading.Thread):
    
    def __init__(self,(client, address)): 
        threading.Thread.__init__(self)
        self.device = dict()
        self.client = client
        self.address = address
        print 'connection established from' + str(self.address)    
        self.public_ip = str(self.address[0])
        self.public_port = int(self.address[1])        
             
    def run(self):
        running = True
        buffer = 1
        while running:
            print "test"
#             if self.function() == 'S':#shutdown signal
            #get the user preference
            try:
                self.client.send('S')
            except self.client.error as error:
                print "error"
                
            try:
                data = self.client.recv(buffer)
            except self.client.error as error:
                print error
                
            if data == 'Y':
                #GPIO shutdown
                print "send shutdown signal"
            elif data == 'N':
                print "do not send the shutdown signal"
    def sendCommand(self,data):
         self.client.send(data)
    def receive(self):
        return self.client.recv(1)
        
#server = Server('192.168.1.2', 9090)
#server.run()            
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
            print day[daycount]
            print d.strftime("%A")
            temp=s.attributes['out1'].value.split('-')
            loadsheddingtimea=temp[0]
            temp=s.attributes['out2'].value.split('-')
            loadsheddingtimeb=temp[0]
        daycount=daycount+1
    print loadsheddingtimea
    print loadsheddingtimeb


    print time.strftime("%H:%M:%S")
    while True:
     if(loadsheddingtimea == time.strftime("%H:%M:%S") or loadsheddingtimeb == time.strftime("%H:%M:%S")):
        print "Turn off everythng"        
        sendflag=1
        
        time.sleep(2)
        #break
