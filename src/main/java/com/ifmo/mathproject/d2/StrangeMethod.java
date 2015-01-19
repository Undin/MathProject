package com.ifmo.mathproject.d2;

/**
 * Created by Whiplash on 20.01.2015.
 */
public class StrangeMethod extends Method2D {

    public StrangeMethod(Model2D model) {
        super(model);
    }

    @Override
    public Layer2D nextLayer(Layer2D... previousLayers) {
        return previousLayers[0];
    }
}
