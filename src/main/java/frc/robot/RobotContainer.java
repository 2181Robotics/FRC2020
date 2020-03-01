/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DistanceShoot;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PanelSimple;
import frc.robot.commands.RunBelt;
import frc.robot.commands.SetCannonSpeed;
import frc.robot.commands.TargetTrack;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Panel;
import recording.RecordAuto;
import recording.RecordedJoystick;
import recording.ReplayAuto;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

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
  private final JoystickButton rb = new JoystickButton(joy.getJoystick(), 7);
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

    SmartDashboard.putNumber("selected", 0);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    rb.toggleWhenPressed(new RecordAuto(joy, "/home/lvuser/test.txt"));

    joy.toggleWhenPressed(3, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, -2, false)));
    joy.toggleWhenPressed(4, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, 2, true)));

    joy.toggleWhenPressed(1, new DistanceShoot(m_cannon));
    // joy.toggleWhenPressed(1, (new RunBelt(m_cannon, -2, false).withTimeout(.1)).andThen(
      // new RunBelt(m_cannon, 0, false).withTimeout(0), new SetCannonSpeed(m_cannon, 700), new RunBelt(m_cannon, 2, false)));
    joy.toggleWhenPressed(2, new SetCannonSpeed(m_cannon, 0));

    joy.whenHeld(5, new PanelSimple(m_panel, .5));
    joy.whenHeld(6, new PanelSimple(m_panel, -.5));

    joy.whenPressed(8, new InstantCommand(() -> {m_drive.direction = -m_drive.direction;
                                                SmartDashboard.putNumber("selected", 1-SmartDashboard.getNumber("selected", 0));}));
    boolean[] buttons = {true,true,true,true,true,true,true,true,true,false};
    boolean[] joys = {true,true,true,true,true,true};
    joy.toggleWhenPressed(10, new ReplayAuto("/home/lvuser/test.txt", joy, buttons, joys));

    joy.toggleWhenPressed(9, (new TargetTrack(m_drive).withTimeout(2.5)).alongWith((new SetCannonSpeed(m_cannon, -400).withTimeout(.1).andThen(new RunBelt(m_cannon, -2, false).withTimeout(.1)))
                                                                .andThen(new RunBelt(m_cannon, 0, false).withTimeout(0), new DistanceShoot(m_cannon).withTimeout(2)))
                              .andThen(new RunBelt(m_cannon, 2, false).alongWith(new TargetTrack(m_drive).perpetually())));
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
