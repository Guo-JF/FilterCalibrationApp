# 使用 Android Studio 构建应用（推荐方式）

由于项目缺少 Gradle Wrapper JAR 文件，**推荐使用 Android Studio 来构建和运行此项目**。

## 🎯 为什么使用 Android Studio？

Android Studio 会自动：
- ✅ 下载正确版本的 Gradle
- ✅ 配置所有必要的工具
- ✅ 管理 SDK 和依赖
- ✅ 提供图形化界面
- ✅ 集成调试工具

## 📥 步骤1：安装 Android Studio

### 下载
访问官网下载最新版本：
https://developer.android.com/studio

### Linux 安装步骤
```bash
# 解压下载的文件
tar -xzf android-studio-*.tar.gz

# 移动到 /opt (可选)
sudo mv android-studio /opt/

# 运行 Android Studio
/opt/android-studio/bin/studio.sh
```

### 首次启动配置
1. 选择 "Standard" 安装类型
2. 等待下载 SDK 和其他组件（需要一些时间）
3. 完成后关闭欢迎界面

## 📂 步骤2：打开项目

1. 启动 Android Studio
2. 点击 **"Open"** 或 **"Open an Existing Project"**
3. 导航到：`/home/guo/滤波标定/FilterCalibrationApp`
4. 点击 **"OK"**

## ⏳ 步骤3：等待 Gradle 同步

项目打开后，Android Studio 会自动：
- 下载 Gradle 8.2
- 下载所有依赖库
- 构建项目索引

**首次同步可能需要 10-20 分钟**，取决于网络速度。

### 如果同步失败
如果网络较慢或无法访问 Google 服务器，可以配置国内镜像：

编辑 `settings.gradle.kts`：
```kotlin
dependencyResolutionManagement {
    repositories {
        // 添加阿里云镜像
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        
        google()
        mavenCentral()
    }
}
```

然后点击 **File → Sync Project with Gradle Files**

## 📱 步骤4：创建模拟器

### 方法1：使用 Device Manager
1. 点击工具栏的 **Device Manager** 图标（手机图标）
2. 点击 **"Create Device"**
3. 选择一个设备型号（推荐 Pixel 6）
4. 选择系统镜像（推荐 API 33 - Android 13）
5. 点击 **"Next"** 和 **"Finish"**

### 方法2：快速创建
1. 点击顶部工具栏的设备下拉菜单
2. 选择 **"Create New Virtual Device"**
3. 按照向导完成创建

## ▶️ 步骤5：运行应用

1. 确保选择了刚创建的模拟器
2. 点击绿色的 **"Run"** 按钮（▶️）
3. 或按快捷键：`Shift + F10`

应用会自动：
- 编译代码
- 打包成 APK
- 安装到模拟器
- 启动应用

## 🔧 常见问题

### 问题1：SDK 未安装
**症状**：提示 "SDK not found" 或类似错误

**解决方案**：
1. **File → Settings → Appearance & Behavior → System Settings → Android SDK**
2. 确保勾选了 **Android 13.0 (API 33)** 或更高版本
3. 点击 **"Apply"** 等待下载

### 问题2：Gradle 同步一直失败
**症状**：红色错误信息，无法同步

**解决方案**：
1. 配置国内镜像（见上文）
2. **File → Invalidate Caches → Invalidate and Restart**
3. 检查网络连接

### 问题3：模拟器无法启动
**症状**：模拟器显示黑屏或无法启动

**解决方案**：
1. 确保启用了 **KVM**（Linux）或 **HAXM**（Windows）
2. 检查 BIOS 是否启用了虚拟化
3. 尝试重新创建模拟器

### 问题4：编译错误
**症状**：Kotlin 或 Gradle 错误

**解决方案**：
1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. 检查 JDK 版本（需要 JDK 17）

## 💻 在真实设备上运行

### 1. 启用开发者选项
在手机上：
1. **设置 → 关于手机**
2. 连续点击 **"版本号"** 7次
3. 返回设置，找到 **"开发者选项"**

### 2. 启用 USB 调试
1. 进入 **"开发者选项"**
2. 启用 **"USB 调试"**
3. 用 USB 线连接电脑

### 3. 运行应用
1. 在 Android Studio 的设备选择器中选择你的手机
2. 点击 **"Run"** 按钮
3. 在手机上允许 USB 调试授权

## 📊 查看日志

如果应用出现问题：
1. 打开 **Logcat** 窗口（底部工具栏）
2. 选择你的设备
3. 搜索 "com.calibration.filter"
4. 查看错误信息

## 🎓 学习 Android Studio

### 快捷键
- **Shift + F10**：运行
- **Shift + F9**：调试
- **Ctrl + F9**：构建项目
- **Alt + Enter**：快速修复
- **Ctrl + Space**：代码补全

### 有用的窗口
- **Logcat**：查看日志
- **Build**：查看编译输出
- **Device Manager**：管理模拟器
- **Project**：浏览文件

## 🚀 下一步

成功运行应用后：
1. 阅读 `快速开始.md` 了解如何使用应用
2. 阅读 `APP_FEATURES.md` 了解完整功能
3. 开始学习滤波标定算法！

## 💡 提示

- Android Studio 会占用较多内存（建议 8GB+ RAM）
- 首次构建会下载大量文件（需要良好的网络）
- 保持 Android Studio 更新到最新版本
- 遇到问题先尝试 Clean 和 Rebuild

---

**祝你构建成功！如有问题，请查看 Android Studio 的官方文档。**
