package com.sxshunrj.pattern.proxy2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;


/**
 * Java動態代理
 * 		第一種實現方式：JDK自帶的Proxy類、InvocationHandler接口
 * 					   1：定義業務接口及實現類
 * 					   2：自定義代理類實現InvacationHandler接口，實現invoke方法，并定義局部變量承載代理目標類實例
 * 					   3：初始化代理目標類
 * 					   4：使用Proxy類的newProxyInstance方法獲取代理結果類
 *                     5：使用代理結果類調用業務方法
 *
 */
public class JDKDynamicProxyTest {
	public static void main(String[] args) {
		MyService service = new MyServiceImpl();
		service = (MyService)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
				service.getClass().getInterfaces(), 
				new MyInvocationHandler(service));
		
		service.run();
		
		
//		ProxyGeneratorUtils.writeProxyClassToHardDisk("E:/$Proxy11.class");
	}
}


interface MyService{
	void run();
}
class MyServiceImpl implements MyService{
	@Override
	public void run() {
		System.out.println("run");
	}
	
}
class MyInvocationHandler implements InvocationHandler{
	
	Object target;
	
	public MyInvocationHandler(Object object) {
		this.target = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("start");
		Object result = method.invoke(target, args);
		System.out.println("end");
		return result;
	}
}

class ProxyGeneratorUtils {  
	  
    /** 
     * 把代理类的字节码写到硬盘上 
     * @param path 保存路径 
     */  
    public static void writeProxyClassToHardDisk(String path) {  
        // 第一种方法，这种方式在刚才分析ProxyGenerator时已经知道了  
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", true);  
          
        // 第二种方法  
        // 获取代理类的字节码  
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy11", MyServiceImpl.class.getInterfaces());  
          
        FileOutputStream out = null;  
          
        try {  
            out = new FileOutputStream(path);  
            out.write(classFile);  
            out.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  

