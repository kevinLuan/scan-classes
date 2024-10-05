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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Maven Jar Deployment Utility
 * 
 * @CreateTime 2016-06-02 16:57:40
 * @author KEVIN LUAN
 */
public class MavenDeployJarUtils {
    private final static Logger log = LoggerFactory.getLogger(MavenDeployJarUtils.class);

    /**
     * Copy classes to the specified path directory
     */
    public static void includeClass(String path, Class<?>... classes) throws Exception {
        CopyClassUtils.copying(classes);
    }

    public static void includeClass(Class<?>... classes) throws Exception {
        CopyClassUtils.copying(classes);
    }

    /**
     * Get Maven home
     */
    private static String mvn = null;
    static {
        try {
            mvn = getMAVEN_HOME() + "/bin/mvn";

            String path = executeShell("pwd");
            path = path.trim() + "/target/api/";
            // Initialize temporary storage path for Class
            CopyClassUtils.OutputPath = path;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Execute Maven assembly
     */
    public static boolean assemblyProcess() throws Exception {
        log.info("----------------------assembly client api jar----------------------");
        String result = executeShell(mvn + " assembly:assembly");
        return isSuccess(result);
    }

    /**
     * Generate Jar package from files and directories under the com directory in the specified path
     */
    public static String jar(String path, String jarName) throws IOException, InterruptedException {
        return exec(path, "jar -cvf " + jarName + " ./com");
    }

    /**
     * Update Jar file
     * 
     * @param jarName
     *        e.g.: test.jar
     */
    public static String jar(String jarName) throws IOException, InterruptedException {
        return exec(CopyClassUtils.OutputPath, "jar -cvf " + jarName + " ./com");
    }

    /**
     * Generate Maven format Jar file
     */
    public static String jar(String groupId, String artifactId, String version) throws IOException,
                                                                               InterruptedException {
        String jarName = artifactId + "-" + version + ".jar";
        return exec(CopyClassUtils.OutputPath, "jar -cvf " + jarName + " ./com");
    }

    /**
     * Maven deploy to remote repository
     * 
     * @param groupId
     *        com.weidian.open
     * @param artifactId
     *        config_center_client_api
     * @param version
     *        1.0.0-SNAPSHOT
     * @param url Remote repository address
     */
    public static boolean deploySnapshots(String groupId, String artifactId, String version, String url)
                                                                                                        throws Exception {
        log.info("----------------------Deploy to remote repository----------------------");
        String jarName = artifactId + "-" + version + ".jar";
        String shell = mvn + " deploy:deploy-file -DgroupId=" + groupId + " -DartifactId=" + artifactId + " -Dversion="
                       + version + " -Dpackaging=jar -Dfile=" + jarName + " -DrepositoryId=snapshots -Durl=" + url;
        String result = exec(CopyClassUtils.OutputPath, shell);
        return isSuccess(result);
    }

    /**
     * Maven INSTALL to local repository
     * 
     * @param groupId
     *        com.weidian.open
     * @param artifactId
     *        config_center_client_api
     * @param version
     *        1.0.0-SNAPSHOT
     */
    public static boolean installJarProcess(String groupId, String artifactId, String version) throws Exception {
        log.info("----------------------Install to local repository----------------------");
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
            log.error(result);
            return false;
        }
    }

    public static String exec(String path, String shell) throws IOException, InterruptedException {
        log.info("$shell>>" + shell);
        log.info("path:" + path);
        Process process = Runtime.getRuntime().exec(shell, new String[] {}, new File(path));
        try {
            String result = println(process.getInputStream()).toString();
            if (result == null || result.length() == 0) {
                String error = println(process.getErrorStream()).toString();
                log.error(error);
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
            log.info(line);
            line = br.readLine();
        }
        br.close();
        in.close();
        return builder;
    }

    public static String executeShell(String command) throws Exception {
        log.info("$shell>> " + command);
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
     * Get Maven environment variable
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
            throw new IllegalArgumentException("Please configure maven environment variable `M2_HOME` or `M3_HOME`");
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
     * Deploy Jar to Maven repository
     * <p>
     * 1. Copying classes //Copy included Class files
     * </p>
     * <p>
     * 2. jar -cvf xxx.jar ./com //Generate Jar package
     * </p>
     * <p>
     * 3. mvn install xxx //Install to local repository
     * </p>
     * <p>
     * 4. mvn deploy xxx //Deploy to remote repository
     * </p>
     * 
     */
    public static void start(String groupId, String artifactId, String version, String url, Class<?>... classes)
                                                                                                                throws Exception {
        MavenDeployJarUtils.includeClass(classes);
        MavenDeployJarUtils.jar(groupId, artifactId, version);
        boolean install = MavenDeployJarUtils.installJarProcess(groupId, artifactId, version);
        boolean deploy = MavenDeployJarUtils.deploySnapshots(groupId, artifactId, version, url);
        log.info("--------------------------------------------");
        if (install) {
            log.info("Successfully installed to local repository");
        } else {
            log.error("Failed to install to local repository");
        }
        if (deploy) {
            log.info("Successfully deployed to remote repository");
        } else {
            log.error("Failed to deploy to remote repository");
        }
        if (deploy && install) {
            print(groupId, artifactId, version);
        }
    }

    static void print(String groupId, String artifactId, String version) {
        log.info("<dependency>");
        log.info("\t<groupId>" + groupId + "</groupId>");
        log.info("\t<artifactId>" + artifactId + "</artifactId>");
        log.info("\t<version>" + version + "</version>");
        log.info("</dependency>");
    }
}
