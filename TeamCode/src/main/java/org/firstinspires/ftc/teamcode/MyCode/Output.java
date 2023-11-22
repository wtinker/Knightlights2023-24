package org.firstinspires.ftc.teamcode.MyCode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Output {
    private LinearOpMode myOpMode = null;
    DcMotor Slide;
    Servo Base, Claw;
    int SlideTarget = 0;
    boolean SlideExtended = false;
    ClawStatus clawStatus = ClawStatus.OPEN;
    public Output (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        Slide = hardwareMap.get(DcMotor.class, "Slide");
        Slide.setDirection(DcMotorSimple.Direction.REVERSE);
        Slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Base = hardwareMap.get(Servo.class, "Base");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Base.setPosition(0);
        Claw.setPosition(0.5);

        myOpMode.telemetry.addData("Output Initialized", null);
        myOpMode.telemetry.update();
    }
    public void Extend(int targ){
        SlideTarget = targ;
        SlideExtended = true;
        Double();
    }
    public void Adjust(int value){
        SlideTarget += value;
        if(SlideTarget < 700){SlideTarget = 700;}
    }
    public void Retract(){
        SlideExtended = false;
        SlideTarget = 0;
        Open();
    }
    public void RunOutput(){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(0.6);

        if(!SlideExtended){
            Base.setPosition(0.02);
        } else if(SlideExtended && Slide.getCurrentPosition() > 700){
            Base.setPosition(0.6);
        }
    }
    public void Single(){
        Claw.setPosition(0.525);
        clawStatus = ClawStatus.SINGLE;
    }
    public void Double(){
        Claw.setPosition(0.5);
        clawStatus = ClawStatus.DOUBLE;
    }
    public void Open(){
        Claw.setPosition(0.6);
        clawStatus = ClawStatus.OPEN;
    }
    public void Cycle(){
        switch (clawStatus){
            case OPEN:
                Double();
                break;
            case DOUBLE:
                Open();
                break;
            case SINGLE:
                Open();
                break;
        }
    }
    public int DetectPixels(){
        return 0;
    }
}
