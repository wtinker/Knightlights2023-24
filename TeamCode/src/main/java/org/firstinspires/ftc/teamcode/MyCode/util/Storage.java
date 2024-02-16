package org.firstinspires.ftc.teamcode.MyCode.util;


import com.acmerobotics.roadrunner.geometry.Pose2d;

public class Storage {
    public static boolean targetFound = false;
    public static double drive;
    public static double strafe;
    public static double turn;
    public static int aprilTagTarg = 0;
    public static Pose2d poseStorage = new Pose2d(0, 0, 0);
}
