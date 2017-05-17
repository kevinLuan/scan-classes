package com.classes.scan.test;

import java.util.concurrent.Callable;

public class ObjClassName {
	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(".....");
			}
		}).start();

		new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				return null;
			}
		}.call();
	}
}
