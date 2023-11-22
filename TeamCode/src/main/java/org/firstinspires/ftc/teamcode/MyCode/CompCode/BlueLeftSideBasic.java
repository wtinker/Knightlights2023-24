package org.firstinspires.ftc.teamcode.MyCode.CompCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.MyCode.Intake;
import org.firstinspires.ftc.teamcode.MyCode.Output;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "BlueLeftSideBasic")
public class BlueLeftSideBasic extends LinearOpMode {

    DcMotor leftFront, leftRear, rightRear, rightFront;
    boolean left = false;
    boolean middle = false;
    boolean right = true;

    public void runOpMode(){

        leftFront = hardwareMap.dcMotor.get("Front Left");
        leftRear = hardwareMap.dcMotor.get("Back Left");
        rightRear = hardwareMap.dcMotor.get("Back Left");
        rightFront = hardwareMap.dcMotor.get("Front Right");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        Intake intake = new Intake(this, hardwareMap);

        waitForStart();

        leftRear.setPower(-0.8);
        leftFront.setPower(-0.8);
        rightRear.setPower(-0.8);
        rightFront.setPower(-0.8);

        sleep(1500);

        leftRear.setPower(0);
        leftFront.setPower(0);
        rightRear.setPower(0);
        rightFront.setPower(0);

        sleep(250);


        intake.Start();
        sleep(100);
        intake.Stop();
        sleep(1000);
        intake.Reverse();
        sleep(2000);
        intake.Stop();
    }

}
