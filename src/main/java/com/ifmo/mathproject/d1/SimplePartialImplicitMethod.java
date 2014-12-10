package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Model;
import com.ifmo.mathproject.Utils;

/**
 * Created by warrior on 11.12.14.
 */
public class SimplePartialImplicitMethod extends Method1D {

    public SimplePartialImplicitMethod(Model model) {
        super(model);
    }

    @Override
    public Layer1D nextLayer(Layer1D... previousLayers) {
        Layer1D prevLayer = previousLayers[0];
        double[] prev = new double[prevLayer.size()];
        double[] cur = new double[prevLayer.size()];
        double[] next = new double[prevLayer.size()];
        double[] freeCoef = new double[prevLayer.size()];

        final double dx2DivDt = model.getDx() * model.getDx() / model.getDt();
        final double dx2OnK = model.getDx() * model.getDx() * model.getK();
        final double EOnR = model.getE() / model.getR();
        for (int i = 0; i < prevLayer.size(); i++) {
            prev[i] = -model.getD();
            cur[i] = dx2DivDt +
                    2 * model.getD() +
                    dx2OnK * Math.pow(prevLayer.getConcentration()[i], model.getAlpha() - 1) * Math.exp(-EOnR / prevLayer.getTemperature()[i]);
            next[i] = -model.getD();
            freeCoef[i] = dx2DivDt * prevLayer.getConcentration()[i];
        }
        double[] newConcentration = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

        final double lambdaDivCDivP = model.getLambda() / model.getP() / model.getC();
        final double dx2OnKOnQDivC = dx2OnK * model.getQ() / model.getC();
        for (int i = 0; i < prevLayer.size(); i++) {
            prev[i] = -lambdaDivCDivP;
            cur[i] = dx2DivDt + 2 * lambdaDivCDivP;
            next[i] = -lambdaDivCDivP;
            freeCoef[i] = dx2OnKOnQDivC * Math.pow(newConcentration[i], model.getAlpha()) * Math.exp(-EOnR / prevLayer.getTemperature()[i]) + dx2DivDt * prevLayer.getTemperature()[i];
        }

        double[] newTemperature = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);
        return new Layer1D(newConcentration, newTemperature);
    }
}
