package org.firstinspires.ftc.teamcode.Utils;

public class PidCoeff {
    public double kP, kI, kD, kF;

    public PidCoeff(double kp, double ki, double kd, double kf) {
        kP = kp;
        kI = ki;
        kD = kd;
        kF = kf;
    }
}
