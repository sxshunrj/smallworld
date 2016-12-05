package com.sxshunrj.pattern.proxy2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;


/**
 * Java�ӑB����
 * 		��һ�N���F��ʽ��JDK�Ԏ���Proxy�InvocationHandler�ӿ�
 * 					   1�����x�I�սӿڼ����F�
 * 					   2���Զ��x������FInvacationHandler�ӿڣ����Finvoke�����������x�ֲ�׃�����d����Ŀ�����
 * 					   3����ʼ������Ŀ���
 * 					   4��ʹ��Proxy�newProxyInstance�����@ȡ����Y���
 *                     5��ʹ�ô���Y����{�ØI�շ���
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
     * �Ѵ�������ֽ���д��Ӳ���� 
     * @param path ����·�� 
     */  
    public static void writeProxyClassToHardDisk(String path) {  
        // ��һ�ַ��������ַ�ʽ�ڸղŷ���ProxyGeneratorʱ�Ѿ�֪����  
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", true);  
          
        // �ڶ��ַ���  
        // ��ȡ��������ֽ���  
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

