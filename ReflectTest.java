package com.sxshunrj.pattern.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 *	Java反射：
 *		1：得到要調用類的class
 *		2：得到要調用類中的方法
 *		3：使用invoke方法調用 
 */
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