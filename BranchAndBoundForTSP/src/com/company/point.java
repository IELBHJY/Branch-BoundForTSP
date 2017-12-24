package com.company;

/**
 * Created by apple on 2017/12/4.
 */
public class point  {
    public double c[][];
    public int rowNumber[];
    public int colNumber[];
    public int ad[];
    public int k;
    public double lowbound;

    public point(int count){
        c=new double[count][count];
        rowNumber=new int[count];
        colNumber=new int[count];
        ad=new int[Main.city];
        k=count;
    }
}
