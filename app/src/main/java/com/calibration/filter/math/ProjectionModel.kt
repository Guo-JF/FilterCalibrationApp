package com.calibration.filter.math

import kotlin.math.sqrt

/**
 * 投影模型计算器
 * 实现完整的针孔相机投影模型
 */
class ProjectionModel {

    /**
     * 将3D点投影到2D像素坐标
     *
     * 步骤：
     * 1. 刚体变换：标定板坐标系 -> 相机坐标系
     * 2. 透视投影：相机坐标系 -> 归一化平面
     * 3. 畸变：应用径向和切向畸变
     * 4. 像素变换：归一化坐标 -> 像素坐标
     *
     * @param point3D 3D点（标定板坐标系）
     * @param pose 位姿（旋转+平移）
     * @param intrinsics 相机内参
     * @return 投影后的2D像素坐标
     */
    fun project(
        point3D: Point3D,
        pose: Pose,
        intrinsics: CameraIntrinsics
    ): ProjectionResult {
        // 步骤1: 刚体变换（标定板 -> 相机）
        val R = rodrigues(pose.rvec)
        val pointCamera = transformPoint(point3D, R, pose.tvec)

        if (pointCamera.z <= 0.0) {
            return ProjectionResult.Invalid("点在相机后方")
        }

        // 步骤2: 透视投影
        val x = pointCamera.x / pointCamera.z
        val y = pointCamera.y / pointCamera.z

        // 步骤3: 畸变
        val (xd, yd) = applyDistortion(x, y, intrinsics)

        // 步骤4: 像素变换
        val u = intrinsics.fx * xd + intrinsics.cx
        val v = intrinsics.fy * yd + intrinsics.cy

        return ProjectionResult.Success(
            pixel = Point2D(u, v),
            normalized = Pair(x, y),
            distorted = Pair(xd, yd),
            camera = pointCamera
        )
    }

    /**
     * 应用畸变模型
     */
    private fun applyDistortion(
        x: Double,
        y: Double,
        intrinsics: CameraIntrinsics
    ): Pair<Double, Double> {
        val x2 = x * x
        val y2 = y * y
        val xy = x * y
        val r2 = x2 + y2
        val r4 = r2 * r2

        // 径向畸变
        val radial = 1.0 + intrinsics.k1 * r2 + intrinsics.k2 * r4

        // 切向畸变
        val tangentialX = 2.0 * intrinsics.p1 * xy + intrinsics.p2 * (r2 + 2.0 * x2)
        val tangentialY = intrinsics.p1 * (r2 + 2.0 * y2) + 2.0 * intrinsics.p2 * xy

        // 完整畸变
        val xd = x * radial + tangentialX
        val yd = y * radial + tangentialY

        return Pair(xd, yd)
    }

    /**
     * 罗德里格斯公式：旋转向量 -> 旋转矩阵
     *
     * R = I + sin(θ)[k]× + (1-cos(θ))[k]×²
     * 其中 θ = ||rvec||, k = rvec/θ
     */
    private fun rodrigues(rvec: DoubleArray): Array<DoubleArray> {
        val theta = sqrt(rvec[0] * rvec[0] + rvec[1] * rvec[1] + rvec[2] * rvec[2])

        if (theta < 1e-10) {
            // 小角度近似：R ≈ I
            return Array(3) { i ->
                DoubleArray(3) { j -> if (i == j) 1.0 else 0.0 }
            }
        }

        val k = rvec.map { it / theta }.toDoubleArray()
        val kx = arrayOf(
            doubleArrayOf(0.0, -k[2], k[1]),
            doubleArrayOf(k[2], 0.0, -k[0]),
            doubleArrayOf(-k[1], k[0], 0.0)
        )

        val sinTheta = kotlin.math.sin(theta)
        val cosTheta = kotlin.math.cos(theta)
        val oneMinusCos = 1.0 - cosTheta

        val R = Array(3) { DoubleArray(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                R[i][j] = (if (i == j) 1.0 else 0.0) + sinTheta * kx[i][j]
                for (k in 0..2) {
                    R[i][j] += oneMinusCos * kx[i][k] * kx[k][j]
                }
            }
        }

        return R
    }

    /**
     * 刚体变换：P_camera = R * P_board + t
     */
    private fun transformPoint(
        point: Point3D,
        R: Array<DoubleArray>,
        t: DoubleArray
    ): Point3D {
        val rotated = Point3D(
            R[0][0] * point.x + R[0][1] * point.y + R[0][2] * point.z,
            R[1][0] * point.x + R[1][1] * point.y + R[1][2] * point.z,
            R[2][0] * point.x + R[2][1] * point.y + R[2][2] * point.z
        )
        return Point3D(
            rotated.x + t[0],
            rotated.y + t[1],
            rotated.z + t[2]
        )
    }

    /**
     * 批量投影
     */
    fun projectBatch(
        points3D: List<Point3D>,
        pose: Pose,
        intrinsics: CameraIntrinsics
    ): List<ProjectionResult> {
        return points3D.map { project(it, pose, intrinsics) }
    }

    /**
     * 计算重投影误差（RMS）
     */
    fun computeRMS(
        objectPoints: List<Point3D>,
        imagePoints: List<Point2D>,
        pose: Pose,
        intrinsics: CameraIntrinsics
    ): Double {
        require(objectPoints.size == imagePoints.size)

        var sumSquaredError = 0.0
        var count = 0

        for (i in objectPoints.indices) {
            when (val result = project(objectPoints[i], pose, intrinsics)) {
                is ProjectionResult.Success -> {
                    val error = result.pixel.distanceTo(imagePoints[i])
                    sumSquaredError += error * error
                    count++
                }
                is ProjectionResult.Invalid -> continue
            }
        }

        return if (count > 0) {
            sqrt(sumSquaredError / count)
        } else {
            Double.POSITIVE_INFINITY
        }
    }
}

/**
 * 投影结果
 */
sealed class ProjectionResult {
    data class Success(
        val pixel: Point2D,           // 最终像素坐标
        val normalized: Pair<Double, Double>, // 归一化坐标 (x, y)
        val distorted: Pair<Double, Double>,  // 畸变后坐标 (xd, yd)
        val camera: Point3D           // 相机坐标系
    ) : ProjectionResult()

    data class Invalid(val reason: String) : ProjectionResult()
}
