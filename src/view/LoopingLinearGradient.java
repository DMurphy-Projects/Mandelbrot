package view;

import java.awt.*;
import java.util.ArrayList;

public class LoopingLinearGradient extends MultiLinearGradient {

    ArrayList<Integer> oColors = new ArrayList<>();
    ArrayList<Double> oPos = new ArrayList<>();

    public LoopingLinearGradient(Integer[] c, Double[] pos) {
        super(c, pos);
        oColors.addAll(colors);
        oPos.addAll(gradPositions);
    }

    private double map(double iStart, double iEnd, double oStart, double oEnd,
                       double in) {
        double slope = 1.0 * (oEnd - oStart) / (iEnd - iStart);
        return oStart + slope * (in - iStart);
    }

    private void addMoreColors(double from)
    {
        for (int i=0;i<oPos.size()-1;i++)
        {
            int color = oColors.get(i);
            double pos = oPos.get(i);
            double newPos = map(
                    gradPositions.get(0), gradPositions.get(gradPositions.size()-1),
                    from, 1,
                    pos);
            colors.add(color);
            gradPositions.add(newPos);
        }
    }

    @Override
    public int getColor(double pos) {
        while(pos > gradPositions.get(gradPositions.size()-1))
        {
            addMoreColors(gradPositions.get(gradPositions.size()-1));
        }

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
}
