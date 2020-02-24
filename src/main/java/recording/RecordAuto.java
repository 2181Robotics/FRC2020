/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package recording;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class RecordAuto extends CommandBase {

  private RecordedJoystick joystick;
  private SendableChooser<String> choice;
  private String choice2 = null;
  private SaveCommand sc;

  public RecordAuto(RecordedJoystick joystick, SendableChooser<String> choice) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.joystick = joystick;
    this.choice = choice;
  }

  public RecordAuto(RecordedJoystick joystick, String choice) {
    this.joystick = joystick;
    this.choice2 = choice;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    System.out.println("starting recording");
    joystick.startRecord();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    joystick.stepRecord();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    System.out.println("Stopping recording");
    joystick.stopRecord();
    String filepath;
    if (choice2 == null) {
      filepath = choice.getSelected();
    } else {
      filepath = choice2;
    }
    joystick.updateReplay(filepath, joystick.start);
    sc = new SaveCommand(filepath, joystick.start, joystick.total);
    sc.schedule();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  //@Override
  public void interrupted() {
    System.out.println("interrupted");
    end(true);
  }

  public boolean isSaving() {
    if (sc!=null) return sc.isScheduled();
    else return false;
  }
}
