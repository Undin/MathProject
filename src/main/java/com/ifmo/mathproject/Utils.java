package com.ifmo.mathproject;

import java.util.Objects;

/**
 * Created by warrior on 10.12.14.
 */
public class Utils {

    public static double[] tridiagonalMatrixAlgorithm(double[] a, double[] b, double[] c, double[] d) {
        int length = Objects.requireNonNull(a).length;
        Objects.requireNonNull(b);
        Objects.requireNonNull(c);
        Objects.requireNonNull(d);
        if (b.length != length || c.length != length || d.length != length) {
            throw new IllegalArgumentException("arrays must have same lengths");
        }
        double[] x = new double[length];
        double[] alpha = new double[length];
        double[] beta = new double[length];
        alpha[0] = -c[0] / b[0];
        beta[0] = d[0] / b[0];
        for (int i = 1; i < length - 1; i++) {
            alpha[i] = -c[i] / (a[i] * alpha[i - 1] + b[i]);
            beta[i] = (d[i] - a[i] * beta[i - 1]) / (a[i] * alpha[i - 1] + b[i]);
        }
        x[length - 1] = (d[length - 1] - a[length - 1] * beta[length - 2]) /
                        (a[length - 1] * alpha[length - 2] + b[length - 1]);
        for (int i = length - 2; i >= 0; i--) {
            x[i] = alpha[i] * x[i + 1] + beta[i];
        }
        return x;
    }
}
