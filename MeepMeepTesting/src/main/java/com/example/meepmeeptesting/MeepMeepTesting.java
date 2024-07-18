package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 50, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-35, 64, Math.toRadians(90)))
                .strafeToSplineHeading(new Vector2d(-33,30) ,Math.toRadians(-170))
                .waitSeconds(1)
                .strafeToSplineHeading(new Vector2d(-36,12) ,Math.toRadians(180))
                .waitSeconds(0)
                //.lineToXSplineHeading(33, Math.toRadians(180))
                .lineToX(33)
                .splineToSplineHeading(new Pose2d(47,37,Math.toRadians(180)),Math.toRadians(60))
                .waitSeconds(1)
                .splineToConstantHeading(new Vector2d(30,13),Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-13,13,Math.toRadians(180)),Math.toRadians(180))
                .waitSeconds(1)
                .strafeToSplineHeading(new Vector2d(5,13), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(47,29,Math.toRadians(180)),Math.toRadians(0))
                .waitSeconds(1)
                .strafeToSplineHeading(new Vector2d(5,13), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-13,13),Math.toRadians(180))
                .waitSeconds(1)
                .strafeToSplineHeading(new Vector2d(5,13),Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(47,29,Math.toRadians(180)),Math.toRadians(0))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(47,10), Math.toRadians(180))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}