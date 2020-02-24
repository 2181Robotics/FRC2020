How to use:

The first thing that must be done is create an instance of RecordedJoystick with the 
port number of the desired joystick. This works with any joystick that wpilib's Joystick 
class allows. From there you are able to use getRawAxis for anything that requires axis to 
work. All of the loaded recordings are held within that joystick for use later.
NOTE: recording a new replay overrides the currently loaded one, and is immediately available for
use afterwards. There is no need to load anything again.

Buttons work slightly differently, and you are able to create them using RecordedJoystick
itself. Use something like recordedJoystick.whenPressed([button number], [command]), instead
of creating your own buttons. The only time you should make a button yourself is when assigning
the record button, as that in most cases should not be triggered by a replay.

You are likely only going to create one instance of RecordAuto, as it will be tied to a button as
stated before. This caused some issues when designing RecordAuto, so some potenially unwanted steps
must be taken. RecordAuto takes your instance of RecordedJoystick, whichever one you want to record,
and then will take an instance of SendableChooser<String>. This of course may not be wanted by you,
but this is how RecordAuto will figure out where to save to. At the time that RecordAuto is stopped,
it will get whatever is selected from SendableChooser<String> and use that as the filepath. When
setting up a button to run RecordAuto, make sure to use an option like toggleWhenPressed, because 
the command doesn't have a timeout, and will run until it is cancelled.

Finally, all that needs to be made are instances of ReplayAuto. These act like normal commands and 
can be used anywhere a normal command can (for example, autonomous, command groups, tied to a 
button). ReplayAuto requires the filepath it will be tied to, and an instance of RecordedJoystick that
matches the one it was saved from (meaning don't record with an xbox controller and then try to
play back on a large flight joystick). You can create multiple instances of ReplayAuto with the 
same filename, and it won't take up much more memory as long as you use the same SaveOrganizer
for all of them.

For debug purposes, RecordAuto also provides an isSaving() method which returns true if it is in 
the process of saving. It is possible to be saving multiple files at a time, as long as they are 
different files. Because of this, isSaving() is rarely needed, but could be useful to have on 
SmartDashboard if you are re-recording replays multiple times.

P.S.
Make sure that the robot has permissions to read and write files to where you are saving them. It 
is reccomended that a USB Flash Drive is used as this will reduce the chance that the storage on
the roboRIO is filled. The roboRIO uses the lvuser account to run the robot code, so make sure that
account has appropriate permissions (read and write). The simplest, while not nessecarily the safest
way to do this is by giving everyone those permissions.