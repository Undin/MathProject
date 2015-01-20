package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Utils;

public class NewtonLinearization extends Method1D {
    private final double dDivDz2;
    private final double invDt;
    private final double lambdaDivPCdz2;
    private final double eDivR;

    public NewtonLinearization(Model1D model) {
        super(model);
        dDivDz2 = model.getD() / (model.getDx() * model.getDx());
        invDt = 1 / model.getDt();
        lambdaDivPCdz2 = model.getLambda() / (model.getP() * model.getC() * model.getDx() * model.getDx());
        eDivR = model.getE() / model.getR();
    }

    @Override
    public Layer1D nextLayer(Layer1D... previousLayers) {
        Layer1D prevLayer = previousLayers[0];
        int size = prevLayer.size();

        final double[] prev = new double[size];
        final double[] cur = new double[size];
        final double[] next = new double[size];
        final double[] freeCoef = new double[size];

        final double[] prevLayerConcentration = prevLayer.getConcentration();
        final double[] prevLayerTemperature = prevLayer.getTemperature();
        double[] prevIterationConcentration = prevLayerConcentration;
        double[] prevIterationTemperature = prevLayerTemperature;

        cur[0] = 1;
        next[0] = 0;
        freeCoef[0] = prevIterationConcentration[0];
        for (int i = 1; i < size - 1; i++) {
            prev[i] = dDivDz2;
            double exp = Math.exp(-eDivR / prevIterationTemperature[i]);
            cur[i] = -invDt -
                    2 * dDivDz2 -
                    model.getAlpha() * model.getK()  * exp;
            next[i] = dDivDz2;
            freeCoef[i] = prevLayerConcentration[i] * (-invDt + model.getK() * (1 - model.getAlpha()) * exp);
        }
        cur[size - 1] = 1;
        prev[size - 1] = 0;
        freeCoef[size - 1] = prevIterationConcentration[size - 1];

        prevIterationConcentration = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

        cur[0] = 1;
        next[0] = 0;
        freeCoef[0] = prevIterationTemperature[0];
        for (int i = 1; i < size - 1; i++) {
            prev[i] = lambdaDivPCdz2;
            double ultimateFactor = model.getQ() * model.getK() / model.getC() * Math.pow(prevIterationConcentration[i], model.getAlpha()) *
                    Math.exp(- eDivR / prevIterationTemperature[i]);
            cur[i] = -invDt - 2 * lambdaDivPCdz2 + ultimateFactor * eDivR / Math.pow(prevIterationTemperature[i], 2);
            next[i] = lambdaDivPCdz2;
            freeCoef[i] = -prevIterationTemperature[i] * invDt - ultimateFactor * (1 - eDivR / prevIterationTemperature[i]);
        }
        cur[size - 1] = 1;
        prev[size - 1] = 0;
        freeCoef[size - 1] = prevIterationTemperature[size - 1];

        prevIterationTemperature = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

        return new Layer1D(prevIterationConcentration, prevIterationTemperature);
    }
}
