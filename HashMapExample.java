package com.examples;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HashMapExample {
	
	public static boolean staticBool = Boolean.TRUE;
	
	public static void main(String[] args) {
		Map<Object, Object> hashMap = new HashMap();
		TestBean bean1 = new TestBean("John", "CA");
		TestBean bean2 = new TestBean("Scott", "NY");
		TestBean bean3 = new TestBean("John", "NC");
		hashMap.put("abc", bean1);
		hashMap.put(bean1, bean1);
		hashMap.put(bean2, bean2);
		hashMap.put(bean3, bean3);
		
		//System.out.println(hashMap);
		
		Set<Entry<Object, Object>> entrySet = hashMap.entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		while(iterator.hasNext()){
			Entry<Object, Object> entry = iterator.next();
			//System.out.println(entry.getKey() +" "+ entry.getValue());
			if(entry.getKey() instanceof TestBean)
				System.out.println(((TestBean)entry.getKey()).getName() + " : " +((TestBean)entry.getValue()).getCity());
			else
				System.out.println((String)entry.getKey() + " : " +((TestBean)entry.getValue()).getCity());
		}
		
		printStatic();
		//sayHello();
	}
	
	public static void printStatic(){
		
		//public static String abc = "Hello";
		System.out.println(" staticBool in printStatic() : "+staticBool);
		//sayHello();
	}
	
	public String sayHello(){
		return "Hello Vinay";
	}
}
