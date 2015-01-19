package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Model;
import com.ifmo.mathproject.Utils;

public class NewtonLinearization extends Method1D {
    private final double dDivDz2;
    private final double invDt;
    private final double lambdaDivPCdz2;
    private final double eDivR;

    public NewtonLinearization(Model model) {
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

        for (int i = 0; i < size; i++) {
            prev[i] = dDivDz2;
            double exp = Math.exp(-eDivR / prevIterationTemperature[i]);
            cur[i] = -invDt -
                    2 * dDivDz2 -
                    model.getAlpha() * model.getK()  * exp;
            next[i] = dDivDz2;
            freeCoef[i] = prevLayerConcentration[i] * (-invDt + model.getK() * (1 - model.getAlpha()) * exp);
        }
        //freeCoef[0] -= prev[0] * prevIterationConcentration[0];
        freeCoef[size - 1] -= next[size - 1] * prevIterationConcentration[size - 1];

        prevIterationConcentration = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

        for (int i = 0; i < size; i++) {
            prev[i] = lambdaDivPCdz2;
            double ultimateFactor = model.getQ() * model.getK() / model.getC() * Math.pow(prevIterationConcentration[i], model.getAlpha()) *
                    Math.exp(- eDivR / prevIterationTemperature[i]);
            cur[i] = -invDt - 2 * lambdaDivPCdz2 + ultimateFactor * eDivR / Math.pow(prevIterationTemperature[i], 2);
            next[i] = lambdaDivPCdz2;
            freeCoef[i] = -prevIterationTemperature[i] * invDt - ultimateFactor * (1 - eDivR / prevIterationTemperature[i]);
        }
        //freeCoef[0] -= prev[0] * prevIterationTemperature[0];
        freeCoef[size - 1] -= next[size - 1] * prevIterationTemperature[size - 1];

        prevIterationTemperature = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

        return new Layer1D(prevIterationConcentration, prevIterationTemperature);
    }
}
