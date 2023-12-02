package org.firstinspires.ftc.teamcode.MyCode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private LinearOpMode myOpMode = null;
    DcMotor intake;
    boolean isPowered = false;
    public Intake (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        myOpMode.telemetry.addData("Intake Initialized", null);
        myOpMode.telemetry.update();
    }
    public void Start(){
        intake.setPower(0.9);
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
}
