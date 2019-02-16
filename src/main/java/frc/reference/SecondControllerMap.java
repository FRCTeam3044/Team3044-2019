/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.reference;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Climb;
import frc.robot.Drive;
import frc.robot.Intake;

/**
 * Add your docs here.
 */
public class SecondControllerMap {
    XboxController secondController;
    XboxController firstController;
    public String driverMode; // score, climb, failure

    Intake intake = new Intake();
    Climb climb = new Climb();
    Drive drive = new Drive();

    public SecondControllerMap() {
        secondController = Hardware.getInstance().secondController;
        firstController = Hardware.getInstance().firstController;
    }

    public void secondControllerMapPeriodic() {
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
    }

    void scoreMode() {
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
            intake.spinCargoWheels(.5);
        }
        if (secondController.getTriggerAxis(Hand.kLeft) > .1) {
            intake.spinCargoWheels(-.5);
        }

        intake.ejectHatch(secondController.getBumper(Hand.kRight));
    }

    void climbMode() {
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
            } else {
                driverMode = "score";
            }
        }
    }
}
