#!C:\Python27\python.exe

# Import modules for CGI handling 
import cgi, cgitb 

# Create instance of FieldStorage 
form = cgi.FieldStorage() 

# Get data from fields
grouppreference = form.getvalue('Group')
delaypreference=form.getvalue('Delay')
print "Content-type:text/html\r\n\r\n"
print "<html>"
print "<head>"
print "<title>Hello - Second CGI Program</title>"
print "</head>"
print "<body>"
print "<h2>%s Selected.</h2>" % (grouppreference)
print "<h2>%s Delay Selected.</h2>" % (grouppreference)
print "</body>"
print "</html>"

f = open('preference','w')
f.write(grouppreference)
f.write(delaypreference)
f.close()
