# 滤波标定算法学习应用

这是一个完整的安卓应用，用于学习和理解相机标定中的滤波算法。

## 功能特性

### 📚 理论学习模块
- **完整数学推导**：从基础概念到高级算法的逐步推导
- **交互式公式**：点击公式查看详细解释
- **分步推导**：每个数学步骤都有详细说明

### 🎨 可视化模块
- **坐标系变换动画**：3D可视化标定板→相机→像素的转换过程
- **投影过程演示**：实时显示透视投影的几何原理
- **畸变效果对比**：滑动调节畸变参数，实时看到效果
- **滤波过程动画**：展示信息矩阵如何逐帧累积

### 🧮 交互式计算器
- **投影计算器**：输入3D点和内参，计算投影结果
- **雅可比矩阵计算**：实时计算并显示偏导数
- **信息滤波演示**：模拟多帧观测的融合过程

### 🎯 实战演练
- **标定板检测**：使用相机检测棋盘格
- **实时标定**：边拍摄边标定，显示收敛过程
- **结果分析**：展示RMS误差、信息增益等指标

## 目录结构

```
FilterCalibrationApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/calibration/filter/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theory/      # 理论学习界面
│   │   │   │   │   ├── visualization/ # 可视化界面
│   │   │   │   │   ├── calculator/   # 计算器界面
│   │   │   │   │   └── practice/     # 实战界面
│   │   │   │   ├── math/            # 数学计算核心
│   │   │   │   ├── renderer/        # 3D渲染引擎
│   │   │   │   └── utils/
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── raw/             # 数学公式LaTeX文件
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── build.gradle.kts
└── README.md
```

## 构建说明

1. 使用 Android Studio 打开项目
2. 同步 Gradle 依赖
3. 运行到设备或模拟器

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **数学渲染**: MathJax-Android / KaTeX
- **3D可视化**: OpenGL ES / Rajawali
- **图表**: MPAndroidChart
- **相机**: CameraX

## 最低要求

- Android 8.0 (API 26)
- 推荐 Android 10+ 以获得最佳体验
