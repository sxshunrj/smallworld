package com.sxshunrj.test.servlet;

import java.util.Scanner;
/**
 * Created by sunxianshun on 2016/11/29.
 *
 * 令牌桶算法
 *
 * 《采用令牌漏桶进行报文限流的方法》
 *   专利：http://www.google.com/patents/CN1536815A?cl=zh
 *       http://www.pss-system.gov.cn/sipopublicsearch/patentsearch/showSearchResult-drillSearchByViewSetting.shtml
 */
public class TokenBucket {

    long currCapacity;//当前容量
    final long totalCapacity=200;//规定总容量

    final int power = 2;
    final double inSpeed = Math.pow(2d,power);// 注入速度

    long preTime;// 上次请求时间，单位（秒）
    long timeInterval;//本地请求与上次请求的时间间隔，单位（秒）

    private void inject(){
        if(currCapacity == totalCapacity){
            System.out.println("当前容量与总容量相等，无需注入");
            return ;
        }
        //当前容量=Min((本地注入的容量+原来剩余的容量),规定最大容量)
        long surplusCapacity = currCapacity;
        currCapacity = Math.min((int)inSpeed * (timeInterval << power) + currCapacity , totalCapacity);
        System.out.println("原来剩余："+surplusCapacity+"，本次注入了："+(int)inSpeed * (timeInterval << power)+"，注入之后的容量为："+currCapacity);
    }

    public boolean handlerReq(int length){
        boolean result = false;

        long currTime = System.currentTimeMillis() / 1000;
        if(preTime == 0){
            preTime = currTime + 36000;
        }
        timeInterval = currTime - preTime;
        preTime = currTime;

        this.inject();

        long surplusCapacity = currCapacity;
        if(length <= currCapacity){
            currCapacity = currCapacity - length;
            System.out.println("当前容量为："+surplusCapacity+",大于等于本次请求的容量："+length+",运行执行，执行后容量为："+currCapacity);
            result = true;
        }else{
            System.out.println("当前容量为："+surplusCapacity+",小于本次请求的容量："+length+",拒绝执行");
        }
        return result;
    }

    public static void main(String [] a){
        TokenBucket m = new TokenBucket();
        while (true){
            Scanner reader=new Scanner(System.in);
            String input = reader.next();
            System.out.println("输入："+input);
            m.handlerReq(input.length());
            System.out.println("------------------------");
        }

    }

}
