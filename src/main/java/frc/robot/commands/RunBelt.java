/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cannon;

public class RunBelt extends CommandBase {
  /**
   * Creates a new RunBelt.
   */
  private Cannon can;
  private double speed;
  private boolean limiting;
  private boolean spike = false;
  // private double ballcount = 0;
  private static PowerDistributionPanel pdp = new PowerDistributionPanel();

  public RunBelt(Cannon cannon, double speed, boolean limit) {
    addRequirements(cannon);
    can = cannon;
    this.speed = speed;
    limiting = limit;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    can.ballshoot = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (limiting) can.spin_in(-.5);
    // if (!limiting || can.get_intake()) {
      can.move_belt(speed);
    // } else {
      // can.move_belt(0);
    // }
    if (!limiting) {
      double v = pdp.getCurrent(1);
      if (spike) {
        if (v < 9) spike = false;
      } else {
        if (v > 13) {
          spike = true;
          can.setSetpoint(can.getController().getSetpoint()*(1-(5-can.ballshoot)*.01));
          can.ballshoot += 2.1;
          // if (ballcount == 3) ballcount = 5;
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    can.spin_in(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
