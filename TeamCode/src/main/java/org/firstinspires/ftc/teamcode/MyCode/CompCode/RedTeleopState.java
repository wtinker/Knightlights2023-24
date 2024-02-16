package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.RobotState;

import org.firstinspires.ftc.teamcode.MyCode.util.AprilTag;
import org.firstinspires.ftc.teamcode.MyCode.util.Intake;
import org.firstinspires.ftc.teamcode.MyCode.util.Output;
import org.firstinspires.ftc.teamcode.MyCode.util.RGB;
import org.firstinspires.ftc.teamcode.MyCode.util.Storage;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name = "Red Teleop state")
public class RedTeleopState extends LinearOpMode {

    enum robotState {
        INTAKING,
        REVERSE,
        IDLE,
        OUTPUT,
        ENDGAME
    }
    boolean running = true;
    public double leftStartTime, rightStartTime, bStartTime;
    double xTrim = 0;
    double yTrim = 0;
    double xCoord, yCoord, rot, startTime;
    SampleMecanumDrive Drive;
    boolean fieldCentric = false;
    boolean mark = false;
    final double slowspeed = 0.5;
    double timeout = 5;
    int FullExtension = 2850;
    int MinExtension = 1100;
    int mod = 0;
    Vector2d input;
    public void runOpMode(){
        Output output = new Output(this, hardwareMap);
        Intake intake = new Intake(this, hardwareMap);
        AprilTag aprilTag = new AprilTag(this, hardwareMap);
        RGB rgb = new RGB(this, hardwareMap);
        Drive = new SampleMecanumDrive(hardwareMap);
        Drive.setPoseEstimate(Storage.poseStorage);

        telemetry.addData("Finished Initialization", null);
        telemetry.update();

        Trajectory Trajright, Trajleft;

        robotState robotstate = robotState.IDLE;
        waitForStart();
        startTime = getRuntime();
        output.SetTime(getRuntime());
        rgb.SetBlue();
        while(opModeIsActive()&&running){
            telemetry.addData("current state", robotstate);
            xCoord = Drive.getPoseEstimate().getX();
            yCoord = Drive.getPoseEstimate().getY();
            rot = Drive.getPoseEstimate().getHeading();

            Trajright = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(49 + xTrim, -42 + yTrim, 0)).build();
            Trajleft = Drive.trajectoryBuilder(new Pose2d(xCoord, yCoord, rot)).lineToLinearHeading(new Pose2d(49 + xTrim, -29 + yTrim, 0)).build();

            rgb.Run(getRuntime() - startTime);
            switch (robotstate){
                case IDLE:
                    if(gamepad1.left_bumper && leftCooldown()){
                        robotstate = robotState.INTAKING;
                    }
                    if(gamepad1.b && bCooldown()){
                        robotstate = robotState.REVERSE;
                    }
                    if(gamepad1.right_trigger > 0.5 || gamepad2.right_trigger > 0.5){
                        robotstate = robotState.OUTPUT;
                    }
                    if(gamepad1.x){
                        robotstate = robotState.ENDGAME;
                    }
                    intake.Raise();
                    intake.Stop();
                    output.Retract();
                    output.RunOutput();
                    RunDriveTrain();
                    telemetry.addData("LB to intake", null);
                    telemetry.addData("B to reverse intake", null);
                    telemetry.addData("RT to output", null);
                    telemetry.addData("X to endgame", null);
                    break;
                case INTAKING:
                    if(gamepad1.left_bumper && leftCooldown()){
                        robotstate = robotState.IDLE;
                    }
                    if(gamepad1.b && bCooldown()){
                        robotstate = robotState.REVERSE;
                    }
                    intake.Lower();
                    intake.Start();
                    output.Retract();
                    output.RunOutput();
                    RunDriveTrain();
                    telemetry.addData("LB to stop intake", null);
                    telemetry.addData("B to reverse intake", null);
                    break;
                case REVERSE:
                    if(gamepad1.left_bumper && leftCooldown()){
                        robotstate = robotState.IDLE;
                    }
                    if(gamepad1.b && bCooldown()){
                        robotstate = robotState.INTAKING;
                    }
                    intake.Lower();
                    intake.Reverse();
                    output.Retract();
                    output.RunOutput();
                    RunDriveTrain();
                    telemetry.addData("LB to stop intake", null);
                    telemetry.addData("B to reverse intake", null);
                    break;
                case OUTPUT:
                    if(gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5){
                        robotstate = robotState.IDLE;
                    }
                    if(gamepad2.right_bumper && rightCooldown()){
                        output.Cycle();
                    }
                    if(gamepad2.dpad_left){Drive.followTrajectoryAsync(Trajleft);}
                    if(gamepad2.dpad_right){Drive.followTrajectoryAsync(Trajright);}
                    runMod();
                    intake.Raise();
                    intake.Stop();
                    output.Extend(MinExtension + mod);
                    output.RunOutput();
                    RunDriveTrainSlow();
                    telemetry.addData("RB to cycle scoring", null);
                    telemetry.addData("both open -> one open -> none open", null);
                    telemetry.addData("Dpad left to auto align left", null);
                    telemetry.addData("Dpad right to auto align right", null);
                    telemetry.addData("Dpad up to raise output", null);
                    telemetry.addData("Dpad down to lower output", null);
                    telemetry.addData("LT to stop output", null);
                    break;
                case ENDGAME:
                    if(gamepad1.b && bCooldown()){
                        robotstate = robotState.IDLE;
                    }
                    if(gamepad1.right_bumper){
                        output.Climb();
                        while(opModeIsActive()){RunDriveTrain();}
                    }
                    if(gamepad1.a){
                        output.LaunchDrone();
                    }
                    intake.Raise();
                    intake.Stop();
                    output.Extend(FullExtension);
                    output.RunOutputRetracted();
                    RunDriveTrain();
                    telemetry.addData("RB to climb(cant go back)", null);
                    telemetry.addData("A to launch drone", null);
                    telemetry.addData("B to leave endgame", null);
                    break;
            }
            telemetry.update();
        }
        if(isStopRequested()){Storage.poseStorage = Drive.getPoseEstimate();}
    }
    public void RunDriveTrain(){
        input = new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x);
        Drive.setWeightedDrivePower(new Pose2d(
                input.rotated(-Drive.getPoseEstimate().getHeading() + Math.toRadians(90)),
                -gamepad1.right_stick_x
        ));
        Drive.update();
    }
    public void RunDriveTrainSlow(){
        input = new Vector2d(-gamepad2.left_stick_y * slowspeed, -gamepad2.left_stick_x * slowspeed);
        Drive.setWeightedDrivePower(new Pose2d(
                input.rotated(-Drive.getPoseEstimate().getHeading() + Math.toRadians(90)),
                -gamepad2.right_stick_x
        ));
        Drive.update();
    }
    public void runMod(){
        if(gamepad2.dpad_up){mod += 50;}
        if(gamepad2.dpad_down){mod -= 50;}
        if(mod + MinExtension > FullExtension){
            mod = FullExtension - MinExtension;
        }
        if(mod <= 0){
            mod = 0;
        }
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
    public boolean bCooldown() {
        if (getRuntime() - bStartTime > .25){
            bStartTime = getRuntime();
            return true;
        }
        return false;
    }
}
