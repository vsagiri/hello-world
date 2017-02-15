package com.examples;

public class SuperClass {
	public SuperClass(){
		
	}
	
	public String sayHello(String helloMsg){
		return helloMsg+" SuperClass";
	}
	
	public static String sayStaticHello(String helloMsg){
		return helloMsg+" from SuperClass.sayStaticHello.... ";
	}
}
