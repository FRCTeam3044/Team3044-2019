/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Climb {
    TalonSRX climbArm1, climbArm2;
    TalonSRX climbWheels;
    DoubleSolenoid climbPiston1, climbPiston2;
    DoubleSolenoid lockPiston;

    public Climb() {
        climbArm1 = Hardware.getInstance().climbArm1;
        climbArm2 = Hardware.getInstance().climbArm2;
        climbWheels = Hardware.getInstance().climbWheels;
        climbPiston1 = Hardware.getInstance().climbPiston1;
        climbPiston2 = Hardware.getInstance().climbPiston2;
        lockPiston = Hardware.getInstance().lockPiston;
        
        lockPiston.set(Value.kForward);
    }

    void habPistonLift(int level) {
        switch (level) {
        case 0:
            retractBothPistons();
            break;

        case 1:
            if (getPistonLimitSwitch()) {
                pistonsOff();
                level = 3;
            } else {
                extendBothPistons();
            }
            break;

        case 2:
            extendBothPistons();
            break;

        case 3:
        default:
            pistonsOff();

        }
    }

    void extendBothPistons() {
        climbPiston1.set(Value.kForward);
        climbPiston2.set(Value.kForward);
    }

    void retractBothPistons() {
        climbPiston1.set(Value.kReverse);
        climbPiston2.set(Value.kReverse);
    }

    void pistonsOff() {
        climbPiston1.set(Value.kOff);
        climbPiston2.set(Value.kOff);
    }

    boolean getPistonLimitSwitch() {

        return true;
    }

    void unlock() {
        lockPiston.set(Value.kReverse);
    }

    void moveClimbingArm(double speed) {
        climbArm1.set(ControlMode.PercentOutput, speed);
        climbArm2.set(ControlMode.PercentOutput, speed); // Already inverted in hardware.java
    }

    void moveClimbingWheels(double speed) {
        climbWheels.set(ControlMode.PercentOutput, speed);
    }

}
