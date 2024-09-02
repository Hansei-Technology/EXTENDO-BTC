package org.firstinspires.ftc.teamcode.p2p;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.RoadRunner.Localizer;
import org.firstinspires.ftc.teamcode.Utils.PIDController;
import org.firstinspires.ftc.teamcode.Utils.PidCoeff;

public class DriveBase {
    public DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public PidCoeff xCoeff = new PidCoeff(0,0,0,0),
            yCoeff = new PidCoeff(0,0,0,0),
            turnCoeff = new PidCoeff(0,0,0,0);
    public PIDController xPID, yPID, turnPID;
    public Pose2d currentPose;
    public Pose2d targetPose;
    public Localizer localizer;
}
