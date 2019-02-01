/* Creates and defines all necessary inputs on the second controller.
 * Starts by defining the controller and its buttons by giving each preset value a name to be used later.
 * After the buttons are set the joystick values are are created as variable inputs to be used later..
 * Finally, triggers and d-pad inputs are set as booleans (binary values).
 */

package frc.reference;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class SecondController {
	public static SecondController instance = null;

	// Names second controller as secondJoy.
	public Joystick secondJoy;

	// Assigns names to predetermined values that are the buttons and bumpers of the second controller.
	public static int BUTTON_X = 3;
	public static int BUTTON_Y = 4;
	public static int BUTTON_B = 2;
	public static int BUTTON_A = 1;

	public static int BUTTON_RB = 6;
	public static int BUTTON_LB = 5;
	public static int BUTTON_BACK = 7;
	public static int BUTTON_START = 8;

	public SecondController() {
		// Defines secondJoy as the second controller.
		secondJoy = new Joystick(1);
	}

	public static SecondController getInstance() {
		if (instance == null) {
			instance = new SecondController();
		}
		return instance;
	}

	// Pulls value of joystick from the X plane of the left stick.
	public double getLeftX() {
		return secondJoy.getRawAxis(0);
	}

	// Pulls value of joystick from the Y plane of the left stick.
	public double getLeftY() {
		return secondJoy.getRawAxis(1);
	}

	// Pulls value of the joystick from the X plane of the right stick.
	public double getRightX() {
		return secondJoy.getRawAxis(4);
	}

	// Pulls value of the joystick from the Y plane of the right stick.
	public double getRightY() {
		return secondJoy.getRawAxis(5);
	}

	// Sets binary value to the activation of the right trigger.
	public boolean getTriggerRight() {
		return Math.abs(secondJoy.getRawAxis(3)) > 0.1;
	}

	// Sets binary value to the activation of the left trigger.
	public boolean getTriggerLeft() {
		return Math.abs(secondJoy.getRawAxis(2)) > 0.1;
	}

	// Sets binary value to the activation of the left of the d-pad.
	public boolean getDPadLeft() {
		if (secondJoy.getPOV() == 270) {
			return true;
		} else {
			return false;
		}
	}

	// Sets binary value to tge activation of the right of the d-pad.
	public boolean getDPadRight() {
		if (secondJoy.getPOV() == 90) {
			return true;
		} else {
			return false;
		}
	}

	// Sets binary value to the activation of the top of the d-pad.
	public boolean getDPadUp() {
		if (secondJoy.getPOV() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// Sets binary value to the activation of the bottom of the d-pad.
	public boolean getDPadDown() {
		if (secondJoy.getPOV() == 180) {
			return true;
		} else {
			return false;
		}
	}

	// Sets binary value to the activation of the anywhere else on the d-pad.
	public boolean getDPadOther() {
		if (secondJoy.getPOV() == -1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getRawButton(int num) {
		if (!DriverStation.getInstance().isAutonomous()) {
			return secondJoy.getRawButton(num);

		} else {
			return false;
		}
	}
}