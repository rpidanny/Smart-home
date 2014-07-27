#!/usr/bin/python 
import time
from SimpleCV import JpegStreamCamera
from SimpleCV import Color, Image, np, Camera
 

cam = Camera()
quality = 400
minMatch = 0.6
 
try:
    password = Image("password.jpg")
except:
    password = None
 
mode = "unsaved"
saved = False
minDist = 0.25
 
 

image = cam.getImage().scale(320, 240) # get image, scale to speed things up
faces = image.findHaarFeatures('face.xml') # load in trained face file
#image.show()
if faces:
	if not password:
		faces.draw()
		face = faces[-1]
		password = face.crop().save("password.jpg")
		#break
	else:
		faces.draw()
		face = faces[-1]
		template = face.crop()
		template.save("passwordmatch.jpg")
		keypoints = password.findKeypointMatch(template,quality,minDist,minMatch)
		if keypoints:
			print "Open Door"
		else:
			print "Password minMatch"
else:
	print "No faces..."
