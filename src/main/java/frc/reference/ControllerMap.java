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

    Rumble rumbleFirst = new Rumble(firstController);
    Rumble rumbleSecond = new Rumble(secondController);

    public static String driverMode; // score, climb, failureFirst, failureSecond

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
    }

    public void controllerMapPeriodic() {
        rumbleFirst.rumblePeriodic();
        rumbleSecond.rumblePeriodic();
        // rumbleFirst.matchTimer(); //TODO: Work in progress

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
            drive.driveTheBot(firstController.getY(Hand.kLeft), firstController.getY(Hand.kRight));
        }

        if (firstController.getAButton()) {
            intake.resetWristEncoderWithMath();
            rumbleSecond.rumble(.5);
        }

    }

    void scoreMode() {
        intake.IntakePeriodic();

        // Actually does cargo.
        if (secondController.getYButtonPressed()) {
            intake.setCargoScoreInCargoShip();
        }
        if (secondController.getXButtonPressed()) {
            intake.setCargoGroundPickup();
        }
        if (secondController.getAButtonPressed()) {
            intake.setRetracted();
        }
        if (secondController.getBButtonPressed()) {
            intake.setManualControl();
        }

        // Actually does hatches and cargo.
        if (secondController.getPOV() == 0 && intake.getSecondControllerExistence()) {// up d-pad
            intake.setCargoScoreRocketLevel2();
        }
        if (secondController.getPOV() == 90) {// right d-pad
            intake.setCargoScoreRocketLevel1();
        }
        if (secondController.getPOV() == 180) {// down d-pad
            intake.setRetracted();
        }
        if (secondController.getPOV() == 270) {// left d-pad
            intake.setHatchLevel1();
        }

        if (secondController.getBumper(Hand.kLeft)) {
            intake.spinCargoWheels(-1);
        } else if (secondController.getTriggerAxis(Hand.kLeft) > .1) {
            intake.spinCargoWheels(1);
        } else {
            intake.spinCargoWheels(-.05);
        }

        if (secondController.getBumper(Hand.kRight)) {
            intake.grabHatch();
            rumbleFirst.rumble(.5);
        }
        if (secondController.getTriggerAxis(Hand.kRight) > .1) {
            intake.releaseHatch();
            rumbleFirst.rumble(.5);
        }
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
        if (secondController.getBumperPressed(Hand.kRight)) {
            climb.habPistonLift(0);
        }
        if (secondController.getAButtonPressed()) {
            intake.setRetracted();
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
        if (controller.getAButtonPressed()) {
            intake.setRetracted();
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
            // driverMode = "failureSecond";
        } else if (firstController.getBackButton() && firstController.getStartButton()) {
            // driverMode = "failureFirst";
        } else if (secondController.getBackButtonPressed()) {
            if (driverMode == "score") {
                driverMode = "climb";
            } else if (driverMode == "climb") {
                driverMode = "score";
            }
        }
    }

}
