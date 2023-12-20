package org.firstinspires.ftc.teamcode.MyCode;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance.FieldElementAvoider;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "funny")
public class funny extends LinearOpMode {

    public double leftStartTime, rightStartTime;
    double xCoord, yCoord, rot, startTime;
    SampleMecanumDrive Drive;
    FieldElementAvoider FEA = new FieldElementAvoider(1);
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this, hardwareMap);
        Drive = new SampleMecanumDrive(hardwareMap);
        int FullExtension = 2700;

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        Trajectory t1;

        waitForStart();

        startTime = getRuntime();

        while(opModeIsActive()){
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();
            rot = Drive.getPoseEstimate().getHeading();

            t1 = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineTo(new Vector2d(51, -38)).build();

            output.RunOutput();

            if(gamepad1.left_trigger > 0.5){
                aprilTag.RunAprilTags();
                if(Storage.targetFound){AprilTagDrive();}
                else{RunDriveTrain();}}
            else if(gamepad1.right_trigger > 0.5){
                Drive.followTrajectory(t1);
            }
            else {RunDriveTrain();}

            if(xCoord < -24 && intake.isRaised){intake.Lower();}
            if(xCoord > -24 && !intake.isRaised){intake.Raise();}
            if(xCoord < -40 && yCoord > -6 && !intake.isPowered){intake.Start();}
            if((xCoord > -40 || yCoord < -6) && intake.isPowered){intake.Stop();}

            if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}

            if(xCoord > 40 && yCoord < -12 && !output.SlideExtended){output.Extend(FullExtension);}
            if((xCoord < 40 || yCoord > -12)){output.Retract();}

            if(gamepad1.dpad_left){Storage.aprilTagTarg = 4;}
            if(gamepad1.dpad_right){Storage.aprilTagTarg = 6;}

            if(gamepad1.x && getRuntime() - startTime > 90){AutoEndgame();}

        }

    }

    public void RunDriveTrain(){
        Drive.setWeightedDrivePower(new Pose2d(
                new Vector2d(
                        FEA.getCorrectedX(Drive.getPoseEstimate().getX(), Drive.getPoseEstimate().getY(),
                                Drive.getPoseEstimate().getHeading(), -gamepad1.left_stick_y, -gamepad1.left_stick_x),
                        FEA.getCorrectedY(Drive.getPoseEstimate().getX(), Drive.getPoseEstimate().getY(),
                                Drive.getPoseEstimate().getHeading(), -gamepad1.left_stick_y, -gamepad1.left_stick_x)
                ),
                -gamepad1.right_stick_x
        ));

        Drive.update();

        Pose2d poseEstimate = Drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.addData("x correction", FEA.returnX(Drive.getPoseEstimate().getX(), Drive.getPoseEstimate().getY(), Drive.getPoseEstimate().getHeading()));
        telemetry.addData("x correction", FEA.returnY(Drive.getPoseEstimate().getX(), Drive.getPoseEstimate().getY(), Drive.getPoseEstimate().getHeading()));
        telemetry.update();
    }

    public void AprilTagDrive(){
        Drive.setWeightedDrivePower(new Pose2d(
                new Vector2d(
                        Storage.drive,
                        Storage.strafe
                ),
                Storage.turn
        ));

        Drive.update();

        Pose2d poseEstimate = Drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.update();
    }

    public boolean leftCooldown() {
        if(getRuntime() - leftStartTime > .25) { //Must wait 250 milliseconds before input can be used again
            leftStartTime = getRuntime();
            return true;
        }
        return false;
    }

    public boolean rightCooldown() {
        if(getRuntime() - rightStartTime > .25) { //Must wait 250 milliseconds before input can be used again
            rightStartTime = getRuntime();
            return true;
        }
        return false;
    }

    public void AutoEndgame(){

    }

}
