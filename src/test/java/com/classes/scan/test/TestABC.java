package com.classes.scan.test;

public class TestABC {
	
	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				super.run();
			}
		};
		new TestABC() {

		}.toString();
		new Object() {

		}.toString();
	}
}
