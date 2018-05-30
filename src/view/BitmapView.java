package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BitmapView extends JComponent{

    BufferedImage image;
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0,0, getWidth(), getHeight(), null);
    }

    public void setBitmap(BufferedImage i)
    {
        image = i;
    }
}
