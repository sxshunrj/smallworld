package executer;

import com.tuniondata.bigdata.facade.extend.workers.AbstractExecuter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 15:25
 * Desc：
 */
public class AbstractExecuterTest extends AbstractExecuter<String> {

    public void save(List<String> list) throws InterruptedException {
        super.run(list,getClass().getName());
    }

    @Override
    public void work(String data) {
        System.out.println(data);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
