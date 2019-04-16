/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.reference.ControllerMap;
import frc.reference.Hardware;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public Hardware hardware = Hardware.getInstance();
  public ControllerMap controllerMap;

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  UsbCamera intakeCam;
  // UsbCamera climberCam;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Hardware.getInstance().init();
    hardware.init();

    controllerMap = ControllerMap.getInstance();
    controllerMap.controllerMapInit();

    controllerMap.intake.IntakeInit();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    intakeCam = CameraServer.getInstance().startAutomaticCapture("Driver Camera :)", 0);
    intakeCam.setPixelFormat(PixelFormat.kMJPEG);
    intakeCam.setResolution(160, 120);
    intakeCam.setFPS(15);
    // climberCam = CameraServer.getInstance().startAutomaticCapture(1);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    SmartDashboard.putString("DB/String 0", ": " + ControllerMap.driverMode);
    SmartDashboard.putString("DB/String 3",
        "pot value: " + String.valueOf(Intake.getInstance().potentiometer.getVoltage()));
    SmartDashboard.putString("DB/String 7", "Zero position: " + Intake.getInstance().getStartingWristEncoderValue());
    SmartDashboard.putString("DB/String 8",
        "wrist encoder: " + String.valueOf(Hardware.intakeWrist.getSensorCollection().getQuadraturePosition()));
    SmartDashboard.putString("DB/String 9",
        "Corrected wrist: " + String.valueOf(Intake.getInstance().getCorrectedWristEncoderValue()));
    // SmartDashboard.putString("DB/String 4", "pot value: " +
    // String.valueOf(intake.potentiometer.getValue()));
    // SmartDashboard.putString("DB/String 9", "pot voltage: " +
    // String.valueOf(intake.potentiometer.getVoltage()));
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    Intake.getInstance().releaseHatch();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;

    }

    controllerMap.controllerMapPeriodic();
  }

  @Override
  public void teleopInit() {
    Intake.getInstance().releaseHatch();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // drive.DrivePeriodic();
    controllerMap.controllerMapPeriodic();
    // intake.IntakePeriodic();
    // climb.ClimbPeriodic();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
