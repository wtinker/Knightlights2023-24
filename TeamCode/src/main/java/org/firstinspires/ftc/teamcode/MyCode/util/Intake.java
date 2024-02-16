package org.firstinspires.ftc.teamcode.MyCode.util;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private LinearOpMode myOpMode = null;
    DcMotor intake;
    public boolean isPowered = false;
    public boolean isRaised = true;
    Servo servo, servoup;
    double up = 0.3;
    double down = 0;
    public Intake (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        servo = hardwareMap.get(Servo.class, "Intake servo");
        servo.setPosition(up);

        servoup = hardwareMap.get(Servo.class, "Intake servo 2");
        servoup.setPosition(0.17);

        myOpMode.telemetry.addData("Intake Initialized", null);
        myOpMode.telemetry.update();
    }
    public void Start(){
        intake.setPower(1);
        isPowered = true;
        Lower();
    }
    public void Stop(){
        intake.setPower(0);
        isPowered = false;
    }
    public void Reverse(){
        intake.setPower(-1);
        isPowered = true;
    }
    public void Toggle(){
        if(isPowered){
            Stop();
        } else {
            Start();
        }
    }
    public void Raise(){
        isRaised = true;
        Stop();
        servo.setPosition(up);
        servoup.setPosition(0.17);
    }
    public void Lower(){
        isRaised = false;
        servo.setPosition(down);
        servoup.setPosition(0);
    }
}
