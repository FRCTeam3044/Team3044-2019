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
import frc.reference.ControllerMap;
import frc.reference.Hardware;
import frc.reference.TalonEncoderPIDSource;

/**
 * Add your docs here.
 */
public class Intake extends Hardware {
    private static Intake instance = null;

    AnalogInput potentiometer;
    double kP_shoulder, kI_shoulder, kD_shoulder;
    double kP_wrist, kI_wrist, kD_wrist;
    PIDController shoulderPIDController;
    PIDController wristPIDController;
    TalonEncoderPIDSource wristPIDSource;
    public static double startingWristEncoderPosition;
    boolean PIDenabled = false;
    double shoulderSetpoint;
    double wristSetpoint;

    double CONVERSION; // Number of pot counts per x degrees rotation. May need to be a double.

    Boolean secondControllerExists = false;

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
        intakeWrist.setSelectedSensorPosition(0);
        potentiometer = new AnalogInput(3);
        startingWristEncoderPosition = intakeWrist.getSensorCollection().getQuadraturePosition();

        kP_shoulder = 1;
        kI_shoulder = 0;
        kD_shoulder = 0;
        shoulderPIDController = new PIDController(kP_shoulder, kI_shoulder, kD_shoulder, potentiometer, intakeArm1);
        shoulderPIDController.setInputRange(1.5, 4);
        shoulderPIDController.setOutputRange(-.5, .05); // Up, down

        kP_wrist = .0005;
        kI_wrist = 0;
        kD_wrist = 0;
        wristPIDSource = new TalonEncoderPIDSource(intakeWrist, PIDSourceType.kDisplacement);
        wristPIDController = new PIDController(kP_wrist, kI_wrist, kD_wrist, wristPIDSource, intakeWrist);
        wristPIDController.setOutputRange(-.7, .45); // Up, down

    }

    public void IntakePeriodic() {
        if (PIDenabled) {
            setBothSetpoints();

            if (Math.abs(ControllerMap.getInstance().secondController.getY(Hand.kLeft)) > .2) {
                shoulderSetpoint += .01 * ControllerMap.getInstance().secondController.getY(Hand.kLeft);
            }
            if (Math.abs(ControllerMap.getInstance().secondController.getY(Hand.kRight)) > .2) {
                wristSetpoint += 27 * ControllerMap.getInstance().secondController.getY(Hand.kRight);
            }
        } else {
            moveShoulder(ControllerMap.getInstance().secondController.getY(Hand.kLeft));
            moveWrist(ControllerMap.getInstance().secondController.getY(Hand.kRight));
        }

    }

    public void setManualControl() {
        shoulderPIDController.disable();
        wristPIDController.disable();
        PIDenabled = false;
    }

    public void setRetracted() {
        shoulderSetpoint = 2;
        wristSetpoint = 200;
        enablePID();
    }

    public void setCargoGroundPickup() {
        shoulderSetpoint = 1.00;
        wristSetpoint = 6900;
        enablePID();
    }

    public void setCargoScoreInCargoShip() {
        shoulderSetpoint = -.1;
        wristSetpoint = 8400;
        enablePID();
    }

    public void setCargoScoreRocketLevel1() {
        shoulderSetpoint = 0.4;
        wristSetpoint = 7300;
        enablePID();
    }

    public void setCargoScoreRocketLevel2() {
        shoulderSetpoint = -.3;
        wristSetpoint = 7800;
        enablePID();
    }

    public void setHatchLevel1() {
        shoulderSetpoint = 1.70;
        wristSetpoint = 800;
        enablePID();
    }

    void enablePID() {
        shoulderPIDController.enable();
        wristPIDController.enable();
        setBothSetpoints();
        PIDenabled = true;
        secondControllerExists = true;
    }

    void setBothSetpoints() {
        shoulderPIDController.setSetpoint(calcShoulderPosition(shoulderSetpoint));
        wristPIDController.setSetpoint(calcWristPos(wristSetpoint));
    }

    public void resetWristEncoderWithMath() {
        startingWristEncoderPosition = intakeWrist.getSensorCollection().getQuadraturePosition();
    }

    public double getCorrectedWristEncoderValue() {
        return intakeWrist.getSensorCollection().getQuadraturePosition() - startingWristEncoderPosition - 200;
    }

    public void grabHatch() {
        hatchEject.set(Value.kForward);
    }

    public void releaseHatch() {
        hatchEject.set(Value.kReverse);
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
        return setpoint + 2; // TODO: Worlds set to 0
    }

    double calcWristPos(double setpoint) {
        return setpoint + startingWristEncoderPosition;
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

    public boolean getSecondControllerExistence() {
        return secondControllerExists;
    }

}
