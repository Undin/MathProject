package com.ifmo.mathproject.d2;

/**
 * Created by warrior on 20.01.15.
 */
public class ExplicitMethod extends Method2D {

    private final double dtOnDDivDx2;
    private final double dtOnDDivDy2;
    private final double EDivR;
    private final double dtOnK;
    private final double dtOnKappaDivDx2;
    private final double dtOnKappaDivDy2;
    private final double dtOnQOnKDivC;

    public ExplicitMethod(Model2D model) {
        super(model);
        dtOnDDivDx2 = model.getDt() * model.getD() / (model.getDx() * model.getDx());
        dtOnDDivDy2 = model.getDt() * model.getD() / (model.getDy() * model.getDy());
        EDivR = model.getE() / model.getR();
        dtOnK = model.getDt() * model.getK();
        double kappa = model.getLambda() / model.getP() / model.getC();
        dtOnKappaDivDx2 = model.getDt() * kappa / (model.getDx() * model.getDx());
        dtOnKappaDivDy2 = model.getDt() * kappa / (model.getDy() * model.getDy());
        dtOnQOnKDivC = model.getDt() * model.getQ() * model.getK() / model.getC();
    }

    @Override
    public Layer2D nextLayer(Layer2D... previousLayers) {
        Layer2D prevLayer = previousLayers[0];
        double[][] prevLayerConcentration = previousLayers[0].getConcentration();
        double[][] prevLayerTemperature = previousLayers[0].getTemperature();

        double[][] nextLayerConcentration = new double[prevLayer.getSizeY()][prevLayer.getSizeX()];
        double[][] nextLayerTemperature = new double[prevLayer.getSizeY()][prevLayer.getSizeX()];

        for (int x = 0; x < prevLayer.getSizeX(); x++) {
            nextLayerConcentration[0][x] = prevLayerConcentration[0][x];
            nextLayerConcentration[prevLayer.getSizeY() - 1][x] = prevLayerConcentration[prevLayer.getSizeY() - 1][x];
        }
        for (int y = 1; y < prevLayer.getSizeY() - 1; y++) {
            nextLayerConcentration[y][0] = prevLayerConcentration[y][0];
            nextLayerConcentration[y][prevLayer.getSizeX() - 1] = prevLayerConcentration[y][prevLayer.getSizeX() - 1];
            for (int x = 1; x < prevLayer.getSizeX() - 1; x++) {
                nextLayerConcentration[y][x] = dtOnDDivDx2 * (prevLayerConcentration[y][x - 1] - 2 * prevLayerConcentration[y][x] + prevLayerConcentration[y][x + 1]) +
                                               dtOnDDivDy2 * (prevLayerConcentration[y - 1][x] - 2 * prevLayerConcentration[y][x] + prevLayerConcentration[y + 1][x]) -
                                               dtOnK * Math.pow(prevLayerConcentration[y][x], model.getAlpha()) * Math.exp(-EDivR / prevLayerTemperature[y][x]) +
                                               prevLayerConcentration[y][x];
            }
        }

        for (int y = 1; y < prevLayer.getSizeY() - 1; y++) {
            nextLayerTemperature[y][0] = prevLayerTemperature[y][0];
            nextLayerTemperature[y][prevLayer.getSizeX() - 1] = prevLayerTemperature[y][prevLayer.getSizeX() - 1];
            for (int x = 1; x < prevLayer.getSizeX() - 1; x++) {
                nextLayerTemperature[y][x] = dtOnKappaDivDx2 * (prevLayerTemperature[y][x - 1] - 2 * prevLayerTemperature[y][x] + prevLayerTemperature[y][x + 1]) +
                        dtOnKappaDivDy2 * (prevLayerTemperature[y - 1][x] - 2 * prevLayerTemperature[y][x] + prevLayerTemperature[y + 1][x]) -
                        dtOnQOnKDivC * Math.pow(nextLayerConcentration[y][x], model.getAlpha()) * Math.exp(-EDivR / prevLayerTemperature[y][x]) +
                        prevLayerTemperature[y][x];
            }
        }

        return new Layer2D(nextLayerConcentration, nextLayerTemperature);
    }
}
