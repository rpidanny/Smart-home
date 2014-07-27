from xml.dom import minidom
import time
from datetime import date



day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']

while True:
    print "Top of Loop"
    d = date.today()    
    f = open("preference",'r')
    data=f.readline().rstrip('\n')
    print data
    f.close()
    xmldoc = minidom.parse(r"test.xml")
    #print xmldoc
    group = xmldoc.getElementsByTagName(data)
    #print group
    daycount=0
    loadsheddingtimea=0
    loadsheddingtimeaON=0
    loadsheddingtimeb=0
    loadsheddingtimebON=0
    #print d.strftime("%A")
    #print group.getAttribute("out1")
    for s in group :
	#print "inside for loop "
	#print s.getAttribute('out1')
	if(day[daycount]==d.strftime("%A")):
		#print day[daycount]
		#print d.strftime("%A") 
		temp=s.attributes['out1'].value.split('-')
		loadsheddingtimea=temp[0]
		loadsheddingtimeaON=temp[1]
		temp=s.attributes['out2'].value.split('-')
		loadsheddingtimeb=temp[0]
		loadsheddingtimebON=temp[1]
		#print loadsheddingtimea+" - "+loadsheddingtimeaON +" and " + loadsheddingtimeb+" - "+loadsheddingtimebON
	daycount=daycount+1
        
    print loadsheddingtimea + " - " + loadsheddingtimeaON
    print loadsheddingtimeb + " - " + loadsheddingtimebON


    print "Current Time is : " + time.strftime("%H:%M:%S") +"\n \n\nChecking Loadshedding!!"
    while True:
		if(loadsheddingtimea == time.strftime("%H:%M:%S") or loadsheddingtimeb == time.strftime("%H:%M:%S")):
			print "Turn off everythng"        
			sendflag=1
			time.sleep(2)
			break
		if(loadsheddingtimeaON == time.strftime("%H:%M:%S") or loadsheddingtimebON == time.strftime("%H:%M:%S")):
			print "Turn ON everythng"        
			sendflag=1
			time.sleep(2)
			break
		time.sleep(0.6)
		
			
			#break
