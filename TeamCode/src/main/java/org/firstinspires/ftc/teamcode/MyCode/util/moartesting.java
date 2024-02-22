package org.firstinspires.ftc.teamcode.MyCode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "moar")
public class moartesting extends LinearOpMode {

    public void runOpMode(){
        Output output = new Output(this, hardwareMap);

        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.right_bumper){output.BaseOut();}
            if(gamepad1.right_trigger > 0.5){output.BaseIn();}
            if(gamepad1.left_bumper){output.Single();}
            if(gamepad1.left_trigger > 0.5){output.Taken();}
            if(gamepad1.b){output.Open();}
            if(gamepad1.y){output.Half();}
            if(gamepad1.x){output.Close();}
        }
    }

}
