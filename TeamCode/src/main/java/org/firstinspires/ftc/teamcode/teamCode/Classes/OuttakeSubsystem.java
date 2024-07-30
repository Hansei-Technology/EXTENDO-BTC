package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

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
        claw.goToIntake();
        bar.goToReady();
        joint.goToReady();
    }

    public void goToIntake () {
        bar.goToIntake();
        joint.goToIntake();
    }

    public void goToPlace() {
        bar.goToDrop();
        joint.goToDrop();
    }

}
