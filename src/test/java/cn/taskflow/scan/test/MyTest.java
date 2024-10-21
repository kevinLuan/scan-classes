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

import cn.taskflow.scan.core.ClassScanner;
import cn.taskflow.scan.pojo.Api;

import java.lang.reflect.Modifier;
import java.security.Provider;
import java.util.Set;
import java.lang.reflect.Method;

/**
 * @author SHOUSHEN.LUAN
 * @since 2024-10-21
 */
public class MyTest {
    public static void main(String[] args) {
        // 示例1: 扫描带有@Api注解的类
        Set<Class<?>> apiClasses = ClassScanner.scanPackage("cn.taskflow.scan", (clazz) -> {
            return clazz.isAnnotationPresent(Api.class);
        });
        System.out.println("API classes: " + apiClasses);
        // 示例2: 扫描所有接口
        Set<Class<?>> interfaces = ClassScanner.scanPackage("cn.taskflow.scan", Class::isInterface);
        System.out.println("Interfaces: " + interfaces);

        // 示例3: 扫描所有抽象类
        Set<Class<?>> abstractClasses = ClassScanner.scanPackage("cn.taskflow.scan", 
            (clazz) -> Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface());
        System.out.println("Abstract classes: " + abstractClasses);

        // 示例4: 扫描实现了特定接口的类
        Set<Class<?>> serviceImplementations = ClassScanner.scanPackage("cn.taskflow.scan", 
            (clazz) -> !clazz.isInterface() && Provider.Service.class.isAssignableFrom(clazz));
        System.out.println("Service implementations: " + serviceImplementations);

        // 示例5: 扫描带有特定注解的方法的类
        Set<Class<?>> classesWithAnnotatedMethods = ClassScanner.scanPackage("cn.taskflow.scan", (clazz) -> {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Api.class)) {
                    return true;
                }
            }
            return false;
        });
        System.out.println("Classes with @Scheduled methods: " + classesWithAnnotatedMethods);
    }
}
