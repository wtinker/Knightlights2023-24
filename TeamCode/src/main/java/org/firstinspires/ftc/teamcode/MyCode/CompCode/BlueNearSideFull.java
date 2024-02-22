package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MyCode.util.Intake;
import org.firstinspires.ftc.teamcode.MyCode.util.Output;
import org.firstinspires.ftc.teamcode.MyCode.util.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous(name = "BlueNearSideFull")
public class BlueNearSideFull extends LinearOpMode {

    enum robotState {
        FIRST,
        LEFT1,
        LEFT2,
        MID1,
        MID2,
        RIGHT1,
        RIGHT2,
        END
    }
    boolean done = false;
    boolean running = true;
    public void runOpMode(){

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        Rev2mDistanceSensor dist = hardwareMap.get(Rev2mDistanceSensor.class, "dist");
        boolean left = false;
        boolean middle = false;
        boolean right = false;
        Pose2d startpose = new Pose2d(12, 62, Math.toRadians(0));
        TrajectorySequence t1 = drive.trajectorySequenceBuilder(startpose)
                .splineToSplineHeading(new Pose2d(37, 34, 0), Math.toRadians(-90))
                .addTemporalMarker(()->{done = true;})
                .build();
        TrajectorySequence trajright1 = drive.trajectorySequenceBuilder(t1.end())
                .lineTo(new Vector2d(32, 35))
                .addDisplacementMarker(()->{intake.Lower(); sleep(500); intake.Raise();})
                .lineTo(new Vector2d(51, 30))
                .addTemporalMarker(()->{done = true;})
                .build();
        TrajectorySequence trajmid1 = drive.trajectorySequenceBuilder(t1.end())
                .splineToConstantHeading(new Vector2d(25, 26), Math.toRadians(180))
                .addDisplacementMarker(()->{intake.Lower(); sleep(500); intake.Raise();})
                .lineTo(new Vector2d(51, 37))
                .addTemporalMarker(()->{done = true;})
                .build();
        TrajectorySequence trajleft1 = drive.trajectorySequenceBuilder(t1.end())
                .lineTo(new Vector2d(10, 33))
                .addDisplacementMarker(()->{intake.Lower(); sleep(500); intake.Raise();})
                .lineTo(new Vector2d(51, 42))
                .addTemporalMarker(()->{done = true;})
                .build();
        TrajectorySequence trajright2 = drive.trajectorySequenceBuilder(trajright1.end())
                .lineTo(new Vector2d(48, 60)).addTemporalMarker(()->{done = true;}).build();
        TrajectorySequence trajmid2 = drive.trajectorySequenceBuilder(trajmid1.end())
                .lineTo(new Vector2d(48, 60)).addTemporalMarker(()->{done = true;}).build();
        TrajectorySequence trajleft2 = drive.trajectorySequenceBuilder(trajleft1.end())
                .lineTo(new Vector2d(48, 60)).addTemporalMarker(()->{done = true;}).build();

        drive.setPoseEstimate(startpose);
        RedFarSideFull.robotState robotstate = RedFarSideFull.robotState.FIRST;
        waitForStart();

        drive.followTrajectorySequenceAsync(t1);

        out: while(opModeIsActive() && running){
            //limit switch code
            telemetry.addData("current state", robotstate);
            telemetry.update();
            drive.update();
            switch (robotstate){
                case FIRST:
                    if(done&&!drive.isBusy()){
                        done = false;
                        double distance = dist.getDistance(DistanceUnit.CM);
                        if(37<distance && distance<50){robotstate = RedFarSideFull.robotState.RIGHT1; drive.followTrajectorySequenceAsync(trajright1);}
                        else if(distance<17){robotstate = RedFarSideFull.robotState.LEFT1; drive.followTrajectorySequenceAsync(trajleft1);}
                        else {robotstate = RedFarSideFull.robotState.MID1; drive.followTrajectorySequenceAsync(trajmid1);}
                    } break;
                case LEFT1:
                    if(done&&!drive.isBusy()){
                        done = false;
                        output.Extend(730);
                        sleep(1500);
                        output.Single();
                        sleep(500);
                        output.Descore();
                        sleep(500);
                        output.Retract();
                        robotstate = RedFarSideFull.robotState.LEFT2;
                        drive.followTrajectorySequenceAsync(trajleft2);
                    } break;
                case RIGHT1:
                    if(done&&!drive.isBusy()){
                        done = false;
                        output.Extend(730);
                        sleep(1500);
                        output.Single();
                        sleep(500);
                        output.Descore();
                        sleep(500);
                        output.Retract();
                        robotstate = RedFarSideFull.robotState.RIGHT2;
                        drive.followTrajectorySequenceAsync(trajright2);
                    } break;
                case MID1:
                    if(done&&!drive.isBusy()){
                        done = false;
                        output.Extend(730);
                        sleep(1500);
                        output.Single();
                        sleep(500);
                        output.Descore();
                        sleep(500);
                        output.Retract();
                        robotstate = RedFarSideFull.robotState.MID2;
                        drive.followTrajectorySequenceAsync(trajmid2);
                    } break;
                case LEFT2:
                    if(done){robotstate = RedFarSideFull.robotState.END;} break;
                case RIGHT2:
                    if(done){robotstate = RedFarSideFull.robotState.END;} break;
                case MID2:
                    if(done){robotstate = RedFarSideFull.robotState.END;} break;
                case END:
                    Storage.poseStorage = drive.getPoseEstimate();
                    running = false;
                    break;
            }
        }
    }
}
