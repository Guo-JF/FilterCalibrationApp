package com.calibration.filter.math

import kotlin.math.sqrt

/**
 * 相机内参
 * @param fx 水平焦距（像素）
 * @param fy 垂直焦距（像素）
 * @param cx 主点X坐标（像素）
 * @param cy 主点Y坐标（像素）
 * @param k1 径向畸变系数1
 * @param k2 径向畸变系数2
 * @param p1 切向畸变系数1
 * @param p2 切向畸变系数2
 */
data class CameraIntrinsics(
    val fx: Double = 500.0,
    val fy: Double = 500.0,
    val cx: Double = 320.0,
    val cy: Double = 240.0,
    val k1: Double = 0.0,
    val k2: Double = 0.0,
    val p1: Double = 0.0,
    val p2: Double = 0.0
) {
    fun toArray(): DoubleArray = doubleArrayOf(fx, fy, cx, cy, k1, k2, p1, p2)

    companion object {
        fun fromArray(arr: DoubleArray): CameraIntrinsics {
            require(arr.size == 8) { "数组长度必须为8" }
            return CameraIntrinsics(
                arr[0], arr[1], arr[2], arr[3],
                arr[4], arr[5], arr[6], arr[7]
            )
        }
    }
}

/**
 * 3D点（标定板坐标系）
 */
data class Point3D(val x: Double, val y: Double, val z: Double) {
    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Double) = Point3D(x * scalar, y * scalar, z * scalar)

    fun norm(): Double = sqrt(x * x + y * y + z * z)
}

/**
 * 2D点（像素坐标）
 */
data class Point2D(val u: Double, val v: Double) {
    operator fun plus(other: Point2D) = Point2D(u + other.u, v + other.v)
    operator fun minus(other: Point2D) = Point2D(u - other.u, v - other.v)

    fun distanceTo(other: Point2D): Double {
        val du = u - other.u
        val dv = v - other.v
        return sqrt(du * du + dv * dv)
    }
}

/**
 * 位姿（旋转+平移）
 */
data class Pose(
    val rvec: DoubleArray, // 旋转向量 [rx, ry, rz]
    val tvec: DoubleArray  // 平移向量 [tx, ty, tz]
) {
    init {
        require(rvec.size == 3) { "旋转向量必须是3维" }
        require(tvec.size == 3) { "平移向量必须是3维" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Pose
        if (!rvec.contentEquals(other.rvec)) return false
        if (!tvec.contentEquals(other.tvec)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = rvec.contentHashCode()
        result = 31 * result + tvec.contentHashCode()
        return result
    }
}

/**
 * 观测数据（一帧标定板）
 */
data class Observation(
    val objectPoints: List<Point3D>,  // 3D点
    val imagePoints: List<Point2D>    // 2D像素点
) {
    init {
        require(objectPoints.size == imagePoints.size) { "3D点和2D点数量必须相同" }
    }

    val numPoints: Int get() = objectPoints.size
}

/**
 * 标定结果
 */
data class CalibrationResult(
    val intrinsics: CameraIntrinsics,
    val covariance: Array<DoubleArray>, // 8x8 协方差矩阵
    val rmsError: Double,
    val acceptedViews: Int,
    val converged: Boolean
) {
    /**
     * 获取参数的标准差
     */
    fun getStandardDeviations(): DoubleArray {
        return DoubleArray(8) { i ->
            sqrt(covariance[i][i].coerceAtLeast(0.0))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CalibrationResult
        if (intrinsics != other.intrinsics) return false
        if (!covariance.contentDeepEquals(other.covariance)) return false
        if (rmsError != other.rmsError) return false
        if (acceptedViews != other.acceptedViews) return false
        if (converged != other.converged) return false
        return true
    }

    override fun hashCode(): Int {
        var result = intrinsics.hashCode()
        result = 31 * result + covariance.contentDeepHashCode()
        result = 31 * result + rmsError.hashCode()
        result = 31 * result + acceptedViews
        result = 31 * result + converged.hashCode()
        return result
    }
}
