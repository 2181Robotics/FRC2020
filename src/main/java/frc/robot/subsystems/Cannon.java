/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.CannonDefault;

public class Cannon extends PIDSubsystem {
    /**
   * Creates a new Cannon.
   */
  private WPI_VictorSPX spinmotor = new WPI_VictorSPX(2);
  private PowerDistributionPanel pdp = new PowerDistributionPanel();
  private WPI_VictorSPX belt1 = new WPI_VictorSPX(5);
  private WPI_VictorSPX belt2 = new WPI_VictorSPX(6);
  private SpeedControllerGroup belts = new SpeedControllerGroup(belt1, belt2);
  private WPI_VictorSPX inmotor = new WPI_VictorSPX(1);

  private DigitalInput intake = new DigitalInput(0);
  private Encoder flywheel = new Encoder(1, 2);

  private double spinspeed = 0;

  private int state = 0;

  public Cannon() {
    super(
        // The PIDController used by the subsystem
        new PIDController(0.00075,0.0000,.0003)); //DONT MESS WITH THESE, YOU COULD BURN OUT MOTOR
    spinmotor.setSafetyEnabled(false);
    belt1.setSafetyEnabled(false);
    belt2.setSafetyEnabled(false);
    setDefaultCommand(new CannonDefault(this));
    enable();
    getController().setTolerance(25,10);
    // se
    // SmartDashboard.putNumber("cannonspeed", 0);
  }

  public void setPID() {
    double p =SmartDashboard.getNumber("p",0);
    double i =SmartDashboard.getNumber("i",0);
    double d =SmartDashboard.getNumber("d",0);
    getController().setPID(p, i, d);
  }

  @Override
  public void periodic() {
    super.periodic();
    SmartDashboard.putNumber("Flywheel Speed", flywheel.getRate());
    // inmotor.set(-.5);
    SmartDashboard.putBoolean("Intake Sensor", intake.get());
    SmartDashboard.putNumber("Position Error", getController().getPositionError());
    SmartDashboard.putNumber("Velocity Error", getController().getVelocityError());
    // if (spinspeed == 0) state = 0;
    // if (state == 0) {
    //   if (spinspeed != 0) state = 1;
    // }
    // if (state == 1) {
    //   if (pdp.getCurrent(1) > 10) state = 2;
    // }
    // if (state == 2) {
    //   if (pdp.getCurrent(1) < 4) state = 3;
    // }
    // if (state == 3) {
    //   if (pdp.getCurrent(1) > 6) state = 4;
    // }
    // if (spinspeed == 0) spinmotor.set(0);
    // else {
    //   if (state == 3) spinmotor.setVoltage(spinspeed*.945);
    //   else spinmotor.setVoltage(spinspeed);
    // }
    // spinmotor.setVoltage(spinspeed);
    SmartDashboard.putNumber("current draw", pdp.getCurrent(1));

    // This method will be called once per scheduler run
  }

  public double getFlywheel() {
    return flywheel.getRate();
  }

  public void spin(double speed) {
    // spinspeed = speed;
    setSetpoint(speed);
  }

  public void move_belt(double speed) {
    belts.set(speed);
  }

  public void spin_in(double speed) {
    inmotor.set(speed);
  }

  public boolean get_intake() {
    return intake.get();
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    spinspeed -= output;
    if (setpoint == 0) spinspeed = 0;
    spinmotor.setVoltage(spinspeed);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return -flywheel.getRate();
  }
}
