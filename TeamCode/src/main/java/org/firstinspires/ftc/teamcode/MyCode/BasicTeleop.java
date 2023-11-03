package org.firstinspires.ftc.teamcode.MyCode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Basic Teleop")
public class BasicTeleop extends LinearOpMode {

    public double leftStartTime, rightStartTime;

    MecanumDrive Drive;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        //AprilTag aprilTag = new AprilTag(this);
        Drive = new MecanumDrive(hardwareMap, Storage.poseStorage);
        int FullExtension = 2050;

        //aprilTag.init();

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){

            output.RunOutput();
            //aprilTag.RunAprilTags();

            if(Storage.targetFound && gamepad1.right_trigger > 0.5){AprilTagDrive();}
            else {RunDriveTrain();}

            if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
            if(gamepad1.right_bumper && rightCooldown()){output.Cycle();}

            if(gamepad1.y){output.Extend(FullExtension);}
            if(gamepad1.a){output.Retract();}

            if(gamepad1.dpad_up){output.Adjust(100);}
            if(gamepad1.dpad_down){output.Adjust(-100);}

        }

    }

    public void RunDriveTrain(){
        Drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        Drive.updatePoseEstimate();

        telemetry.addData("x", Drive.pose.position.x);
        telemetry.addData("y", Drive.pose.position.y);
        telemetry.addData("heading", Drive.pose.heading);
        telemetry.update();
    }

    public void AprilTagDrive(){
        Drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        Storage.drive,
                        Storage.strafe
                ),
                Storage.turn
        ));

        Drive.updatePoseEstimate();

        telemetry.addData("x", Drive.pose.position.x);
        telemetry.addData("y", Drive.pose.position.y);
        telemetry.addData("heading", Drive.pose.heading);
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
