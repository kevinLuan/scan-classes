package com.classes.utils;

import java.io.File;

public class JavaEntity {
	private Class<?> clazz;

	private JavaEntity(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getClassPath() {
		return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	public static JavaEntity getInstance(Class<?> clazz) {
		return new JavaEntity(clazz);
	}

	public String getJavaCodePath() {
		String projectPath = getProjectPath();
		String pack = getPackPath();
		String javaFileName = clazz.getSimpleName() + ".java";
		String javaCodePath = projectPath + "src/main/java/" + pack + "/" + javaFileName;
		return javaCodePath;
	}

	public String getPackPath() {
		return clazz.getPackage().getName().replace(".", "/");
	}

	public String getProjectPath() {
		String path = getClassPath();
		if (path != null) {
			int end = path.length() - "target/classes/".length();
			return path.substring(0, end);
		} else {
			return null;
		}
	}

	public File getJavaFile() {
		return new File(getJavaCodePath());
	}

	public String getJavaFileName() {
		return getJavaFile().getName();
	}

	public String getTargetPath() {
		return getProjectPath() + "target/";
	}
}
