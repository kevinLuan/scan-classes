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

import java.util.Set;

import cn.taskflow.scan.core.ClassFilter;
import cn.taskflow.scan.core.ClassScanner;
import cn.taskflow.scan.pojo.Api;
import org.junit.Assert;
import org.junit.Test;

public class ScannerAnnotationTests {
    @Test
    public void test() {
        //Scan packages: cn.taskflow.scan All classes below that use @Api on them
        Set<Class<?>> set = ClassScanner.scanPackage("cn.taskflow.scan", (clazz) -> {
            return clazz.getAnnotation(Api.class) != null;
        });
        for (Class<?> clazz : set) {
            System.out.println(clazz);
        }
        Assert.assertTrue(set.size() >= 4);
    }

}
