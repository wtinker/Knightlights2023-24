package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(30, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, -62, Math.toRadians(90)))
                                .splineToSplineHeading(new Pose2d(-56, -36, Math.toRadians(90)), Math.toRadians(90))
                                .splineToSplineHeading(new Pose2d(-56, -25, Math.toRadians(0)), Math.toRadians(90))
                                .addDisplacementMarker(()->{/*scan*/})
                                .waitSeconds(1)
                                //right side
                                .addDisplacementMarker(()->{})
                                .forward(25)
                                .back(14)
                                .waitSeconds(0.001)
                                .splineToSplineHeading(new Pose2d(-24, -12, Math.toRadians(0)), Math.toRadians(0))
                                .splineTo(new Vector2d(36, -24), Math.toRadians(-90))
                                .splineTo(new Vector2d(50, -28), Math.toRadians(0))
                                .addDisplacementMarker(()->{/*score*/})
                                .lineTo(new Vector2d(48, -60))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}