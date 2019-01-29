/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.reference;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Add your docs here.
 */
public class Hardware {

    private static Hardware instance = null;

    // Drive
    WPI_TalonSRX leftFrontDrive;
    WPI_TalonSRX rightFrontDrive;
    WPI_TalonSRX leftBackDrive;
    WPI_TalonSRX rightBackDrive;

    // Used to call the provided tank drive.
    public DifferentialDrive myDrive;

    public static Hardware getInstance() {
        if (instance == null) {
            instance = new Hardware();
        }
        return instance;
    }

    public void init() {
        leftFrontDrive = new WPI_TalonSRX(0);
        rightFrontDrive = new WPI_TalonSRX(2);
        leftBackDrive = new WPI_TalonSRX(1);
        rightBackDrive = new WPI_TalonSRX(3);

        // Sets the back motors to follow the front motors.
        leftBackDrive.follow(leftFrontDrive);
        rightBackDrive.follow(rightFrontDrive);

        // Uses front talons to define motors used in WPI tank drive, the back
        // motors move because of the follower.
        myDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);

    }
}
