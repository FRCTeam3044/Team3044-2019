/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * This is a sample program to demonstrate how to use a soft potentiometer and a
 * PID controller to reach and maintain position setpoints on an elevator
 * mechanism.
 */
public class Robot extends TimedRobot {
  private static final int kPotChannel = 3;
  private static final int kMotorChannel = 4;
  private static final int kJoystickChannel = 0;

  // bottom, middle, and top elevator setpoints
  private static final double[] kSetPoints = {1.0, 2.6, 4.3};

  // proportional, integral, and derivative speed constants; motor inverted
  // DANGER: when tuning PID constants, high/inappropriate values for kP, kI,
  // and kD may cause dangerous, uncontrollable, or undesired behavior!
  // these may need to be positive for a non-inverted motor
  private static final double kP = 1.0;
  private static final double kI = 0.02;
  private static final double kD = 2.0;

  private PIDController m_pidController;
  @SuppressWarnings("PMD.SingularField")
  private AnalogInput m_potentiometer;
  @SuppressWarnings("PMD.SingularField")
  private WPI_TalonSRX m_elevatorMotor;
  private Joystick m_joystick;
  

  private int m_index;
  private boolean m_previousButtonValue;

  @Override
  public void robotInit() {
    m_potentiometer = new AnalogInput(kPotChannel);
    m_elevatorMotor = new WPI_TalonSRX(kMotorChannel);
    m_joystick = new Joystick(kJoystickChannel);

    m_pidController = new PIDController(kP, kI, kD, m_potentiometer, m_elevatorMotor);
    m_pidController.setInputRange(0, 4000);
  }

  @Override
  public void teleopInit() {
    m_pidController.enable();
  }

  @Override
  public void teleopPeriodic() {
    // when the button is pressed once, the selected elevator setpoint
    // is incremented
    boolean currentButtonValue = m_joystick.getTrigger();
    if (currentButtonValue && !m_previousButtonValue) {
      // index of the elevator setpoint wraps around.
      m_index = (m_index + 1) % kSetPoints.length;
    }
    m_previousButtonValue = currentButtonValue;

    m_pidController.setSetpoint(1000);
  }
}
