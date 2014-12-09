package com.ifmo.mathproject;

/**
 * Created by warrior on 10.12.14.
 */
public interface Method<D, T extends Layer<D>> {
    public T nextLayer(T currentLayer);
}
