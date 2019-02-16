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

    int CONVERSION; // Number of pot counts per x degrees rotation. May need to be a double.

    String mode; // retract, hatches, cargo
    String level; // ground, low, medium, feeder

    // All measurements in degrees.
    int SHOULDER_RETRACT = 0;
    int WRIST_RETRACT = 0;

    int SHOULDER_GROUND_HATCHES;
    int SHOULDER_LOW_HATCHES;
    int SHOULDER_MEDIUM_HATCHES;
    int WRIST_GROUND_HATCHES = calcWristPosHatches(SHOULDER_GROUND_HATCHES);
    int WRIST_LOW_HATCHES = calcWristPosHatches(SHOULDER_LOW_HATCHES);
    int WRIST_MEDIUM_HATCHES = calcWristPosHatches(SHOULDER_MEDIUM_HATCHES);

    int SHOULDER_GROUND_CARGO;
    int SHOULDER_LOW_CARGO;
    int SHOULDER_MEDIUM_CARGO;
    int WRIST_GROUND_CARGO = calcWristPosCargo(SHOULDER_GROUND_CARGO);
    int WRIST_LOW_CARGO = calcWristPosCargo(SHOULDER_LOW_CARGO);
    int WRIST_MEDIUM_CARGO = calcWristPosCargo(SHOULDER_MEDIUM_CARGO);
    int SHOULDER_FEEDER_CARGO;
    int WRIST_FEEDER_CARGO = calcWristPosCargo(SHOULDER_FEEDER_CARGO);

    public Intake() {
        intakeArm1 = Hardware.getInstance().intakeArm1;
        intakeArm2 = Hardware.getInstance().intakeArm2;
        intakeWrist = Hardware.getInstance().intakeWrist;
        cargoWheels = Hardware.getInstance().cargoWheels;
        hatchEject = Hardware.getInstance().hatchEject;
    }

    void presetPositions() {
        if (mode == "retract") {
            setPositions(SHOULDER_RETRACT, WRIST_RETRACT);

        } else if (mode == "hatches") {
            if (level == "ground") {
                setPositions(SHOULDER_GROUND_HATCHES, WRIST_GROUND_HATCHES);
            } else if (level == "low") {
                setPositions(SHOULDER_LOW_HATCHES, WRIST_LOW_HATCHES);
            } else if (level == "medium") {
                setPositions(SHOULDER_MEDIUM_HATCHES, WRIST_MEDIUM_HATCHES);
            }

        } else if (mode == "cargo") {
            if (level == "ground") {
                setPositions(SHOULDER_GROUND_CARGO, WRIST_GROUND_CARGO);
            } else if (level == "low") {
                setPositions(SHOULDER_LOW_CARGO, WRIST_LOW_CARGO);
            } else if (level == "medium") {
                setPositions(SHOULDER_MEDIUM_CARGO, WRIST_MEDIUM_CARGO);
            } else if (level == "feeder") {
                setPositions(SHOULDER_FEEDER_CARGO, WRIST_FEEDER_CARGO);
            }
        }
    }

    public void ejectHatch(boolean button) {
        if (button) {
            hatchEject.set(true);
        } else {
            hatchEject.set(false);
        }
    }

    public void spinCargoWheels(double speed) {
        cargoWheels.set(ControlMode.PercentOutput, speed);
    }

    void moveShoulder(double speed) {
        intakeArm1.set(ControlMode.PercentOutput, speed);
        intakeArm2.set(ControlMode.PercentOutput, speed); // Inverted in hardware.java
    }

    void moveWrist(double speed) {
        intakeWrist.set(ControlMode.PercentOutput, speed);
    }

    void shoulderTo(int position) {

    }

    void wristTo(int position) {

    }

    void setPositions(int shoulderPosition, int wristPosition) {
        shoulderTo(degreesToPotCounts(shoulderPosition));
        wristTo(degreesToPotCounts(wristPosition));
    }

    int calcWristPosHatches(int armPositionDeg) {
        return armPositionDeg + 90;
    }

    int calcWristPosCargo(int armPositionDeg) {
        return armPositionDeg;
    }

    int degreesToPotCounts(int degrees) {
        return CONVERSION * degrees;
    }

    public void retractMode() {
        mode = "retract";
    }

    public void hatchMode() {
        mode = "hatches";
    }

    public void cargoMode() {
        mode = "cargo";
    }

    public void goGround() {
        level = "ground";
    }

    public void goLow() {
        level = "low";
    }

    public void goMedium() {
        level = "medium";
    }

    public void goFeeder() {
        level = "feeder";
    }

}
