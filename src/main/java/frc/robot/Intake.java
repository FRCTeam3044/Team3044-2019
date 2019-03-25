/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.reference.ControllerMap;
import frc.reference.Hardware;
import frc.reference.TalonEncoderPIDSource;

/**
 * Add your docs here.
 */
public class Intake extends Hardware {
    private static Intake instance = null;

    AnalogInput potentiometer;
    double startingPosition;
    // double currentPosition;
    double kP, kI, kD;
    PIDController sholderPIDController;
    PIDController wristPIDController;
    TalonEncoderPIDSource wristPIDSource;
    double currentSetpoint;
    public static double startingEncoder;
    boolean PIDenabled = true;

    double CONVERSION; // Number of pot counts per x degrees rotation. May need to be a double.

    public String mode = "retract"; // retract, hatches, cargo
    String level; // ground, low, medium, feeder

    Boolean secondControllerExists;

    // All measurements in degrees.
    double SHOULDER_RETRACT = 0;
    double WRIST_RETRACT = 0;

    double SHOULDER_GROUND_HATCHES = 6.5;
    double SHOULDER_LOW_HATCHES = 6.5;
    double SHOULDER_MEDIUM_HATCHES = 80.5;
    double WRIST_GROUND_HATCHES = calcWristPosHatches(SHOULDER_GROUND_HATCHES);
    double WRIST_LOW_HATCHES = calcWristPosHatches(SHOULDER_LOW_HATCHES);
    double WRIST_MEDIUM_HATCHES = calcWristPosHatches(SHOULDER_MEDIUM_HATCHES);

    double SHOULDER_GROUND_CARGO;
    double SHOULDER_LOW_CARGO;
    double SHOULDER_MEDIUM_CARGO;
    double WRIST_GROUND_CARGO = calcWristPosCargo(SHOULDER_GROUND_CARGO);
    double WRIST_LOW_CARGO = calcWristPosCargo(SHOULDER_LOW_CARGO);
    double WRIST_MEDIUM_CARGO = calcWristPosCargo(SHOULDER_MEDIUM_CARGO);
    double SHOULDER_FEEDER_CARGO;
    double WRIST_FEEDER_CARGO = calcWristPosCargo(SHOULDER_FEEDER_CARGO);

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    public void IntakeInit() {
        mode = "retract";
        intakeWrist.setSelectedSensorPosition(0);
        potentiometer = new AnalogInput(3);
        startingPosition = potentiometer.getValue();
        startingEncoder = intakeWrist.getSensorCollection().getQuadraturePosition();
        System.out.println("start " + startingPosition);

        kP = 0.9;
        kI = 0;
        kD = 0;
        currentSetpoint = 2;
        wristPIDSource = new TalonEncoderPIDSource(intakeWrist, PIDSourceType.kDisplacement);
        sholderPIDController = new PIDController(kP, kI, kD, 0, potentiometer, intakeArm1);
        sholderPIDController.setInputRange(0, 5);
        sholderPIDController.setOutputRange(-.5, .05);

        wristPIDController = new PIDController(.0005, 0, kD, wristPIDSource, intakeWrist);
        wristPIDController.setOutputRange(-.6, .4);

    }

    public void IntakePeriodic() {
        // currentPosition = Double.valueOf(potentiometer.getValue());
        if (PIDenabled) {
            presetPositions();
        } else {
            moveShoulder(ControllerMap.getInstance().secondController.getY(Hand.kLeft));
            moveWrist(ControllerMap.getInstance().secondController.getY(Hand.kRight));
        }

        if (mode != "retract") {
            PIDenabled = true;
            secondControllerExists = true;
        }

        SmartDashboard.putString("DB/String 9", "setpoint: " + Double.toString(currentSetpoint));
    }

    void presetPositions() {
        if (mode != "retract") {
            sholderPIDController.enable();
            wristPIDController.enable();
            PIDenabled = true;
            currentSetpoint += .02 * ControllerMap.getInstance().secondController.getY(Hand.kLeft);
        }

        if (mode == "retract") { // B button
            // setPositions(SHOULDER_RETRACT, WRIST_RETRACT);
            sholderPIDController.disable();
            wristPIDController.disable();
            PIDenabled = false;

            // Actualy does cargo
        } else if (mode == "hatches") {
            if (level == "ground") { // A button
                currentSetpoint = 2;
                sholderPIDController.setSetpoint(calcShoulderPosition(currentSetpoint));
                wristPIDController.setSetpoint(calcWristPos(200));

                // setPositions(SHOULDER_GROUND_HATCHES, WRIST_GROUND_HATCHES);
            } else if (level == "low") { // X button
                sholderPIDController.setSetpoint(calcShoulderPosition(1.08));
                wristPIDController.setSetpoint(calcWristPos(6900));
                // setPositions(SHOULDER_LOW_HATCHES, WRIST_LOW_HATCHES);
            } else if (level == "medium") { // Y button
                sholderPIDController.setSetpoint(calcShoulderPosition(.1));
                wristPIDController.setSetpoint(calcWristPos(8400));
                // setPositions(SHOULDER_MEDIUM_HATCHES, WRIST_MEDIUM_HATCHES);
            }

            // Actualy does hatches
        } else if (mode == "cargo") {
            if (level == "ground") { // Down dpad
                sholderPIDController.setSetpoint(calcShoulderPosition(2));
                wristPIDController.setSetpoint(calcWristPos(200));
            } else if (level == "low") { // Left dpad
                sholderPIDController.setSetpoint(calcShoulderPosition(1.73));
                wristPIDController.setSetpoint(calcWristPos(800));
            } else if (level == "medium") { // Up dpad
                sholderPIDController.setSetpoint(calcShoulderPosition(1.4));
                wristPIDController.setSetpoint(calcWristPos(1300));
            } else if (level == "feeder") { // Right dpad

            }
        }
    }

    public void ejectHatch(boolean button) {
        if (button) {
            hatchEject.set(Value.kForward);
        } else {
            hatchEject.set(Value.kReverse);
        }
    }

    public void spinCargoWheels(double speed) {
        cargoWheels.set(ControlMode.PercentOutput, speed);
    }

    public void moveShoulder(double speed) {
        /*
         * if (((potentiometer.getValue()) - startingPosition) > 1700 && speed < 0) {
         * speed = 0; } if (((potentiometer.getValue()) - startingPosition) < 300 &&
         * speed > 0) { speed = 0; }
         */
        intakeArm1.set(ControlMode.PercentOutput, speed / 3);
    }

    public void moveWrist(double speed) {
        intakeWrist.set(ControlMode.PercentOutput, speed / 1.5);
    }

    double calcShoulderPosition(double setpoint) {
        return setpoint + .55; // TODO: Worlds set to 0
    }

    double calcWristPos(double setpoint) {
        return setpoint + startingEncoder;
    }

    void shoulderTo(double position) {

    }

    void wristTo(double position) {

    }

    void setPositions(double shoulderPosition, double wristPosition) {
        shoulderTo(degreesToPotCounts(shoulderPosition));
        wristTo(degreesToPotCounts(wristPosition));
    }

    double calcWristPosHatches(double armPositionDeg) {
        return armPositionDeg + 90;
    }

    double calcWristPosCargo(double armPositionDeg) {
        return armPositionDeg;
    }

    double degreesToPotCounts(double degrees) {
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

    public boolean getSecondControllerExistance() {
        return secondControllerExists;
    }

}
