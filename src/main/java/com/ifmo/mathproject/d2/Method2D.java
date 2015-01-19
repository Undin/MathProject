package com.ifmo.mathproject.d2;

import com.ifmo.mathproject.Method;
import com.ifmo.mathproject.Model;

/**
 * Created by warrior on 20.01.15.
 */
public class Method2D extends Method<Model2D, double[][], Layer2D> {

    public Method2D(Model2D model) {
        super(model);
    }

    @Override
    public Layer2D nextLayer(Layer2D... previousLayers) {
        return null;
    }
}
