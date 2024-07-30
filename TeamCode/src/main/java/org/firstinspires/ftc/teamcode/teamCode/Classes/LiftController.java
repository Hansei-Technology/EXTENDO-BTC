package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Utils.PIDController;

public class LiftController {
    DcMotorEx left;
    DcMotorEx right;
    //double kp = 0.02, kd = 0.01, ki = 0.002;
    double kp = 0, kd = 0, ki = 0;
    PIDController pidController = new PIDController(kp, kd, ki);
    public static double magicPOWER = -0.3;
    public int position;
    public static int MAX_POS = 1000;
    public static int LOW_POS = 300;
    public static int MID_POS = 700;
    boolean pidON = true;

    public enum States {
        RETRACT_PID,
        RETRACT_MAGIC,
        EXTENDED,
        RETRACTED
    }

    public States currentState = States.EXTENDED;

    ElapsedTime timer;
    int time_to_wait;

    public LiftController (HardwareMap map) {
        left = map.get(DcMotorEx.class, "");
        right = map.get(DcMotorEx.class, "");

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        left.setCurrentAlert(4, CurrentUnit.AMPS);
        right.setCurrentAlert(4, CurrentUnit.AMPS);

        pidController.targetValue = 0;

        timer = new ElapsedTime();
    }

    public void setPower(double power) {
        if(power != 0) pidON = false;
        if(position < MAX_POS - 50 && power > 0) {
            left.setPower(power);
            right.setPower(power);
            currentState = States.EXTENDED;
        }
        if(position > 10 && power < 0) left.setPower(power);
    }


    public void goDown() {
        pidON = true;
        pidController.targetValue = 0;
        currentState = States.RETRACT_PID;
    }

    public void goDownTillMotorOverCurrent() {
        left.setPower(magicPOWER);
        right.setPower(magicPOWER);
        currentState = States.RETRACT_MAGIC;
    }

    public void update() {

        switch (currentState)
        {
            case RETRACT_PID:
            {
                pidON = true;
                if(position <= 0) currentState = States.RETRACTED;
                break;
            }
            case RETRACT_MAGIC:
            {
                if(left.isOverCurrent())
                {
                    ResetEncoders();
                    pidController.targetValue = 0;
                    pidON = true;
                    currentState = States.RETRACTED;
                }
                break;
            }
        }
        if(pidON)
        {
            position = getPosition();
            //pidController.targetValue = rowPosition[targetRow];
            double powerExtendo = pidController.update(position);
            left.setPower(powerExtendo);
            right.setPower(powerExtendo);
        }
    }

    int getPosition() {
        return left.getCurrentPosition();
    }

    public void ResetEncoders() {
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
