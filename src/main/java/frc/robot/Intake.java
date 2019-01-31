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
public class Intake {
    TalonSRX intakeArm1;
    TalonSRX intakeArm2;
    TalonSRX intakeWrist;
    TalonSRX intakeWheels;

    public void IntakeInit(){
        intakeArm1 = Hardware.getInstance().intakeArm1;
        intakeArm2 = Hardware.getInstance().intakeArm2;
        intakeWrist = Hardware.getInstance().intakeWrist;
        intakeWheels = Hardware.getInstance().intakeWheels;
    }
}
