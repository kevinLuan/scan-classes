package com.classes.scan;

import com.classes.utils.MavenDeployJarUtils;

public class ScanNativeFileTest {
	public static final String PATH = "/Users/kevin/JAVA_HOME/";

	public static void main(String arg[]) throws Exception {
		String groupId = "com.x.x";
		String artifactId = "x";
		String version = "0.0.0-SNAPSHOT";
		String url = "http://xxx/nexus/content/repositories/snapshots/";
		// 增加匿名类Copy支持
		Class<?> clazzs[] = new Class<?>[] { ClassEntity.class, ClassFile.class };
		MavenDeployJarUtils.start(groupId, artifactId, version, url, clazzs);
	}

}
