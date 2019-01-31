/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Climb {
    public TalonSRX climbArm1;
    public TalonSRX climbArm2;
    public TalonSRX climbWheels;

    public void ClimbInit(){
        climbArm1 = Hardware.getInstance().climbArm1;
        climbArm2 = Hardware.getInstance().climbArm2;
        climbWheels = Hardware.getInstance().climbWheels;
    }
}
