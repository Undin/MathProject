package com.ifmo.mathproject;

import java.util.Objects;

/**
 * Created by warrior on 10.12.14.
 */
public abstract class Method<D, T extends Layer<D>> {

    protected final Model model;

    public Method(Model model) {
        this.model = Objects.requireNonNull(model);
    }

    public Model getModel() {
        return model;
    }

    public abstract T nextLayer(T... previousLayers);
}
