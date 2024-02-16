package org.firstinspires.ftc.teamcode.MyCode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;

@Disabled
@TeleOp
public class OdoTesting extends LinearOpMode {

    static double TICKS_PER_REV = 8192;
    static double WHEEL_RADIUS = 0.69; // in
    static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public void runOpMode(){

        Encoder leftEncoder, rightEncoder, frontEncoder;

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "Back Left"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "Back Right"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "Front Right"));

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Left with math", encoderTicksToInches(leftEncoder.getCurrentPosition()));
            telemetry.addData("Right with math", encoderTicksToInches(rightEncoder.getCurrentPosition()));
            telemetry.addData("Mid with math", encoderTicksToInches(frontEncoder.getCurrentPosition()));

            telemetry.addData("Left", 0.0004 * leftEncoder.getCurrentPosition());
            telemetry.addData("Right", 0.0004 * rightEncoder.getCurrentPosition());
            telemetry.addData("Mid", 0.0004 * frontEncoder.getCurrentPosition());

            telemetry.update();
        }

    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }
}
