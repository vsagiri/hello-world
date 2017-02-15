package com.examples;

public class SubClass extends SuperClass {
	public SubClass(){
		
	}
	
	@Override
	public String sayHello(String helloMsg){
		return helloMsg+" SubClass";
	}
	
	
	public static String sayStaticHello(String helloMsg){
		return helloMsg+" from SubClass.sayStaticHello.... ";
	}
}
