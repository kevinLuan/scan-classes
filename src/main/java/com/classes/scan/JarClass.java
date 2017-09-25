package com.classes.scan;

import java.io.File;
import java.util.jar.JarEntry;

public class JarClass extends ClassFile {
	private JarEntry jarEntry;
	private String scanPackage;

	public JarClass(JarEntry entry, String scanPackage) {
		this.jarEntry = entry;
		this.scanPackage = scanPackage;
	}

	public boolean isClass() {
		return jarEntry.getName().endsWith(".class");
	}

	/**
	 * 获取Class类名称 x.x.x.MyClass
	 * 
	 * @param className
	 * @return
	 */
	public String getClassPath() {
		// entry.getName() = com/xxx/xxx/MyClass.class
		String className = jarEntry.getName();
		if (className.startsWith("BOOT-INF/classes/")) {
			className = className.substring("BOOT-INF/classes/".length());
		}
		className = className.replace(File.separator, ".");
		if (className.endsWith(".class")) {
			className = className.substring(0, className.length() - (".class".length()));
		}
		return className;
	}

	public boolean isMatches() {
		return getClassPath().startsWith(scanPackage);
	}

	@Override
	public String getScanPackage() {
		if (scanPackage == null) {
			return "";
		}
		return scanPackage;
	}
}
