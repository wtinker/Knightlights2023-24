package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MyCode.Intake;
import org.firstinspires.ftc.teamcode.MyCode.Output;
import org.firstinspires.ftc.teamcode.MyCode.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

//@Disabled
@Autonomous(name = "RedNearSideFull")
public class RedNearSideFull extends LinearOpMode {

    public void runOpMode(){

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        Rev2mDistanceSensor dist = hardwareMap.get(Rev2mDistanceSensor.class, "dist");
        boolean left = false;
        boolean middle = false;
        boolean right = false;
        Pose2d startpose = new Pose2d(12, -65, Math.toRadians(0));
        TrajectorySequence t1 = drive.trajectorySequenceBuilder(startpose)
                .splineToSplineHeading(new Pose2d(34, -35, 0), Math.toRadians(90))
                .build();
        TrajectorySequence trajright1 = drive.trajectorySequenceBuilder(t1.end())
                .lineTo(new Vector2d(32, -35))
                .addDisplacementMarker(()->{intake.Reverse(); sleep(2000); intake.Stop();})
                .lineTo(new Vector2d(51, -38))
                .addDisplacementMarker(()->{/*score*/})
                .waitSeconds(5)
                .lineTo(new Vector2d(48, -60))
                .build();
        TrajectorySequence trajmid1 = drive.trajectorySequenceBuilder(t1.end())
                .splineToConstantHeading(new Vector2d(26, -24), Math.toRadians(180))
                .addDisplacementMarker(()->{intake.Reverse(); sleep(2000); intake.Stop();})
                .lineTo(new Vector2d(51, -32))
                .addDisplacementMarker(()->{/*score*/})
                .waitSeconds(5)
                .lineTo(new Vector2d(48, -60))
                .build();
        TrajectorySequence trajleft1 = drive.trajectorySequenceBuilder(t1.end())
                .lineTo(new Vector2d(12, -30))
                .addDisplacementMarker(()->{intake.Reverse(); sleep(2000); intake.Stop();})
                .lineTo(new Vector2d(51, -26))
                .addDisplacementMarker(()->{/*score*/})
                .waitSeconds(5)
                .lineTo(new Vector2d(48, -60))
                .build();

        drive.setPoseEstimate(startpose);
        waitForStart();

        drive.followTrajectorySequence(t1);
        //scan: close->right far->left none->mid
        double distance = dist.getDistance(DistanceUnit.CM);
        if(7<distance && distance<17){right = true;}
        else if(30<distance && distance<40){left = true;}
        else {middle = true;}
        if(left){drive.followTrajectorySequence(trajleft1);}
        else if(middle){drive.followTrajectorySequence(trajmid1);}
        else if(right){drive.followTrajectorySequence(trajright1);}
        Storage.poseStorage = drive.getPoseEstimate();

    }

}
