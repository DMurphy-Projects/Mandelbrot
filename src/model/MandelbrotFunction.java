package model;

import helper.ComplexCalc;
import helper.Global;

public class MandelbrotFunction {

    //returns a number based on how the function grows:
    //0 does not grow
    public static double test(ComplexNumber c1)
    {
        ComplexNumber z = new ComplexNumber(0, 0);
        int iter = 0;
        //run x times, then decide if should keep going
        for (iter=0; iter< Global.MAX_ITERATIONS; iter++) {
            z = function(z, c1);
            double order = ComplexCalc.getOrder(z);
            //once order gets beyond 1, the function will explode
            if (order > 2)
            {
                break;
            }
        }
        return iter;
    }

    //f_c(z) = z^2 + c
    private static ComplexNumber function(ComplexNumber z, ComplexNumber c)
    {
        return ComplexCalc.add(ComplexCalc.multiply(z, z), c);
    }
}
