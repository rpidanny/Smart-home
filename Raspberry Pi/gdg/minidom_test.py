from xml.dom import minidom
import time
from datetime import date

day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']

xmldoc = minidom.parse(r"test.xml")
PFD = xmldoc.getElementsByTagName("Group1")
daycount=0
#print PFD
for element in PFD:
    print element.getAttribute('out1')
    print day[daycount]
    daycount=daycount+1
