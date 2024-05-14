
## Architecture

![Architecture](...)

## Features
* 轻量级类扫描工具，支持扫描指定包下的类；


## Getting started

The following code snippet comes from [scan-classes Samples](https://github.com/kevinLuan/scan-classes.git). You may clone the sample project.

```bash
git clone https://github.com/kevinLuan/scan-classes.git
```

There's a [README](https://github.com/kevinLuan/scan-classes/blob/master/README.md) file under `scan-classes` directory. We recommend referencing the samples in that directory by following the below-mentioned instructions:

### Maven dependency
```xml
<dependency>
    <groupId>cn.taskflow</groupId>
    <artifactId>scan-classes</artifactId>
    <version>0.1.0</version>
</dependency>
```


### Running the samples

	public class MainRun {//扫描package: cn.taskflow.scan 下面所有类上使用了@Api的类
	public static void main(String[] args) {
		Set<Class<?>> set = ScanClass.scanPackage("cn.taskflow.scan", new ClassFilter() {

			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.getAnnotation(Api.class) != null;
			}
		});
		for (Class<?> clazz : set) {
			System.out.println(clazz);
		}
	}
}
	

### Importing into eclipse
	
	mvn eclipse:eclipse


