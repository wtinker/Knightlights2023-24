package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MyCode.util.AprilTag;
import org.firstinspires.ftc.teamcode.MyCode.util.Intake;
import org.firstinspires.ftc.teamcode.MyCode.util.Output;
import org.firstinspires.ftc.teamcode.MyCode.util.RGB;
import org.firstinspires.ftc.teamcode.MyCode.util.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@TeleOp(name = "Red Teleop better")
public class RedTeleopBetter extends LinearOpMode {

    enum robotState {
        FULLAUTO,
        FULLMANUAL,
        MANUALOUT,
        MANUALIN,
        MANUALOUTSLOW,
        MANUALINSLOW,
        AUTOSLOW,
        MANUALSLOW,
        ENDGAME
    }
    boolean running = true;
    public double leftStartTime, rightStartTime;
    double xTrim = 0;
    double yTrim = 0;
    double xCoord, yCoord, rot, startTime;
    SampleMecanumDrive Drive;
    boolean fieldCentric = false;
    boolean mark = false;
    boolean droneLaunched = false;
    final double slowspeed = 0.5;
    double timeout = 5;
    Vector2d input;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this, hardwareMap);
        RGB rgb = new RGB(this, hardwareMap);
        Drive = new SampleMecanumDrive(hardwareMap);
        Drive.setPoseEstimate(Storage.poseStorage);
        int FullExtension = 1670;

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        Trajectory Trajright, Trajleft;

        robotState robotstate = robotState.FULLMANUAL;
        waitForStart();
        startTime = getRuntime();
        output.SetTime(getRuntime());
        rgb.SetRed();
        out: while(opModeIsActive()&&running){
            telemetry.addData("current state", robotstate);
            telemetry.addData("field centric", fieldCentric);
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();
            rot = Drive.getPoseEstimate().getHeading();
            telemetry.addData("x", xCoord);
            telemetry.addData("y", yCoord);
            telemetry.addData("heading", rot);
            telemetry.update();

            Trajright = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(49 + xTrim, -42 + yTrim, 0)).build();
            Trajleft = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(49 + xTrim, -29 + yTrim, 0)).build();

            if(gamepad2.dpad_up){xTrim += 0.5;}
            if(gamepad2.dpad_down){xTrim -= 0.5;}
            if(gamepad2.dpad_left){yTrim += 0.5;}
            if(gamepad2.dpad_right){yTrim -= 0.5;}
            if(gamepad2.right_bumper){fieldCentric = true;}
            if(gamepad2.left_bumper){fieldCentric = false;}
            if(gamepad2.a){robotstate = robotState.FULLMANUAL; mark = true;}
            if(gamepad2.b){robotstate = robotState.MANUALOUT; mark = true;}
            if(gamepad2.x){robotstate = robotState.MANUALIN; mark = true;}
            if(gamepad2.y){robotstate = robotState.FULLAUTO; mark = true;}
            if(gamepad1.dpad_left){Drive.followTrajectory(Trajleft);}
            if(gamepad1.dpad_right){Drive.followTrajectory(Trajright);}
            output.SlidePID(getRuntime());
            rgb.Run(getRuntime() - startTime);
            //if(getRuntime() - startTime > timeout && !mark){robotstate = robotState.FULLAUTO;}

            switch (robotstate){
                case FULLAUTO:
                    if (xCoord < -24 && yCoord > 0) {intake.Lower();}
                    if (!(xCoord < -24 && yCoord > 0)) {intake.Raise();}
                    if (xCoord < -40 && yCoord > 40) {intake.Start();}
                    if (!(xCoord < -40 && yCoord > 40)) {intake.Stop();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if (xCoord > 40 && yCoord < -12) {output.Extend(FullExtension); robotstate = robotState.AUTOSLOW;}
                    if (!(xCoord > 40 && yCoord < -12)) {output.Retract();}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrain();
                    break;
                case AUTOSLOW:
                    if (xCoord < -24 && yCoord > 0) {intake.Lower();}
                    if (!(xCoord < -24 && yCoord > 0)) {intake.Raise();}
                    if (xCoord < -40 && yCoord > 40) {intake.Start();}
                    if (!(xCoord < -40 && yCoord > 40)) {intake.Stop();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if (xCoord > 40 && yCoord < -12) {output.Extend(FullExtension);}
                    if (!(xCoord > 40 && yCoord < -12)) {output.Retract(); robotstate = robotState.FULLAUTO;}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrainSlow();
                    break;
                case FULLMANUAL:
                    if(gamepad1.dpad_down){intake.Lower();}
                    if(gamepad1.dpad_up){intake.Raise(); robotstate = robotState.MANUALSLOW;}
                    if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if(gamepad1.y){output.Extend(FullExtension); robotstate = robotState.MANUALSLOW;}
                    if(gamepad1.a){output.Retract();}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrain();
                    break;
                case MANUALSLOW:
                    if(gamepad1.dpad_down){intake.Lower(); robotstate = robotState.FULLMANUAL;}
                    if(gamepad1.dpad_up){intake.Raise();}
                    if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if(gamepad1.y){output.Extend(FullExtension);}
                    if(gamepad1.a){output.Retract(); robotstate = robotState.FULLMANUAL;}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrainSlow();
                    break;
                case MANUALIN:
                    if(gamepad1.dpad_down){intake.Lower();}
                    if(gamepad1.dpad_up){intake.Raise(); robotstate = robotState.MANUALINSLOW;}
                    if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if (xCoord > 40 && yCoord < -12) {output.Extend(FullExtension); robotstate = robotState.MANUALINSLOW;}
                    if (!(xCoord > 40 && yCoord < -12)) {output.Retract();}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrain();
                    break;
                case MANUALINSLOW:
                    if(gamepad1.dpad_down){intake.Lower(); robotstate = robotState.MANUALIN;}
                    if(gamepad1.dpad_up){intake.Raise();}
                    if(gamepad1.left_bumper && leftCooldown()){intake.Toggle();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if (xCoord > 40 && yCoord < -12) {output.Extend(FullExtension);}
                    if (!(xCoord > 40 && yCoord < -12)) {output.Retract(); robotstate = robotState.MANUALOUT;}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrainSlow();
                    break;
                case MANUALOUT:
                    if (xCoord < -24 && yCoord > 0) {intake.Lower();}
                    if (!(xCoord < -24 && yCoord > 0)) {intake.Raise();}
                    if (xCoord < -40 && yCoord > 40) {intake.Start();}
                    if (!(xCoord < -40 && yCoord > 40)) {intake.Stop();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if(gamepad1.y){output.Extend(FullExtension);}
                    if(gamepad1.a){output.Retract();}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrain();
                    break;
                case MANUALOUTSLOW:
                    if (xCoord < -24 && yCoord > 0) {intake.Lower();}
                    if (!(xCoord < -24 && yCoord > 0)) {intake.Raise();}
                    if (xCoord < -40 && yCoord > 40) {intake.Start();}
                    if (!(xCoord < -40 && yCoord > 40)) {intake.Stop();}
                    if(gamepad1.right_bumper && rightCooldown()){output.Toggle();}
                    if(gamepad1.b){intake.Reverse(); robotstate = robotState.MANUALIN;}
                    if(gamepad1.y){output.Extend(FullExtension);}
                    if(gamepad1.a){output.Retract();}
                    if(gamepad1.x){output.Climb();}
                    RunDriveTrainSlow();
                    break;
                case ENDGAME:
                    if(!droneLaunched){output.LaunchDrone(); droneLaunched = true; robotstate = robotState.FULLMANUAL;}
                    else{output.Climb();}
            }

        }
        while(!isStopRequested()){output.RunOutput();}
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
    }
    public void RunDriveTrainSlow(){
        input = new Vector2d(-gamepad1.left_stick_y*slowspeed, -gamepad1.left_stick_x*slowspeed);
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
