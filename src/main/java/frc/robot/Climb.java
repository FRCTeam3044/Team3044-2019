/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.reference.Hardware;

/**
 * Add your docs here.
 */
public class Climb extends Hardware {
    private static Climb instance = null;

    boolean pistonsExtended;
    int RETRACT;
    int LEVEL_TWO;
    int LEVEL_TWO_MOD;
    int LEVEL_THREE;
    int LEVEL_THREE_MOD;

    // public Climb() {
    // }

    public static Climb getInstance() {
        if (instance == null) {
            instance = new Climb();
        }
        return instance;
    }

    public void ClimbPeriodic() {

    }

    public void habPistonLift(int level) {
        switch (level) {
        case 0:
            retractBothPistons();
            break;

        case 2:
            if (getPistonLimitSwitch()) {
                pistonsOff();
                level = 4;
            } else {
                extendBothPistons();
            }
            break;

        case 3:
            extendBothPistons();
            break;

        case 4:
        default:
            pistonsOff();

        }
    }

    void extendBothPistons() {
        climbPiston1.set(Value.kForward);
        climbPiston2.set(Value.kForward);
        pistonsExtended = true;
    }

    public void retractBothPistons() {
        climbPiston1.set(Value.kReverse);
        climbPiston2.set(Value.kReverse);
        pistonsExtended = false;
    }

    void pistonsOff() {
        climbPiston1.set(Value.kOff);
        climbPiston2.set(Value.kOff);
    }

    boolean getPistonLimitSwitch() {

        return true;
    }

    public void moveClimbingArm(double speed) {
        climbArm1.set(ControlMode.PercentOutput, speed / 2.5);
        climbArm2.set(ControlMode.PercentOutput, speed / 2.5); // Already inverted in hardware.java
    }

    public void moveClimbingWheels(double speed) {
        climbWheels.set(ControlMode.PercentOutput, speed);
    }

    void armTo(int position) {

    }

    public void retract() {
        armTo(RETRACT);
    }

    public void levelTwo() {
        if (!pistonsExtended) {
            armTo(LEVEL_TWO);
        } else {
            armTo(LEVEL_TWO_MOD);
        }
    }

    public void levelThree() {
        if (!pistonsExtended) {
            armTo(LEVEL_THREE);
        } else {
            armTo(LEVEL_THREE_MOD);
        }
    }

}
