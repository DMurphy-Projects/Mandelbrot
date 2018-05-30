import com.sun.corba.se.spi.activation.ActivatorOperations;
import controller.MandelbrotController;
import helper.Global;
import model.ComplexNumber;
import model.MandelbrotFunction;
import task.MandelbrotTask;
import task.ThreadPool;
import view.BitmapView;
import view.MandelbrotColoring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class MainWindow implements MouseWheelListener, MouseListener, ActionListener{

    double resolution = 0.005, zoomFactor = 0.9, moveFactor = 0.1;
    int width, height, lastX = 0, lastY = 0;

    boolean scroolBlock = false;

    ComplexNumber centre = new ComplexNumber(0, 0), size = new ComplexNumber(1, 1);

    double[] valueVector;

    JFrame frame;
    BitmapView view = new BitmapView();

    boolean renderBlock = false, needsUpdating, threadWorking = true;

    public MainWindow()
    {
        resolution /= 2;
        resolution /= 2;
        width = (int) (1/resolution);
        height = (int) (1/resolution);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadWorking) {
                    if (needsUpdating) {
                        needsUpdating = false;
                        if (!renderBlock) {
                            renderBlock = true;
                            createView();
                            updateView();
                            renderBlock = false;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        }).start();
    }

    public void create()
    {
        view.setPreferredSize(new Dimension(width, height));

        frame = new JFrame("main");
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                threadWorking = false;
            }
        };
        frame.addWindowListener(exitListener);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Settings");
        menuBar.add(menu);

        JMenuItem item = new JMenuItem("Iterations");
        item.setActionCommand("iterations");
        item.addActionListener(this);
        menu.add(item);

        item = new JMenuItem("Dump Position");
        item.setActionCommand("dumpData");
        item.addActionListener(this);
        menu.add(item);

        item = new JMenuItem("Dump Next 1000 pixel data");
        item.setActionCommand("dumpPixel");
        item.addActionListener(this);
        menu.add(item);

        frame.setJMenuBar(menuBar);

        frame.addMouseWheelListener(this);
        frame.addMouseListener(this);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }

    private void sampleComplexPlane(MandelbrotController c, ArrayList<ComplexNumber> numbers)
    {
        while (!c.isFinished())
        {
            numbers.add(c.getNext());
        }
    }

    private MandelbrotController createController()
    {
        MandelbrotController controller = new MandelbrotController();
        controller.setStart(new ComplexNumber(centre.getReal() - size.getReal(), centre.getImaginary() - size.getImaginary()));
        controller.setEnd(new ComplexNumber(centre.getReal() + size.getReal(), centre.getImaginary() + size.getImaginary()));
        controller.setResolution(resolution);
        controller.init();
        return controller;
    }

    public void createView()
    {
        valueVector = new double[width*height];

        MandelbrotController controller = createController();

        ArrayList<ComplexNumber> numbers = new ArrayList<>();
        sampleComplexPlane(controller, numbers);

        ThreadPool threadPool = ThreadPool.getInstance();
        int split = Runtime.getRuntime().availableProcessors();
        int splitSize = numbers.size() / split;
        for (int i=0;i<split;i++)
        {
            ComplexNumber[] temp = new ComplexNumber[splitSize];
            numbers.subList(i*splitSize, ((i+1)*splitSize)-1).toArray(temp);
            threadPool.execute(new MandelbrotTask(temp, i*splitSize, valueVector));
        }

        threadPool.waitUntilDone();
    }

    public void updateView()
    {
        int[] pixelVector = new int[width * height];
        MandelbrotColoring coloring = new MandelbrotColoring();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = valueVector[y * width + x];
                pixelVector[y * width + x] = coloring.getColor(value);
            }
        }

        BufferedImage im =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = im.getRaster();
        raster.setDataElements(0, 0, width, height, pixelVector);

        view.setBitmap(im);
        view.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double r = size.getReal();
        double i = size.getImaginary();
        if (e.getWheelRotation() < 0)
        {
            r = r * zoomFactor;
            i = i * zoomFactor;
        }
        else
        {
            r = r / zoomFactor;
            i = i / zoomFactor;
        }
        size = new ComplexNumber(r, i);
        needsUpdating = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double r = centre.getReal(), i = centre.getImaginary();

        int height = frame.getHeight();
        int width = frame.getWidth();

        if (e.getY() < height * 0.25)
        {
            i = i - (moveFactor * size.getImaginary());
        }
        if(e.getY() > height * 0.75)
        {
            i = i + (moveFactor * size.getImaginary());
        }
        if(e.getX() < width * 0.25)
        {
            r = r - (moveFactor * size.getReal());
        }
        if (e.getX() > width * 0.75)
        {
            r = r + (moveFactor * size.getReal());
        }

        centre = new ComplexNumber(r, i);
        needsUpdating = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("iterations"))
        {
            JDialog popup = new JDialog(frame, Dialog.ModalityType.DOCUMENT_MODAL);
            JLabel label = new JLabel("Iterations");
            JTextField field = new JTextField();

            GridBagConstraints gBag = new GridBagConstraints();

            JPanel c = new JPanel();
            WindowListener exitListener = new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    int typed;
                    try {
                        typed = Integer.parseInt(field.getText());
                    }
                    catch(NumberFormatException ex)
                    {
                        System.out.println("Invalid Number for Max Iteration");
                        return;
                    }
                    if(Global.MAX_ITERATIONS != typed)
                    {
                        Global.MAX_ITERATIONS = typed;
                        needsUpdating = true;
                    }
                }
            };
            popup.addWindowListener(exitListener);
            c.setLayout(new GridBagLayout());

            gBag.weightx = 1;
            gBag.weighty = 1;
            gBag.gridx = 0;
            gBag.gridy = 0;
            gBag.fill = GridBagConstraints.HORIZONTAL;

            c.add(label, gBag);
            gBag.gridx = 1;
            c.add(field, gBag);
            popup.add(c);
            popup.pack();

            field.setText(Global.MAX_ITERATIONS+"");

            popup.setVisible(true);
        }
        else if (e.getActionCommand().equals("dumpData"))
        {
            System.out.println("Position: "+centre);
            System.out.println("Size: "+size);
        }
        else if(e.getActionCommand().equals("dumpPixel"))
        {
            MandelbrotController controller = createController();
            ArrayList<ComplexNumber> n = new ArrayList<>();
            sampleComplexPlane(controller, n);
            int i = 0;
            for (ComplexNumber c:n)
            {
                System.out.println(c);
                if (i > 1000)
                {
                    break;
                }
                i++;
            }
        }
    }
}
