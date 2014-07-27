#!C:\Python27\python.exe

from xml.dom import minidom
import time

f = open("preference",'r')
data=f.readline()
xmldoc = minidom.parse('test.xml')
group = xmldoc.getElementsByTagName(data)
#group2 = xmldoc.getElementsByTagName('
day =['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
daycount=0
print 'Content-type: text/html\r\n\r'
print '<head>'
print '<title>Result</title>'
print '<link rel="stylesheet" type="text/css" href="css/style.css">'
print '</head>'
print '<html>'
print time.strftime("%H:%M:%S")
print '<h2>Loadshedding Routine for ',data,'</h2>'
#print group1[0].attributes['out1'].value
print '<table>'
print '<tr><th><b>Day</b></th><th>Morning</th><th>Afternoon</th></tr>'
#print day[0]
for s in group :
        print '<tr>'
        print '<th>',day[daycount],'</th> ','<th>',s.attributes['out1'].value ,"</th><th>",s.attributes['out2'].value,"</th>"
        daycount=daycount+1
        print '</tr>'

print '</table>'
print '</html>'
