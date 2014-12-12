package com.ifmo.mathproject.d1;

import com.ifmo.mathproject.Model;

/**
 * Created by warrior on 12.12.14.
 */
public class PredictorCorrectorMethod extends PartialImplicitMethod {
    public PredictorCorrectorMethod(Model model) {
        super(model, 2);
    }
}
