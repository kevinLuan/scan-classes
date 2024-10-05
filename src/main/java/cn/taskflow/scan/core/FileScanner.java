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
package cn.taskflow.scan.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {
    private static boolean matches(String scanFileName, String filePath) {
        int lastFileIndex = filePath.lastIndexOf(File.separator);
        if (filePath.substring(lastFileIndex + 1).equals(scanFileName)) {
            return true;
        }
        return false;
    }

    /**
     * Scan file
     * 
     * @param path Path to scan
     * @param scanFileName File name to scan for
     * @return
     * @throws Exception
     */
    public static File scanFile(String path, String scanFileName) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("Specified path does not exist:" + file.getPath());
        } else {
            String[] files = file.list();
            List<String> isDirectory = new ArrayList<String>();
            while (true) {
                for (int i = 0; i < files.length; i++) {
                    String pathB = path + File.separator + files[i];
                    File f = new File(pathB);
                    if (f.isFile()) {
                        if (matches(scanFileName, f.getPath())) {
                            return f;
                        }
                    } else if (f.isDirectory()) {
                        isDirectory.add(f.getAbsolutePath());
                    }
                }
                while (isDirectory.size() > 0) {
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < isDirectory.size(); i++) {
                        String pathC = isDirectory.get(i);
                        File fil = new File(pathC);
                        String[] flist = fil.list();
                        for (int j = 0; j < flist.length; j++) {
                            String pathD = pathC;
                            pathD = pathD + File.separator + flist[j];
                            File ff = new File(pathD);
                            if (ff.isFile()) {
                                if (matches(scanFileName, ff.getPath())) {
                                    return ff;
                                }
                            } else if (ff.isDirectory()) {
                                list.add(ff.getAbsolutePath());
                            }
                        }

                    }
                    isDirectory = list;
                }
                break;
            }
            return null;
        }
    }
}
