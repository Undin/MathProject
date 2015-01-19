package com.ifmo.mathproject;

import java.util.Objects;

/**
 * Created by warrior on 10.12.14.
 */
public abstract class Method<M extends Model, D, T extends Layer<D>> {

    protected final M model;

    public Method(M model) {
        this.model = Objects.requireNonNull(model);
    }

    public M getModel() {
        return model;
    }

    public abstract T nextLayer(T... previousLayers);
}
