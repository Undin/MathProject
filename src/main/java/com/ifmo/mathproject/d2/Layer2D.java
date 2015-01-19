package com.ifmo.mathproject.d2;

import com.ifmo.mathproject.Layer;

/**
 * Created by warrior on 10.12.14.
 */
public class Layer2D extends Layer<double[][]> {

    private final int sizeX;
    private final int sizeY;

    public Layer2D(double[][] concentration, double[][] temperature) {
        super(concentration, temperature);
        if (concentration.length != temperature.length) {
            throw new IllegalArgumentException("concentration.length != temperature.length");
        }
        sizeY = concentration.length;
        sizeX = concentration[0].length;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
