package recording;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LoadCommand extends CommandBase {
  private String filename;
  private Saved last;
  private int left;
  private ObjectInputStream ois;

  public LoadCommand(String filename, Saved start) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.filename = filename;
    this.last = start;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    try {
        FileInputStream fos = new FileInputStream(filename);
        ois = new ObjectInputStream(fos);
        left = ois.readInt();
    } catch (Exception e) {
        this.cancel();
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
      for (int i=0; i<50; i++) {
          try {
              last.next = (Saved)ois.readObject();
              last = last.next;
          } catch (Exception e) {
              this.cancel();
              break;
          }
          left--;
          if (left==0) {
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
          ois.close();
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
