package com.hpe.dna.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author chun-yang.wang@hpe.com
 */
public class NumericUtils {
    public static Double formatDouble(Object d) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(df.format(d));
    }
}
