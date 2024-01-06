package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

public class FieldElementAvoider extends GuidedVectorField{
    public FieldElementAvoider(double Xgain, double Ygain){
        super(Xgain, Ygain);
    }
    public FieldElementAvoider(){
        super();
    }
    public Vector getCorrectedVector(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = new Vector();
        Vector correction = returnVector(xPos, yPos, rot);
        Vector input = new Vector(xInput, yInput);
        out.addVector(correction);
        out.addVector(input);
        out.clip(1);
        return out;
    }
    public double getCorrectedX(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = getCorrectedVector(xPos, yPos, rot, xInput, yInput);
        return out.getxValue();
    }
    public double getCorrectedY(double xPos, double yPos, double rot, double xInput, double yInput){
        Vector out = getCorrectedVector(xPos, yPos, rot, xInput, yInput);
        return out.getyValue();
    }
}
