package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class IntakeController {
    DcMotorEx motor;
    public static double speed1 = 1, speed2 = -1, speed3 = -0.7;
    public IntakeController(HardwareMap map) {
        motor = map.get(DcMotorEx.class, "m3");

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void turnOn() {
        motor.setPower(speed1);
    }

    public void turnOff() {
        motor.setPower(0);
    }

    public void reverse() {
        motor.setPower(speed2);
    }

    public void reversePreload(){
        motor.setPower(speed3);
    }
    public void reverseAuto() {
        motor.setPower(speed3);
    }

}
