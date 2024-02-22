package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

import com.acmerobotics.roadrunner.geometry.Vector2d;

public class Vector {
    double xValue, yValue, mag, theta;
    public Vector(){
        xValue = 0;
        yValue = 0;
        mag = 0;
        theta = 0;
    }
    public Vector(double x, double y){
        xValue = x;
        yValue = y;
        mag = Math.sqrt(xValue*xValue + yValue*yValue);
        theta = Math.atan2(yValue, xValue);
        if((xValue > 0 && yValue < 0) || (xValue < 0 && yValue > 0)){theta += Math.PI;}
        else if(xValue < 0 && yValue < 0){theta += 2*Math.PI;}
    }
    public double getxValue(){return xValue;}
    public double getyValue(){return yValue;}
    public double getMag(){return mag;}
    public double getTheta(){return theta;}
    public void rotate(double rad){
        theta += rad;
        xValue = mag*Math.cos(theta);
        yValue = mag*Math.sin(theta);
    }
    public void invert(){
        if(mag != 0) {
            mag = 1 / mag;
            xValue = mag * Math.cos(theta);
            yValue = mag * Math.sin(theta);
        }
    }
    public void addVector(Vector v1){
        xValue += v1.getxValue();
        yValue += v1.getyValue();
        mag = Math.sqrt(xValue*xValue + yValue*yValue);
        theta = Math.atan2(yValue, xValue);
        if((xValue > 0 && yValue < 0) || (xValue < 0 && yValue > 0)){theta += Math.PI;}
        else if(xValue < 0 && yValue < 0){theta += 2*Math.PI;}
    }
    public void scale(double xFactor, double yFactor){
        xValue = xValue * xFactor;
        yValue = yValue * yFactor;
        mag = Math.sqrt(xValue*xValue + yValue*yValue);
        theta = Math.atan2(yValue, xValue);
        if((xValue > 0 && yValue < 0) || (xValue < 0 && yValue > 0)){theta += Math.PI;}
        else if(xValue < 0 && yValue < 0){theta += 2*Math.PI;}
    }
    public void scale(double factor){
        mag = mag * factor;
        xValue = mag * Math.cos(theta);
        yValue = mag * Math.sin(theta);
    }
    public void clip(double range){
        if(mag > range){mag = range;}
        xValue = mag * Math.cos(theta);
        yValue = mag * Math.sin(theta);
    }
    public Vector2d toV2D(){
        Vector2d out = new Vector2d(xValue, yValue);
        return out;
    }
    public static Vector addVectors(Vector v1, Vector v2){
        v1.addVector(v2);
        return v1;
    }
}
