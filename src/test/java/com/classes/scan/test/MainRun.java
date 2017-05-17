package com.classes.scan.test;

import java.util.Set;

import com.classes.scan.ClassFilter;
import com.classes.scan.ScanClass;

public class MainRun {
	public static void main(String[] args) {
		Set<Class<?>> set = ScanClass.scanPackage("com.classes.scan.test", new ClassFilter() {

			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.getAnnotation(Api.class) != null;
			}
		});
		for (Class<?> clazz : set) {
			System.out.println(clazz);
		}
	}
}
