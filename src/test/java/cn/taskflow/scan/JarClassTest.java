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
package cn.taskflow.scan;

import java.util.jar.JarEntry;

import cn.taskflow.scan.core.JarClass;
import junit.framework.Assert;

import org.junit.Test;

public class JarClassTest {
    @Test
    public void test() {
        JarEntry entry = new JarEntry("com/xxx/xxx/MyClass.class");
        JarClass jarClass = new JarClass(entry, "com");
        Assert.assertEquals(jarClass.getClassPath(), "com.xxx.xxx.MyClass");
    }

    @Test
    public void matches() {
        JarEntry entry = new JarEntry("com/xxx/xxx/MyClass.class");
        JarClass jarClass = new JarClass(entry, "com");
        Assert.assertTrue(jarClass.isMatches());
    }
}
