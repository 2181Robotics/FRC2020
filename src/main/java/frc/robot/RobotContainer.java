/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PanelSimple;
import frc.robot.commands.RunBelt;
import frc.robot.commands.SetCannonSpeed;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Panel;
import recording.RecordedJoystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DriveTrain m_drive = new DriveTrain();
  private final Cannon m_cannon = new Cannon();
  private final Panel m_panel = new Panel();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);



  public static RecordedJoystick joy = new RecordedJoystick(0);
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    SmartDashboard.putNumber("p",m_cannon.getController().getP());
    SmartDashboard.putNumber("i",m_cannon.getController().getI());
    SmartDashboard.putNumber("d",m_cannon.getController().getD());

    SmartDashboard.putData(new InstantCommand(() -> m_cannon.setPID()));
    SmartDashboard.putData("Start", new SetCannonSpeed(m_cannon, 800));
    SmartDashboard.putData("Stop", new SetCannonSpeed(m_cannon, 0));
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    joy.toggleWhenPressed(3, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, -2, false)));
    joy.toggleWhenPressed(4, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, 2, true)));

    joy.toggleWhenPressed(1, (new RunBelt(m_cannon, -2, false).withTimeout(.1)).andThen(
      new RunBelt(m_cannon, 0, false).withTimeout(0), new SetCannonSpeed(m_cannon, 700),new WaitCommand(4), new RunBelt(m_cannon, 2, false)));
    joy.toggleWhenPressed(2, new SetCannonSpeed(m_cannon, 0));

    joy.whenHeld(5, new PanelSimple(m_panel, .5));
    joy.whenHeld(6, new PanelSimple(m_panel, -.5));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  public void resetSystems() {
    m_cannon.spin(0);
  }
}
