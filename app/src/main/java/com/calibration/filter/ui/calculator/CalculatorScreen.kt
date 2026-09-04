package com.calibration.filter.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.calibration.filter.math.*

@Composable
fun CalculatorScreen() {
    var selectedCalc by remember { mutableStateOf(CalculatorType.Projection) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = CalculatorType.values().indexOf(selectedCalc)) {
            CalculatorType.values().forEach { type ->
                Tab(
                    selected = selectedCalc == type,
                    onClick = { selectedCalc = type },
                    text = { Text(type.title) },
                    icon = { Icon(type.icon, contentDescription = null) }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedCalc) {
                CalculatorType.Projection -> ProjectionCalculator()
                CalculatorType.Jacobian -> JacobianCalculator()
                CalculatorType.RMS -> RMSCalculator()
            }
        }
    }
}

enum class CalculatorType(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Projection("投影计算", Icons.Default.CameraAlt),
    Jacobian("雅可比", Icons.Default.Functions),
    RMS("RMS误差", Icons.Default.Analytics)
}

@Composable
fun ProjectionCalculator() {
    var point3DX by remember { mutableStateOf("0.08") }
    var point3DY by remember { mutableStateOf("0.04") }
    var point3DZ by remember { mutableStateOf("0.0") }

    var rvec0 by remember { mutableStateOf("0.2") }
    var rvec1 by remember { mutableStateOf("-0.1") }
    var rvec2 by remember { mutableStateOf("0.05") }

    var tvec0 by remember { mutableStateOf("0.0") }
    var tvec1 by remember { mutableStateOf("0.0") }
    var tvec2 by remember { mutableStateOf("0.8") }

    var fx by remember { mutableStateOf("500.0") }
    var fy by remember { mutableStateOf("500.0") }
    var cx by remember { mutableStateOf("320.0") }
    var cy by remember { mutableStateOf("240.0") }
    var k1 by remember { mutableStateOf("-0.08") }
    var k2 by remember { mutableStateOf("0.01") }
    var p1 by remember { mutableStateOf("0.001") }
    var p2 by remember { mutableStateOf("-0.001") }

    var result by remember { mutableStateOf<ProjectionResult?>(null) }

    fun calculate() {
        try {
            val point = Point3D(
                point3DX.toDouble(),
                point3DY.toDouble(),
                point3DZ.toDouble()
            )
            val pose = Pose(
                rvec = doubleArrayOf(rvec0.toDouble(), rvec1.toDouble(), rvec2.toDouble()),
                tvec = doubleArrayOf(tvec0.toDouble(), tvec1.toDouble(), tvec2.toDouble())
            )
            val intrinsics = CameraIntrinsics(
                fx = fx.toDouble(),
                fy = fy.toDouble(),
                cx = cx.toDouble(),
                cy = cy.toDouble(),
                k1 = k1.toDouble(),
                k2 = k2.toDouble(),
                p1 = p1.toDouble(),
                p2 = p2.toDouble()
            )

            val projModel = ProjectionModel()
            result = projModel.project(point, pose, intrinsics)
        } catch (e: Exception) {
            result = ProjectionResult.Invalid("输入错误: ${e.message}")
        }
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
                        text = "投影计算器",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "输入3D点、位姿和内参，计算投影结果",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            InputSection(
                title = "3D点（标定板坐标系）",
                fields = listOf(
                    Triple("X (m)", point3DX) { point3DX = it },
                    Triple("Y (m)", point3DY) { point3DY = it },
                    Triple("Z (m)", point3DZ) { point3DZ = it }
                )
            )
        }

        item {
            InputSection(
                title = "旋转向量 (rvec)",
                fields = listOf(
                    Triple("rx (rad)", rvec0) { rvec0 = it },
                    Triple("ry (rad)", rvec1) { rvec1 = it },
                    Triple("rz (rad)", rvec2) { rvec2 = it }
                )
            )
        }

        item {
            InputSection(
                title = "平移向量 (tvec)",
                fields = listOf(
                    Triple("tx (m)", tvec0) { tvec0 = it },
                    Triple("ty (m)", tvec1) { tvec1 = it },
                    Triple("tz (m)", tvec2) { tvec2 = it }
                )
            )
        }

        item {
            InputSection(
                title = "相机内参",
                fields = listOf(
                    Triple("fx (px)", fx) { fx = it },
                    Triple("fy (px)", fy) { fy = it },
                    Triple("cx (px)", cx) { cx = it },
                    Triple("cy (px)", cy) { cy = it },
                    Triple("k1", k1) { k1 = it },
                    Triple("k2", k2) { k2 = it },
                    Triple("p1", p1) { p1 = it },
                    Triple("p2", p2) { p2 = it }
                )
            )
        }

        item {
            Button(
                onClick = { calculate() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("计算")
            }
        }

        result?.let { res ->
            item {
                when (res) {
                    is ProjectionResult.Success -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "✓ 计算结果",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                ResultRow(
                                    "相机坐标 P^C",
                                    String.format(
                                        "[%.4f, %.4f, %.4f]",
                                        res.camera.x, res.camera.y, res.camera.z
                                    )
                                )

                                ResultRow(
                                    "归一化坐标 (x,y)",
                                    String.format(
                                        "[%.6f, %.6f]",
                                        res.normalized.first, res.normalized.second
                                    )
                                )

                                ResultRow(
                                    "畸变后坐标 (xd,yd)",
                                    String.format(
                                        "[%.6f, %.6f]",
                                        res.distorted.first, res.distorted.second
                                    )
                                )

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                ResultRow(
                                    "像素坐标 (u,v)",
                                    String.format("[%.2f, %.2f]", res.pixel.u, res.pixel.v),
                                    highlight = true
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
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "✗ 计算失败",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = res.reason,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputSection(
    title: String,
    fields: List<Triple<String, String, (String) -> Unit>>
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            fields.forEach { (label, value, onValueChange) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(label) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (highlight) MaterialTheme.typography.titleSmall
                   else MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            text = value,
            style = if (highlight) MaterialTheme.typography.titleSmall
                   else MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
fun JacobianCalculator() {
    var result by remember { mutableStateOf<String>("") }

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
                imageVector = Icons.Default.Functions,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "雅可比矩阵计算器",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "计算投影函数对内参的偏导数",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "此功能使用投影计算器的输入\n自动计算雅可比矩阵（2×8）",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RMSCalculator() {
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
                imageVector = Icons.Default.Analytics,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "RMS误差计算器",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "计算重投影均方根误差",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "RMS = √(Σ||detected - projected||² / N)\n\n" +
                        "此功能将在实战模块中实时显示",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
