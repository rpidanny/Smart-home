from xml.dom import minidom
import time
from datetime import date



day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']


d = date.today()    
f = open("preference",'r')
data=f.readline().rstrip('\n')
print data
f.close()
xmldoc = minidom.parse(r"test.xml")
print xmldoc
groupX = xmldoc.getElementsByTagName(data)

daycount=0
loadsheddingtimea=0
loadsheddingtimeaON=0
loadsheddingtimeb=0
loadsheddingtimebON=0
print d.strftime("%A")

for element in groupX:
	print "inside for loop "
	print element.getAttribute('out1')
	
