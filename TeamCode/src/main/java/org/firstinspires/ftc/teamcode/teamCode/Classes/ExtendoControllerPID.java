package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Utils.PIDController;

public class ExtendoControllerPID {
    DcMotorEx extendo;
    //double kp = 0.02, kd = 0.01, ki = 0.002;
    double kp = 0, kd = 0, ki = 0;
    PIDController pidController = new PIDController(kp, kd, ki);
    double magicPOWER = 0.3;
    public int position;
    public static int MAX_POS = 1000;
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

    public ExtendoControllerPID(HardwareMap map) {
        extendo = map.get(DcMotorEx.class, "Extendo");

        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extendo.setCurrentAlert(4, CurrentUnit.AMPS);

        pidController.targetValue = 0;

        timer = new ElapsedTime();
    }

    public void setPower(double power) {
        if(power != 0) pidON = false;
        if(extendo.getCurrentPosition() < MAX_POS - 50 && power > 0) {
            extendo.setPower(power);
            currentState = States.EXTENDED;
        }
        if(extendo.getCurrentPosition() > 10 && power < 0) extendo.setPower(power);
    }


    public void goDown() {
        pidON = true;
        pidController.targetValue = 0;
        currentState = States.RETRACT_PID;
    }

    public void goDownTillMotorOverCurrent() {
        extendo.setPower(-0.5);
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
                if(extendo.isOverCurrent())
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
            position = extendo.getCurrentPosition();
            //pidController.targetValue = rowPosition[targetRow];
            double powerExtendo = pidController.update(position);
            extendo.setPower(powerExtendo);
        }

    }


    public void ResetEncoders() {
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
