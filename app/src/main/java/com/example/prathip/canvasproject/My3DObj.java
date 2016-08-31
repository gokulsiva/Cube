package com.example.prathip.canvasproject;

/**
 * Created by Prathip on 30-08-2016.
 */
public class My3DObj {
    protected double x;
    protected double y;
    protected double z;

    public My3DObj(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public My3DObj rotateX(double angle) {
        double rad, cosa, sina, yn, zn;

        rad = angle * Math.PI / 180;
        cosa = Math.cos(rad);
        sina = Math.sin(rad);
        yn = this.y * cosa - this.z * sina;
        zn = this.y * sina + this.z * cosa;

        return new My3DObj(this.x, yn, zn);
    }

    public My3DObj rotateY(double angle) {
        double rad, cosa, sina, xn, zn;

        rad = angle * Math.PI / 180;
        cosa = Math.cos(rad);
        sina = Math.sin(rad);
        zn = this.z * cosa - this.x * sina;
        xn = this.z * sina + this.x * cosa;

        return new My3DObj(xn, this.y, zn);
    }

    public My3DObj rotateZ(double angle) {
        double rad, cosa, sina, xn, yn;

        rad = angle * Math.PI / 180;
        cosa = Math.cos(rad);
        sina = Math.sin(rad);
        xn = this.x * cosa - this.y * sina;
        yn = this.x * sina + this.y * cosa;

        return new My3DObj(xn, yn, this.z);
    }

    public My3DObj project(int viewWidth, int viewHeight, float fov, float viewDistance) {
        double factor, xn, yn;

        factor = fov / (viewDistance + this.z);
        xn = this.x * factor + viewWidth / 2;
        yn = this.y * factor + viewHeight / 2;

        return new My3DObj(xn, yn, this.z);
    }
}
