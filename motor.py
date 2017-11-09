# file: rfcomm-server.py
# auth: Albert Huang <albert@csail.mit.edu>
# desc: simple demonstration of a server application that uses RFCOMM sockets
#
# $Id: rfcomm-server.py 518 2007-08-10 07:20:07Z albert $

from bluetooth import *
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.OUT)
pwm=GPIO.PWM(11,50)
pwm.start(5)

pwm.ChangeDutyCycle(2)

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

#advertise_service( server_sock, "SampleServer",
#                   service_id = uuid,
#                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
#                   profiles = [ SERIAL_PORT_PROFILE ])

advertise_service( server_sock, "SampleServer",uuid)
print "Waiting for connection on RFCOMM channel %d" % port

client_sock, client_info = server_sock.accept()
print "Accepted connection from ", client_info

try:
    while True:
        data = client_sock.recv(1024)
        print(data)
        if data == '0':
            pwm.ChangeDutyCycle(2)
            #print(data)
        elif data == '1':
            pwm.ChangeDutyCycle(7)
        #if len(data) == 0: break
        print "received [%s]" % data
except IOError:
    pass

print "disconnected"

client_sock.close()
server_sock.close()
print "all done"
