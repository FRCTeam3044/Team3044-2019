/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.reference;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

/**
 * Add your docs here.
 */
public class Rumble {
    XboxController selectedController;
    Timer rumbleTime = new Timer();
    double rumbleLength;
    double rumbleStarted;

    public Rumble(XboxController selectedController) {
        this.selectedController = selectedController;
    }

    public void rumble(double seconds) {
        rumbleLength = seconds;
        rumbleTime.start();
        rumbleStarted = rumbleTime.get();
    }

    void rumblePeriodic() {
        if (rumbleTime.get() - rumbleStarted < rumbleLength) {
            selectedController.setRumble(RumbleType.kLeftRumble, 1);
            selectedController.setRumble(RumbleType.kRightRumble, 1);

        } else {
            selectedController.setRumble(RumbleType.kLeftRumble, 0);
            selectedController.setRumble(RumbleType.kRightRumble, 0);
        }
    }

    public void matchTimer() {
        if (rumbleTime.getMatchTime() < 10 && rumbleTime.getMatchTime() > 5) {
            if ((int) rumbleTime.getMatchTime() % 2 == 0) {
                selectedController.setRumble(RumbleType.kLeftRumble, 1);
                selectedController.setRumble(RumbleType.kRightRumble, 0);
            } else {
                selectedController.setRumble(RumbleType.kRightRumble, 1);
                selectedController.setRumble(RumbleType.kLeftRumble, 0);
            }
        }
    }
}
