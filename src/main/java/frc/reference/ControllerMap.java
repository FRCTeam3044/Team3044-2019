/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.reference;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Climb;
import frc.robot.Drive;
import frc.robot.Intake;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Add your docs here.
 */
public class ControllerMap {
    private static ControllerMap instance = null;

    public Drive drive = Drive.getInstance();
    public Intake intake = Intake.getInstance();
    public Climb climb = Climb.getInstance();

    public XboxController firstController = new XboxController(0);
    public XboxController secondController = new XboxController(1);

    public static String driverMode; // score, climb, failureFirst, failureSecond
    public static Boolean pidMode; //enabled, disabled

    static TalonEncoderPIDSource leftPIDSource;
    static TalonEncoderPIDSource rightPIDSource;

    double kP, kI, kD;
    PIDController leftPIDController;
    PIDController rightPIDController;

    /*
     * public ControllerMap() { }
     */

    public static ControllerMap getInstance() {
        if (instance == null) {
            instance = new ControllerMap();
        }
        return instance;
    }

    public void controllerMapInit() {
        driverMode = "score";
        pidMode = false;
        kP = 1;
        kI = 0.1;
        kD = 0;
        /* leftPIDSource = new TalonEncoderPIDSource(leftFrontDrive, PIDSourceType.kRate);
        rightPIDSource = new TalonEncoderPIDSource(rightFrontDrive, PIDSourceType.kRate);
        leftPIDController = new PIDController(kP, kI, kD, leftPIDSource, leftFrontDrive);
        rightPIDController = new PIDController(kP, kI, kD, rightPIDSource, rightFrontDrive); */
    }

    public void controllerMapPeriodic() {
        switchMode();
        if (driverMode == "score") {
            scoreMode();
        } else if (driverMode == "climb") {
            climbMode();
        } else if (driverMode == "failureSecond") {
            failureMode(secondController);
        } else if (driverMode == "failureFirst") {
            failureMode(firstController);
        }

        if (driverMode != "failureFirst" && driverMode != "failureSecond") {
            if (!pidMode) {
                drive.driveTheBot(firstController.getY(Hand.kLeft), firstController.getY(Hand.kRight));
            }  else {
                leftPIDController.setSetpoint(firstController.getY(Hand.kLeft));
                rightPIDController.setSetpoint(firstController.getY(Hand.kRight));
            }
        }

        intake.IntakePeriodic();
    }

    void scoreMode() {
        intake.moveShoulder(secondController.getY(Hand.kLeft));
        intake.moveWrist(secondController.getY(Hand.kRight));

        if (secondController.getYButtonPressed()) {
            intake.hatchMode();
            intake.goMedium();
        }
        if (secondController.getXButtonPressed()) {
            intake.hatchMode();
            intake.goLow();
        }
        if (secondController.getAButtonPressed()) {
            intake.hatchMode();
            intake.goGround();
        }
        if (secondController.getBButtonPressed()) {
            intake.retractMode();
        }

        if (secondController.getPOV() == 0) {// up d-pad
            intake.cargoMode();
            intake.goMedium();
        }
        if (secondController.getPOV() == 90) {// right d-pad
            intake.cargoMode();
            intake.goFeeder();
        }
        if (secondController.getPOV() == 180) {// down d-pad
            intake.cargoMode();
            intake.goGround();
        }
        if (secondController.getPOV() == 270) {// left d-pad
            intake.cargoMode();
            intake.goLow();
        }

        if (secondController.getBumper(Hand.kLeft)) {
            intake.spinCargoWheels(-1);
        } else if (secondController.getTriggerAxis(Hand.kLeft) > .1) {
            intake.spinCargoWheels(1);
        } else {
            intake.spinCargoWheels(0);
        }

        intake.ejectHatch(secondController.getBumper(Hand.kRight));
    }

    void climbMode() {
        climb.moveClimbingArm(secondController.getY(Hand.kRight));
        climb.moveClimbingWheels(secondController.getY(Hand.kLeft));

        if (secondController.getTriggerAxis(Hand.kLeft) > .1) {
            climb.habPistonLift(2);
        }
        if (secondController.getTriggerAxis(Hand.kRight) > .1) {
            climb.habPistonLift(3);
        }
        if (secondController.getBumperPressed(Hand.kLeft)) {
            climb.habPistonLift(0);
        }
        if (secondController.getBButtonPressed()) {
            intake.retractMode();
        }

        if (secondController.getYButtonPressed()) {
            climb.retract();
        }

        if (secondController.getXButtonPressed()) {
            climb.levelThree();
        }
        if (secondController.getAButtonPressed()) {
            climb.levelTwo();
        }
    }

    void failureMode(XboxController controller) {
        if (controller.getTriggerAxis(Hand.kLeft) > .1) {
            climb.habPistonLift(2);
        }
        if (controller.getTriggerAxis(Hand.kRight) > .1) {
            climb.habPistonLift(3);
        }
        if (controller.getBumperPressed(Hand.kLeft)) {
            climb.habPistonLift(0);
        }
        if (controller.getBButtonPressed()) {
            intake.retractMode();
        }

        if (controller.getYButtonPressed()) {
            climb.retract();
        }

        if (controller.getXButtonPressed()) {
            climb.levelThree();
        }
        if (controller.getAButtonPressed()) {
            climb.levelTwo();
        }

        drive.driveTheBot(controller.getY(Hand.kLeft), controller.getY(Hand.kRight));
    }

    void switchMode() {
        if (secondController.getBackButton() && secondController.getStartButton()) {
            driverMode = "failureSecond";
        } else if (firstController.getBackButton() && firstController.getStartButton()) {
            driverMode = "failureFirst";
        } else if (secondController.getStartButtonPressed()) {
            if (driverMode == "score") {
                driverMode = "climb";
            } else if (driverMode == "climb") {
                driverMode = "score";
            }
        }
    }

    double PID(double setpoint) {
        return setpoint;
    }
}
