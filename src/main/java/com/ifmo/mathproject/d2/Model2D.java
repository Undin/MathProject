package com.ifmo.mathproject.d2;

import com.ifmo.mathproject.d1.Model1D;

/**
 * Created by warrior on 20.01.15.
 */
public class Model2D extends Model1D {

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    private double dy = 0.001;
    private int yN = 100;

    public int getYN() {
        return yN;
    }

    public void setYN(int yn) {
        yN = yn;
    }
}
