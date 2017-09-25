package com.classes.scan;

import java.util.HashSet;
import java.util.Set;

public class TestScanSpringBootProject {
	public static void main(String[] args) {
		String file = "/Users/kevin/java_home/open/route/target/route.jar!/BOOT-INF/classes!/com/hivescm/open/api/";
		Set<Class<?>> classes = new HashSet<Class<?>>();
		ScanClass.fillClasses(file, "com.hivescm.open.api", new ClassFilter() {

			@Override
			public boolean accept(Class<?> clazz) {
//				return clazz.getAnnotation(Api.class)!=null;
				return true;
			}

		}, classes);
		System.out.println(classes.size());
	}
}
