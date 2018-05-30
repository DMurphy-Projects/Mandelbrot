package view;
import helper.Global;

import java.awt.*;

public class MandelbrotColoring {

    float hueOffset = 3f/6;

    MultiLinearGradient grad;

    public MandelbrotColoring()
    {
        grad = new LoopingLinearGradient(
                new Integer[]{
                        new Color(10, 10, 90).getRGB(),
                        new Color(70, 125, 245).getRGB(),
                        new Color(255, 255, 255).getRGB()},
                new Double[]{0d, 0.5d, 0.9d});;
    }

    public int getColor(double value)
    {
        if (value == Global.MAX_ITERATIONS)
        {
            return Color.BLACK.getRGB();
        }
//        return Color.HSBtoRGB((float) ((value / Global.MAX_ITERATIONS) + hueOffset), 0.3f, 1f);
        return grad.getColor((value / Global.MAX_ITERATIONS)%1);
    }
}
