package org.firstinspires.ftc.teamcode.teamCode.Classes;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Utils.PIDController;
import org.firstinspires.ftc.teamcode.Utils.SimplePIDController;

@Config
public class ExtendoController {
    public PIDController extendoPID;
    public DcMotorEx motor;
    public static double kp=0.03, ki=0, kd=0.04;
    public enum extendoStatus {
        INITIALIZE,
        RETRACTED,
        EXTENDED,
        DRIVE,
        TRANSFER
    }
    public static double maxSpeed = 1;
    public static double driveSpeed = 0.6;
    public extendoStatus CS = extendoStatus.INITIALIZE, PS = extendoStatus.INITIALIZE;
    public static double currentPosition = 0;
    public static double maxExtension = 1400;
    public static double retracted = -10;
    public static double transfer = -50;
    public static double drive = 0;
    public static double powerCap = 1;

    public double power;
    public ExtendoController(HardwareMap hardwareMap){
        motor = hardwareMap.get(DcMotorEx.class, "m2e");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        extendoPID = new PIDController(kp, ki, kd);
        extendoPID.targetValue = retracted;
        extendoPID.maxOutput = maxSpeed;
        currentPosition = retracted;
    }

    public void update(double voltage)
    {
        if(CS != PS) {
            switch (CS) {
                case INITIALIZE:
                    extendoPID.targetValue = retracted;
                    extendoPID.maxOutput = maxSpeed;
                    break;
                case EXTENDED:
                    extendoPID.targetValue = maxExtension;
                    extendoPID.maxOutput = maxSpeed;
                    break;
                case RETRACTED:
                    extendoPID.targetValue = retracted;
                    extendoPID.maxOutput = maxSpeed;
                    break;
                case DRIVE:
                    extendoPID.targetValue = drive;
                    extendoPID.maxOutput = driveSpeed;
                    break;
                case TRANSFER:
                    extendoPID.targetValue = transfer;
                    extendoPID.maxOutput = maxSpeed;
                    break;
            }
        }
        currentPosition = motor.getCurrentPosition();
        power = extendoPID.update(currentPosition);
        power = Math.max(-powerCap, Math.min(power * 14 / voltage, powerCap));
        motor.setPower(power);
        PS = CS;
    }


}
