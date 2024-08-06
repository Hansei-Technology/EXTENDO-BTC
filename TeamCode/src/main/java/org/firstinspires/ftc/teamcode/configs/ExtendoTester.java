package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoController;
import org.firstinspires.ftc.teamcode.teamCode.Classes.ExtendoControllerPID;


@TeleOp(name = "testextendoluca")
public class ExtendoTester extends LinearOpMode {
    ExtendoController extendo;
    double voltage;
    @Override
    public void runOpMode() throws InterruptedException {
//        extendo = new ExtendoControllerPID(hardwareMap);
        extendo = new ExtendoController(hardwareMap);
        VoltageSensor batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        extendo.CS = ExtendoController.extendoStatus.RETRACTED;
        waitForStart();
        while (opModeIsActive()) {
            extendo.CS = ExtendoController.extendoStatus.EXTENDED;
            voltage = batteryVoltageSensor.getVoltage();
            extendo.update(voltage);

            telemetry.addData("voltage", voltage);
            telemetry.update();
//            extendo.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
//            if (gamepad1.a) extendo.goDown();
//            if (gamepad1.y) extendo.goDownTillMotorOverCurrent();
//            extendo.update();
        }
    }
}
