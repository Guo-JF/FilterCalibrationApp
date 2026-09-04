# 获取GitHub Personal Access Token 详细步骤

## 为什么需要Token？

从2021年8月13日起，GitHub不再接受密码认证，必须使用Personal Access Token。

## 获取步骤

### 1. 访问Token设置页面

打开浏览器，访问：https://github.com/settings/tokens

或者手动导航：
- 点击右上角头像
- Settings
- 左侧菜单最下方：Developer settings
- Personal access tokens → Tokens (classic)

### 2. 生成新Token

点击 **"Generate new token"** → **"Generate new token (classic)"**

### 3. 填写Token信息

- **Note** (备注): `FilterCalibrationApp` (随便填，自己记得就行)
- **Expiration** (过期时间): 
  - 选择 `90 days`（90天）
  - 或 `No expiration`（永不过期，但不太安全）

### 4. 选择权限

**只需要勾选一个权限组**：
- ✅ **repo** (勾选这个，会自动勾选所有子项)
  - repo:status
  - repo_deployment
  - public_repo
  - repo:invite
  - security_events

其他权限不需要勾选。

### 5. 生成Token

滚动到页面底部，点击绿色按钮 **"Generate token"**

### 6. 复制Token

⚠️ **重要**：生成后的页面会显示Token（一串字母数字），例如：
```
ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

**必须立即复制保存**：
- 这个Token只显示一次
- 离开页面后无法再查看
- 如果忘记保存，只能删除重新生成

### 7. 保存Token

将Token保存到安全的地方：
- 密码管理器
- 本地加密文件
- 记事本（临时）

## 使用Token

推送代码时：
```bash
git push
```

系统会提示：
```
Username for 'https://github.com': Guo-JF
Password for 'https://Guo-JF@github.com': 
```

这里：
- **Username**: 输入你的GitHub用户名 `Guo-JF`
- **Password**: 粘贴刚才复制的Token（不是你的GitHub密码！）

## 验证Token是否有效

获取Token后，可以测试：

```bash
# 方式1：通过命令行测试
curl -H "Authorization: token ghp_你的token" https://api.github.com/user

# 方式2：直接推送测试（推荐）
cd /home/guo/滤波标定/FilterCalibrationApp
git push -u origin main
# 输入用户名和Token
```

## 常见问题

### Q1: 忘记保存Token怎么办？
A: 删除旧Token，重新生成一个新的

### Q2: Token过期了怎么办？
A: 生成新Token，推送时使用新Token即可

### Q3: 用户名输错了？
A: 
```bash
# 重新配置remote
git remote remove origin
git remote add origin https://github.com/Guo-JF/FilterCalibrationApp.git
git push -u origin main
```

### Q4: 是否可以保存Token避免每次输入？
A: 可以，使用credential helper：
```bash
# 永久保存（存储在~/.git-credentials）
git config --global credential.helper store

# 下次push输入一次Token后，就会自动保存
```

## 总结

**记住**：
- 密码 ❌ 不能用
- Token ✅ 必须用
- Token要安全保存

获取Token后，运行：
```bash
cd /home/guo/滤波标定/FilterCalibrationApp
./一键推送到GitHub.sh
```

输入Token代替密码即可！
