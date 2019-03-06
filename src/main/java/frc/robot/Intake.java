/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.reference.Hardware;
import frc.reference.TalonEncoderPIDSource;

/**
 * Add your docs here.
 */
public class Intake extends Hardware {
    private static Intake instance = null;

    double CONVERSION = 1; // TODO Number of pot counts per x degrees rotation. May need to be a double.

    String mode; // retract, hatches, cargo
    String level; // ground, low, medium, feeder

    // All measurements in degrees.
    double SHOULDER_RETRACT = 0;
    double WRIST_RETRACT = 0;

    double SHOULDER_GROUND_HATCHES = 150;
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

    static TalonEncoderPIDSource wristPIDSource;
    static Potentiometer pot;
    AnalogInput ai;

    double kP, kI, kD;
    PIDController sholderPIDController;
    PIDController wristPIDController;

    public Intake() {
        /*
         * intakeArm1 = Hardware.getInstance().intakeArm1; intakeArm2 =
         * Hardware.getInstance().intakeArm2; intakeWrist =
         * Hardware.getInstance().intakeWrist;
         * 
         * cargoWheels = Hardware.getInstance().cargoWheels; hatchEject =
         * Hardware.getInstance().hatchEject;
         */
        AnalogInput ai = new AnalogInput(3);
        pot = new AnalogPotentiometer(ai, 270, 0);
        kP = 1;
        kI = 1;
        kD = 1;
        wristPIDSource = new TalonEncoderPIDSource(intakeWrist, PIDSourceType.kDisplacement);
        sholderPIDController = new PIDController(kP, kI, kD, ai, intakeArm1);
        sholderPIDController.setInputRange(0, 4000);

        // wristPIDController = new PIDController(kP, kI, kD, wristPIDSource,
        // intakeWrist);
    }

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    public void IntakeInit() {
        sholderPIDController.enable();
    }

    public void IntakePeriodic() {
        System.out.println(sholderPIDController.isEnabled());
        System.out.println(sholderPIDController.getSetpoint());
        sholderPIDController.setSetpoint(1000);
        // presetPositions();
        /*
         * SmartDashboard.putString("DB/String 0", String.valueOf(pot.get()));
         * SmartDashboard.putString("DB/String 1",
         * String.valueOf(Hardware.intakeWrist.getSensorCollection().
         * getQuadraturePosition()));
         */
    }

    void presetPositions() {

        /*
         * if (pot.get() < 150) { intakeArm1.set(ControlMode.PercentOutput, 0.4); }
         */
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

    public void moveShoulder(double speed) {
        intakeArm1.set(ControlMode.PercentOutput, speed / 3);
        intakeArm2.set(ControlMode.PercentOutput, speed / 3); // Inverted in hardware.java
    }

    public void moveWrist(double speed) {
        intakeWrist.set(ControlMode.PercentOutput, speed / 1.5);
    }

    void shoulderTo(double position) {
        sholderPIDController.setSetpoint(150);
    }

    void wristTo(double position) {
        // wristPIDController.setSetpoint(position);
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

}
