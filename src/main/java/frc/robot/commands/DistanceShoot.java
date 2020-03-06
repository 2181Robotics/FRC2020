/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Cannon;

public class DistanceShoot extends CommandBase {
  /**
   * Creates a new CannonDefault.
   */
  private Cannon cannon;
  private static double[] x = {14,18,20,28,35,41,48,58,69};
  private static double[] y = {718,703,701,713,729,740,750,790,859};
  private double speed;
  private AnalogInput ultra = new AnalogInput(0);

  public DistanceShoot(Cannon can) {
    addRequirements(can);
    cannon = can;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  private double interpolate(double in) {
    int selected = -1;
    for (int i = 0; i < x.length-1; i++) {
      if (x[i+1] >= in) {
        selected = i;
        break;
      }
    }
    if (selected == -1) selected = x.length-2;
    return y[selected]+(in-x[selected])*(y[selected+1]-y[selected])/(x[selected+1]-x[selected]);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    // SmartDashboard.putNumber("cannonspeed", 0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //cannon.spin(SmartDashboard.getNumber("cannonspeed", 0));
      //double x = SmartDashboard.getNumber("posy", 80);
      double x = ultra.getVoltage();
      SmartDashboard.putNumber("try speed", ultra.getVoltage());
      // SmartDashboard.putNumber("Flywheel Speed", ultra.getVoltage());
    // if (Math.abs(x-83) > 30) x = 83;
    // cannon.spin(Math.min((1.0/504.0)*(x-83)*(x-83)+7.5,12));
    speed = interpolate(x);
    cannon.spin(speed);
    //SmartDashboard.putNumber("try speed", speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    // return cannon.getController().atSetpoint() && (Math.abs(cannon.getFlywheel() + speed) < 50);
  }
}
