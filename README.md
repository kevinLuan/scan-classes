scan-classes
============
<div align="left">
  <a href="javascript:void(0);"><img src="https://img.shields.io/badge/build-passing-brightgreen" /></a>
  <a href="javascript:void(0);" target="_blank"><img src="https://img.shields.io/badge/docs-latest-brightgreen" /></a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
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
    //Scan packages: cn.taskflow.scan All classes below that use @Api on them
    Set<Class<?>> set = ClassScanner.scanPackage("cn.taskflow.scan", (clazz) -> {
        return clazz.getAnnotation(Api.class) != null;
    });
    for (Class<?> clazz : set) {
        System.out.println(clazz);
    }
```
	



