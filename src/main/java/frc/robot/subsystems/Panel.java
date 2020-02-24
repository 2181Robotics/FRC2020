/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.PanelSimple;

public class Panel extends SubsystemBase {

  private WPI_TalonSRX motor = new WPI_TalonSRX(9);
  /**
   * Creates a new Panel.
   */
  public Panel() {
    setDefaultCommand(new PanelSimple(this, 0));

  }

  @Override
  public void periodic() {
    // motor.set(.5);
    // This method will be called once per scheduler run
  }

  public void spin(double speed) {
    motor.set(speed);
  }
}
