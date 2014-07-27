import time
from datetime import date
d = date.fromordinal(730920)
print d.strftime("%A")
loadshedding = '13:32:00'
print time.strftime("%H:%M:%S")
while True:
	if(loadshedding == time.strftime("%H:%M:%S")):
		print "Turn off everythng"
		break
