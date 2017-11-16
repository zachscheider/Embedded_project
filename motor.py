#!/usr/bin/env python
# /etc/init.d/motor.py
### BEGIN INIT INFO
# Provides:          motor.py
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time
# Description:       Enable service provided by daemon.
### END INIT INFO

from bluetooth import *
import RPi.GPIO as GPIO
import shlex
from subprocess import Popen, PIPE
import time

def get_exitcode_stdout_stderr(cmd):
    """
    Execute the external command and get its exitcode, stdout and stderr.
    """
    args = shlex.split(cmd)

    proc = Popen(args, stdout=PIPE, stderr=PIPE)
    out, err = proc.communicate()
    exitcode = proc.returncode
    #
    return exitcode, out, err

cmd = "hcitool rssi E4:FA:ED:7F:25:F5"  # arbitrary external command, e.g. "python mytest.py"
exitcode, out, err = get_exitcode_stdout_stderr(cmd)

GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.OUT)
pwm=GPIO.PWM(11,50)
pwm.start(5)

pwm.ChangeDutyCycle(2)
while True:
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
    exitcode, out, err = get_exitcode_stdout_stderr(cmd)
    print "exitcode ", exitcode
    print "out ", out
    print "err ", err
    #rssi_out = out.split(' ')
    #rssi = int(rssi_out[3])
    #print rssi #This is the RSSI value
    try:
        while True:
            print "hello"
            exitcode, out, err = get_exitcode_stdout_stderr(cmd)
            rssi_out = out.split(' ')
            print "out",out
            if(out != ""):
                rssi = int(rssi_out[3])
            else:
                break
                
            print "rssi", rssi #This is the RSSI value

            #data = client_sock.recv(1024)
            
            #if data == '0':
            if rssi < 0:
                pwm.ChangeDutyCycle(2)
                #print "received [%s]" % data
            #elif data == '1':
            elif rssi >= 0:
                pwm.ChangeDutyCycle(7)
                #if len(data) == 0: break
                #print "received [%s]" % data
            time.sleep(2)
    except IOError:
        pass
    
    pwm.ChangeDutyCycle(2)
    print "disconnected"

    client_sock.close()
    server_sock.close()


print "all done"
