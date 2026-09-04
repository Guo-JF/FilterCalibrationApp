#!/bin/bash

# 本地构建测试脚本
# 这个脚本会在本地尝试构建，找出问题

echo "=========================================="
echo "  本地构建测试"
echo "=========================================="
echo ""

cd /home/guo/滤波标定/FilterCalibrationApp

echo "1. 检查项目文件..."
echo "   gradlew: $(ls -lh gradlew 2>/dev/null | awk '{print $5}')"
echo "   gradle-wrapper.jar: $(ls -lh gradle/wrapper/gradle-wrapper.jar 2>/dev/null | awk '{print $5}')"

echo ""
echo "2. 尝试执行 gradlew..."
chmod +x gradlew

echo ""
echo "3. 列出可用任务..."
./gradlew tasks --console=plain 2>&1 | head -20

echo ""
echo "=========================================="
echo "如果看到错误，请复制完整的错误信息"
echo "=========================================="
