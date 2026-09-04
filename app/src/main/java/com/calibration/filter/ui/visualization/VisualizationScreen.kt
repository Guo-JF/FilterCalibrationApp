package com.calibration.filter.ui.visualization

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.calibration.filter.math.*
import kotlin.math.*

@Composable
fun VisualizationScreen() {
    var selectedVis by remember { mutableStateOf<VisualizationType>(VisualizationType.Projection) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 可视化类型选择
        ScrollableTabRow(
            selectedTabIndex = VisualizationType.values().indexOf(selectedVis),
            modifier = Modifier.fillMaxWidth()
        ) {
            VisualizationType.values().forEach { type ->
                Tab(
                    selected = selectedVis == type,
                    onClick = { selectedVis = type },
                    text = { Text(type.title) }
                )
            }
        }

        // 显示对应的可视化
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedVis) {
                VisualizationType.Projection -> ProjectionVisualization()
                VisualizationType.Distortion -> DistortionVisualization()
                VisualizationType.Convergence -> ConvergenceVisualization()
                VisualizationType.Jacobian -> JacobianVisualization()
            }
        }
    }
}

enum class VisualizationType(val title: String) {
    Projection("投影过程"),
    Distortion("畸变效果"),
    Convergence("滤波收敛"),
    Jacobian("雅可比矩阵")
}

/**
 * 投影过程可视化
 */
@Composable
fun ProjectionVisualization() {
    var point3D by remember { mutableStateOf(Point3D(0.08, 0.04, 0.0)) }
    var distance by remember { mutableStateOf(0.5f) }
    var tilt by remember { mutableStateOf(0.3f) }

    val intrinsics = remember { CameraIntrinsics() }
    val pose = remember(distance, tilt) {
        Pose(
            rvec = doubleArrayOf(tilt.toDouble(), 0.1, 0.0),
            tvec = doubleArrayOf(0.0, 0.0, distance.toDouble())
        )
    }

    val projModel = remember { ProjectionModel() }
    val result = remember(point3D, pose, intrinsics) {
        projModel.project(point3D, pose, intrinsics)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "投影过程演示",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "调节参数观察3D点如何投影到2D像素",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("标定板距离: ${String.format("%.2f", distance)}m")
                    Slider(
                        value = distance,
                        onValueChange = { distance = it },
                        valueRange = 0.3f..1.5f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("标定板倾角: ${String.format("%.2f", tilt)}rad")
                    Slider(
                        value = tilt,
                        onValueChange = { tilt = it },
                        valueRange = -0.5f..0.5f
                    )
                }
            }
        }

        item {
            // 3D可视化（简化版）
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3D视图（侧视图）",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val scale = 200f

                        // 绘制相机（三角形）
                        drawCircle(
                            color = Color.Blue,
                            radius = 10f,
                            center = Offset(centerX, centerY)
                        )
                        drawLine(
                            color = Color.Blue,
                            start = Offset(centerX, centerY),
                            end = Offset(centerX + 30, centerY - 20),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = Color.Blue,
                            start = Offset(centerX, centerY),
                            end = Offset(centerX + 30, centerY + 20),
                            strokeWidth = 2f
                        )

                        // 绘制Z轴
                        drawLine(
                            color = Color.Gray,
                            start = Offset(centerX, centerY),
                            end = Offset(centerX + distance * scale, centerY),
                            strokeWidth = 2f
                        )

                        // 绘制标定板（矩形）
                        val boardX = centerX + distance * scale
                        val boardSize = 60f
                        drawLine(
                            color = Color.Red,
                            start = Offset(
                                boardX - boardSize / 2 * cos(tilt),
                                centerY - boardSize / 2 - boardSize / 2 * sin(tilt)
                            ),
                            end = Offset(
                                boardX - boardSize / 2 * cos(tilt),
                                centerY + boardSize / 2 - boardSize / 2 * sin(tilt)
                            ),
                            strokeWidth = 3f
                        )
                        drawLine(
                            color = Color.Red,
                            start = Offset(
                                boardX + boardSize / 2 * cos(tilt),
                                centerY - boardSize / 2 + boardSize / 2 * sin(tilt)
                            ),
                            end = Offset(
                                boardX + boardSize / 2 * cos(tilt),
                                centerY + boardSize / 2 + boardSize / 2 * sin(tilt)
                            ),
                            strokeWidth = 3f
                        )

                        // 绘制3D点
                        val pointX = boardX + point3D.x * scale * 2
                        val pointY = centerY + point3D.y * scale * 2
                        drawCircle(
                            color = Color.Green,
                            radius = 8f,
                            center = Offset(pointX.toFloat(), pointY.toFloat())
                        )
                    }
                }
            }
        }

        item {
            // 投影结果
            when (result) {
                is ProjectionResult.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "投影结果",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            ProjectionStepDisplay(
                                "1. 相机坐标",
                                String.format(
                                    "[%.3f, %.3f, %.3f]",
                                    result.camera.x,
                                    result.camera.y,
                                    result.camera.z
                                )
                            )

                            ProjectionStepDisplay(
                                "2. 归一化坐标",
                                String.format(
                                    "[%.4f, %.4f]",
                                    result.normalized.first,
                                    result.normalized.second
                                )
                            )

                            ProjectionStepDisplay(
                                "3. 畸变后坐标",
                                String.format(
                                    "[%.4f, %.4f]",
                                    result.distorted.first,
                                    result.distorted.second
                                )
                            )

                            ProjectionStepDisplay(
                                "4. 像素坐标",
                                String.format(
                                    "[%.1f, %.1f]",
                                    result.pixel.u,
                                    result.pixel.v
                                )
                            )
                        }
                    }
                }
                is ProjectionResult.Invalid -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "投影失败: ${result.reason}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectionStepDisplay(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

/**
 * 畸变效果可视化
 */
@Composable
fun DistortionVisualization() {
    var k1 by remember { mutableStateOf(0.0f) }
    var k2 by remember { mutableStateOf(0.0f) }
    var p1 by remember { mutableStateOf(0.0f) }
    var p2 by remember { mutableStateOf(0.0f) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "畸变效果演示",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "调节畸变系数，观察网格的变形",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("径向畸变 k1: ${String.format("%.3f", k1)}")
                    Slider(
                        value = k1,
                        onValueChange = { k1 = it },
                        valueRange = -0.3f..0.2f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("径向畸变 k2: ${String.format("%.3f", k2)}")
                    Slider(
                        value = k2,
                        onValueChange = { k2 = it },
                        valueRange = -0.1f..0.1f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("切向畸变 p1: ${String.format("%.4f", p1)}")
                    Slider(
                        value = p1,
                        onValueChange = { p1 = it },
                        valueRange = -0.01f..0.01f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("切向畸变 p2: ${String.format("%.4f", p2)}")
                    Slider(
                        value = p2,
                        onValueChange = { p2 = it },
                        valueRange = -0.01f..0.01f
                    )
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "畸变网格",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val gridSize = 8
                        val spacing = size.width / (gridSize + 1)

                        // 绘制原始网格（灰色）
                        for (i in 0..gridSize) {
                            // 横线
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(0f, spacing * i),
                                end = Offset(size.width, spacing * i),
                                strokeWidth = 1f
                            )
                            // 竖线
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(spacing * i, 0f),
                                end = Offset(spacing * i, size.height),
                                strokeWidth = 1f
                            )
                        }

                        // 绘制畸变后的网格（红色）
                        val intrinsics = CameraIntrinsics(
                            fx = size.width / 2,
                            fy = size.width / 2,
                            cx = centerX.toDouble(),
                            cy = centerY.toDouble(),
                            k1 = k1.toDouble(),
                            k2 = k2.toDouble(),
                            p1 = p1.toDouble(),
                            p2 = p2.toDouble()
                        )

                        for (i in 0..gridSize) {
                            val points = mutableListOf<Offset>()
                            for (j in 0..gridSize) {
                                val x = (spacing * j - centerX) / intrinsics.fx
                                val y = (spacing * i - centerY) / intrinsics.fy

                                val (xd, yd) = applyDistortionHelper(x, y, intrinsics)

                                val u = intrinsics.fx * xd + intrinsics.cx
                                val v = intrinsics.fy * yd + intrinsics.cy

                                points.add(Offset(u.toFloat(), v.toFloat()))
                            }

                            // 绘制横线
                            for (j in 0 until points.size - 1) {
                                drawLine(
                                    color = Color.Red,
                                    start = points[j],
                                    end = points[j + 1],
                                    strokeWidth = 2f
                                )
                            }
                        }

                        for (j in 0..gridSize) {
                            val points = mutableListOf<Offset>()
                            for (i in 0..gridSize) {
                                val x = (spacing * j - centerX) / intrinsics.fx
                                val y = (spacing * i - centerY) / intrinsics.fy

                                val (xd, yd) = applyDistortionHelper(x, y, intrinsics)

                                val u = intrinsics.fx * xd + intrinsics.cx
                                val v = intrinsics.fy * yd + intrinsics.cy

                                points.add(Offset(u.toFloat(), v.toFloat()))
                            }

                            // 绘制竖线
                            for (i in 0 until points.size - 1) {
                                drawLine(
                                    color = Color.Red,
                                    start = points[i],
                                    end = points[i + 1],
                                    strokeWidth = 2f
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "灰色：原始网格  红色：畸变后网格",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💡 提示",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• k1 < 0：枕形畸变（边缘向内收）\n" +
                                "• k1 > 0：桶形畸变（边缘向外鼓）\n" +
                                "• k2：高阶修正，增强边缘效果\n" +
                                "• p1, p2：切向畸变，造成不对称变形",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

private fun applyDistortionHelper(
    x: Double,
    y: Double,
    intrinsics: CameraIntrinsics
): Pair<Double, Double> {
    val x2 = x * x
    val y2 = y * y
    val xy = x * y
    val r2 = x2 + y2
    val r4 = r2 * r2

    val radial = 1.0 + intrinsics.k1 * r2 + intrinsics.k2 * r4
    val tangentialX = 2.0 * intrinsics.p1 * xy + intrinsics.p2 * (r2 + 2.0 * x2)
    val tangentialY = intrinsics.p1 * (r2 + 2.0 * y2) + 2.0 * intrinsics.p2 * xy

    val xd = x * radial + tangentialX
    val yd = y * radial + tangentialY

    return Pair(xd, yd)
}

/**
 * 收敛过程可视化
 */
@Composable
fun ConvergenceVisualization() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Construction,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "收敛过程可视化",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "此功能将在实战模块中实时展示",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 雅可比矩阵可视化
 */
@Composable
fun JacobianVisualization() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "雅可比矩阵可视化",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "请前往计算器模块查看详细计算",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
