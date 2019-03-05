/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.reference;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Add your docs here.
 */
public class Hardware {

    private static Hardware instance = null;

    public static Compressor compressor = new Compressor();

    // Drive
    public static WPI_TalonSRX leftFrontDrive, rightFrontDrive, leftBackDrive, rightBackDrive;

    // Used to call the provided tank drive.
    public static DifferentialDrive myDrive;

    // These may need to be changed from TalonSRX to WPI_TalonSRX
    // Intake
    public static WPI_TalonSRX intakeArm1, intakeArm2;
    public static WPI_TalonSRX intakeWrist;
    public static TalonSRX cargoWheels;
    public static Solenoid hatchEject;

    // Climb
    public static WPI_TalonSRX climbArm1, climbArm2;
    public static TalonSRX climbWheels;
    public static DoubleSolenoid climbPiston1, climbPiston2;
    public static DoubleSolenoid lockPiston;

    public static Hardware getInstance() {
        if (instance == null) {
            instance = new Hardware();
        }
        return instance;
    }

    public void init() {
        compressor.setClosedLoopControl(true);
        // compressor.stop(); //Use this to stop the compressor.
        // time = DriverStation.getInstance().getMatchTime(); //Maybe stop
        // compressor in last 15 seconds.

        leftFrontDrive = new WPI_TalonSRX(0);
        rightFrontDrive = new WPI_TalonSRX(2);
        leftBackDrive = new WPI_TalonSRX(1);
        rightBackDrive = new WPI_TalonSRX(3);

        // Sets the back motors to follow the front motors.
        leftBackDrive.follow(leftFrontDrive);
        rightBackDrive.follow(rightFrontDrive);

        // Uses front talons to define motors used in WPI tank drive, the back
        // motors move because of the follower.
        myDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);
        leftBackDrive.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 10);
        rightBackDrive.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 10);

        // Intake
        intakeArm1 = new WPI_TalonSRX(4);
        intakeArm2 = new WPI_TalonSRX(5);
        intakeWrist = new WPI_TalonSRX(6);
        cargoWheels = new TalonSRX(7);
        hatchEject = new Solenoid(6);

        intakeArm2.setInverted(true);

        // Climb
        climbArm1 = new WPI_TalonSRX(8);
        climbArm2 = new WPI_TalonSRX(9);
        climbWheels = new TalonSRX(10);
        climbPiston1 = new DoubleSolenoid(0, 1);
        climbPiston2 = new DoubleSolenoid(2, 3);
        lockPiston = new DoubleSolenoid(4, 5);

        climbArm2.setInverted(true);
        
        // This is how you instantiate the custom PIDSource 
        TalonEncoderPIDSource pidSource = new TalonEncoderPIDSource(leftFrontDrive, 
                PIDSourceType.kRate); // Set the 2nd Parameter (PIDSourceType) appropriately 


    }
}
