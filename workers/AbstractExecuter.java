package com.tuniondata.bigdata.facade.extend.workers;

import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 15:18
 * Desc：抽象多线程工作类，使用者需要继承此类，测试详见AbstractExecuterTest类
 */
public abstract class AbstractExecuter<T> {

    static Executor executor = null;
    static {
        int pro = Runtime.getRuntime().availableProcessors();
        System.out.println("当前CPU个数："+pro);
        executor = Executors.newFixedThreadPool(pro);
    }

    public void run(List<T> datas,String currentTaskName) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("当前启动的任务："+currentTaskName);

        final CountDownLatch countDownLatch = new CountDownLatch(datas.size());

        for (int i = 0;i < datas.size(); i++){
            final T data = datas.get(i);

            if (null != executor){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        work(data);
                        countDownLatch.countDown();
                    }
                });
            }else {
                work(data);
            }
        }
        countDownLatch.await();

        stopWatch.stop();
        System.out.println(currentTaskName+"-Millis:"+stopWatch.getTotalTimeMillis());
        System.out.println(currentTaskName+"-Seconds:"+stopWatch.getTotalTimeSeconds());
    }

    public abstract void work(T data);

}
