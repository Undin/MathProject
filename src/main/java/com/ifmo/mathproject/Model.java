package com.ifmo.mathproject;

/**
 * Created by warrior on 10.12.14.
 */
public abstract class Model {

    public static final double R = 8.134;

    public static final double DEFAULT_K = 1.6 * 1e6;
    public static final double DEFAULT_E = 8 * 1e4;
    public static final double DEFAULT_alpha = 1;
    public static final double DEFAULT_Q = 7 * 1e5;
    public static final double DEFAULT_p = 830;
    public static final double DEFAULT_Tw = 800;
    public static final double DEFAULT_c = 1980;
    public static final double DEFAULT_lambda = 0.13;
    public static final double DEFAULT_D = DEFAULT_lambda / (DEFAULT_p * DEFAULT_c);

    private double dt = 1;
    private double initT = 293;
    private double K = DEFAULT_K;
    private double E = DEFAULT_E;
    private double alpha = DEFAULT_alpha;
    private double Q = DEFAULT_Q;
    private double p = DEFAULT_p;
    private double Tw = DEFAULT_Tw;
    private double c = DEFAULT_c;
    private double lambda = DEFAULT_lambda;
    private double D = DEFAULT_D;

    public double getInitT() {
        return initT;
    }

    public void setInitT(double initT) {
        this.initT = initT;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }

    public double getE() {
        return E;
    }

    public void setE(double e) {
        E = e;
    }

    public double getR() {
        return R;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getTw() {
        return Tw;
    }

    public void setTw(double tw) {
        Tw = tw;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getD() {
        return D;
    }

    public void setD(double d) {
        D = d;
    }

    public double getTm() {
        return initT + Q / c;
    }

    public double getBetta() {
        return R * getTm() / E;
    }

    public double getGamma() {
        return R * Math.pow(getTm(), 2) / (E * (Q / c));
    }

    public double getU() {
        return Math.pow(2 * K * lambda * R * Math.pow(getTm(), 2) * Math.exp(-1 / getBetta()) / (Q * p * E), 0.5);
    }

    public double getDeltaH() {
        return lambda / (p * c * getU());
    }

    public double getDeltaR() {
        return getDeltaH() * getBetta();
    }
}
