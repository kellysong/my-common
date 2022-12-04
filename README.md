
## 基本说明

base-core: 一个基于MVP快速开发框架，封装了MVP、网络、日志、常用工具类、集成了最常用最基础类库(设置、插件换肤、沉寂式)、官方库、第三方依赖库（Rxjava、Retrofit等框架）

base-core-ktx: 一个基于base-core构建的kotlin MVVM快速开发框架，使用kotlin协程、 retrofit、Jetpack全家桶等组件，基于androidx(appcompat 1.2.0)编译

app: 是测试示例，包含MVC、MVP、MVVM、MVI架构示例

## 版本变迁历史

### 基于android support 26.0.2编译

    implementation 'com.github.kellysong.my-common:base-adapter:1.0.0'
    implementation 'com.github.kellysong.my-common:base-core:1.0.0'


### 基于android support 28.0.0编译

    implementation 'com.github.kellysong.my-common:base-adapter:2.0.2'
    implementation 'com.github.kellysong.my-common:base-core:2.0.2'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'


### 基于androidx(appcompat 1.1.0)编译

    implementation 'com.github.kellysong.my-common:base-adapter:2.1.0'
    implementation 'com.github.kellysong.my-common:base-core:2.1.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'



### 基于androidx(appcompat 1.2.0)编译(开始支持Kotlin MVVM)

    implementation 'com.github.kellysong.my-common:base-adapter:2.3.0'
    implementation 'com.github.kellysong.my-common:base-core:2.3.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'

     // optional - Kotlin MVVM support for base-core-ktx
    implementation 'com.github.kellysong.my-common:base-core-ktx:2.3.0'

### 最新依赖

    implementation 'com.github.kellysong.my-common:base-adapter:2.3.5'
    implementation 'com.github.kellysong.my-common:base-core:2.3.5'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'

     // optional - Kotlin MVVM support for base-core-ktx
    implementation 'com.github.kellysong.my-common:base-core-ktx:2.3.5'

更新日志：  [点我](更新说明.md)

## 使用

步骤 1. 在工程跟目录build.gradle文件下添加仓库

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

步骤2. 添加依赖

	dependencies {
	    
	     
        implementation 'com.github.kellysong.my-common:base-core:2.3.5'
        // 可选
        annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
        annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
         // optional - Kotlin MVVM support for base-core-ktx
        implementation 'com.github.kellysong.my-common:base-core-ktx:2.3.5'
	    
	}

步骤3. 使用自定义的Application继承BaseApplication