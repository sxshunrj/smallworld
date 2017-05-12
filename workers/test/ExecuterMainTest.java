package executer;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sunxs
 * Date: 2017/5/11 14:49
 * Desc：
 */
public class ExecuterMainTest {

//    ExecutorCommonTest dao = new ExecutorCommonTest();
//    AbstractExecuterTest dao = new AbstractExecuterTest();
    ExecuterBuilderTest dao = new ExecuterBuilderTest();


    public void test1(String flag, int count) throws Exception {
        List<String> list = Lists.newArrayList();
        for (int i= 1;i<=count;i++){
            list.add(flag+"-"+i);
        }
        dao.save(list);
    }

    public static void main(String[] args) throws Exception {
        ExecuterMainTest service  = new ExecuterMainTest();
        service.test1("a",16);
        service.test1("b",16);

    }


}
