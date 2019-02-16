/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.reference.Hardware;
import frc.reference.secondControllerMap;

/**
 * Add your docs here.
 */
public class Drive {
    DifferentialDrive myDrive;
    WPI_TalonSRX leftBackDrive;
    double P, I, D;
    double error, previous_error, setpoint, integral, derivative, pidout;
    XboxController firstController = Hardware.getInstance().firstController;
    secondControllerMap map = new secondControllerMap();

    public Drive() {
        myDrive = Hardware.getInstance().myDrive;
        firstController = FirstController.getInstance();
        leftBackDrive = Hardware.getInstance().leftBackDrive;
        leftBackDrive.setSelectedSensorPosition(0);
        P = 1;
        I = 0.1;
        D = 0;
        error = 0;
        previous_error = 0;
        setpoint = 0;
        integral = 0;
        derivative = 0;
        pidout = 0;
    }

    public void PID(){
        error = setpoint - leftBackDrive.getSelectedSensorVelocity()/1000;
        integral += (error*.02);
        derivative = (error - previous_error) / .02;
        pidout = P*error + I*integral + D*derivative;
        pidout = Math.min(pidout, 0.4);
        pidout = pidout*-1;
    }

    public void DrivePeriodic() {
        PID();
        if (map.driverMode != "failure") {
          setpoint = firstController.getY(Hand.kLeft);
        } else {
          setpoint = 0; 
        }
        driveTheBot(pidout, pidout);
        //driveTheBot(firstController.getLeftY(), firstController.getRightY());
        if (firstController.getRawButton(4)) {
            leftBackDrive.setSelectedSensorPosition(0);
            integral = 0;
            previous_error = 0;
            setpoint = 0;
        }
        SmartDashboard.putString("DB/String 0", "Encoders: " + leftBackDrive.getSelectedSensorVelocity());
        SmartDashboard.putString("DB/String 1", "PID Output: " + pidout);
        SmartDashboard.putString("DB/String 2", "Setpoint: " + setpoint);
        SmartDashboard.putString("DB/String 3", "Proportional: " + P*error);
        SmartDashboard.putString("DB/String 4", "Integral: " + I*integral);
    }

    /**
     * Tank drive method that can be called anywhere. It calls upon the differential
     * drive platform.
     * 
     * @param leftInput  The robot left side's speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     * @param rightInput The robot right side's speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     */
    public void driveTheBot(double leftInput, double rightInput) {
        myDrive.tankDrive(leftInput, rightInput, true);
    }
}
