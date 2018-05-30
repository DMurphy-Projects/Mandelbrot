package helper;

import model.ComplexNumber;

import java.math.BigDecimal;

public class ComplexCalc {
    public static ComplexNumber add(ComplexNumber c1, ComplexNumber c2)
    {
        double real = c1.getReal() + c2.getReal();
        double imag = c1.getImaginary() + c2.getImaginary();
        return new ComplexNumber(real, imag);
    }
    public static ComplexNumber sub(ComplexNumber c1, ComplexNumber c2)
    {
        double real = c1.getReal() - c2.getReal();
        double imag = c1.getImaginary() - c2.getImaginary();
        return new ComplexNumber(real, imag);
    }
    public static ComplexNumber multiply(ComplexNumber c1, ComplexNumber c2)
    {
        //uses foil
        double f = c1.getReal() * c2.getReal();//will be regular number
        double o = c1.getReal() * c2.getImaginary();//will have imaginary part
        double i = c1.getImaginary() * c2.getReal();
        double l = c1.getImaginary() * c2.getImaginary();//will have i^2 as imaginary part
        //as i^2 == -1 then:
        l *= -1;
        return new ComplexNumber(f+l, o+i);
    }
    //not be used as absolute values just for comparision needs
    public static double getOrder(ComplexNumber c1)
    {
        //uses a^2 + b^2 = c^2
        //squaring isnt necessary as we are only trying to get a scoring value
        double a = c1.getReal();
        a = a * a;
//        if (a<0)
//        {
//            a*=-1;
//        }
        double b = c1.getImaginary();
        b = b * b;
//        if (b<0)
//        {
//            b*=-1;
//        }

        return a+b;
    }
}
