package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Model;

/**
 * Created by warrior on 20.01.15.
 */
public class Model1D extends Model {

    private double dx = 0.001;
    private int xN = 100;

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public int getXN() {
        return xN;
    }

    public void setXN(int xn) {
        xN = xn;
    }
}
