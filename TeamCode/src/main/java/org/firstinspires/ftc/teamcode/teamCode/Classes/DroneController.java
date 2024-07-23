//package org.firstinspires.ftc.teamcode.teamCode.Classes;
//
//import com.acmerobotics.dashboard.config.Config;
//@Config
//public class DroneController {
//
//    public enum droneStatus
//    {
//        INITIALIZE,
//        SECURED,
//        RELEASED,
//    }
//
//    public droneController()
//    {
//        CS = INITIALIZE;
//        PS = INITIALIZE;
//    }
//
//    public static droneStatus CS = INITIALIZE, PS = INITIALIZE;
//
//    public static double secured = 0;
//    public static double released = 1;
//
//    public void update(robotMap r)
//    {
//        if(CS != PS || CS == INITIALIZE)
//        {
//            switch (CS)
//            {
//                case INITIALIZE:
//                {
//                    r.drone.setPosition(secured);
//                    break;
//                }
//
//                case SECURED:
//                {
//                    r.drone.setPosition(secured);
//                    break;
//                }
//
//                case RELEASED:
//                {
//                    r.drone.setPosition(released);
//                    break;
//                }
//            }
//        }
//
//        PS = CS;
//    }
//
//}
