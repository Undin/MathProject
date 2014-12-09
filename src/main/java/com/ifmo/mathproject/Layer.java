package com.ifmo.mathproject;

import java.util.Objects;

/**
 * Created by warrior on 10.12.14.
 */
public abstract class Layer<T> {

    private final T concentration;
    private final T temperature;

    public Layer(T concentration, T temperature) {
        this.concentration = Objects.requireNonNull(concentration);
        this.temperature = Objects.requireNonNull(temperature);
    }

    public final T getConcentration() {
        return concentration;
    }

    public final T getTemperature() {
        return temperature;
    }
}
