package org.firstinspires.ftc.teamcode.MyCode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class BasicTeleop extends LinearOpMode {

    Output output = new Output(this);
    Intake intake = new Intake(this);
    MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
    int FullExtension;
    public void runOpMode(){

        output.init();
        intake.init();

        waitForStart();

        while(opModeIsActive()){

            RunDriveTrain();
            output.RunOutput();

            if(gamepad1.left_bumper){intake.Toggle();}
            if(gamepad1.right_bumper){output.Cycle();}

            if(gamepad1.y){output.Extend(FullExtension);}
            if(gamepad1.x){output.Extend((FullExtension / 2));}
            if(gamepad1.a){output.Retract();}

            if(gamepad1.dpad_up){output.Adjust(100);}
            if(gamepad1.dpad_down){output.Adjust(-100);}

        }

    }

    public void RunDriveTrain(){
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();

        telemetry.addData("x", drive.pose.position.x);
        telemetry.addData("y", drive.pose.position.y);
        telemetry.addData("heading", drive.pose.heading);
        telemetry.update();
    }

}
