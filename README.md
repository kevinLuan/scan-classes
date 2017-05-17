#JAVA class 类扫描工具


### author:kevin Luan

	email: kevin_Luan@126.com

	git clone https://github.com/kevinLuan/scan-classes.git
	
### 打包成JAR 包命令

	安装包本地仓库
	mvn clean compile install -Dmaven.test.skip=true
	打包成jar包安装到本地仓库并发布到远程私服
	mvn clean compile install deploy -Dmaven.test.skip=true


###Running the samples

	public class MainRun {//扫描package: com.classes.scan.test 下面所有类上使用了@Api的类
	public static void main(String[] args) {
		Set<Class<?>> set = ScanClass.scanPackage("com.classes.scan.test", new ClassFilter() {

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
	

###Importing into eclipse
	
	mvn eclipse:eclipse
	

###打包成JAR 包命令

	安装包本地仓库
	mvn clean compile install -Dmaven.test.skip=true
	打包成jar包安装到本地仓库并发布到远程私服
	mvn clean compile install deploy -Dmaven.test.skip=true