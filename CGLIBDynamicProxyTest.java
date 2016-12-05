package com.sxshunrj.pattern.proxy2;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 * 
 * Java�ӑB����ڶ��N��ʽ��
 * 		CGLIB��ʽ��
 * 			1�����x�I���
 * 			2���Զ��x������FMethodInterceptor�����Fintercept�����������xһ��Object����ձ���������
 * 
 *
 */
public class CGLIBDynamicProxyTest {
	
	public static void main(String[] args) {
		MyMethodInterceptor interceptor = new MyMethodInterceptor();
		
		MyBean bean = (MyBean)interceptor.getInstance(new MyBean());
		String s = bean.run();
		
		
		System.out.println(s);
	}
	
	
}


class MyBean{
	String run(){
		System.out.println("run----");
		return "asasas";
	}
}


class MyMethodInterceptor implements MethodInterceptor{
	
	Object target;
	
	public Object getInstance(Object object){
		this.target = object;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
	
	@Override
	public Object intercept(Object object, Method method, Object[] objects,
			MethodProxy methodProxy) throws Throwable {
		System.out.println("--start");
		//Object result = methodProxy.invoke(target, objects);
		Object result = methodProxy.invokeSuper(object, objects);
		System.out.println("--end-"+result);
		return result;
	}
	
}
