package recording;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SaveCommand extends CommandBase {
  private String filename;
  private Saved start;
  private int left;
  private ObjectOutputStream oos;

  public SaveCommand(String filename, Saved start, int length) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.filename = filename;
    this.start = start;
    this.left = length;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    try {
        FileOutputStream fos = new FileOutputStream(filename);
        oos = new ObjectOutputStream(fos);
        oos.writeInt(left);
    } catch (Exception e) {
        this.cancel();
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
      for (int i=0; i<50; i++) {
          try {
              oos.writeObject(start);
          } catch (Exception e) {
              this.cancel();
              break;
          }
          start = start.next;
          left--;
          if (start==null) {
              break;
          }
      }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return (left == 0);
  }

  // Called once after isFinished returns true
  //@Override
  public void end() {
      try {
          oos.flush();
          oos.close();
      } catch (Exception e) {
          //I have no clue what to do here
      }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  //@Override
  public void interrupted() {
    end();
  }
}
