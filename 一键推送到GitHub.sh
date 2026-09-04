#!/bin/bash

echo "=========================================="
echo "  GitHub 代码推送脚本"
echo "=========================================="
echo ""

# 检查是否在正确的目录
if [ ! -f "settings.gradle.kts" ]; then
    echo "❌ 错误：请在项目根目录运行此脚本"
    exit 1
fi

# 检查git是否初始化
if [ ! -d ".git" ]; then
    echo "1. 初始化 Git 仓库..."
    git init
    echo "   ✓ 完成"
else
    echo "1. Git 仓库已存在"
fi

echo ""
echo "2. 添加所有文件..."
git add .
echo "   ✓ 完成"

echo ""
echo "3. 提交更改..."
read -p "   请输入提交信息 (默认: Initial commit): " commit_msg
commit_msg=${commit_msg:-"Initial commit: Filter Calibration Learning App"}
git commit -m "$commit_msg"

echo ""
echo "4. 设置远程仓库..."
read -p "   请输入你的GitHub用户名: " username

if [ -z "$username" ]; then
    echo "   ❌ 用户名不能为空"
    exit 1
fi

# 检查是否已有remote
if git remote | grep -q "origin"; then
    echo "   远程仓库已存在，跳过添加"
else
    git remote add origin https://github.com/$username/FilterCalibrationApp.git
    echo "   ✓ 添加远程仓库: https://github.com/$username/FilterCalibrationApp.git"
fi

echo ""
echo "5. 推送到GitHub..."
echo "   (首次推送需要输入GitHub用户名和Token)"
git branch -M main
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✅ 推送成功！"
    echo "=========================================="
    echo ""
    echo "📋 下一步："
    echo "   1. 访问: https://github.com/$username/FilterCalibrationApp"
    echo "   2. 点击 'Actions' 标签"
    echo "   3. 等待构建完成（5-10分钟）"
    echo "   4. 下载生成的APK"
    echo ""
else
    echo ""
    echo "=========================================="
    echo "  ❌ 推送失败"
    echo "=========================================="
    echo ""
    echo "💡 可能的原因："
    echo "   1. GitHub仓库不存在"
    echo "      → 先访问 https://github.com/new 创建仓库"
    echo ""
    echo "   2. 认证失败"
    echo "      → 确保使用Token而不是密码"
    echo "      → 获取Token: https://github.com/settings/tokens"
    echo ""
    echo "   3. 网络问题"
    echo "      → 检查网络连接"
    echo ""
fi
