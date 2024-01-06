package org.firstinspires.ftc.teamcode.MyCode;

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
    Servo servo;
    double up = 0;
    double down = 0.38;
    public Intake (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        servo = hardwareMap.get(Servo.class, "Intake servo");
        servo.setPosition(up);

        myOpMode.telemetry.addData("Intake Initialized", null);
        myOpMode.telemetry.update();
    }
    public void Start(){
        intake.setPower(1);
        isPowered = true;
    }
    public void Stop(){
        intake.setPower(0);
        isPowered = false;
    }
    public void Reverse(){
        intake.setPower(-0.5);
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
    }
    public void Lower(){
        isRaised = false;
        servo.setPosition(down);
    }
}
