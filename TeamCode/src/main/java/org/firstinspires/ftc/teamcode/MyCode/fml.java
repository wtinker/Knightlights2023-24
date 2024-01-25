package org.firstinspires.ftc.teamcode.MyCode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.Encoder;

@TeleOp(name = "buh")
public class fml extends LinearOpMode {

    public void runOpMode(){

        //DcMotorEx motor;
        //motor = hardwareMap.get(DcMotorEx.class, "Slide312");
        //motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Output output = new Output(this, hardwareMap);

        Servo servo;
        servo = hardwareMap.get(Servo.class, "Intake servo");
        double servopos = 0.0;
        servo.setPosition(servopos);

        Rev2mDistanceSensor dist;
        dist = hardwareMap.get(Rev2mDistanceSensor.class, "dist");

        waitForStart();
        output.SetTime(getRuntime());

        while(opModeIsActive()){
            servo.setPosition(servopos);
            if(gamepad1.y){servopos += 0.1;}
            if(gamepad1.a){servopos -= 0.1;}
            if(gamepad1.dpad_up){servopos += 0.01;}
            if(gamepad1.dpad_down){servopos -= 0.01;}
            if(gamepad1.right_bumper){output.Extend(1000);}
            if(gamepad1.left_bumper){output.Retract();}
            output.SlidePID(getRuntime());
            //telemetry.addData("Motor encoder", motor.getCurrentPosition());
            telemetry.addData("Servo positition", servopos);
            telemetry.update();
            sleep(50);
        }

    }

}
