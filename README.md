#JAVA class 类扫描工具

### author:kevin Luan
	email: kevin_Luan@126.com

### 打包成JAR 包命令
	安装包本地仓库
	mvn clean compile install -Dmaven.test.skip=true
	打包成jar包安装到本地仓库并发布到远程私服
	mvn clean compile install deploy -Dmaven.test.skip=true

###Running the samples
	#扫描项目中的classes 目录下的类
	Set<Class<?>> packageSet = ScanClass.scanPackage();
		for (Class<?> clazz : packageSet) {
			if(clazz.toString().indexOf("com.classes")!=-1)
				System.out.println(clazz);	
		}
	#扫描包：“com.classes.scan” 下面的所有Class文件，包括依赖的JAR包
	 Set<Class<?>> packageSet = ScanClass.scanPackage("com.classes.scan");
	 	for (Class<?> clazz : packageSet) {
			System.out.println(clazz);
		}

###Importing into eclipse
	mvn eclipse:eclipse
	
