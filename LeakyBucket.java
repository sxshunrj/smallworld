package com.sxshunrj.test.servlet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunxianshun on 2016/11/30.
 *
 * 漏桶算法
 *
 */
public class LeakyBucket {

    long currCapacity;//当前容量
    final long totalCapacity=10;//规定总容量

    final double rate = 1;// 漏水速度

    long preTime = System.currentTimeMillis() / 1000;// 上次请求时间，单位（秒），默认当前启动时间

    /**
     * 漏水
     */
    public void leakage(){
        long currTime = System.currentTimeMillis() / 1000;
        long temp = currCapacity;
        // 时间间隔 * 漏水速度 = 本次漏水量
        long currLeakCapacity = Math.min(totalCapacity, (currTime - preTime) * (long) rate);
        currCapacity = Math.max(0, currCapacity - currLeakCapacity);
        System.out.println(Thread.currentThread().getName()+"漏水前："+temp+",本次漏水："+currLeakCapacity+",漏水后："+currCapacity);
    }

    public boolean handler(){
        boolean result = false;
        this.leakage();

        if(currCapacity < totalCapacity){
            long temp = currCapacity;
            currCapacity ++;
            result = true;
            System.out.println(Thread.currentThread().getName()+"当前桶剩余："+temp+",最大容量："+totalCapacity+",可以执行,执行后容量："+currCapacity);
        }else{
            System.out.println(Thread.currentThread().getName()+"当前桶剩余："+currCapacity+",最大容量："+totalCapacity+",无法执行");
        }

        return result;
    }


    public static void main(String [] a) throws InterruptedException {
        final LeakyBucket leakyBucket = new LeakyBucket();

        ExecutorService pool = Executors.newCachedThreadPool();
        while (true){
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    leakyBucket.handler();
                    System.out.println(Thread.currentThread().getName()+"---------------------------");
                }
            });
            Thread.sleep(10);
        }


    }
}
