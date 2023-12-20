package org.firstinspires.ftc.teamcode.MyCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Encoder;

@TeleOp(name = "buh")
public class fml extends LinearOpMode {

    public void runOpMode(){

        DcMotorEx motor;
        motor = hardwareMap.get(DcMotorEx.class, "Slide312");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Servo servo;
        servo = hardwareMap.get(Servo.class, "Intake Servo");
        double servopos = 0.0;
        servo.setPosition(servopos);

        waitForStart();

        while(opModeIsActive()){
            servo.setPosition(servopos);
            if(gamepad1.y){servopos += 0.1;}
            if(gamepad1.a){servopos -= 0.1;}
            if(gamepad1.dpad_up){servopos += 0.01;}
            if(gamepad1.dpad_down){servopos -= 0.01;}
            telemetry.addData("Motor encoder", motor.getCurrentPosition());
            telemetry.addData("Servo positition", servopos);
            telemetry.update();
            sleep(50);
        }

    }

}
