package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MyCode.Intake;
import org.firstinspires.ftc.teamcode.MyCode.Output;
import org.firstinspires.ftc.teamcode.MyCode.RGB;
import org.firstinspires.ftc.teamcode.MyCode.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "Blue Teleop")
public class BlueTeleop extends LinearOpMode {

    public double leftStartTime, rightStartTime, runtime;

    SampleMecanumDrive Drive;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        RGB rgb = new RGB(this, hardwareMap);
        //AprilTag aprilTag = new AprilTag(this);
        Drive = new SampleMecanumDrive(hardwareMap);
        int FullExtension = 2050;

        //aprilTag.init();
        rgb.SetBlue();

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        waitForStart();

        runtime = getRuntime();

        while(opModeIsActive()){

            output.RunOutput();
            rgb.Run(getRuntime() - runtime);
            //aprilTag.RunAprilTags();

            if(Storage.targetFound && gamepad1.right_trigger > 0.5){AprilTagDrive();}
            else {RunDriveTrain();}

            if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
            if(gamepad1.b){intake.Reverse();}
            if(gamepad1.right_bumper && rightCooldown()){output.Cycle();}

            if(gamepad1.y){output.Extend(FullExtension);}
            if(gamepad1.a){output.Retract();}

            if(gamepad1.dpad_up){output.Adjust(50);}
            if(gamepad1.dpad_down){output.Adjust(-50);}

        }

    }

    public void RunDriveTrain(){
        Drive.setWeightedDrivePower(new Pose2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ).rotated(-Drive.getPoseEstimate().getHeading() - Math.toRadians(90)),
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

}
