package org.firstinspires.ftc.teamcode.MyCode;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "Special tool we'll use for later")
public class Secret extends LinearOpMode {

    public double leftStartTime, rightStartTime;
    double xCoord, yCoord, startTime;
    SampleMecanumDrive Drive;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this);
        Drive = new SampleMecanumDrive(hardwareMap);
        int FullExtension = 2050;

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        waitForStart();

        startTime = getRuntime();

        while(opModeIsActive()){
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();

            output.RunOutput();
            aprilTag.RunAprilTags();

            if(Storage.targetFound && gamepad1.left_trigger > 0.5){AprilTagDrive();}
            else {RunDriveTrain();}

            if(xCoord < -24 && intake.isRaised){intake.Lower();}
            if(xCoord > -24 && !intake.isRaised){intake.Raise();}
            if(xCoord < -46 && yCoord < 2 && !intake.isPowered){intake.Start();}
            if((xCoord > -46 || yCoord > 2) && intake.isPowered){intake.Stop();}

            if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}

            if(xCoord > 40 && yCoord > 12 && !output.SlideExtended){output.Extend(FullExtension);}
            if((xCoord < 40 || yCoord < 12) && output.SlideExtended){output.Retract();}

            if(gamepad1.dpad_left){Storage.aprilTagTarg = 4;}
            if(gamepad1.dpad_right){Storage.aprilTagTarg = 6;}

            if(gamepad1.x && getRuntime() - startTime > 90){AutoEndgame();}

        }

    }

    public void RunDriveTrain(){
        Drive.setWeightedDrivePower(new Pose2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        Drive.update();

        Pose2d poseEstimate = Drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
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
