package executer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tuniondata.bigdata.facade.extend.workers.ExecuterBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 15:48
 * Desc：
 */
public class ExecuterBuilderTest {

    public void save(List<String> datas) throws Exception {
        List<List<String>> lists = Lists.partition(datas, datas.size());
        System.out.println(JSON.toJSONString(lists.size()));

        // 同步执行，多次调用期间等待指定的时间
        new ExecuterBuilder<String>().setTaskName("你好").setDatas(datas).setWorkFunction(new ExecuterBuilder.WorkFunction() {
            @Override
            public void work(Object data) {
                System.out.println(data);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).execute();

        // 异步执行，多次调用期间不等待
//        new ExecuterBuilder<List<String>>().setDatas(lists).setIsAsync(true).setWorkFunction(new ExecuterBuilder.WorkFunction() {
//            @Override
//            public void work(Object data) {
//                System.out.println(data);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).execute();



    }

}
