#requires pyfrc, pyserial, use pip to install

from networktables import NetworkTablesInstance, NetworkTables
import threading, time
import serial
import serial.tools.list_ports

cond = threading.Condition()
notified = [False]
con = False

def connectionListener(connected, info):
    global con
    con = True
    print(info, '; Connected=%s' % connected)
    with cond:
        notified[0] = True
        cond.notify()

NetworkTables.initialize(server='10.21.81.2')
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)

ser = None
print("Looking for arduino")
while not ser:
##    for port in serial.tools.list_ports.comports():
    try:
        ser = serial.Serial("COM5", 2400, timeout = .01)
        print("Connected to arduino on", "COM5")
    except: pass

##ser.close()

with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()

# Insert your processing code here
print("Connected!")

table = NetworkTables.getTable("FMSInfo")
table2 = NetworkTables.getTable("SmartDashboard")

##ser.write("SE".encode("ascii"))
prev = ""

def send(message):
##    ser.open()
##    for c in message:
##        ser.write(c.encode("ascii"))
##        print(c, end="")
##        time.sleep(.02)
##    print()
    ser.write(message.encode("ascii"))
    ser.flush()
    print(message)
##    ser.close()

##while True:
##    s = ser.read()
##    if s and s.decode("ascii") == "0":
##        break
##
##print("Data received from arduino")

while True:
    try:
    ##        if ser.in_waiting:
    ##            ser.open()
        s = ser.read()
    ##            ser.close()
        if s:
            print("in: ", s.decode("ascii"))
            if s and s.decode("ascii") == "0":
            
                send(prev)
        ##    print(table.getString("GameSpecificMessage", ""))
        ##    message = table.getString("GameSpecificMessage", "0")
        ##    try:
        ####        num = int(message)
        ####        num = 20
        ##        print(message)
        ##        ser.write(message.encode("ascii"))
        ##    except:
        ##        pass
        message = "S"
        message += "1" if table.getBoolean("IsRedAlliance", False) else "0"
        message += "1" if table2.getBoolean("Enabled", False) else "0"
        message += "1" if table2.getBoolean("Auto", False) else "0"
        message += "1" if table2.getBoolean("EStop", False) else "0"
        message += "E"
    ##    print("Debug: ",message)
        if message != prev:
            prev = message
    ##            print(message)
            send(message)
    except serial.serialutil.SerialException:
        try: ser.close()
        except: pass
        print("Disconnected from adruino")
        ser = None
        while not ser:
    ##    for port in serial.tools.list_ports.comports():
            try:
                ser = serial.Serial("COM5", 2400, timeout = .01)
                print("Connected to arduino on", "COM5")
            except: pass
