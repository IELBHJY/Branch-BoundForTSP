package com.company;

/**
 * Created by apple on 2017/12/4.
 */
public class BBTSP  {

    public static int n=Main.city;

    /**
     * 计算某一行最小值和次小值
     * @param p 点
     * @param row p的第row行
     * @return 返回 第row行的最小值和次小值
     */
    double[] row_min(point p,int row){
        double result[]=new double[2];
        if(p.c[row][0]<p.c[row][1]){
            result[0]=p.c[row][0];
            result[1]=p.c[row][1];
        }
        else{
            result[0]=p.c[row][1];
            result[1]=p.c[row][0];
        }
        for(int i=2;i<p.k;i++){
            if(p.c[row][i]<result[0]){
                result[1]=result[0];
                result[0]=p.c[row][i];
            }
            else if(p.c[row][i]<result[1]){
                result[1]=p.c[row][i];
            }
        }
        return result;
    }

    double[] col_min(point p,int col){
        double result[]=new double[2];
        if(p.c[0][col]<p.c[1][col]){
            result[0]=p.c[0][col];
            result[1]=p.c[1][col];
        }
        else{
            result[0]=p.c[1][col];
            result[1]=p.c[0][col];
        }
        for(int i=2;i<p.k;i++){
            if(p.c[i][col]<result[0]){
                result[1]=result[0];
                result[0]=p.c[i][col];
            }
            else if(p.c[i][col]<result[1]){
                result[1]=p.c[i][col];
            }
        }
        return result;
    }

    /**
     * 规约矩阵
     * @param p 点
     * @return p对应的费用矩阵的规约值
     */
    double changeMaxtrix(point p){
        double sum=0,temp;
        for(int i=0;i<p.k;i++){
            temp=row_min(p,i)[0];
            for(int j=0;j<p.k;j++){
                p.c[i][j]-=temp;
            }
            sum+=temp;
        }
        for(int i=0;i<p.k;i++){
            temp=col_min(p,i)[0];
            for(int j=0;j<p.k;j++){
                p.c[j][i]-=temp;
            }
            sum+=temp;
        }
        return sum;
    }

    /**
     * 选择分支边
     * @param p 点
     * @return 返回分支边
     */
    double[] edge_sel(point p){
        int i,j;
        double temp,d=-1;
        double result[]=new double[3];
        double row_value[]=new double[p.k];
        double col_value[]=new double[p.k];
        for(i=0;i<p.k;i++){
            row_value[i]=row_min(p,i)[1];
        }
        for(i=0;i<p.k;i++){
            col_value[i]=col_min(p,i)[1];
        }
        for(i=0;i<p.k;i++){
            for(j=0;j<p.k;j++){
                if(p.c[i][j]==0) {
                    temp = row_value[i] + col_value[j];
                    if (temp > d) {
                        d = temp;
                        result[0] = d;
                        result[1] = i;
                        result[2] = j;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 修改费用矩阵，删除对应行和列
     * @param p  点
     * @param vk 第vk行
     * @param vl 第vl列
     * @return  返回修改后的点
     */
    point del_rowcolnew(point p,int vk,int vl){
        point result=new point(p.k-1);
        result.k=p.k-1;
        int i=0;
        int j=0;
        int r,c;
        for(r=0;r<p.k;r++){
            j=0;
            if(r==vk){continue;}
            for(c=0;c<p.k;c++){
                if(c==vl){continue;}
                result.c[i][j]=p.c[r][c];
                j++;
            }
            i++;
        }
        i=0;
        for(r=0;r<p.k;r++){
            if(r==vk){continue;}
            result.rowNumber[i]=p.rowNumber[r];
            i++;
        }
        i=0;
        for(r=0;r<p.k;r++){
            if(r==vl){continue;}
            result.colNumber[i]=p.colNumber[r];
            i++;
        }
        for(r=0;r<n;r++){
            result.ad[r]=p.ad[r];
        }
        return result;
    }

    /**
     * 防止成环操作
     * @param p
     * @param vk
     * @param vl
     * @return
     */
    point edge_bypnew(point p,int vk,int vl){
        int vk1=p.rowNumber[vk];
        int vl1=p.colNumber[vl];
        p.ad[vk1]=vl1;
        if(vk>=0&&vl>=0){
            p.c[vl][vk]=Double.MAX_VALUE;
        }
        int e=vk1;
        int s=vl1;
        int c=0;
        while(c<n){
            for(int i=0;i<n;i++){
                if(p.ad[i]==e){
                    e=i;
                    break;
                }
            }
            break;
        }
        c=0;
        while(c<n){
            for(int i=0;i<n;i++){
                if(i==s&&p.ad[i]>-1){
                    s=p.ad[i];
                    break;
                }
            }
            break;
        }
        int d;
        for(c=0;c<p.rowNumber.length;c++){
            for(d=0;d<p.colNumber.length;d++){
                if(p.rowNumber[c]==s&&p.colNumber[d]==e){
                    p.c[c][d]=Double.MAX_VALUE;
                }
            }
        }
        return p;
    }

    /**
     * 初始化
     * @param c
     * @param m
     * @return
     */
    point initalnew(double c[][],int m){
        int i,j;
        point node=new point(m);
        for(i=0;i<m;i++){
            for(j=0;j<m;j++){
                node.c[i][j]=c[i][j];
            }
        }
        for(i=0;i<m;i++){
            node.rowNumber[i]=i;
            node.colNumber[i]=i;
        }
        for(i=0;i<m;i++){
            node.ad[i]=-1;
        }
        node.k=m;
        return node;
    }
}
