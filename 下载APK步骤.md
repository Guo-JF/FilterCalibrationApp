# 📱 下载APK详细步骤

## ✅ 代码已推送成功！

现在GitHub正在自动构建你的应用。

---

## 📋 查看构建状态

### 1. 访问仓库的Actions页面

打开浏览器，访问：
```
https://github.com/Guo-JF/FilterCalibrationApp/actions
```

### 2. 查看构建进度

你会看到：
- 一个名为 "Android CI" 的workflow正在运行
- 状态图标：
  - 🟡 **黄色圆点**：正在构建中...
  - 🟢 **绿色对勾**：构建成功！
  - 🔴 **红色叉号**：构建失败

**首次构建大约需要5-10分钟**（需要下载所有依赖）

### 3. 等待构建完成

构建过程中你会看到多个步骤：
- ✅ Set up job
- ✅ Checkout
- ✅ Set up JDK 17
- ✅ Grant execute permission for gradlew
- ✅ Build Debug APK
- ✅ Upload APK

全部显示绿色对勾后，构建就完成了！

---

## 📦 下载APK

### 步骤1：进入成功的workflow

1. 在Actions页面，点击最新的（最上面的）workflow运行
2. 确保所有步骤都是绿色✅

### 步骤2：下载Artifact

1. **滚动到页面底部**
2. 找到 **"Artifacts"** 区域
3. 看到一个名为 **"app-debug"** 的文件
4. **点击下载**（会下载一个zip文件）

### 步骤3：解压APK

下载的是一个zip文件，需要解压：

```bash
# 在下载目录解压
cd ~/Downloads
unzip app-debug.zip

# 得到 app-debug.apk 文件
ls -lh app-debug.apk
```

---

## 📱 安装APK到手机

### 方法1：USB传输

```bash
# 如果手机连接了电脑
adb install ~/Downloads/app-debug.apk
```

### 方法2：微信/QQ传输

1. 通过微信文件传输助手发送APK到手机
2. 在手机上下载
3. 点击安装

### 方法3：网盘

1. 上传APK到百度云/OneDrive等
2. 手机端下载
3. 安装

### 方法4：直接下载（手机浏览器）

1. 手机浏览器访问：https://github.com/Guo-JF/FilterCalibrationApp/actions
2. 登录GitHub账号
3. 下载Artifacts
4. 直接安装

---

## 🔧 安装APK注意事项

### 首次安装需要允许"未知来源"

#### Android 8.0+
1. 安装时会提示"不允许安装未知应用"
2. 点击"设置"
3. 开启"允许来自此来源"
4. 返回继续安装

#### 具体步骤（不同手机略有差异）
- **小米**：设置 → 应用设置 → 安装未知应用
- **华为**：设置 → 安全 → 更多安全设置 → 安装外部来源应用
- **OPPO/Vivo**：设置 → 安全 → 未知来源
- **原生Android**：设置 → 应用和通知 → 特殊应用权限 → 安装未知应用

---

## 🎓 打开应用

安装完成后：

1. **找到应用图标**："滤波标定算法学习"
2. **点击打开**
3. **开始学习！**

### 应用界面

- **底部4个标签**：
  - 📚 **理论**：10章详细内容
  - 🎨 **可视化**：动画演示
  - 🧮 **计算器**：手动计算
  - 🎯 **实战**：相机标定

### 学习建议

1. 从"理论"标签开始
2. 点击第1章"算法概述"
3. 按顺序阅读10章内容
4. 使用"可视化"加深理解
5. 用"计算器"验证计算

---

## ❓ 常见问题

### Q1: 构建失败怎么办？
**A**: 点击失败的workflow，查看错误日志。通常重新运行即可：
- 点击右上角 "Re-run all jobs"

### Q2: 找不到Artifacts区域？
**A**: 
- 确保workflow已完成（全部绿色✅）
- 滚动到页面最底部
- Artifacts在所有步骤下方

### Q3: 下载的zip文件无法解压？
**A**: 
- 确保下载完整（大小约100-200KB）
- 使用系统自带的解压工具
- 或使用 `unzip` 命令

### Q4: 手机无法安装APK？
**A**: 
- 检查是否允许"未知来源"
- 确保Android版本 ≥ 8.0
- 尝试使用adb安装

### Q5: 应用闪退？
**A**: 
- 检查Android版本
- 查看logcat日志
- 在GitHub上创建Issue报告问题

---

## 🔄 后续更新

如果你修改了代码：

```bash
cd /home/guo/滤波标定/FilterCalibrationApp
git add .
git commit -m "更新说明"
git push
```

推送后GitHub会自动重新构建，生成新的APK。

---

## 📊 构建状态实时查看

当前构建状态：
https://github.com/Guo-JF/FilterCalibrationApp/actions

预计完成时间：**推送后5-10分钟**

---

**现在去访问Actions页面，等待构建完成吧！** 🚀
