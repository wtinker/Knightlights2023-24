package org.firstinspires.ftc.teamcode.MyCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MyCode.util.AprilTag;
import org.firstinspires.ftc.teamcode.MyCode.util.Intake;
import org.firstinspires.ftc.teamcode.MyCode.util.Output;
import org.firstinspires.ftc.teamcode.MyCode.util.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@TeleOp(name = "Special tool we'll use for later")
public class Secret extends LinearOpMode {

    public double leftStartTime, rightStartTime;
    double xTrim = 0;
    double yTrim = 0;
    double xCoord, yCoord, rot, startTime;
    SampleMecanumDrive Drive;
    boolean manualOverride = false;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this, hardwareMap);
        Drive = new SampleMecanumDrive(hardwareMap);
        int FullExtension = 2700;

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        Trajectory Trajright, Trajleft;

        waitForStart();

        startTime = getRuntime();

        while(opModeIsActive()){
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();
            rot = Drive.getPoseEstimate().getHeading();

            Trajright = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(51 + xTrim, -38 + yTrim, 0)).build();
            Trajleft = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(51 + xTrim, -26 + yTrim, 0)).build();

            if(gamepad2.dpad_up){xTrim += 0.5;}
            if(gamepad2.dpad_down){xTrim -= 0.5;}
            if(gamepad2.dpad_left){yTrim += 0.5;}
            if(gamepad2.dpad_right){yTrim -= 0.5;}

            output.RunOutput();

            if(gamepad1.dpad_left){Drive.followTrajectory(Trajleft);}
            if(gamepad1.dpad_right){Drive.followTrajectory(Trajright);}
            RunDriveTrain();

            if(!manualOverride) {
                if (xCoord < -24 && intake.isRaised) {
                    intake.Lower();
                }
                if (xCoord > -24 && !intake.isRaised) {
                    intake.Raise();
                }
                if (xCoord < -40 && yCoord > -6 && !intake.isPowered) {
                    intake.Start();
                }
                if ((xCoord > -40 || yCoord < -6) && intake.isPowered) {
                    intake.Stop();
                }
            }

            if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}

            if(!manualOverride) {
                if (xCoord > 40 && yCoord < -12 && !output.SlideExtended) {
                    output.Extend(FullExtension);
                }
                if ((xCoord < 40 || yCoord > -12)) {
                    output.Retract();
                }
            }

            //if(gamepad1.dpad_left){Storage.aprilTagTarg = 4;}
            //if(gamepad1.dpad_right){Storage.aprilTagTarg = 6;}

            if(gamepad1.x /*&& getRuntime() - startTime > 90*/){
                manualOverride = true;
                Trajectory end = Drive.trajectoryBuilder(Drive.getPoseEstimate()).lineToLinearHeading(new Pose2d(41, -34, Math.toRadians(180))).build();
                Trajectory hang = Drive.trajectoryBuilder(end.end()).lineTo(new Vector2d(11, -34)).build();
                Drive.followTrajectory(end);
                output.LaunchDrone();
                output.Extend(FullExtension);
                output.RunOutput();
                sleep(1500);
                Drive.followTrajectory(hang);
                output.Retract();
                output.RunOutput();
            }

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
        Trajectory end = Drive.trajectoryBuilder(Drive.getPoseEstimate()).lineToLinearHeading(new Pose2d(36, -36, Math.toRadians(180))).build();
        Trajectory hang = Drive.trajectoryBuilder(end.end()).lineTo(new Vector2d(50, -26)).build();
        Drive.followTrajectory(end);
    }

}
