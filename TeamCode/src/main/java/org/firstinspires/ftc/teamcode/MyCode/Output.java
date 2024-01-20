package org.firstinspires.ftc.teamcode.MyCode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Output {
    private LinearOpMode myOpMode = null;
    DcMotor Slide, Slide2;
    Servo Base, Claw, Drone;
    int SlideTarget = 0;
    public boolean SlideExtended = false;
    //ClawStatus clawStatus = ClawStatus.OPEN;
    boolean scoring = false;
    private double scoreTime;
    public Output (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        Slide = hardwareMap.get(DcMotor.class, "Slide312");
        Slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Slide2 = hardwareMap.get(DcMotor.class, "Slide223");
        Slide2.setDirection(DcMotorSimple.Direction.REVERSE);
        Slide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Base = hardwareMap.get(Servo.class, "Base");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Drone = hardwareMap.get(Servo.class, "Drone");
        Base.setPosition(1);
        Claw.setPosition(0.5);
        Drone.setPosition(0);

        myOpMode.telemetry.addData("Output Initialized", null);
        myOpMode.telemetry.update();
    }
    public void Extend(int targ){
        SlideTarget = targ;
        SlideExtended = true;
        RunOutput();
        //Double();
    }
    public void Adjust(int value){
        SlideTarget += value;
        if(SlideTarget < 700){SlideTarget = 700;}
    }
    public void Retract(){
        SlideExtended = false;
        SlideTarget = 0;
        RunOutput();
        //Open();
    }
    public void Climb(){
        SlideExtended = false;
        SlideTarget = 0;
        RunOutput();
    }
    public void RunOutput(){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(1);

        if(!SlideExtended){Descore();}
        //if(myOpMode.getRuntime() - scoreTime > 2 && scoring){Descore();}
    }
    public void Score(){
        scoring = true;
        Base.setPosition(0.8);
        scoreTime = myOpMode.getRuntime();
    }
    public void Descore(){
        scoring = false;
        Base.setPosition(1);
    }
    public void Toggle(){
        if(scoring){Descore();}
        else{Score();}
    }
    public void Single(){
        Base.setPosition(0.87);
        scoring = true;
    }
    public void Double(){
        Claw.setPosition(0.5);
        //clawStatus = ClawStatus.DOUBLE;
        if(SlideExtended){Base.setPosition(0);}
    }
    public void Open(){
        Claw.setPosition(0.6);
        //clawStatus = ClawStatus.OPEN;
        if(SlideExtended){Base.setPosition(0.6);}
    }
    /*
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
     */
    public int DetectPixels(){
        return 0;
    }
    public void LaunchDrone(){
        Drone.setPosition(0.3);
    }
}
