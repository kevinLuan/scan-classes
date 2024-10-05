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
package cn.taskflow.scan.test;

import java.io.File;

import cn.taskflow.scan.core.ClassInfo;
import org.junit.Test;

import junit.framework.Assert;

public class ClassInfoTests {
    @Test
    public void test() {
        File file = new File("/r1/r2/r3/com/xx/MyClass.class");
        String scanPackage = "com.xx";
        String searchClassPath = "/r1/r2/r3/com/xx";
        ClassInfo classInfo = new ClassInfo(file, scanPackage, searchClassPath);
        Assert.assertEquals(classInfo.getClassPath(), "com.xx.MyClass");
    }

    @Test
    public void test_1() {
        File file = new File(ClassInfo.class.getResource("").getPath());
        String scanPackage = "com.classes.scan.";
        String searchClassPath = "/Users/kevin/Desktop/scan-classes/target/";
        ClassInfo entity = new ClassInfo(file, scanPackage, searchClassPath);
        //        Assert.assertEquals(entity.getClassPath(), "cn.taskflow.scan.ClassEntity");
    }

    @Test
    public void matches() {
        File file = new File("/r1/r2/r3/com/xx/MyClass.class");
        String scanPackage = "com.xx";
        String searchClassPath = "/r1/r2/r3/com/xx";
        ClassInfo classInfo = new ClassInfo(file, scanPackage, searchClassPath);
        Assert.assertEquals(classInfo.getClassPath(), "com.xx.MyClass");
        Assert.assertTrue(classInfo.isMatches());
    }
}
