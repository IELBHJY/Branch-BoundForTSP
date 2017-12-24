package com.company;

import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    static final int city=28;
    static Integer[] number=new Integer[city];
    static double[] x=new double[city];
    static double[] y=new double[city];
    public static void main(String[] args){
	// write your code here
        long a=System.currentTimeMillis();
        int ad[]=new int[city];
        readData();
        double c[][]=creatMatrix();
        /*double c[][]={
                {inf,17,7,35,18},
                {9,inf,5,14,19},
                {29,24,inf,30,12},
                {27,21,25,inf,48},
                {15,16,28,18,inf},
        };*/
        double result=traveling_salesman(c,city,ad);
        int i=0;
        System.out.print(i+"--");
        while(ad[i]!=0){
            System.out.print(ad[i]+"--");
            i=ad[i];
        }
        System.out.println(0);
        System.out.println(result);
        System.out.println("\r<br>执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
    static double traveling_salesman(double c[][],int n,int ad[]){
        BBTSP bb=new BBTSP();
        Queue<point> priorityQueue=new PriorityQueue<>(idComparator);
        int i;
        int vk,vl;
        double d,w,bound=Double.MAX_VALUE;
        point xnode,ynode,znode;
        xnode=bb.initalnew(c,n);
        xnode.lowbound=bb.changeMaxtrix(xnode);
        while(xnode.k!=0) {
            double temp[];
            temp = bb.edge_sel(xnode);
            d = temp[0];
            vk = (int)Double.parseDouble(String.valueOf(temp[1]));
            vl = (int)Double.parseDouble(String.valueOf(temp[2]));
            znode=new point(xnode.k);
            znode.k=xnode.k;
            znode.c=new double[znode.k][znode.k];
            for(int z=0;z<znode.k;z++){
                for(int l=0;l<znode.k;l++){
                    znode.c[z][l]=xnode.c[z][l];
                }
            }
            znode.rowNumber=new int[znode.k];
            for(int z=0;z<znode.k;z++){
                znode.rowNumber[z]=xnode.rowNumber[z];
            }
            znode.colNumber=new int[znode.k];
            for(int z=0;z<znode.k;z++){
                znode.colNumber[z]=xnode.colNumber[z];
            }
            znode.ad=new int[city];
            for(int z=0;z<city;z++){
                znode.ad[z]=xnode.ad[z];
            }
            znode.lowbound=xnode.lowbound;
            znode.c[vk][vl] = Double.MAX_VALUE;
            d = bb.changeMaxtrix(znode);
            znode.lowbound = xnode.lowbound + d;
            if (znode.lowbound < bound) {
                priorityQueue.add(znode);
            }
            ynode=new point(xnode.k);
            ynode.c=new double[ynode.k][ynode.k];
            for(int z=0;z<ynode.k;z++){
                for(int l=0;l<ynode.k;l++){
                    ynode.c[z][l]=xnode.c[z][l];
                }
            }
            ynode.rowNumber=new int[ynode.k];
            for(int z=0;z<ynode.k;z++){
                ynode.rowNumber[z]=xnode.rowNumber[z];
            }
            ynode.colNumber=new int[ynode.k];
            for(int z=0;z<ynode.k;z++){
                ynode.colNumber[z]=xnode.colNumber[z];
            }
            ynode.ad=new int[city];
            for(int z=0;z<city;z++){
                ynode.ad[z]=xnode.ad[z];
            }
            ynode.lowbound=xnode.lowbound;
            ynode=bb.edge_bypnew(ynode,vk,vl);
            ynode=bb.del_rowcolnew(ynode, vk, vl);
            ynode.lowbound = bb.changeMaxtrix(ynode);
            ynode.lowbound += xnode.lowbound;
            if (ynode.k == 2) {
                if ((ynode.c[0][0] == 0) && (ynode.c[1][1]) == 0) {
                    ynode.ad[ynode.rowNumber[0]] = ynode.colNumber[0];
                    ynode.ad[ynode.rowNumber[1]] = ynode.colNumber[1];
                } else {
                    ynode.ad[ynode.rowNumber[0]] = ynode.colNumber[1];
                    ynode.ad[ynode.rowNumber[1]] = ynode.colNumber[0];
                }
                ynode.k = 0;
            }
            if (ynode.lowbound < bound) {
                priorityQueue.add(ynode);
                if (ynode.k == 0) {
                    bound = ynode.lowbound;
                }
            }
            xnode=priorityQueue.poll();
        }
        w=xnode.lowbound;
        for(i=0;i<city;i++){
            ad[i]=xnode.ad[i];
        }
       return w;
    }

    public static Comparator<point> idComparator=new Comparator<point>() {
        @Override
        public int compare(point o1, point o2) {
            if(o1.lowbound>o2.lowbound){
                return 1;
            }else if(o1.lowbound<o2.lowbound){
                return -1;
            }else{
                return 0;
            }
        }
    };

    private static void readData()
    {
        try {
                ArrayList<String[]> csvFileList = new ArrayList<String[]>();
                String csvFilePath = "/Users/apple/Desktop/tsp.csv";
                CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
                while (reader.readRecord()){
                    csvFileList.add(reader.getValues());
                }
                reader.close();
                for (int i = 0; i<city; i++) {
                    String[] strData = csvFileList.get(i);
                    number[i]=Integer.valueOf(strData[0]);
                    x[i]=Double.valueOf(strData[1]);
                    y[i]=Double.valueOf(strData[2]);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double[][] creatMatrix(){
        double c[][]=new double[city][city];
        for(int i=0;i<city;i++){
            for(int j=0;j<city;j++){
                double temp1=Math.abs(x[i]-x[j]);
                double temp2=Math.abs(y[i]-y[j]);
                c[i][j]=(int)Math.sqrt(Math.pow(temp1,2)+Math.pow(temp2,2));
            }
        }
        for(int i=0;i<city;i++){
            c[i][i]=Double.MAX_VALUE;
        }
        return c;
    }
}
