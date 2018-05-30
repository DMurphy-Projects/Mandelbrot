package model;

public class ComplexNumber {

    double real, imaginary;

    public ComplexNumber(double r, double i) {
        real = r;
        imaginary = i;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary()
    {
        return imaginary;
    }

    @Override
    public String toString() {
        String s ="";
        if (imaginary > 0)
        {
            s = "+";
        }
        return real +s+imaginary+"i";
    }

    public boolean isInfinite()
    {
        return Double.isInfinite(real) || Double.isInfinite(imaginary);
    }
}
