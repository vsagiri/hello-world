package com.examples;

public class SuperSubStaticMethodTest {

	public static void main(String[] args) {
		try {
			System.out.println(" 111 ");
			SuperClass superClass = new SuperClass();
			System.out.println(superClass.sayHello("Hello "));
			System.out.println(superClass.sayStaticHello(" Static Hello "));

			SuperClass subClass = new SubClass();
			System.out.println(subClass.sayHello("Hello "));
			System.out.println(subClass.sayStaticHello(" Static Hello "));
			
			SubClass subClass2 = new SubClass();
			System.out.println(subClass2.sayHello("Hello "));
			System.out.println(subClass2.sayStaticHello(" Static Hello "));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
