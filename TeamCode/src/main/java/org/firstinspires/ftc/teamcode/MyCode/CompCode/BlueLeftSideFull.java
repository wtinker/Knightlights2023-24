package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MyCode.Intake;
import org.firstinspires.ftc.teamcode.MyCode.Output;
import org.firstinspires.ftc.teamcode.MyCode.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous(name = "BlueLeftSideFull")
public class BlueLeftSideFull extends LinearOpMode {
    SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
    Output output = new Output(this, hardwareMap);
    Intake intake = new Intake(this, hardwareMap);
    boolean left = false;
    boolean middle = false;
    boolean right = true;
    TrajectorySequence t1 = drive.trajectorySequenceBuilder(new Pose2d(12, 63, Math.toRadians(180)))
            .lineTo(new Vector2d(12, 34))
            .addDisplacementMarker(() ->{
                //scan and score
            })
            .turn(Math.toRadians(-90))
            .addDisplacementMarker(() ->{
                //scan and score
            })
            .turn(Math.toRadians(-90))
            .addDisplacementMarker(() ->{
                //scan and score
            })
            .splineToConstantHeading(new Vector2d(12, 48), Math.toRadians(0))
            .splineTo(new Vector2d(51, 42), Math.toRadians(0))
            .build();
    TrajectorySequence trajright1 = drive.trajectorySequenceBuilder(t1.end())
            .strafeRight(10)
            .back(6)
            .splineToLinearHeading(new Pose2d(36, 24, Math.toRadians(315)), Math.toRadians(0))
            .lineTo(new Vector2d(48, 12))
            .build();
    Trajectory trajmid1 = drive.trajectoryBuilder(t1.end())
            .back(6)
            .splineToLinearHeading(new Pose2d(36, 24, Math.toRadians(315)), Math.toRadians(0))
            .lineTo(new Vector2d(48, 12))
            .build();
    TrajectorySequence trajleft1 = drive.trajectorySequenceBuilder(t1.end())
            .strafeLeft(8)
            .back(6)
            .splineToLinearHeading(new Pose2d(36, 24, Math.toRadians(315)), Math.toRadians(0))
            .lineTo(new Vector2d(48, 12))
            .build();
    public void runOpMode(){

        drive.followTrajectorySequence(t1);
        if(left){drive.followTrajectorySequence(trajleft1);}
        else if(middle){drive.followTrajectory(trajmid1);}
        else if(right){drive.followTrajectorySequence(trajright1);}
        Storage.poseStorage = drive.getPoseEstimate();

    }

}
