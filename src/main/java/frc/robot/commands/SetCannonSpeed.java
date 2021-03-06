/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cannon;

public class SetCannonSpeed extends CommandBase {
  /**
   * Creates a new SetCannonSpeed.
   */

  private Cannon can;
  private double speed;

  public SetCannonSpeed(Cannon cannon, double speed) {
    addRequirements(cannon);
    can = cannon;
    this.speed = speed;
    SmartDashboard.putNumber("shooting speed", 0);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // if (can.get_intake()){
      can.spin(speed);
      // can.spin(SmartDashboard.getNumber("shooting speed", 0));
    // }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return can.getController().atSetpoint() || speed == 0;
  }
}
