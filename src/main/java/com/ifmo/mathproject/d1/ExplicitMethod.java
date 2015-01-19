package com.ifmo.mathproject.d1;

public class ExplicitMethod extends Method1D {
    public ExplicitMethod(Model1D model) {
        super(model);
    }

    private double x2Derivation(double[] f, int i) {
        return (f[i - 1] - 2 * f[i] + f[i + 1]) / (model.getDx() * model.getDx());
    }

    private double w(double x, double t) {
        return model.getK() * Math.pow(x, model.getAlpha()) * Math.exp(-model.getE() / (model.getR() * t));
    }

    @Override
    public Layer1D nextLayer(Layer1D... previousLayers) {
        final Layer1D prevLayer = previousLayers[0];
        final int size = prevLayer.size();
        final double[] prevLayerConcentration = prevLayer.getConcentration();
        final double[] prevLayerTemperature = prevLayer.getTemperature();
        final double[] concentration = new double[size];
        final double[] temperature = new double[size];

        for (int i = 1; i < size - 1; i++) {
            concentration[i] = (model.getD() *
                    x2Derivation(prevLayerConcentration, i) -
                    w(prevLayerConcentration[i], prevLayerTemperature[i])) *
                    model.getDt() + prevLayerConcentration[i];
            temperature[i] = (model.getLambda() / (model.getP() * model.getC()) *
                    x2Derivation(prevLayerTemperature, i) +
                    model.getQ() / model.getC() * w(concentration[i], prevLayerTemperature[i])) *
                    model.getDt() + prevLayerTemperature[i];
        }
        concentration[0] = prevLayerConcentration[0];
        temperature[0] = prevLayerTemperature[0];
        concentration[size - 1] = concentration[size - 2];
        temperature[size - 1] = temperature[size - 2];

        return new Layer1D(concentration, temperature);
    }
}
