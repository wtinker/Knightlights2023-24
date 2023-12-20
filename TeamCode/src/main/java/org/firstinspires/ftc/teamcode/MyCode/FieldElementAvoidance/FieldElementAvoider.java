package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

public class FieldElementAvoider extends GuidedVectorField{
    public FieldElementAvoider(double gain){
        super(gain);
    }
    public FieldElementAvoider(){
        super();
    }
    public Vector getCorrectedVector(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = new Vector(0, 0);
        Vector correction = returnVector(xPos, yPos, rot);
        Vector input = new Vector(xInput, yInput);
        out.addVector(correction);
        out.addVector(input);
        out.clip();
        return out;
    }
    public double getCorrectedX(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = new Vector(0, 0);
        Vector correction = returnVector(xPos, yPos, rot);
        Vector input = new Vector(xInput, yInput);
        out.addVector(correction);
        out.addVector(input);
        out.clip();
        return out.getxValue();
    }
    public double getCorrectedY(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = new Vector(0, 0);
        Vector correction = returnVector(xPos, yPos, rot);
        Vector input = new Vector(xInput, yInput);
        out.addVector(correction);
        out.addVector(input);
        out.clip();
        return out.getyValue();
    }
}
