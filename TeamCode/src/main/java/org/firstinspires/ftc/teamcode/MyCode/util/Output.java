package org.firstinspires.ftc.teamcode.MyCode.util;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Output {
    private LinearOpMode myOpMode = null;
    DcMotor Slide, Slide2;
    Servo Base, Claw, Claw2, Drone, Single;
    ClawStatus clawStatus = ClawStatus.OPEN;
    int SlideTarget = 0;
    public boolean SlideExtended = false;
    //ClawStatus clawStatus = ClawStatus.OPEN;
    boolean scoring = false;
    private double scoreTime;
    boolean disable = false;
    double prevtime = 0;
    static double kP = 0.0025;
    static double kI = 0;
    static double kD = 0.000;
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
        Claw2 = hardwareMap.get(Servo.class, "Claw2");
        Drone = hardwareMap.get(Servo.class, "Drone");
        Single = hardwareMap.get(Servo.class, "Single");
        Single.setPosition(1);
        Base.setPosition(1);
        Claw.setPosition(0.26);
        Claw2.setPosition(0.26);
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
    }
    public void Retract(){
        SlideExtended = false;
        SlideTarget = 0;
    }
    public void RunOutput(){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(1);
        if(!SlideExtended || Slide.getCurrentPosition() < 1000){Descore();}
        else{Score();}
    }
    public void RunOutputRetracted(){
        Slide.setTargetPosition(SlideTarget);
        Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slide.setPower(1);
        Descore();
    }
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
        if(!SlideExtended || Slide.getCurrentPosition() < 1000){Descore();}
        else {Score();}
        myOpMode.telemetry.addData("error:", error);
    }
    public void Climb(){
        Descore();
        Slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slide2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slide.setPower(-1);
        Slide2.setPower(-1);
    }
    public void Score(){
        scoring = true;
        Base.setPosition(0.47);
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
    public void Single() {
        Single.setPosition(0.78);
    }
    public void Taken(){
        Single.setPosition(1);
    }
    public void BaseOut(){
        Base.setPosition(0.47);
    }
    public void BaseIn(){
        Base.setPosition(1);
    }
    public void Close(){
        Claw.setPosition(0.7);
        Claw2.setPosition(0.7);
        clawStatus = ClawStatus.DOUBLE;
    }
    public void Half(){
        Claw.setPosition(0.7);
        Claw.setPosition(0.26);
        clawStatus = ClawStatus.SINGLE;
    }
    public void Open(){
        Claw.setPosition(0.26);
        Claw2.setPosition(0.26);
        clawStatus = ClawStatus.OPEN;
    }
    public void Cycle(){
        switch (clawStatus){
            case SINGLE:
                Open();
                break;
            case OPEN:
                Close();
                break;
            case DOUBLE:
                Half();
                break;
        }
    }
    public int DetectPixels(){
        return 0;
    }
    public void LaunchDrone(){
        Drone.setPosition(1);
    }
    public void Read(){
        myOpMode.telemetry.addData("Encoder value", Slide.getCurrentPosition());
    }
    public void Disable(){
        Slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Slide.setPower(0);
        Slide2.setPower(0);
    }
}
