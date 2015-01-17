package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Model;
import com.ifmo.mathproject.Utils;

/**
 * Created by warrior on 12.12.14.
 */
public class PartialImplicitMethod extends Method1D {

    protected final double dx2DivDt;
    protected final double dx2OnK;
    protected final double EOnR;
    protected final double lambdaDivCDivP;
    protected final double dx2OnKOnQDivC;

    protected final int iteration;

    public PartialImplicitMethod(Model model, int iteration) {
        super(model);
        this.iteration = iteration;
        this.dx2DivDt = model.getDx() * model.getDx() / model.getDt();
        this.dx2OnK = model.getDx() * model.getDx() * model.getK();
        this.EOnR = model.getE() / model.getR();
        this.lambdaDivCDivP = model.getLambda() / model.getP() / model.getC();
        this.dx2OnKOnQDivC = dx2OnK * model.getQ() / model.getC();
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

        for (int it = 0; it < iteration; it++) {
            for (int i = 0; i < size; i++) {
                prev[i] = -model.getD();
                cur[i] = dx2DivDt +
                        2 * model.getD() +
                        dx2OnK * Math.pow(prevIterationConcentration[i], model.getAlpha() - 1) * Math.exp(-EOnR / prevIterationTemperature[i]);
                next[i] = -model.getD();
                freeCoef[i] = dx2DivDt * prevLayerConcentration[i];
            }
            freeCoef[0] -= prev[0] * prevIterationConcentration[0];
            freeCoef[size - 1] -= next[size - 1] * prevIterationConcentration[size - 1];

            prevIterationConcentration = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);

            for (int i = 0; i < size; i++) {
                prev[i] = -lambdaDivCDivP;
                cur[i] = dx2DivDt + 2 * lambdaDivCDivP;
                next[i] = -lambdaDivCDivP;
                freeCoef[i] = dx2OnKOnQDivC * Math.pow(prevIterationConcentration[i], model.getAlpha()) * Math.exp(-EOnR / prevIterationTemperature[i]) + dx2DivDt * prevLayerTemperature[i];
            }
            freeCoef[0] -= prev[0] * prevIterationTemperature[0];
            freeCoef[size - 1] -= next[size - 1] * prevIterationTemperature[size - 1];

            prevIterationTemperature = Utils.tridiagonalMatrixAlgorithm(prev, cur, next, freeCoef);
        }

        return new Layer1D(prevIterationConcentration, prevIterationTemperature);
    }
}
