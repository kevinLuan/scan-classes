package com.classes.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanNativeFile {
  private static boolean matches(String scanFileName, String filePath) {
    int lastFileIndex = filePath.lastIndexOf(File.separator);
    if (filePath.substring(lastFileIndex + 1).equals(scanFileName)) {
      return true;
    }
    return false;
  }

  /**
   * 扫描文件
   * 
   * @param path 执行扫描路径
   * @param scanFileName 扫描文件名称
   * @return
   * @throws Exception
   */
  public static File scanFile(String path, String scanFileName) throws Exception {
    File file = new File(path);
    if (!file.exists()) {
      throw new Exception("指定路径不存在:" + file.getPath());
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
