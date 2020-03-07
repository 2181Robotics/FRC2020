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

 private final Command m_autoCommand = (new TargetTrack(m_drive).withTimeout(2.5)).alongWith((new SetCannonSpeed(m_cannon, -400).withTimeout(.1).andThen(new RunBelt(m_cannon, -2, false).withTimeout(.2)))
 .andThen(new RunBelt(m_cannon, 0, false).withTimeout(0), new DistanceShoot(m_cannon).withTimeout(1.7)))
.andThen(new RunBelt(m_cannon, 1, false).alongWith(new TargetTrack(m_drive).perpetually())).withInterrupt(() -> {return m_cannon.ballshoot >= 6;}).andThen(new InstantCommand(() -> {m_drive.drive(.5,0);}, m_drive).perpetually().withTimeout(1));

//without aim assit
// private final Command m_autoCommand = (new SetCannonSpeed(m_cannon, 695).withTimeout(2)).andThen(new RunBelt(m_cannon, 1, false)).withTimeout(4).andThen(new InstantCommand(() -> {m_drive.drive(.5,0);}, m_drive).perpetually().withTimeout(2));


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

    // set speed   line     (9, new SetCannonSpeed(m_cannon, 680).alongWith(new RunBelt(m_cannon, 0, false))
    joy.toggleWhenPressed(9, (new TargetTrack(m_drive).withTimeout(2.5)).alongWith((new SetCannonSpeed(m_cannon, -500).withTimeout(.2).andThen(new RunBelt(m_cannon, -2, false).withTimeout(.2)))
    .andThen(new RunBelt(m_cannon, 0, false).withTimeout(0), new SetCannonSpeed(m_cannon, 680).withTimeout(2)))
.andThen(new RunBelt(m_cannon, 1, false).alongWith(new TargetTrack(m_drive).perpetually())));
// joy.toggleWhenPressed(9, new SetCannonSpeed(m_cannon, 680).withTimeout(1.5).andThen(new RunBelt(m_cannon, 1, false))); // no aim assait
// joy.toggleWhenPressed(10, new SetCannonSpeed(m_cannon, 895.25).withTimeout(1.75).andThen(new RunBelt(m_cannon, 1, false))); // no aim assait
   
// auto
    joy.toggleWhenPressed(10, (new TargetTrack(m_drive).withTimeout(2.5)).alongWith((new SetCannonSpeed(m_cannon, -400).withTimeout(.1).andThen(new RunBelt(m_cannon, -2, false).withTimeout(.2)))
    .andThen(new RunBelt(m_cannon, 0, false).withTimeout(0), new DistanceShoot(m_cannon).withTimeout(1.7)))
   .andThen(new RunBelt(m_cannon, 1, false).alongWith(new TargetTrack(m_drive).perpetually())));

    joy.toggleWhenPressed(1, new InstantCommand(() -> {m_cannon.spin_in(.5);}, m_cannon).perpetually());
// 
    joy.toggleWhenPressed(4, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, 1, true)));
    // joy.toggleWhenPressed(1, new DistanceShoot(m_cannon));
    joy.toggleWhenPressed(3, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, -1, false)));
    // joy.toggleWhenPressed(1, (new RunBelt(m_cannon, -2, false).withTimeout(.1)).andThen(
      // new RunBelt(m_cannon, 0, false).withTimeout(0), new SetCannonSpeed(m_cannon, 700), new RunBelt(m_cannon, 2, false)));
    joy.toggleWhenPressed(2, new SetCannonSpeed(m_cannon, 0).andThen(new RunBelt(m_cannon, 0, false)));

    joy.whenHeld(5, new PanelSimple(m_panel, .5));
    joy.whenHeld(6, new PanelSimple(m_panel, -.5));

    // joy.toggleWhenPressed(7, new TargetTrack(m_drive).perpetually());
    joy.whenPressed(8, new InstantCommand(() -> {m_drive.direction = -m_drive.direction;
                                                SmartDashboard.putNumber("selected", 1-SmartDashboard.getNumber("selected", 0));}));
    boolean[] buttons = {true,true,true,true,true,true,true,true,true,false};
    boolean[] joys = {true,true,true,true,true,true};
    //joy.toggleWhenPressed(10, new ReplayAuto("/home/lvuser/test.txt", joy, buttons, joys));

    // joy.toggleWhenPressed(1, (new TargetTrack(m_drive).withTimeout(2.5)).alongWith((new SetCannonSpeed(m_cannon, -500).withTimeout(.2).andThen(new RunBelt(m_cannon, -2, false).withTimeout(.2)))
    //                                                             .andThen(new RunBelt(m_cannon, 0, false).withTimeout(0), new DistanceShoot(m_cannon).withTimeout(2)))
    //                           .andThen(new RunBelt(m_cannon, 1, false).alongWith(new TargetTrack(m_drive).perpetually())));
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
