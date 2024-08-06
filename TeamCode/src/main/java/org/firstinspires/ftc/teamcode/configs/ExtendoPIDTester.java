package org.firstinspires.ftc.teamcode.configs;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Utils.SimplePIDController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;

import java.util.List;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Photon
@Config
@TeleOp(group = "Teste")
public class ExtendoPIDTester extends LinearOpMode {
    public static double DISTANCE = 60; // in
    public static double Kp = 0.00325;
    public static double Ki = 0.0022;
    public static double Kd = 0;
    public static double maxSpeed = 1;
    public static double RetractedPosition = 0 , ExtendedPosition = 300;
    public static double vMax = 0, AccMax = 0, JerkMax =0 , EndPos = 700;
    int TargetLift = 0;
    ElapsedTime timerPID = new ElapsedTime();
    double voltage, currentPos;

    ExtendoController extendo;

    @Override

    public void runOpMode() throws InterruptedException {
        List <LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        double loopTime = 0;

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        ElapsedTime changePositions = new ElapsedTime();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        extendo = new ExtendoController(hardwareMap);
        VoltageSensor batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        waitForStart();

        if (isStopRequested()) return;
        ElapsedTime now = new ElapsedTime();
        now.reset();
        telemetry.update();
        changePositions.reset();
        extendo.CS = ExtendoController.extendoStatus.RETRACTED;
        while (opModeIsActive() && !isStopRequested())
        {
//            int ColectarePosition = extendo.getCurrentPosition();
//            double powerColectare = hello.update(ColectarePosition);
//            powerColectare = Math.max(-1,Math.min(powerColectare,1));
//            extendo.setPower(powerColectare);
            // robot.extendoRight.setPower(powerColectare);
            if (changePositions.seconds()>5)
            {
                if (extendo.CS == ExtendoController.extendoStatus.DRIVE)
                {
                    extendo.CS = ExtendoController.extendoStatus.EXTENDED;
                }
                else
                {
                    extendo.CS = ExtendoController.extendoStatus.DRIVE;
                }
                voltage = batteryVoltageSensor.getVoltage();
                extendo.update(voltage);
                changePositions.reset();
            }
            currentPos = extendo.motor.getCurrentPosition();
            telemetry.addData("currentPos", currentPos);
            telemetry.addData("TargetLift", extendo.extendoPID.targetValue);
            telemetry.addData("Error", extendo.extendoPID.measuredError(currentPos));
            telemetry.addData("ChangePosTime", changePositions.seconds());
            telemetry.addData("voltage", voltage);
            telemetry.addData("power", extendo.power);
            telemetry.addData("kp", extendo.extendoPID.p);

            double loop = System.nanoTime();

            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            telemetry.update();
            loopTime = loop;


//            telemetry.addData("distance 1", robot.pixelLeft.getState());
//            telemetry.addData("distance2", robot.pixelRight.getState());
//            telemetry.addData("distance3", robot.back.getDistance(DistanceUnit.CM));
//            telemetry.addData("distance4", robot.extendoDistance.getDistance(DistanceUnit.CM));
//            telemetry.addData("encoder1", robot.leftBack.getCurrentPosition());
//            telemetry.addData("encoder2", robot.leftFront.getCurrentPosition());
//            telemetry.addData("encoder3", robot.rightBack.getCurrentPosition());
//            telemetry.addData("encoder4", robot.rightFront.getCurrentPosition())


            if(ExtendoController.kp !=extendo.extendoPID.p || ExtendoController.kd !=extendo.extendoPID.d || ExtendoController.ki !=extendo.extendoPID.i) {
                extendo.extendoPID.p = ExtendoController.kp;
                extendo.extendoPID.i = ExtendoController.ki;
                extendo.extendoPID.d = ExtendoController.kd;
            }
        }
    }
}