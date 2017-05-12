package com.tuniondata.bigdata.facade.extend.workers;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 15:41
 * Desc：多线程执行任务构造器，使用者需要构造出自定义的执行计划，然后进行执行，测试详见ExecuterBuilderTest类
 */
public class ExecuterBuilder<T> {

    //当前任务名称，默认为当前线程name
    String taskName = Thread.currentThread().getName();

    List<T> datas;

    //是否异步执行，默认为同步执行任务
    boolean isAsync = false;

    // 同步情况下默认等待0秒
    int waitTime = 0;
    TimeUnit waitTimeUnit = TimeUnit.SECONDS;

    WorkFunction workFunction = null;

    ExecutorService executorService;
    public ExecuterBuilder(){
        int pro = Runtime.getRuntime().availableProcessors();
        System.out.println("当前CPU个数："+pro);
        executorService = Executors.newFixedThreadPool(pro);
    }

    public ExecuterBuilder setTaskName(String taskName){
        this.taskName = taskName;
        return this;
    }
    public ExecuterBuilder setIsAsync(boolean isAsync){
        this.isAsync = isAsync;
        return this;
    }

    public ExecuterBuilder setWaitTime(int waitTime) {
        this.waitTime = waitTime;
        return this;
    }
    public ExecuterBuilder setWaitTimeUnit(TimeUnit waitTimeUnit) {
        this.waitTimeUnit = waitTimeUnit;
        return this;
    }

    public ExecuterBuilder setWorkFunction(WorkFunction workFunction) {
        this.workFunction = workFunction;
        return this;
    }

    public ExecuterBuilder setDatas(List<T> datas) {
        this.datas = datas;
        return this;
    }

    public void execute() throws Exception {
        if (null!=executorService){
            if(CollectionUtils.isEmpty(this.datas)){
                throw new Exception("datas is empty");
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            System.out.println("当前启动的任务："+this.taskName);

            CountDownLatch countDownLatch = null;
            if (!this.isAsync) {
                // 如果当前为同步，则需要加CountDownLatch
                countDownLatch = new CountDownLatch(datas.size());
            }

            for (int i = 0;i < datas.size(); i++){
                final T data = datas.get(i);

                final CountDownLatch finalCountDownLatch = countDownLatch;
                Future<?> submit = executorService.submit(new InternalWorkRunFunction(this.workFunction, data, this){
                    @Override
                    public void run() {
                        //调用父类方法，执行真正的任务
                        super.run();

                        if (!this.executerBuilder.isAsync) {
                            // 如果当前为同步，则需要将CountDownLatch减1
                            finalCountDownLatch.countDown();
                        }

                    }
                });
            }

            if(!this.isAsync){
                // 如果当前为同步，CountDownLatch需要等待
                if(this.waitTime > 0){
                    countDownLatch.await(this.waitTime, this.waitTimeUnit);
                }else{
                    countDownLatch.await();
                }
            }

            executorService.shutdown();

            stopWatch.stop();
            System.out.println(this.taskName+"-Millis:"+stopWatch.getTotalTimeMillis());
            System.out.println(this.taskName+"-Seconds:"+stopWatch.getTotalTimeSeconds());
        }else{
            throw new Exception("init ExecutorService failure");
        }
    }

    private static class InternalWorkRunFunction<T> implements Runnable{

        WorkFunction workFunction;
        T data;
        ExecuterBuilder executerBuilder;

        public InternalWorkRunFunction(WorkFunction workFunction, T data, ExecuterBuilder executerBuilder) {
            this.workFunction = workFunction;
            this.data = data;
            this.executerBuilder = executerBuilder;
        }

        @Override
        public void run() {
            this.workFunction.work(data);
        }
    }

    /**
     * 真正的执行者
     * @param <T>
     */
    public abstract static class WorkFunction<T> {
        T data;
        public T getData() {
            return data;
        }
        public void setData(T data) {
            this.data = data;
        }
        public abstract void work(T data);
    }


}
