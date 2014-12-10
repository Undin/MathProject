package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Layer;

/**
 * Created by warrior on 10.12.14.
 */
public class Layer1D extends Layer<double[]> {

    private final int size;

    public Layer1D(double[] concentration, double[] temperature) {
        super(concentration, temperature);
        if (concentration.length != temperature.length) {
            throw new IllegalArgumentException("concentration.length != temperature.length");
        }
        size = concentration.length;
    }

    public int size() {
        return size;
    }
}
