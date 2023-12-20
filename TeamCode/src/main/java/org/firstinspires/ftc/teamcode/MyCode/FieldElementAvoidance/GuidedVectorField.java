package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

public class GuidedVectorField {
    double GAIN;
    public GuidedVectorField(){
        GAIN = 0.1;
    }
    public GuidedVectorField(double gain){
        GAIN = gain;
    }
    public Vector returnVector(double x, double y, double rot){
        Vector out = new Vector(0, 0);

        Vector p1 = new Vector(x, y+24, true);
        Vector p2 = new Vector(x, y+48, true);
        Vector p3 = new Vector(x, y+72, true);
        Vector p4 = new Vector(x+24, y+24, true);
        Vector p5 = new Vector(x+24, y+48, true);
        Vector p6 = new Vector(x+24, y+72, true);
        Vector p7 = new Vector(x, y-24, true);
        Vector p8 = new Vector(x, y-48, true);
        Vector p9 = new Vector(x, y-72, true);
        Vector p10 = new Vector(x+24, y-24, true);
        Vector p11 = new Vector(x+24, y-48, true);
        Vector p12 = new Vector(x+24, y-72, true);

        out.addVector(p1);
        out.addVector(p2);
        out.addVector(p3);
        out.addVector(p4);
        out.addVector(p5);
        out.addVector(p6);
        out.addVector(p7);
        out.addVector(p8);
        out.addVector(p9);
        out.addVector(p10);
        out.addVector(p11);
        out.addVector(p12);

        out.rotate(rot * -1);
        out.scale(GAIN);

        return out;
    }
    public double returnX(double x, double y, double rot){
        Vector out = returnVector(x, y, rot);
        return out.getxValue();
    }
    public double returnY(double x, double y, double rot){
        Vector out = returnVector(x, y, rot);
        return out.getyValue();
    }
}
