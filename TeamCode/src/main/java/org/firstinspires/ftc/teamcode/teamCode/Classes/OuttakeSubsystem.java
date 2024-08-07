package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class OuttakeSubsystem {
    public Outtake4Bar bar;
    public OuttakeJoint joint;
    public OuttakeRotation rotation;
    public ClawController claw;
    public ElapsedTime timer;

    public OuttakeSubsystem (HardwareMap map) {
        bar = new Outtake4Bar(map);
        joint = new OuttakeJoint(map);
        rotation = new OuttakeRotation(map);
        claw = new ClawController(map);
        timer = new ElapsedTime();
    }

    public void goToReady () {
        rotation.goToLevel();
        bar.goToReady();
        joint.goToReady();
    }

    public void goToArrange() {
        rotation.goToLevel();
        joint.goToPlaceAuto();
        bar.goToDrop();
        claw.goToArrange();
    }
    public void goToIntake () {
        bar.goToIntake();
        joint.goToIntake();
    }

    public void goToPlace() {
        bar.goToDrop();
        joint.goToDrop();
        rotation.goToLevel();
    }
    public void goToMoving() {
        rotation.goToLevel();
        bar.goingToPizdaMati();
        joint.goToReady();
    }

    public void goToPreloads() {
        bar.goToPreloads();
        joint.goToDrop();
        rotation.goToLevel();
    }

}
