package org.firstinspires.ftc.teamcode.configs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sfdev.assembly.state.*;

// @Autonomous or @TeleOp
@TeleOp(group = "Teste")
public class StateMachineTest extends LinearOpMode {
    ElapsedTime timer;
    enum States {
        FIRST,
        SECOND,
        THIRD
    }

    @Override
    public void runOpMode() throws InterruptedException {
        timer = new ElapsedTime();

        StateMachine machine = new StateMachineBuilder()
                .state(States.FIRST)
                .onEnter( () -> {
                    telemetry.addData("first", "state");
                    telemetry.update();
                })
                .transition( () ->  gamepad1.x, States.SECOND) // transition when gamepad1.x is clicked

                .state(States.SECOND)
                .onEnter( () -> {
                    telemetry.addData("second", "state");
                    telemetry.update();
                    timer.reset();
                } )
                .loop(() -> {
                    telemetry.addData("timer", timer.seconds());
                    telemetry.update();
                })
                .transition(() -> timer.seconds() > 10, States.FIRST)
                .transition( () -> gamepad1.b, States.THIRD) // if check2 is false transition

                .state(States.THIRD)
                .onEnter(() -> {
                    telemetry.addData("state", 3);
                    telemetry.update();
                })
                .build();

        waitForStart();

        machine.start();
        machine.setState(States.FIRST);

        while(opModeIsActive()) { // autonomous loop
            machine.update();
        }
    }
}