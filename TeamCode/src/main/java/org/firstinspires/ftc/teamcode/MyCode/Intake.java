package org.firstinspires.ftc.teamcode.MyCode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {
    private LinearOpMode myOpMode = null;
    DcMotor intake;
    boolean isPowered = false;
    public Intake (LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        myOpMode.telemetry.addData("Intake Initialized", null);
    }
    public void Start(){
        intake.setPower(0.5);
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
