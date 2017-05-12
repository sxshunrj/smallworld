package executer;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 14:47
 * Desc：
 */
public class ExecutorCommonTest {
    public void save(final List<String> list) throws InterruptedException {
        int pro = Runtime.getRuntime().availableProcessors();
        System.out.println("当前CPU："+pro);


        Executor executor = Executors.newFixedThreadPool(pro);
        final CountDownLatch countDownLatch = new CountDownLatch(list.size());

        for (int i=0;i<list.size();i++){
            final int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(list.get(finalI));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    countDownLatch.countDown();
                }
            });
        }
//        countDownLatch.await(3, TimeUnit.SECONDS);
        countDownLatch.await();


//        for (int i=0;i<list.size();i++){
//            System.out.println(list.get(i));
//            Thread.sleep(1000);
//        }



    }
}
