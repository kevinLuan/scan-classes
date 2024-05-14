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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Maven 发布Jar使用工具
 * 
 * @CreateTime 2016年6月2日 下午4:57:40
 * @author KEVIN LUAN
 */
public class MavenDeployJarUtils {
    /**
     * 将classes拷贝到path目录
     */
    public static void includeClass(String path, Class<?>... classes) throws Exception {
        CopyClassUtils.copying(classes);
    }

    public static void includeClass(Class<?>... classes) throws Exception {
        CopyClassUtils.copying(classes);
    }

    /**
     * 获取Maven home
     */
    private static String mvn = null;
    static {
        try {
            mvn = getMAVEN_HOME() + "/bin/mvn";

            String path = executeShell("pwd");
            path = path.trim() + "/target/api/";
            // 初始化Class临时存储路径
            CopyClassUtils.OutputPath = path;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 执行Maven assembly
     */
    public static boolean assemblyProcess() throws Exception {
        System.out.println("----------------------assembly client api jar----------------------");
        String result = executeShell(mvn + " assembly:assembly");
        return isSuccess(result);
    }

    /**
     * 将path下com目录下文件及目录生成Jar包
     */
    public static String jar(String path, String jarName) throws IOException, InterruptedException {
        return exec(path, "jar -cvf " + jarName + " ./com");
    }

    /**
     * 升级Jar文件
     * 
     * @param jarName
     *        如：test.jar
     */
    public static String jar(String jarName) throws IOException, InterruptedException {
        return exec(CopyClassUtils.OutputPath, "jar -cvf " + jarName + " ./com");
    }

    /**
     * 生成Maven 格式的Jar文件
     */
    public static String jar(String groupId, String artifactId, String version) throws IOException,
                                                                               InterruptedException {
        String jarName = artifactId + "-" + version + ".jar";
        return exec(CopyClassUtils.OutputPath, "jar -cvf " + jarName + " ./com");
    }

    /**
     * Maven deploy 发布到远程私服
     * 
     * @param groupId
     *        com.weidian.open
     * @param artifactId
     *        config_center_client_api
     * @param version
     *        1.0.0-SNAPSHOT
     * @param url 远程私服地址
     */
    public static boolean deploySnapshots(String groupId, String artifactId, String version, String url)
                                                                                                        throws Exception {
        System.out.println("----------------------发布到远程私服----------------------");
        String jarName = artifactId + "-" + version + ".jar";
        String shell = mvn + " deploy:deploy-file -DgroupId=" + groupId + " -DartifactId=" + artifactId + " -Dversion="
                       + version + " -Dpackaging=jar -Dfile=" + jarName + " -DrepositoryId=snapshots -Durl=" + url;
        String result = exec(CopyClassUtils.OutputPath, shell);
        return isSuccess(result);
    }

    /**
     * Maven INSTALL 安装到本地仓库
     * 
     * @param groupId
     *        com.weidian.open
     * @param artifactId
     *        config_center_client_api
     * @param version
     *        1.0.0-SNAPSHOT
     */
    public static boolean installJarProcess(String groupId, String artifactId, String version) throws Exception {
        System.out.println("----------------------发布到本地仓库----------------------");
        String jarName = artifactId + "-" + version + ".jar";

        String shell = mvn + " install:install-file -Dfile=" + jarName + " -DgroupId=" + groupId + " -DartifactId="
                       + artifactId + " -Dversion=" + version + " -Dpackaging=jar";
        String result = exec(CopyClassUtils.OutputPath, shell);
        return isSuccess(result);
    }

    private static boolean isSuccess(String result) {
        if (result.indexOf("[INFO] BUILD SUCCESS") != -1) {
            return true;
        } else {
            System.err.println(result);
            return false;
        }
    }

    public static String exec(String path, String shell) throws IOException, InterruptedException {
        System.out.println("$shell>>" + shell);
        System.out.println("path:" + path);
        Process process = Runtime.getRuntime().exec(shell, new String[] {}, new File(path));
        try {
            String result = println(process.getInputStream()).toString();
            if (result == null || result.length() == 0) {
                String error = println(process.getErrorStream()).toString();
                System.err.println(error);
            }
            return result;
        } finally {
            process.waitFor();
            process.destroy();
        }
    }

    public static String read(InputStream is) throws IOException {
        byte[] data = new byte[is.available()];
        is.read(data);
        return new String(data);
    }

    public static StringBuilder println(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder(200);
        BufferedInputStream in = new BufferedInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        while (line != null) {
            builder.append(line + "\n");
            System.out.println(line);
            line = br.readLine();
        }
        br.close();
        in.close();
        return builder;
    }

    public static String executeShell(String command) throws Exception {
        System.out.println("$shell>> " + command);
        try {
            Process process = Runtime.getRuntime().exec(command);
            try {
                InputStream inputStream = process.getInputStream();
                return println(inputStream).toString();
            } finally {
                process.waitFor();
                process.destroy();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取Maven 环境变量
     * 
     * @return
     * @throws Exception
     */
    private static String getMAVEN_HOME() throws Exception {
        Map<String, String> envMap = getEnv();
        if (envMap.containsKey("M3_HOME")) {
            return envMap.get("M3_HOME");
        } else if (envMap.containsKey("M2_HOME")) {
            return envMap.get("M2_HOME");
        } else if (envMap.containsKey("MAVEN_HOME")) {
            return envMap.get("MAVEN_HOME");
        } else {
            throw new IllegalArgumentException("请配置maven环境变量`M2_HOME` or `M3_HOME`");
        }
    }

    private static Map<String, String> getEnv() throws Exception {
        String result = executeShell("/bin/bash -cl env");
        String[] kvs = result.split("\n");
        Map<String, String> envMap = new HashMap<String, String>();
        for (int i = 0; i < kvs.length; i++) {
            int index = kvs[i].indexOf("=");
            String key = kvs[i].substring(0, index);
            String value = kvs[i].substring(index + 1);
            envMap.put(key, value);
        }
        return envMap;
    }

    /**
     * 发布Jar 到Maven私服
     * <p>
     * 1. Copying classes //拷贝包含的Class文件
     * </p>
     * <p>
     * 2. jar -cvf xxx.jar ./com //生成Jar包
     * </p>
     * <p>
     * 3. mvn install xxx //发布到本地私服
     * </p>
     * <p>
     * 4. mvn deploy xxx //发布到远程私服
     * </p>
     * 
     */
    public static void start(String groupId, String artifactId, String version, String url, Class<?>... classes)
                                                                                                                throws Exception {
        MavenDeployJarUtils.includeClass(classes);
        MavenDeployJarUtils.jar(groupId, artifactId, version);
        boolean install = MavenDeployJarUtils.installJarProcess(groupId, artifactId, version);
        boolean deploy = MavenDeployJarUtils.deploySnapshots(groupId, artifactId, version, url);
        System.out.println("--------------------------------------------");
        if (install) {
            System.out.println("成功发布到本地私服");
        } else {
            System.err.println("发布到本地私服失败");
        }
        if (deploy) {
            System.out.println("成功发布到远程私服");
        } else {
            System.err.println("发布到远程私服失败");
        }
        if (deploy && install) {
            print(groupId, artifactId, version);
        }
    }

    static void print(String groupId, String artifactId, String version) {
        System.out.println("<dependency>");
        System.out.println("\t<groupId>" + groupId + "</groupId>");
        System.out.println("\t<artifactId>" + artifactId + "</artifactId>");
        System.out.println("\t<version>" + version + "</version>");
        System.out.println("</dependency>");
    }
}
