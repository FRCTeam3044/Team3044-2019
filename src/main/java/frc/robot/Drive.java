/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.reference.FirstController;
import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Drive {
    DifferentialDrive myDrive;
    FirstController firstController;

    public void DriveInit() {
        myDrive = Hardware.getInstance().myDrive;
        firstController = FirstController.getInstance();
    }

    public void DrivePeriodic() {
        driveTheBot(firstController.getLeftY(), firstController.getRightY()); // These raw axis are possibly correct
    }

    /**
     * Tank drive method that can be called anywhere. It calls upon the
     * differential drive platform.
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
