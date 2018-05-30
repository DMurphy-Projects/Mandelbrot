package controller;

import model.ComplexNumber;

public class MandelbrotController {
    ComplexNumber start, end;
    double curX = 0, curY = 0, width, height;
    double resolution = 0.1;

    int localX = 0, localY = 0, localSize;

    boolean finished = false;

    public void setStart(ComplexNumber s)
    {
        start = s;
    }
    public void setEnd(ComplexNumber e)
    {
        end = e;
    }
    public void setResolution(double res)
    {
        resolution = res;
    }

    public void init()
    {
        curX = start.getReal();
        curY = start.getImaginary();

        //may not be needed
        width = end.getReal()-start.getReal();
        height = end.getImaginary()-start.getImaginary();

        //is square, so only need 1
        localSize = (int) (1d / resolution);
    }

    public ComplexNumber getNext()
    {
        double r = ((double) localX / localSize) * width;
        double i = ((double)localY / localSize) * height;
        r += start.getReal();
        i += start.getImaginary();

        localX++;
        if (localX >= localSize)
        {
            localX = 0;
            localY++;
            if (localY >= localSize)
            {
                finished = true;
            }
        }

        return new ComplexNumber(r, i);
    }
    public boolean isFinished()
    {
        return finished;
    }
}
