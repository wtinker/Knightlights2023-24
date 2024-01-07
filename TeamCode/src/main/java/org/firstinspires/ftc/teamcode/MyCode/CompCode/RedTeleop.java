package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MyCode.AprilTag;
import org.firstinspires.ftc.teamcode.MyCode.Intake;
import org.firstinspires.ftc.teamcode.MyCode.Output;
import org.firstinspires.ftc.teamcode.MyCode.RGB;
import org.firstinspires.ftc.teamcode.MyCode.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "Red Teleop")
public class RedTeleop extends LinearOpMode {

    public double leftStartTime, rightStartTime;
    double xTrim = 0;
    double yTrim = 0;
    double xCoord, yCoord, rot, startTime;
    SampleMecanumDrive Drive;
    boolean manualOverride = false;
    boolean intakeOverride = false;
    boolean outputOverride = false;
    boolean fieldCentric = true;
    Vector2d input;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this, hardwareMap);
        Drive = new SampleMecanumDrive(hardwareMap);
        Drive.setPoseEstimate(Storage.poseStorage);
        int FullExtension = 2700;

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        Trajectory Trajright, Trajleft;

        waitForStart();

        startTime = getRuntime();

        while(opModeIsActive()){
            telemetry.addData("Manual Override", manualOverride);
            telemetry.addData("Intake Override", intakeOverride);
            telemetry.addData("Output Override", outputOverride);
            telemetry.addData("Field Centric", fieldCentric);
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();
            rot = Drive.getPoseEstimate().getHeading();

            if(gamepad2.dpad_up){xTrim += 0.5;}
            if(gamepad2.dpad_down){xTrim -= 0.5;}
            if(gamepad2.dpad_left){yTrim += 0.5;}
            if(gamepad2.dpad_right){yTrim -= 0.5;}

            Trajright = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(51 + xTrim, -38 + yTrim, 0)).build();
            Trajleft = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(51 + xTrim, -26 + yTrim, 0)).build();

            output.RunOutput();

            if(gamepad2.right_bumper){fieldCentric = true;}
            if(gamepad2.left_bumper){fieldCentric = false;}
            if(gamepad1.dpad_left){Drive.followTrajectory(Trajleft);}
            if(gamepad1.dpad_right){Drive.followTrajectory(Trajright);}
            RunDriveTrain();

            if(gamepad2.a){manualOverride = true;}
            if(gamepad2.b){outputOverride = true;}
            if(gamepad2.x){intakeOverride = true;}
            if(gamepad2.y){manualOverride = false; outputOverride = false; intakeOverride = false;}

            if(!manualOverride && !intakeOverride) {
                if (xCoord < -24 && yCoord < 0) {
                    intake.Lower();
                }
                if (!(xCoord < -24 && yCoord < 0)) {
                    intake.Raise();
                }
                if (xCoord < -40 && yCoord > -40) {
                    intake.Start();
                }
                if (!(xCoord < -40 && yCoord > -40)) {
                    intake.Stop();
                }
            }else{
                if(gamepad1.dpad_down){intake.Lower();}
                if(gamepad1.dpad_up){intake.Raise();}
                if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
            }
            if(gamepad1.b){intake.Reverse(); intakeOverride = true;}

            if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}

            if(!manualOverride && !outputOverride) {
                if (xCoord > 40 && yCoord < -12) {
                    output.Extend(FullExtension);
                }
                if (!(xCoord > 40 && yCoord < -12)) {
                    output.Retract();
                }
            }else{
                if(gamepad1.y){output.Extend(FullExtension);}
                if(gamepad1.a){output.Retract();}
            }

            //if(gamepad1.dpad_left){Storage.aprilTagTarg = 4;}
            //if(gamepad1.dpad_right){Storage.aprilTagTarg = 6;}

            if(gamepad1.x /*&& getRuntime() - startTime > 90*/){
                manualOverride = true;
                Trajectory end = Drive.trajectoryBuilder(Drive.getPoseEstimate()).lineToLinearHeading(new Pose2d(41, -34, Math.toRadians(180))).build();
                Trajectory hang = Drive.trajectoryBuilder(end.end()).lineTo(new Vector2d(11, -34)).build();
                Drive.followTrajectory(end);
                while(Drive.isBusy()){}
                output.LaunchDrone();
                output.Extend(FullExtension);
                output.RunOutput();
                sleep(1500);
                Drive.followTrajectory(hang);
                while(Drive.isBusy()){}
                output.Retract();
                output.RunOutput();
            }

        }

    }

    public void RunDriveTrain(){
        input = new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x);
        if(fieldCentric){
            Drive.setWeightedDrivePower(new Pose2d(
                    input.rotated(-Drive.getPoseEstimate().getHeading() - Math.toRadians(-90)),
                    -gamepad1.right_stick_x
            ));
        }else{
            Drive.setWeightedDrivePower(new Pose2d(
                    input,
                    -gamepad1.right_stick_x
            ));
        }
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
