package org.firstinspires.ftc.teamcode.MyCode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp(name = "DualServoTest")
public class DualServoTest extends LinearOpMode {
    Servo s1, s2;
    public void runOpMode(){
        s1 = hardwareMap.get(Servo.class, "Test1");
        s2 = hardwareMap.get(Servo.class, "Test2");
        s1.setPosition(0);
        s2.setPosition(1);
        waitForStart();
        while(opModeIsActive()){
        if(gamepad1.right_bumper){runSmooth(s1.getPosition(), 1, s2.getPosition(), 0);}
        if(gamepad1.left_bumper){runSmooth(s1.getPosition(), 0, s2.getPosition(), 1);}}
    }

    public void runSmooth(double start1, double end1, double start2, double end2){
        int loops = 100;
        double diff1 = end1 - start1;
        double diff2 = end2 - start2;
        double step1 = diff1 / loops;
        double step2 = diff2 / loops;
        for(int i = 1; i<=loops; i++){
            s1.setPosition(start1 + i*step1);
            s2.setPosition(start2 + i*step2);
        }
    }

}
