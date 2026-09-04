# 滤波标定算法学习应用 - 构建指南

## 🛠️ 开发环境要求

### 必需软件
- **Android Studio**: Hedgehog (2023.1.1) 或更高版本
- **JDK**: 17 或更高版本
- **Android SDK**: API 26 (Android 8.0) 或更高
- **Gradle**: 8.2 或更高版本
- **Kotlin**: 1.9.20 或更高版本

### 推荐配置
- 操作系统：Windows 10/11, macOS 12+, Ubuntu 20.04+
- RAM: 8GB 以上（推荐16GB）
- 硬盘空间: 10GB 以上

## 📥 项目导入

### 1. 克隆或下载项目
```bash
cd /home/guo/滤波标定
# 项目已在 FilterCalibrationApp/ 目录中
```

### 2. 使用Android Studio打开
1. 启动 Android Studio
2. 选择 "Open" 或 "Open an Existing Project"
3. 导航到 `/home/guo/滤波标定/FilterCalibrationApp`
4. 点击 "OK"

### 3. 等待Gradle同步
- 首次打开会自动下载依赖
- 可能需要5-15分钟（取决于网络速度）
- 如果遇到网络问题，可以配置国内镜像

## 🔧 Gradle配置（可选）

### 配置国内镜像加速

编辑 `settings.gradle.kts`，在 `repositories` 中添加：

```kotlin
dependencyResolutionManagement {
    repositories {
        // 添加阿里云镜像
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        
        // 原有的仓库
        google()
        mavenCentral()
    }
}
```

## 📦 依赖说明

### 核心依赖
- **Jetpack Compose**: UI框架
- **Material3**: Material Design 3组件
- **Navigation Compose**: 导航框架
- **Lifecycle**: 生命周期管理
- **ViewModel**: MVVM架构

### 数学计算
- **Kotlin标准库**: 数学运算

### 相机功能（待集成）
- **CameraX**: 相机API
- **OpenCV for Android**: 图像处理

### 可视化
- **Canvas (Compose)**: 2D绘图
- **MPAndroidChart**: 图表绘制（待集成）

## 🏗️ 构建项目

### 方法1：使用Android Studio
1. 点击工具栏的 "Build" → "Make Project"
2. 或使用快捷键：
   - Windows/Linux: `Ctrl + F9`
   - macOS: `Cmd + F9`

### 方法2：使用命令行
```bash
cd /home/guo/滤波标定/FilterCalibrationApp

# 清理
./gradlew clean

# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease
```

## 📱 运行应用

### 在模拟器上运行
1. 打开 AVD Manager: Tools → Device Manager
2. 创建或启动一个模拟器（推荐API 30+）
3. 点击 "Run" 按钮或按 `Shift + F10`

### 在真实设备上运行
1. 启用开发者选项和USB调试
2. 用USB连接设备
3. 在设备选择器中选择你的设备
4. 点击 "Run" 按钮

## 🐛 常见问题

### 问题1：Gradle同步失败
**症状**: "Sync failed: ..."

**解决方案**:
1. 检查网络连接
2. 配置镜像（见上文）
3. File → Invalidate Caches → Invalidate and Restart

### 问题2：SDK版本不匹配
**症状**: "Installed Build Tools revision X is corrupted"

**解决方案**:
1. Tools → SDK Manager
2. SDK Tools 标签
3. 勾选需要的版本，点击 Apply

### 问题3：内存不足
**症状**: "Out of memory: Java heap space"

**解决方案**:
编辑 `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### 问题4：Kotlin编译错误
**症状**: "Kotlin: ..."

**解决方案**:
1. 检查Kotlin版本是否为1.9.20
2. Tools → Kotlin → Configure Kotlin Plugin Updates
3. 更新到最新稳定版

## 📂 项目结构

```
FilterCalibrationApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/calibration/filter/
│   │   │   │   ├── MainActivity.kt              # 主Activity
│   │   │   │   ├── math/                        # 数学计算核心
│   │   │   │   │   ├── DataTypes.kt
│   │   │   │   │   ├── ProjectionModel.kt
│   │   │   │   │   └── JacobianCalculator.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/                   # 主题配置
│   │   │   │   │   ├── theory/                  # 理论学习界面
│   │   │   │   │   │   ├── TheoryScreen.kt
│   │   │   │   │   │   └── ChapterContent.kt
│   │   │   │   │   ├── visualization/           # 可视化界面
│   │   │   │   │   │   └── VisualizationScreen.kt
│   │   │   │   │   ├── calculator/              # 计算器界面
│   │   │   │   │   │   └── CalculatorScreen.kt
│   │   │   │   │   └── practice/                # 实战界面
│   │   │   │   │       └── PracticeScreen.kt
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🔍 代码说明

### 数学计算模块
**位置**: `app/src/main/java/com/calibration/filter/math/`

这个模块包含所有核心算法：
- 投影模型计算
- 畸变校正
- 雅可比矩阵计算
- 数据类型定义

**特点**:
- 纯Kotlin实现
- 无外部依赖
- 可独立测试
- 与原C++代码逻辑一致

### UI模块
**位置**: `app/src/main/java/com/calibration/filter/ui/`

使用Jetpack Compose构建：
- 声明式UI
- 响应式状态管理
- Material Design 3
- 组件化设计

## 🧪 测试

### 运行单元测试
```bash
./gradlew test
```

### 运行集成测试
```bash
./gradlew connectedAndroidTest
```

## 📦 生成APK

### Debug APK
```bash
./gradlew assembleDebug
```
输出: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK（需要签名）
```bash
./gradlew assembleRelease
```
输出: `app/build/outputs/apk/release/app-release.apk`

## 🔐 签名配置（可选）

创建 `keystore.properties` 文件：
```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=your_key_alias
storeFile=path/to/keystore.jks
```

在 `app/build.gradle.kts` 中配置签名。

## 📊 性能优化

### 启用R8代码压缩
已在 `build.gradle.kts` 中配置：
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(...)
    }
}
```

### 启用构建缓存
在 `gradle.properties` 中：
```properties
org.gradle.caching=true
org.gradle.parallel=true
```

## 🌐 国际化（未来）

目前应用为中文版，要添加其他语言：
1. 创建 `values-en/strings.xml`（英文）
2. 创建 `values-ja/strings.xml`（日文）
3. 等等...

## 📝 开发建议

### 1. 代码风格
- 遵循 Kotlin 官方代码风格
- 使用有意义的变量名
- 添加必要的注释

### 2. 提交规范
- 提交前运行测试
- 提交信息清晰明确
- 遵循约定式提交

### 3. 分支策略
- `main`: 稳定版本
- `develop`: 开发版本
- `feature/*`: 新功能
- `bugfix/*`: Bug修复

## 🚀 部署

### Google Play（未来）
1. 生成签名的Release APK
2. 创建Google Play开发者账号
3. 上传APK并填写应用信息
4. 提交审核

### 其他渠道
可以将APK上传到：
- GitHub Releases
- 自建服务器
- 第三方应用商店

## 📖 学习资源

- [Android开发官方文档](https://developer.android.com)
- [Jetpack Compose教程](https://developer.android.com/jetpack/compose)
- [Kotlin语言指南](https://kotlinlang.org/docs/home.html)
- [Material Design 3](https://m3.material.io)

## 💡 贡献指南

欢迎贡献代码！请遵循以下步骤：
1. Fork项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 📧 联系方式

如有问题或建议，请通过以下方式联系：
- 创建Issue
- 发送邮件
- 在讨论区留言

---

**祝开发顺利！🎉**
