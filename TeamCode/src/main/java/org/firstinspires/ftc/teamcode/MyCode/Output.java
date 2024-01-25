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
    boolean disable = false;
    double prevtime = 0;
    static double kP = 0.0025;
    static double kI = 0.00;
    static double kD = 0.0000;
    double preverror = 0;
    double Pgain = 0;
    double Igain = 0;
    double Dgain = 0;
    public Output (LinearOpMode opmode, HardwareMap hardwareMap) {
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        Slide = hardwareMap.get(DcMotor.class, "Slide312");
        Slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Slide2 = hardwareMap.get(DcMotor.class, "Slide223");
        Slide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slide2.setDirection(DcMotorSimple.Direction.REVERSE);

        Base = hardwareMap.get(Servo.class, "Base");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Drone = hardwareMap.get(Servo.class, "Drone");
        Base.setPosition(1);
        Claw.setPosition(0.5);
        Drone.setPosition(0);

        myOpMode.telemetry.addData("Output Initialized", null);
        myOpMode.telemetry.update();
    }
    public void SetTime(double runtime){
        prevtime = runtime;
    }
    public void Extend(int targ){
        SlideTarget = targ;
        SlideExtended = true;
        //RunOutput();
        //Double();
    }
    public void Adjust(int value){
        SlideTarget += value;
        if(SlideTarget < 700){SlideTarget = 700;}
    }
    public void Retract(){
        SlideExtended = false;
        SlideTarget = 0;
        //Slide2.setPower(-Slide.getPower());
        //RunOutput();
        //Open();
    }
    public void RunOutput(){
        if(!disable){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(1);
        //if(Math.abs(Slide.getCurrentPosition() - SlideTarget) < 100){Slide2.setPower(0);}
        if(!SlideExtended){Descore();}
        //Slide2.setPower(-Slide.getPower());
        //if(myOpMode.getRuntime() - scoreTime > 2 && scoring){Descore();}
    }}
    public void SlidePID(double runtime){
        double elapsed = runtime - prevtime;
        double error = SlideTarget - Slide.getCurrentPosition();
        Pgain = kP * error;
        Igain += kI * error * elapsed;
        Dgain = kD * (error - preverror) / elapsed;
        double gain = Pgain + Igain + Dgain;
        if (gain > 1) {gain = 1;}
        if (gain < -1) {gain = -1;}
        Slide.setPower(gain);
        Slide2.setPower(gain);
        prevtime = runtime;
        preverror = error;
        if(!SlideExtended){Descore();}
        myOpMode.telemetry.addData("error:", error);
    }
    public void Climb(){
        SlideTarget = 0;
        SlideExtended = false;
        Slide2.setPower(-Slide.getPower());
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
    public int DetectPixels(){
        return 0;
    }
    public void LaunchDrone(){
        Drone.setPosition(0.3);
    }
}
