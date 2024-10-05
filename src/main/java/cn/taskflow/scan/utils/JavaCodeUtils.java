/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.taskflow.scan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.taskflow.scan.core.ClassFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaCodeUtils {
    private static final Logger log = LoggerFactory.getLogger(JavaCodeUtils.class);

    public static void main(String[] args) throws Exception {
        copyJavaFile(JavaCodeUtils.class, ClassFilter.class);
        jar("com.test", "hello", "1.0.0");
    }

    /**
     * Copy the Java file of the Class to this directory: /project_name/target/api_source/
     * <p>
     * Only supports Classes with Java source code existing in the local project
     * </p>
     */
    public static void copyJavaFile(Class<?>... clazz) throws IOException {
        for (int i = 0; i < clazz.length; i++) {
            JavaEntity javaEntity = JavaEntity.getInstance(clazz[i]);
            boolean result = copy(javaEntity);
            log.info("copy:" + clazz[i] + ".java->result:" + result);
        }
    }

    /**
     * Generate Maven format Jar file
     */
    public static String jar(String groupId, String artifactId, String version) throws IOException,
                                                                               InterruptedException {
        String jarName = artifactId + "-" + version + "-source.jar";
        log.info(getApiSourcePath() + " jar -cvf " + jarName + " ./com");
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
