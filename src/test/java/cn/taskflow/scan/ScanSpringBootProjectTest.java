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

import cn.taskflow.scan.core.ClassFilter;
import cn.taskflow.scan.core.ScanClass;

import java.util.HashSet;
import java.util.Set;

public class ScanSpringBootProjectTest {
    public static void main(String[] args) {
        String file = "/Users/kevin/java_home/open/route/target/route.jar!/BOOT-INF/classes!/com/hivescm/open/api/";
        Set<Class<?>> classes = new HashSet<Class<?>>();
        ScanClass.fillClasses(file, "com.hivescm.open.api", new ClassFilter() {

            @Override
            public boolean accept(Class<?> clazz) {
                //				return clazz.getAnnotation(Api.class)!=null;
                return true;
            }

        }, classes);
        System.out.println(classes.size());
    }
}
