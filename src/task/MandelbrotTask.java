package task;

import model.ComplexNumber;
import model.MandelbrotFunction;

public class MandelbrotTask implements Runnable {

    ComplexNumber[] numbersToTest;
    double[] values;
    int indexStart;

    public MandelbrotTask(ComplexNumber[] arr, int start, double[] v)
    {
        numbersToTest = arr;
        indexStart = start;
        values = v;
    }
    @Override
    public void run() {
        for (int i=0;i<numbersToTest.length-1;i++)
        {
            double value = MandelbrotFunction.test(numbersToTest[i]);
            values[i+indexStart] = value;
        }
    }
}
