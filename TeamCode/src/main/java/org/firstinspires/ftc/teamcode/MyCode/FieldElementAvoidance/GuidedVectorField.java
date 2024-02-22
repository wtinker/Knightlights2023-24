package org.firstinspires.ftc.teamcode.MyCode.FieldElementAvoidance;

public class GuidedVectorField {
    double xGain, yGain;
    double range = 24;
    double scaling = 24;
    public GuidedVectorField(){
        xGain = 1;
        yGain = 1;
    }
    public GuidedVectorField(double Xgain, double Ygain){
        xGain = Xgain;
        yGain = Ygain;
    }
    public Vector returnVector(double x, double y, double rot){
        Vector out = new Vector();
        Vector pos = new Vector(x, y);

        Vector p1 = delta(pos, new Vector(0, -24));
        Vector p2 = delta(pos, new Vector(0, -48));
        Vector p3 = delta(pos, new Vector(0, -72));
        Vector p4 = delta(pos, new Vector(-24, -24));
        Vector p5 = delta(pos, new Vector(-24, -48));
        Vector p6 = delta(pos, new Vector(-24, -72));
        Vector p7 = delta(pos, new Vector(0, 24));
        Vector p8 = delta(pos, new Vector(0, 48));
        Vector p9 = delta(pos, new Vector(0, 72));
        Vector p10 = delta(pos, new Vector(-24, 24));
        Vector p11 = delta(pos, new Vector(-24, 48));
        Vector p12 = delta(pos, new Vector(-24, 72));
        Vector p13 = delta(pos, new Vector(-12, -24));
        Vector p14 = delta(pos, new Vector(-12, -48));
        Vector p15 = delta(pos, new Vector(-12, -72));
        Vector p16 = delta(pos, new Vector(-12, 24));
        Vector p17 = delta(pos, new Vector(-12, 48));
        Vector p18 = delta(pos, new Vector(-12, 72));

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
        out.addVector(p13);
        out.addVector(p14);
        out.addVector(p15);
        out.addVector(p16);
        out.addVector(p17);
        out.addVector(p18);

        out.scale(xGain, yGain);
        out.clip(0.8);
        out.rotate(-rot);

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
    Vector delta(Vector pos, Vector coord){
        Vector out = new Vector();
        coord.rotate(Math.PI);
        Vector sum = Vector.addVectors(pos, coord);
        if(sum.getMag() <= range){out.addVector(sum);}
        out.scale(scaling);
        out.invert();
        return out;
    }

}
