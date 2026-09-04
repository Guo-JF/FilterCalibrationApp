package com.calibration.filter.math

import kotlin.math.sqrt

/**
 * 雅可比矩阵计算器
 * 计算投影函数对内参的偏导数
 */
class JacobianCalculator {

    /**
     * 计算对内参的雅可比矩阵（简化版本）
     *
     * 对于单个点，雅可比是 2×8 矩阵：
     * J = [∂u/∂fx, ∂u/∂fy, ∂u/∂cx, ∂u/∂cy, ∂u/∂k1, ∂u/∂k2, ∂u/∂p1, ∂u/∂p2]
     *     [∂v/∂fx, ∂v/∂fy, ∂v/∂cx, ∂v/∂cy, ∂v/∂k1, ∂v/∂k2, ∂v/∂p1, ∂v/∂p2]
     */
    fun computeJacobian(
        point3D: Point3D,
        pose: Pose,
        intrinsics: CameraIntrinsics
    ): JacobianResult {
        val projModel = ProjectionModel()
        val projResult = projModel.project(point3D, pose, intrinsics)

        if (projResult !is ProjectionResult.Success) {
            return JacobianResult.Invalid("投影失败")
        }

        val (x, y) = projResult.normalized
        val (xd, yd) = projResult.distorted

        // 计算中间变量
        val x2 = x * x
        val y2 = y * y
        val xy = x * y
        val r2 = x2 + y2
        val r4 = r2 * r2

        // 雅可比矩阵 2×8
        val J = Array(2) { DoubleArray(8) }

        // 对 fx 的偏导
        J[0][0] = xd  // ∂u/∂fx = xd
        J[1][0] = 0.0 // ∂v/∂fx = 0

        // 对 fy 的偏导
        J[0][1] = 0.0 // ∂u/∂fy = 0
        J[1][1] = yd  // ∂v/∂fy = yd

        // 对 cx 的偏导
        J[0][2] = 1.0 // ∂u/∂cx = 1
        J[1][2] = 0.0 // ∂v/∂cx = 0

        // 对 cy 的偏导
        J[0][3] = 0.0 // ∂u/∂cy = 0
        J[1][3] = 1.0 // ∂v/∂cy = 1

        // 对 k1 的偏导（径向畸变）
        val dxd_dk1 = intrinsics.fx * x * r2
        val dyd_dk1 = intrinsics.fy * y * r2
        J[0][4] = dxd_dk1
        J[1][4] = dyd_dk1

        // 对 k2 的偏导（高阶径向畸变）
        val dxd_dk2 = intrinsics.fx * x * r4
        val dyd_dk2 = intrinsics.fy * y * r4
        J[0][5] = dxd_dk2
        J[1][5] = dyd_dk2

        // 对 p1 的偏导（切向畸变）
        val dxd_dp1 = intrinsics.fx * 2.0 * xy
        val dyd_dp1 = intrinsics.fy * (r2 + 2.0 * y2)
        J[0][6] = dxd_dp1
        J[1][6] = dyd_dp1

        // 对 p2 的偏导（切向畸变）
        val dxd_dp2 = intrinsics.fx * (r2 + 2.0 * x2)
        val dyd_dp2 = intrinsics.fy * 2.0 * xy
        J[0][7] = dxd_dp2
        J[1][7] = dyd_dp2

        return JacobianResult.Success(J)
    }

    /**
     * 批量计算雅可比矩阵
     * 对于 N 个点，返回 2N×8 的矩阵
     */
    fun computeJacobianBatch(
        points3D: List<Point3D>,
        pose: Pose,
        intrinsics: CameraIntrinsics
    ): Array<DoubleArray>? {
        val numPoints = points3D.size
        val J = Array(2 * numPoints) { DoubleArray(8) }

        for (i in points3D.indices) {
            when (val result = computeJacobian(points3D[i], pose, intrinsics)) {
                is JacobianResult.Success -> {
                    J[2 * i] = result.jacobian[0]
                    J[2 * i + 1] = result.jacobian[1]
                }
                is JacobianResult.Invalid -> return null
            }
        }

        return J
    }
}

/**
 * 雅可比计算结果
 */
sealed class JacobianResult {
    data class Success(val jacobian: Array<DoubleArray>) : JacobianResult() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Success
            if (!jacobian.contentDeepEquals(other.jacobian)) return false
            return true
        }

        override fun hashCode(): Int {
            return jacobian.contentDeepHashCode()
        }
    }
    data class Invalid(val reason: String) : JacobianResult()
}

/**
 * 数值微分计算雅可比（用于验证）
 */
fun numericalJacobian(
    point3D: Point3D,
    pose: Pose,
    intrinsics: CameraIntrinsics,
    epsilon: Double = 1e-6
): Array<DoubleArray> {
    val projModel = ProjectionModel()
    val baseResult = projModel.project(point3D, pose, intrinsics) as? ProjectionResult.Success
        ?: return Array(2) { DoubleArray(8) }

    val J = Array(2) { DoubleArray(8) }
    val params = intrinsics.toArray()

    for (i in 0..7) {
        val paramsPlus = params.clone()
        paramsPlus[i] += epsilon
        val intrinsicsPlus = CameraIntrinsics.fromArray(paramsPlus)

        val resultPlus = projModel.project(point3D, pose, intrinsicsPlus) as? ProjectionResult.Success
            ?: continue

        J[0][i] = (resultPlus.pixel.u - baseResult.pixel.u) / epsilon
        J[1][i] = (resultPlus.pixel.v - baseResult.pixel.v) / epsilon
    }

    return J
}
