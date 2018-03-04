package org.ai.forms.utils;

import javax.vecmath.Color3f;

/**
 *
 * @author Edison
 */
public class Utils3D {

    public static Color3f value2color(double val, double min, double max) {
        if (max == min) {
            val = 0;
        } else {
            val = (val - min) / (max - min);
        }
        return new Color3f((float) val, 0.0f, (float) (1 - val));

    }
}
