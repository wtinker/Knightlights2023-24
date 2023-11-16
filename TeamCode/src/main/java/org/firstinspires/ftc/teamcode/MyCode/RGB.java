package org.firstinspires.ftc.teamcode.MyCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RGB {
    private LinearOpMode myOpMode = null;
    RevBlinkinLedDriver LED;

    RevBlinkinLedDriver.BlinkinPattern red;
    RevBlinkinLedDriver.BlinkinPattern blue;
    RevBlinkinLedDriver.BlinkinPattern green;
    RevBlinkinLedDriver.BlinkinPattern end;
    RevBlinkinLedDriver.BlinkinPattern old;
    RevBlinkinLedDriver.BlinkinPattern current;
    public RGB(LinearOpMode opmode, HardwareMap hardwareMap){
        myOpMode = opmode;
        hardwareMap = hardwareMap;

        LED = hardwareMap.get(RevBlinkinLedDriver.class, "LED");

        red = RevBlinkinLedDriver.BlinkinPattern.RED;
        blue = RevBlinkinLedDriver.BlinkinPattern.BLUE;
        green = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        end = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
    }
    boolean inEndGame = false;
    boolean isRed;
    public void SetBlue(){
        isRed = false;
    }
    public void SetRed(){
        isRed = true;
    }
    public void SetGreen(){

    }
    public void Run(double time){
        old = current;
        if(time > 90){inEndGame = true;}
        if(inEndGame){current = end;}
        else if(isRed){current = red;}
        else if(!isRed){current = blue;}
        LED.setPattern(current);
    }
}
