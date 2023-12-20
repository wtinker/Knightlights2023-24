package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

public class Vector {
    double xValue, yValue, mag, theta;
    public Vector(double x, double y){
        xValue = x;
        yValue = y;
        mag = Math.sqrt(xValue*xValue + yValue*yValue);
        theta = Math.atan2(yValue, xValue);
    }
    public Vector(double x, double y, boolean inverted){
        if(inverted && (x!=0 || y!=0)){
            mag = Math.sqrt(x*x + y*y);
            theta = Math.atan2(y, x);
            mag = 1 / mag;
            xValue = mag*Math.cos(theta);
            yValue = mag*Math.sin(theta);
        }else{
            xValue = x;
            yValue = y;
            mag = Math.sqrt(xValue*xValue + yValue*yValue);
            theta = Math.atan2(yValue, xValue);
        }
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
    }
    public void scale(double factor){
        mag = mag * factor;
        xValue = mag * Math.cos(theta);
        yValue = mag * Math.sin(theta);
    }
    public void clip(){
        mag = 1;
        xValue = mag * Math.cos(theta);
        yValue = mag * Math.sin(theta);
    }
}
