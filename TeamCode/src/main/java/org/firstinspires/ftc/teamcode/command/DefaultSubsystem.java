package org.firstinspires.ftc.teamcode.command;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class DefaultSubsystem implements Subsystem {
    private HardwareMap hardwareMap;
    private Command defaultCommand;
    private String subsystemKey;

    public DefaultSubsystem(HardwareMap hardwareMap, String key) {
        this.subsystemKey = key;
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void updatePeriodic() {
    }

    @Override
    public String getSubsystemKey() {
        return this.subsystemKey;
    }

    @Override
    public Command getDefaultCommand() {
        return defaultCommand;
    }

}
