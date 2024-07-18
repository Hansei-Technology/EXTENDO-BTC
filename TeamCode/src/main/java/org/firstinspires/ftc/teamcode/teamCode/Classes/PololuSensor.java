package org.firstinspires.ftc.teamcode.teamCode.Classes;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Utils.StickyGamepad;

public class PololuSensor {
    DigitalChannel left;
    DigitalChannel right;
    public PololuSensor(HardwareMap map) {
        left = map.get(DigitalChannel.class, "sensorLeft");
        right = map.get(DigitalChannel.class, "sensorRight");
        left.setMode(DigitalChannel.Mode.INPUT);
        right.setMode(DigitalChannel.Mode.INPUT);
    }

    public int detect() {
        int ans = 0;
        if(!left.getState()) ans++;
        if(!right.getState()) ans++;
        return  ans;
    }
}
