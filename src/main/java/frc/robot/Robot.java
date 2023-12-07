// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.redneckrobotics.zipti.ControlBase;
import com.redneckrobotics.zipti.swerve.SwerveDrive;

/**
 * This is an example usage of the Zipti control system.
 */

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private ControlBase control;
  private Joystick joystick;
  private WPI_TalonFX motor;
  private SwerveDrive drive;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    this.joystick = new Joystick(0);
    this.drive = new SwerveDrive(
      new WPI_TalonFX(0), new WPI_TalonFX(1),
      new WPI_TalonFX(2), new WPI_TalonFX(3),
      new WPI_TalonFX(4), new WPI_TalonFX(5),
      new WPI_TalonFX(6), new WPI_TalonFX(7)
    );
    this.motor = new WPI_TalonFX(8);

    this.control = new ControlBase(this.joystick);
    this.control.bindDrive(this.drive,     // Drivebase
      this.joystick.getXChannel(),         // Strafe
      this.joystick.getYChannel(),         // Drive
      this.joystick.getTwistChannel(),     // Turn
      this.joystick.getThrottleChannel(),  // Trim
      true); // Use HAT switch for center of rotation
    control.bindMotorPower(0, this.motor, 1.0, 0.0);

    control.robotInit();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    control.teleopPeriodic();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
