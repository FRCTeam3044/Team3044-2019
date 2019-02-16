/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.reference.Hardware;
import frc.reference.secondControllerMap;

/**
 * Add your docs here.
 */
public class Drive {
    DifferentialDrive myDrive;
    XboxController firstController = Hardware.getInstance().firstController;
    secondControllerMap map = new secondControllerMap();

    public Drive() {
        myDrive = Hardware.getInstance().myDrive;
    }

    public void DrivePeriodic() {
        if (map.driverMode != "failure") {
            driveTheBot(firstController.getY(Hand.kLeft), firstController.getY(Hand.kRight));
        }
    }

    /**
     * Tank drive method that can be called anywhere. It calls upon the differential
     * drive platform.
     * 
     * @param leftInput  The robot left side's speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     * @param rightInput The robot right side's speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     */
    public void driveTheBot(double leftInput, double rightInput) {
        myDrive.tankDrive(leftInput, rightInput, true);
    }
}
