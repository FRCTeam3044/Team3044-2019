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
import frc.robot.Robot;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Add your docs here.
 */
public class SecondControllerMap extends Robot {
    public Drive drive = Drive.getInstance();
    public Intake intake = Intake.getInstance();
    public Climb climb = Climb.getInstance();
    private static SecondControllerMap instance = null;

    /*
     * XboxController secondController; public XboxController firstController;
     */
    public static String driverMode; // score, climb, failureFirst, failureSecond

    /*
     * Intake intake = new Intake(); Climb climb = new Climb(); Drive drive = new
     * Drive();
     */

    // public SecondControllerMap() {
    /*
     * secondController = Hardware.getInstance().secondController; firstController =
     * Hardware.getInstance().firstController;
     */
    // }

    public static SecondControllerMap getInstance() {
        if (instance == null) {
            instance = new SecondControllerMap();
        }
        return instance;
    }

    public void secondControllerMapInit() {
        driverMode = "score";
    }

    public void secondControllerMapPeriodic() {
        switchMode();
        if (driverMode == "score") {
            scoreMode();
        } else if (driverMode == "climb") {
            climbMode();
        } else if (driverMode == "failureSecond") {
            failureMode(hardware.secondController);
        } else if (driverMode == "failureFirst") {
            failureMode(hardware.firstController);
        }

        if (driverMode != "failureFirst" && driverMode != "failureSecond") {
            drive.driveTheBot(hardware.firstController.getY(Hand.kLeft), hardware.firstController.getY(Hand.kRight));
        }

        intake.IntakePeriodic();
    }

    void scoreMode() {
        if (hardware.secondController.getYButtonPressed()) {
            intake.hatchMode();
            intake.goMedium();
        }
        if (hardware.secondController.getXButtonPressed()) {
            intake.hatchMode();
            intake.goLow();
        }
        if (hardware.secondController.getAButtonPressed()) {
            intake.hatchMode();
            intake.goGround();
        }
        if (hardware.secondController.getBButtonPressed()) {
            intake.retractMode();
        }

        if (hardware.secondController.getPOV() == 0) {// up d-pad
            intake.cargoMode();
            intake.goMedium();
        }
        if (hardware.secondController.getPOV() == 90) {// right d-pad
            intake.cargoMode();
            intake.goFeeder();
        }
        if (hardware.secondController.getPOV() == 180) {// down d-pad
            intake.cargoMode();
            intake.goGround();
        }
        if (hardware.secondController.getPOV() == 270) {// left d-pad
            intake.cargoMode();
            intake.goLow();
        }

        if (hardware.secondController.getBumper(Hand.kLeft)) {
            intake.spinCargoWheels(.5);
        }
        if (hardware.secondController.getTriggerAxis(Hand.kLeft) > .1) {
            intake.spinCargoWheels(-.5);
        }

        intake.ejectHatch(hardware.secondController.getBumper(Hand.kRight));
    }

    void climbMode() {
        if (hardware.secondController.getTriggerAxis(Hand.kLeft) > .1) {
            climb.habPistonLift(2);
        }
        if (hardware.secondController.getTriggerAxis(Hand.kRight) > .1) {
            climb.habPistonLift(3);
        }
        if (hardware.secondController.getBumperPressed(Hand.kLeft)) {
            climb.habPistonLift(0);
        }
        if (hardware.secondController.getBButtonPressed()) {
            intake.retractMode();
        }

        if (hardware.secondController.getYButtonPressed()) {
            climb.retract();
        }

        if (hardware.secondController.getXButtonPressed()) {
            climb.levelThree();
        }
        if (hardware.secondController.getAButtonPressed()) {
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
        if (hardware.secondController.getBackButton() && hardware.secondController.getStartButton()) {
            driverMode = "failureSecond";
        } else if (hardware.firstController.getBackButton() && hardware.firstController.getStartButton()) {
            driverMode = "failureFirst";
        } else if (hardware.secondController.getStartButtonPressed()) {
            if (driverMode == "score") {
                driverMode = "climb";
            } else if (driverMode == "climb") {
                driverMode = "score";
            }
        } else {
        }
    }
}
