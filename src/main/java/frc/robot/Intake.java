/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Intake {
    TalonSRX intakeArm1, intakeArm2;
    TalonSRX intakeWrist;
    TalonSRX cargoWheels;
    Solenoid hatchEject;

    public Intake() {
        intakeArm1 = Hardware.getInstance().intakeArm1;
        intakeArm2 = Hardware.getInstance().intakeArm2;
        intakeWrist = Hardware.getInstance().intakeWrist;
        cargoWheels = Hardware.getInstance().cargoWheels;
        hatchEject = Hardware.getInstance().hatchEject;
    }

    void ejectHatch(boolean button) {
        if (button) {
            hatchEject.set(true);
        } else {
            hatchEject.set(false);
        }
    }

    void spinCargoWheels(double speed) {
        cargoWheels.set(ControlMode.PercentOutput, speed);
    }

    void moveShoulder(double speed) {
        intakeArm1.set(ControlMode.PercentOutput, speed);
        intakeArm2.set(ControlMode.PercentOutput, speed); // Inverted in hardware.java
    }

    void moveWrist(double speed) {
        intakeWrist.set(ControlMode.PercentOutput, speed);
    }
}
