package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiLinearGradient {

    ArrayList<Integer> colors;
    ArrayList<Double> gradPositions;

    public MultiLinearGradient(Integer[] c, Double[] pos)
    {
        colors = new ArrayList<Integer>(Arrays.asList(c));
        gradPositions = new ArrayList<Double>(Arrays.asList(pos));
    }

    public int getColor(double pos)
    {
        //find out which two colors pos is between
        //assumes gradPositions are in order
        int i;
        for (i=0;i<gradPositions.size()-1;i++)
        {
            if (pos >= gradPositions.get(i) && pos <= gradPositions.get(i+1))
            {
                break;
            }
        }
        return findColor(i, pos);
    }

    protected int findColor(int i, double pos)
    {
        //interpolate to find color
        Color c1, c2;
        try {
            c1 = new Color(colors.get(i));
            c2 = new Color(colors.get(i + 1));
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println("Index out of bounds: "+pos);
            throw new IndexOutOfBoundsException();
        }
        //translate rgb colors to hsb colors
        float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
        //interpolate new color
        float[] interpColor = new float[3];
        interpColor[0] = (float) linearInterp(hsb1[0], hsb2[0], pos);
        interpColor[1] = (float) linearInterp(hsb1[1], hsb2[1], pos);
        interpColor[2] = (float) linearInterp(hsb1[2], hsb2[2], pos);
        //translate back to rgb int value
        return Color.HSBtoRGB(interpColor[0], interpColor[1], interpColor[2]);
    }

    protected double linearInterp(double min, double max, double percValue)
    {
        if (max < min)
        {
            double t1 = min;
            min = max;
            max = t1;
        }
        double dist = max - min;
        double value = dist * percValue;
        value += min;
        return value;
    }
}
