package org.firstinspires.ftc.teamcode.MyCode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Output {
    private LinearOpMode myOpMode = null;
    DcMotor Slide;
    Servo Base, Claw;
    int SlideTarget = 0;
    boolean SlideExtended = false;
    ClawStatus clawStatus = ClawStatus.OPEN;
    public Output (LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        Slide = hardwareMap.get(DcMotor.class, "Slide");
        Slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Base = hardwareMap.get(Servo.class, "Base");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Base.setPosition(0);
        Claw.setPosition(0.4);

        myOpMode.telemetry.addData("Output Initialized", null);
    }
    public void Extend(int targ){
        SlideTarget = targ;
        SlideExtended = true;
        Double();
    }
    public void Adjust(int value){
        SlideTarget += value;
    }
    public void Retract(){
        SlideExtended = false;
        SlideTarget = 0;
        Open();
    }
    public void RunOutput(){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(0.8);

        if(!SlideExtended){
            Base.setPosition(1);
        } else if(SlideExtended && Slide.getCurrentPosition() > 1000){
            Base.setPosition(0.3);
        }
    }
    public void Single(){
        Claw.setPosition(0.5);
        clawStatus = ClawStatus.SINGLE;
    }
    public void Double(){
        Claw.setPosition(0.54);
        clawStatus = ClawStatus.DOUBLE;
    }
    public void Open(){
        Claw.setPosition(0.4);
        clawStatus = ClawStatus.OPEN;
    }
    public void Cycle(){
        switch (clawStatus){
            case OPEN:
                Double();
                break;
            case DOUBLE:
                Single();
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
