scan-classes
============
<div align="left">
  <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
  <a href="javascript:void(0);"><img src="https://img.shields.io/badge/build-passing-brightgreen" /></a>
  <a href="javascript:void(0);" target="_blank"><img src="https://img.shields.io/badge/docs-latest-brightgreen" /></a>
  <a href="https://javadoc.io/doc/cn.taskflow/scan-classes/latest/index.html" target="_blank"><img src="https://javadoc.io/badge/cn.taskflow/scan-classes/0.3.0.svg" /></a>
  <a href="https://central.sonatype.com/artifact/cn.taskflow/scan-classes?smo=true"><img src="https://img.shields.io/maven-metadata/v.svg?label=Maven%20Central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcn%2Ftaskflow%2Fscan-classes%2Fmaven-metadata.xml" alt="License"></a>
</div>

English | [简体中文](./README-zh_CN.md)

## Overview
Lightweight java class scanner library

## Features
* Supports scanning classes under packages


## Installation
To integrate the project into your Java project.
The Maven project adds the following dependencies to the 'pom.xml' file:
```xml
<dependency>
    <groupId>cn.taskflow</groupId>
    <artifactId>scan-classes</artifactId>
    <version>latest</version>
</dependency>
```
Gradle dependency
```groovy
dependencies {
    implementation 'cn.taskflow:scan-classes:latest'
}
```

###  Sample

```java
    // Example 1: Scan classes with @Api annotation
    Set<Class<?>>apiClasses=ClassScanner.scanPackage("cn.taskflow.scan",(clazz)->{
        return clazz.isAnnotationPresent(Api.class);
    });
    System.out.println("API classes: "+apiClasses);
    // Example 2: Scan all interfaces
    Set<Class<?>>interfaces=ClassScanner.scanPackage("cn.taskflow.scan",Class::isInterface);
    System.out.println("Interfaces: "+interfaces);

    // Example 3: Scan all abstract classes
    Set<Class<?>>abstractClasses=ClassScanner.scanPackage("cn.taskflow.scan",
        (clazz)->Modifier.isAbstract(clazz.getModifiers())&&!clazz.isInterface());
    System.out.println("Abstract classes: "+abstractClasses);

    // Example 4: Scan classes that implement a specific interface
    Set<Class<?>>serviceImplementations=ClassScanner.scanPackage("cn.taskflow.scan",
        (clazz)->!clazz.isInterface()&&Service.class.isAssignableFrom(clazz));
    System.out.println("Service implementations: "+serviceImplementations);

    // Example 5: Scan classes with methods that have a specific annotation
    Set<Class<?>>classesWithAnnotatedMethods=ClassScanner.scanPackage("cn.taskflow.scan",(clazz)->{
        for(Method method:clazz.getDeclaredMethods()){
            if(method.isAnnotationPresent(Scheduled.class)){
                return true;
            }
        }return false;
    });
    System.out.println("Classes with @Scheduled methods: "+classesWithAnnotatedMethods);
```
	



