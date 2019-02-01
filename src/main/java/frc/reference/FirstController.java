/* Creates and defines all necessary inputs on the first controller.
 * Starts by defining the controller and its buttons by giving each preset value a name to be used later.
 * After the buttons are set the triggers and d-pad inputs are set as booleans (binary values).
 */

package frc.reference;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class FirstController {
	private static FirstController instance = null;

	private Joystick firstJoy;

	// Sets names to the preset values on the controller that are the buttons and bumpers.
	public static int BUTTON_X = 3;
	public static int BUTTON_Y = 4;
	public static int BUTTON_B = 2;
	public static int BUTTON_A = 1;

	public static int BUTTON_RB = 6;
	public static int BUTTON_LB = 5;
	public static int BUTTON_BACK = 7;
	public static int BUTTON_START = 8;

	// Defines first controller as firstjoy.
	private FirstController() {
		firstJoy = new Joystick(0);
	}

	public static FirstController getInstance() {
		if (instance == null) {
			instance = new FirstController();
		}
		return instance;
	}

	// Pulls value of joystick on the X plane of left stick.
	public double getLeftX() {
		return firstJoy.getRawAxis(0);
	}

	// Pulls value of joystick on the Y plane of the left stick.
	public double getLeftY() {
		return firstJoy.getRawAxis(1);
	}

	// Pulls value of the joystick on the X plane of the right stick.
	public double getRightX() {
		return firstJoy.getRawAxis(4);
	}

	// Pulls value of the joystick on the y plane of the right stick.
	public double getRightY() {
		return firstJoy.getRawAxis(5);
	}

	// Creates a binary value based on input from the right trigger.
	public boolean getTriggerRight() {
		return Math.abs(firstJoy.getRawAxis(3)) > 0.1;
	}

	// Creates a binary value based on the input from the left trigger.
	public boolean getTriggerLeft() {
		return Math.abs(firstJoy.getRawAxis(2)) > 0.1;
	}

	// Creates a binary value based on the input from the left of the d-pad.
	public boolean getDPadLeft() {
		if (firstJoy.getPOV() == 270) {
			return true;
		} else {
			return false;
		}
	}

	// Creates a binary value based on the input from the right of the d-pad.
	public boolean getDPadRight() {
		if (firstJoy.getPOV() == 90) {
			return true;
		} else {
			return false;
		}
	}

	// Creates a binary value based on the input from the top of the d-pad.
	public boolean getDPadUp() {
		if (firstJoy.getPOV() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// Creates a binary value based on the input from the bottom of the d-pad.
	public boolean getDPadDown() {
		if (firstJoy.getPOV() == 180) {
			return true;
		} else {
			return false;
		}
	}

	// Creates a binary value based on the input from anywhere on the d-pad except the 4 main directions.
	public boolean getDPadOther() {
		if (firstJoy.getPOV() == -1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getRawButton(int num) {
		if (!DriverStation.getInstance().isAutonomous()) {
			return firstJoy.getRawButton(num);

		} else {
			return false;
		}
	}
}