# GitHub Actions 自动构建设置完成指南

## ✅ 已完成的准备工作

1. ✅ Gradle Wrapper JAR 已下载（62KB）
2. ✅ GitHub Actions workflow 配置已创建
3. ✅ 项目结构完整

## 📋 下一步操作步骤

### 步骤1：初始化Git仓库

在终端执行：

```bash
cd /home/guo/滤波标定/FilterCalibrationApp

# 初始化git
git init

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit: Filter Calibration Learning App"
```

### 步骤2：创建GitHub仓库

1. **打开浏览器访问**：https://github.com/new

2. **填写仓库信息**：
   - Repository name: `FilterCalibrationApp`（或你喜欢的名字）
   - Description: `滤波标定算法学习应用 - Android`
   - 选择 **Public**（公开仓库，免费使用Actions）
   - ❌ **不要**勾选任何初始化选项（README、.gitignore等）

3. **点击 "Create repository"**

### 步骤3：推送代码到GitHub

GitHub会显示一些命令，或者直接执行：

```bash
cd /home/guo/滤波标定/FilterCalibrationApp

# 添加远程仓库（替换为你的GitHub用户名）
git remote add origin https://github.com/你的用户名/FilterCalibrationApp.git

# 推送代码
git branch -M main
git push -u origin main
```

**首次推送会要求输入GitHub账号密码**：
- Username: 你的GitHub用户名
- Password: 需要使用Personal Access Token（不是密码）

### 步骤4：获取GitHub Personal Access Token

如果还没有Token：

1. 访问：https://github.com/settings/tokens
2. 点击 "Generate new token" → "Generate new token (classic)"
3. 设置：
   - Note: `FilterCalibrationApp`
   - Expiration: `90 days`（或选择其他）
   - 勾选权限：`repo`（所有repo相关权限）
4. 点击 "Generate token"
5. **复制token**（只显示一次，请保存好）

使用token推送：
```bash
# Username: 你的GitHub用户名
# Password: 粘贴刚才复制的token
git push -u origin main
```

### 步骤5：等待自动构建

推送成功后：

1. **访问你的仓库**：`https://github.com/你的用户名/FilterCalibrationApp`

2. **点击 "Actions" 标签**

3. **查看构建进度**：
   - 会看到一个名为 "Android CI" 的workflow正在运行
   - 等待5-10分钟（首次构建需要下载依赖）
   - 构建过程完全在GitHub服务器上进行

4. **构建状态**：
   - 🟡 黄色点：正在构建
   - 🟢 绿色勾：构建成功
   - 🔴 红色叉：构建失败

### 步骤6：下载APK

构建成功后：

1. **点击刚完成的workflow运行**

2. **滚动到页面底部，找到 "Artifacts" 区域**

3. **点击下载 `app-debug`**（这是一个zip文件）

4. **解压后得到 `app-debug.apk`**

### 步骤7：安装APK到手机

1. **将APK传到手机**（USB、微信、网盘等）

2. **在手机上安装**：
   - 首次安装需要允许"安装未知来源应用"
   - 点击APK文件安装

3. **打开应用开始学习！**

---

## 🔧 可能遇到的问题

### 问题1：推送时要求密码
**解决**：使用Personal Access Token代替密码（见步骤4）

### 问题2：构建失败（红色×）
**解决**：
1. 点击失败的workflow查看日志
2. 通常是依赖下载问题，重新运行即可
3. 点击 "Re-run all jobs"

### 问题3：没有看到Actions标签
**解决**：确保仓库是Public（公开），Private仓库需要付费计划

### 问题4：构建时间过长
**解决**：首次构建需要下载所有依赖（约5-10分钟），后续构建会快很多（2-3分钟）

---

## 📱 使用配置好的SSH方式推送（可选）

如果你配置了SSH key，可以使用SSH方式：

```bash
git remote set-url origin git@github.com:你的用户名/FilterCalibrationApp.git
git push
```

---

## 🎯 快速命令备忘

```bash
# 进入项目目录
cd /home/guo/滤波标定/FilterCalibrationApp

# 初始化git
git init
git add .
git commit -m "Initial commit"

# 添加远程仓库（替换用户名）
git remote add origin https://github.com/你的用户名/FilterCalibrationApp.git

# 推送
git branch -M main
git push -u origin main

# 后续更新代码后推送
git add .
git commit -m "Update code"
git push
```

---

## 🎓 后续使用

每次修改代码后：

```bash
cd /home/guo/滤波标定/FilterCalibrationApp
git add .
git commit -m "修改说明"
git push
```

推送后GitHub会自动重新构建，生成新的APK。

---

## ✅ 验证清单

完成以下步骤即可：

- [ ] git init（步骤1）
- [ ] 创建GitHub仓库（步骤2）
- [ ] 推送代码（步骤3）
- [ ] 等待构建完成（步骤5）
- [ ] 下载APK（步骤6）
- [ ] 安装到手机（步骤7）

---

**现在你可以开始执行步骤1了！** 🚀

有任何问题随时问我！
