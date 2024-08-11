package org.firstinspires.ftc.teamcode.teamCode.Classes;

import static org.firstinspires.ftc.teamcode.teamCode.Classes.DroneController.droneStatus.INITIALIZE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class DroneController {

    public enum droneStatus
    {
        INITIALIZE,
        SECURED,
        RELEASED,
    }
    public Servo drone;

    public DroneController(HardwareMap map)
    {
        drone =  map.get(Servo.class, "s3");
        CS = INITIALIZE;
        PS = INITIALIZE;
        update();
    }

    public  droneStatus CS = INITIALIZE, PS = INITIALIZE;

    public static double secured = 0.54;
    public static double released = 0.43;


    public void update()
    {
        if(CS != PS || CS == INITIALIZE)
        {
            switch (CS)
            {
                case INITIALIZE:
                {
                    drone.setPosition(secured);
                    break;
                }

                case SECURED:
                {
                    drone.setPosition(secured);
                    break;
                }

                case RELEASED:
                {
                    drone.setPosition(released);
                    break;
                }
            }
        }

        PS = CS;
    }

}
