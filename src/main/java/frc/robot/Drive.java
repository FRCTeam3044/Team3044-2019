/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Drive {
    DifferentialDrive myDrive;
    Joystick first; //Not a good name

    public void DriveInit(){
        myDrive = Hardware.getInstance().myDrive;

       first = new Joystick(0);
    }

    public void DrivePeriodic(){
        myDrive.tankDrive(first.getRawAxis(1), first.getRawAxis(2), true); //These raw axis are wrong
    }
}
