package com.ifmo.mathproject;

/**
 * Created by warrior on 10.12.14.
 */
public class Model {

    public static final double R = 8.134;

    public static final double DEFAULT_K = 1.6 * 1e6;
    public static final double DEFAULT_E = 8 * 1e4;
    public static final double DEFAULT_alpha = 1;
    public static final double DEFAULT_Q = 7 * 1e5;
    public static final double DEFAULT_p = 830;
    public static final double DEFAULT_Tw = 600;
    public static final double DEFAULT_c = 1980;
    public static final double DEFAULT_lambda = 0.13;
    public static final double DEFAULT_D = DEFAULT_lambda / (DEFAULT_p * DEFAULT_c);

    private double dx = 0.00001;
    private double dt = 0.00001;
    private int N = 1000;
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

    public int getN() {
        return N;
    }

    public Model setN(int n) {
        N = n;
        return this;
    }

    public double getInitT() {
        return initT;
    }

    public Model setInitT(double initT) {
        this.initT = initT;
        return this;
    }

    public double getDx() {
        return dx;
    }

    public Model setDx(double dx) {
        this.dx = dx;
        return this;
    }

    public double getDt() {
        return dt;
    }

    public Model setDt(double dt) {
        this.dt = dt;
        return this;
    }

    public double getK() {
        return K;
    }

    public Model setK(double k) {
        K = k;
        return this;
    }

    public double getE() {
        return E;
    }

    public Model setE(double e) {
        E = e;
        return this;
    }

    public double getR() {
        return R;
    }

    public double getAlpha() {
        return alpha;
    }

    public Model setAlpha(double alpha) {
        this.alpha = alpha;
        return this;
    }

    public double getQ() {
        return Q;
    }

    public Model setQ(double q) {
        Q = q;
        return this;
    }

    public double getP() {
        return p;
    }

    public Model setP(double p) {
        this.p = p;
        return this;
    }

    public double getTw() {
        return Tw;
    }

    public Model setTw(double tw) {
        Tw = tw;
        return this;
    }

    public double getC() {
        return c;
    }

    public Model setC(double c) {
        this.c = c;
        return this;
    }

    public double getLambda() {
        return lambda;
    }

    public Model setLambda(double lambda) {
        this.lambda = lambda;
        return this;
    }

    public double getD() {
        return D;
    }

    public Model setD(double d) {
        D = d;
        return this;
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
