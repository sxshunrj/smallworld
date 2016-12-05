package com.sxshunrj.pattern.reflect;

import java.lang.reflect.InvocationTargetException;

public class ReflectTest {
	public static void main(String[] args) {
		MyBean bean = new MyBean();
		Class<? extends MyBean> clazz = bean.getClass();
		
		try {
			Object result = clazz.getMethod("run",new Class[]{String.class}).invoke(bean, "sss");
			System.out.println(result);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		
	}
}

class MyBean{
	public MyBean() {
	}
	public String run(String s){
		System.out.println("run--"+s);
		return "aaaaa";
	}
}