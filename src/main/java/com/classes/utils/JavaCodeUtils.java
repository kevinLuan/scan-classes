package com.classes.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.classes.scan.ClassFilter;

public class JavaCodeUtils {
    public static void main(String[] args) throws Exception {
        copyJavaFile(JavaCodeUtils.class, ClassFilter.class);
        jar("com.test", "hello", "1.0.0");
    }

    /**
     * 拷贝Class的java文件到该目录下：/project_name/target/api_source/
     * <p>
     * 只支持本地项目中存在Java源代码的Class
     * </p>
     */
    public static void copyJavaFile(Class<?>... clazz) throws IOException {
        for (int i = 0; i < clazz.length; i++) {
            JavaEntity javaEntity = JavaEntity.getInstance(clazz[i]);
            boolean result = copy(javaEntity);
            System.out.println("copy:" + clazz[i] + ".java->result:" + result);
        }
    }

    /**
     * 生成Maven 格式的Jar文件
     */
    public static String jar(String groupId, String artifactId, String version) throws IOException,
            InterruptedException {
        String jarName = artifactId + "-" + version + "-source.jar";
        System.out.println(getApiSourcePath() + " jar -cvf " + jarName + " ./com");
        return MavenDeployJarUtils.exec(getApiSourcePath(), "jar -cvf " + jarName + " ./com");
    }

    public static String getApiSourcePath(Class<?> clazz) {
        return JavaEntity.getInstance(clazz).getTargetPath() + "api_source/";
    }

    public static String getApiSourcePath() {
        String path = CopyClassUtils.class.getResource(".").getFile();
        int index = path.indexOf("/target/") + "/target".length();
        return path.substring(0, index) + "/api_source/";
    }

    public static boolean copy(JavaEntity javaEntity) throws IOException {
        File source = javaEntity.getJavaFile();
        byte[] data = read(source);
        File target = new File(javaEntity.getTargetPath() + "api_source/" + javaEntity.getPackPath() + "/"
                + javaEntity.getJavaFileName());
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        write(target, data);
        return true;
    }

    private static void write(File target, byte[] data) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(target);
            fos.write(data);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

    }

    private static byte[] read(File source) throws IOException {
        FileInputStream fis = null;
        byte[] data = new byte[0];
        try {
            fis = new FileInputStream(source);
            data = new byte[fis.available()];
            fis.read(data);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return data;
    }

}
