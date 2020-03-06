/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TargetTrack extends PIDCommand {
  /**
   * Creates a new TargetTrack.
   */

  public TargetTrack(DriveTrain driveTrain) {
    super(
        // The controller that the command will use
        new PIDController(.018, 0.00000000, 0.004),
        // This should return the measurement
        () -> SmartDashboard.getNumber("posx", 102),
        // This should return the setpoint (can also be a constant)
        () -> 104,
        // This uses the output
        output -> {
          driveTrain.drive(0, -output/2);
        });
    addRequirements(driveTrain);
    // getController().setTolerance(1,1)
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return getController().atSetpoint();
    return Math.abs(SmartDashboard.getNumber("posx", 102)-102)< 10 && SmartDashboard.getBoolean("stable", false);
  }
}
