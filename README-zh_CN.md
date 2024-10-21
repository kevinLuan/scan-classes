scan-classes
============
<div align="left">
  <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
  <a href="javascript:void(0);"><img src="https://img.shields.io/badge/build-passing-brightgreen" /></a>
  <a href="javascript:void(0);" target="_blank"><img src="https://img.shields.io/badge/docs-latest-brightgreen" /></a>
  <a href="https://javadoc.io/doc/cn.taskflow/scan-classes/latest/index.html" target="_blank"><img src="https://javadoc.io/badge/cn.taskflow/scan-classes/0.3.0.svg" /></a>
  <a href="https://central.sonatype.com/artifact/cn.taskflow/scan-classes?smo=true"><img src="https://img.shields.io/maven-metadata/v.svg?label=Maven%20Central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcn%2Ftaskflow%2Fscan-classes%2Fmaven-metadata.xml" alt="License"></a>
</div>

[English](./README.md) | 简体中文

## 目录
- [简述](#简述)
- [特点](#特点)
- [安装](#安装)
- [示例](#示例)
## 简述
轻量级java类扫描工具库

## 特点
* 支持扫描package 下的类


## 安装

要将项目中集成到您的 Java 项目中。

Maven 项目在 'pom.xml' 文件中添加以下依赖：
```xml
<dependency>
    <groupId>cn.taskflow</groupId>
    <artifactId>scan-classes</artifactId>
    <version>latest</version>
</dependency>
```
Gradle 项目添加以下依赖：
```groovy
dependencies {
    implementation 'cn.taskflow:scan-classes:latest'
}
```
### 示例

```java
    //Scan packages: cn.taskflow.scan All classes below that use @Api on them
    Set<Class<?>> set = ClassScanner.scanPackage("cn.taskflow.scan", (clazz) -> {
        return clazz.getAnnotation(Api.class) != null;
    });
    for (Class<?> clazz : set) {
        System.out.println(clazz);
    }
```