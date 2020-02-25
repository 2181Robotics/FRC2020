/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DriveTrainDefault;

public class DriveTrain extends SubsystemBase {
  /**
   * Creates a new DriveTrain.
   */

  private WPI_VictorSPX right1 = new WPI_VictorSPX(7);
  private WPI_VictorSPX right2 = new WPI_VictorSPX(3);
  private SpeedControllerGroup right = new SpeedControllerGroup(right1, right2);

  private WPI_VictorSPX left1 = new WPI_VictorSPX(8);
  private WPI_VictorSPX left2 = new WPI_VictorSPX(4);
  private SpeedControllerGroup left = new SpeedControllerGroup(left1, left2);

  public int direction = 1;

  // private DifferentialDrive drive = new DifferentialDrive(left, right);

  public DriveTrain() {
    right.setInverted(true);

    right1.setSafetyEnabled(false);
    right2.setSafetyEnabled(false);
    left1.setSafetyEnabled(false);
    left2.setSafetyEnabled(false);

    setDefaultCommand(new DriveTrainDefault(this));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void drive(double speed, double turn) {
    speed *= direction;

    // drive.arcadeDrive(turn, -speed); //backwards bc reasons
    left.set((turn+speed)/2);
    right.set(1.0*(turn-speed)/2); // to maybe fix drift error
  }
}
