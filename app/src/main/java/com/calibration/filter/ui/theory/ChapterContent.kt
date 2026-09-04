package com.calibration.filter.ui.theory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

/**
 * 第1章：算法概述
 */
fun getChapter1Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "想象你拿着一台相机拍照。光线从三维世界穿过镜头，打到传感器上形成二维图像。这个过程可以用数学方程描述。"
    ),
    ContentSection.Formula(
        title = "基本映射关系",
        latex = "三维世界的点 → [相机] → 二维图像的像素",
        explanation = "这就是相机标定要解决的核心问题"
    ),
    ContentSection.Text(
        "但是，这个映射关系受到相机内部参数的影响：\n\n" +
                "• 焦距 fx, fy：决定了物体在图像中的大小\n" +
                "• 主点 cx, cy：光轴与图像平面的交点位置\n" +
                "• 畸变系数 k1, k2, p1, p2：真实镜头的缺陷"
    ),
    ContentSection.Important(
        title = "相机标定的目标",
        content = "准确估计出这8个参数。有了它们，我们就能：\n" +
                "• 知道空间中的点会投影到图像的哪个位置\n" +
                "• 从图像像素反推对应的三维射线方向\n" +
                "• 进行三维重建、机器人导航、AR/VR等应用"
    ),
    ContentSection.Text(
        "为什么用滤波器？\n\n" +
                "传统的标定方法是批处理：拍摄很多张标定板照片，一次性计算所有参数。缺点是：\n" +
                "• 需要存储所有图像数据\n" +
                "• 计算量随图像数量平方增长\n" +
                "• 无法实时更新"
    ),
    ContentSection.Note(
        title = "信息滤波器的优势",
        content = "本算法使用信息滤波器，采用增量式方法：\n" +
                "• 每来一帧新图像，就更新一次参数估计\n" +
                "• 只需存储当前的参数均值和协方差（8×8矩阵）\n" +
                "• 可以实时运行，边拍边标定\n" +
                "• 随着观测增多，参数估计越来越准确"
    ),
    ContentSection.Formula(
        title = "核心思想",
        latex = "状态：θ = [fx, fy, cx, cy, k1, k2, p1, p2]ᵀ\n" +
                "观测：每帧图像中检测到的标定板角点像素坐标\n" +
                "滤波器：融合多帧观测，逐步优化参数估计",
        explanation = "把标定问题看作状态估计问题"
    ),
    ContentSection.Text(
        "算法全称：迭代扩展信息滤波器（Iterated Extended Information Filter, IEIF）\n\n" +
                "• 信息滤波器：用信息矩阵（协方差的逆）表示不确定性\n" +
                "• 扩展：对非线性投影方程进行线性化\n" +
                "• 迭代：每帧内部多次迭代，改善线性化点"
    )
)

/**
 * 第2章：坐标系统
 */
fun getChapter2Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "标定过程中涉及三个坐标系的转换，理解它们之间的关系是掌握算法的关键。"
    ),
    ContentSection.Formula(
        title = "标定板坐标系（Board/Object）",
        latex = "P^B = [X^B, Y^B, Z^B]ᵀ",
        explanation = "原点在标定板左上角第一个内角点，Z=0（平面），单位：米"
    ),
    ContentSection.Formula(
        title = "相机坐标系（Camera）",
        latex = "P^C = [X^C, Y^C, Z^C]ᵀ",
        explanation = "原点在光心，Z轴向前（光轴），X向右，Y向下，单位：米"
    ),
    ContentSection.Formula(
        title = "像素坐标系（Image/Pixel）",
        latex = "[u, v]",
        explanation = "左上角是(0,0)，u向右增大，v向下增大，单位：像素"
    ),
    ContentSection.Note(
        title = "通俗理解",
        content = "• 标定板坐标系：就像在棋盘上放一把尺子，我们知道每个格子的实际大小（如2.5cm）\n" +
                "• 相机坐标系：以相机镜头为中心看世界，前方是+Z，右边是+X，下方是+Y\n" +
                "• 像素坐标系：就是你在电脑屏幕上看到的图像，左上角(0,0)，往右往下坐标增大"
    ),
    ContentSection.Formula(
        title = "刚体变换：标定板 → 相机",
        latex = "P^C = R·P^B + t",
        explanation = "R是3×3旋转矩阵，t是平移向量。描述标定板相对于相机的位姿。"
    ),
    ContentSection.Important(
        title = "旋转向量",
        content = "在OpenCV和本代码中，旋转矩阵R用旋转向量（轴角表示）rvec = [rx, ry, rz]来存储，它只有3个数而不是9个。\n\n" +
                "通过罗德里格斯公式可以在旋转向量和旋转矩阵之间转换。\n\n" +
                "旋转向量的方向是旋转轴，长度是旋转角度（弧度）。"
    ),
    ContentSection.Example(
        title = "坐标转换示例",
        content = "假设标定板上某个角点在标定板坐标系下是 [0.08, 0.04, 0]（第1行第2列，间距4cm），\n\n" +
                "经过旋转平移后，在相机坐标系下可能变成 [0.05, -0.02, 0.8]（在相机前方80cm处）。"
    )
)

/**
 * 第3章：标定板角点
 */
fun getChapter3Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "棋盘格标定板是打印在平面上的黑白方格图案，像国际象棋的棋盘。我们用它的原因：\n\n" +
                "✓ 制作简单：用普通打印机打印即可\n" +
                "✓ 检测可靠：黑白对比强烈，角点容易识别\n" +
                "✓ 精度高：可以做亚像素优化\n" +
                "✓ 位置已知：每个角点的3D坐标已知（基于格子尺寸）"
    ),
    ContentSection.Important(
        title = "方格数 vs 内角点数",
        content = "假设打印一个 12×9 的棋盘格：\n" +
                "• 12×9 个方格：横向12个格子，纵向9个格子\n" +
                "• 11×8 个内角点：黑白交界处的十字点，总共 11×8 = 88 个\n\n" +
                "代码中的 rows 和 cols 指的是内角点数，不是方格数！"
    ),
    ContentSection.Text(
        "3D坐标的生成：\n\n" +
                "假设标定板有 6 行 × 8 列内角点，相邻角点间距 4cm（0.04m）。我们定义标定板坐标系：\n" +
                "• 原点：左上角第一个内角点\n" +
                "• X轴：沿着第一行向右\n" +
                "• Y轴：沿着第一列向下\n" +
                "• Z轴：垂直于标定板向外"
    ),
    ContentSection.Formula(
        title = "角点3D坐标",
        latex = "第0行第0列：[0.00, 0.00, 0.0]\n" +
                "第0行第1列：[0.04, 0.00, 0.0]\n" +
                "第0行第2列：[0.08, 0.00, 0.0]\n" +
                "第1行第0列：[0.00, 0.04, 0.0]\n" +
                "第1行第1列：[0.04, 0.04, 0.0]",
        explanation = "X = 列索引 × 间距，Y = 行索引 × 间距，Z = 0"
    ),
    ContentSection.Note(
        title = "关键理解",
        content = "• 这些3D坐标是已知的（我们用尺子量出来的）\n" +
                "• 所有点的Z坐标都是0（标定板是平面）\n" +
                "• 单位必须一致（代码用米）\n" +
                "• 顺序很重要：必须和检测到的2D点一一对应"
    ),
    ContentSection.Text(
        "2D像素坐标的检测：\n\n" +
                "当相机拍到标定板后，OpenCV的函数会自动检测角点：\n" +
                "1. findChessboardCorners：检测角点，精度约 ±0.5 像素\n" +
                "2. cornerSubPix：亚像素优化，可以达到 ±0.05 像素的精度"
    ),
    ContentSection.Important(
        title = "亚像素精化的重要性",
        content = "这对最终的标定精度至关重要——差10倍！\n\n" +
                "没有亚像素优化，标定误差可能是0.5像素；\n" +
                "使用亚像素优化后，可以达到0.05像素的精度。"
    )
)

/**
 * 第4章：投影模型
 */
fun getChapter4Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "这一章是核心！我们要推导：三维点如何变成二维像素。"
    ),
    ContentSection.Formula(
        title = "投影流程（四步）",
        latex = "P^B → [刚体变换] → P^C → [透视投影] → (x,y) → [畸变] → (xd,yd) → [像素变换] → (u,v)",
        explanation = "从3D世界坐标到2D像素坐标的完整过程"
    ),
    ContentSection.Text(
        "第一步：刚体变换（标定板→相机）\n\n" +
                "标定板在空间中的位置和姿态由 R 和 t 描述。"
    ),
    ContentSection.Formula(
        title = "刚体变换公式",
        latex = "[X^C, Y^C, Z^C]ᵀ = R·[X^B, Y^B, Z^B]ᵀ + [tx, ty, tz]ᵀ",
        explanation = "每次拍摄标定板，它的位置都不一样，所以R和t每帧都不同"
    ),
    ContentSection.Text(
        "第二步：透视投影（相机→归一化平面）\n\n" +
                "针孔相机的核心是透视投影。"
    ),
    ContentSection.Formula(
        title = "透视投影公式",
        latex = "x = X^C / Z^C\ny = Y^C / Z^C",
        explanation = "为什么要除以Z？这是透视效应：同样大小的物体，离得远看起来小，离得近看起来大"
    ),
    ContentSection.Example(
        title = "透视投影示例",
        content = "假设相机坐标系下有两个点：\n" +
                "• 点A：[0.1, 0.05, 1.0]（距离相机1米）\n" +
                "• 点B：[0.2, 0.10, 2.0]（距离相机2米）\n\n" +
                "透视投影后：\n" +
                "• 点A：x = 0.1/1.0 = 0.1, y = 0.05/1.0 = 0.05\n" +
                "• 点B：x = 0.2/2.0 = 0.1, y = 0.10/2.0 = 0.05\n\n" +
                "两个点投影到同一位置！这就是透视：它们在相机看来处于同一条射线上。"
    ),
    ContentSection.Note(
        title = "归一化平面",
        content = "(x, y) 叫归一化平面坐标。想象在相机前方 Z=1 的位置放一个虚拟平面，所有的3D点都投影到这个平面上。这个平面的单位是"焦距单位"（后面会乘以焦距转成像素）。"
    ),
    ContentSection.Text(
        "现在的 (x, y) 还不是像素，它是理想针孔模型下的归一化坐标。接下来要加上真实镜头的畸变（下一章），然后才转换到像素。"
    )
)

/**
 * 第5章：畸变模型
 */
fun getChapter5Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "理想针孔相机假设光线沿直线传播，但真实镜头有缺陷。主要有两种畸变：径向畸变和切向畸变。"
    ),
    ContentSection.Formula(
        title = "径向畸变",
        latex = "radial = 1 + k1·r² + k2·r⁴\n其中 r² = x² + y²",
        explanation = "图像中心附近的点基本准确，但越靠近边缘，畸变越严重"
    ),
    ContentSection.Text(
        "径向畸变系数的意义：\n" +
                "• k1 > 0：桶形畸变（边缘向外鼓）\n" +
                "• k1 < 0：枕形畸变（边缘向内凹）\n" +
                "• k2：高阶修正，边缘处影响更大"
    ),
    ContentSection.Example(
        title = "径向畸变示例",
        content = "假设 k1 = -0.08, k2 = 0.015，对于不同径向距离：\n\n" +
                "• r = 0（中心）：radial = 1.000（无畸变）\n" +
                "• r = 0.5：radial ≈ 0.981\n" +
                "• r = 1.0（边缘）：radial = 0.935\n\n" +
                "边缘处的点被向内"拉"了约6.5%，这就是枕形畸变。"
    ),
    ContentSection.Formula(
        title = "切向畸变",
        latex = "Δx_tan = 2·p1·xy + p2·(r² + 2x²)\n" +
                "Δy_tan = p1·(r² + 2y²) + 2·p2·xy",
        explanation = "当镜头与传感器不完全平行时产生，表现为图像被"剪切"（shear）"
    ),
    ContentSection.Note(
        title = "畸变大小比较",
        content = "实际标定中，切向畸变通常比径向畸变小得多：\n|p1|, |p2| << |k1|, |k2|\n\n" +
                "因此切向畸变对结果的影响相对较小。"
    ),
    ContentSection.Formula(
        title = "完整畸变公式",
        latex = "xd = x·(1 + k1·r² + k2·r⁴) + 2·p1·xy + p2·(r² + 2x²)\n" +
                "yd = y·(1 + k1·r² + k2·r⁴) + p1·(r² + 2y²) + 2·p2·xy",
        explanation = "综合径向和切向畸变的完整公式"
    ),
    ContentSection.Text(
        "最后一步：从归一化坐标到像素坐标"
    ),
    ContentSection.Formula(
        title = "像素变换",
        latex = "u = fx·xd + cx\nv = fy·yd + cy",
        explanation = "fx, fy 是焦距（像素单位），cx, cy 是主点坐标"
    ),
    ContentSection.Text(
        "每个参数的含义：\n\n" +
                "• fx：水平焦距。fx 越大，图像横向拉伸越厉害，物体看起来越大\n" +
                "• fy：垂直焦距。理想情况下 fx ≈ fy，但像素不是完美正方形时会有差异\n" +
                "• cx：主点X坐标。光轴与图像平面交点的横坐标，理论上应该在图像中心\n" +
                "• cy：主点Y坐标。光轴与图像平面交点的纵坐标"
    ),
    ContentSection.Example(
        title = "完整投影示例",
        content = "假设：\n" +
                "• 内参：fx=500, fy=500, cx=320, cy=240\n" +
                "• 畸变：k1=-0.08, k2=0.01, p1=0.001, p2=-0.001\n" +
                "• 相机坐标：P^C = [0.1, 0.05, 1.0]\n\n" +
                "计算过程：\n" +
                "1. 透视投影：x = 0.1, y = 0.05\n" +
                "2. r² = 0.0125\n" +
                "3. radial ≈ 0.999\n" +
                "4. 畸变后：xd ≈ 0.0999, yd ≈ 0.0500\n" +
                "5. 像素坐标：u ≈ 370.0, v = 265.0\n\n" +
                "最终：这个3D点投影到像素 (370, 265)。"
    )
)

/**
 * 第6章：雅可比矩阵
 */
fun getChapter6Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "观测方程 z = h(θ) 是非线性的。为了用滤波器估计 θ，我们需要线性化它。这就需要计算雅可比矩阵。"
    ),
    ContentSection.Formula(
        title = "什么是雅可比矩阵",
        latex = "J_ij = ∂f_i / ∂x_j",
        explanation = "雅可比矩阵就是多元函数的导数。它告诉我们：当输入 xj 变化一点点时，输出 fi 会变化多少。"
    ),
    ContentSection.Text(
        "我们需要计算两个雅可比矩阵：\n\n" +
                "1. H_θ：对内参的雅可比（维度：2×8）- 这是我们真正关心的！\n" +
                "2. H_pose：对位姿的雅可比（维度：2×6）- 这个会被消除掉"
    ),
    ContentSection.Formula(
        title = "对内参的雅可比",
        latex = "H_θ = ∂[u,v]ᵀ / ∂[fx, fy, cx, cy, k1, k2, p1, p2]ᵀ",
        explanation = "2×8 矩阵，每个元素是像素坐标对某个内参的偏导数"
    ),
    ContentSection.Note(
        title = "OpenCV帮我们算了雅可比",
        content = "好消息：OpenCV已经实现了这些复杂的求导！\n\n" +
                "函数 cv::projectPoints 不仅返回投影后的像素坐标，还返回雅可比矩阵。"
    ),
    ContentSection.Text(
        "手工推导：对 fx 的偏导数\n\n" +
                "回顾投影方程的最后一步：u = fx·xd + cx\n\n" +
                "其中 xd 是畸变后的归一化坐标，它依赖于畸变系数但不依赖 fx。"
    ),
    ContentSection.Formula(
        title = "对fx的偏导",
        latex = "∂u/∂fx = xd\n∂v/∂fx = 0",
        explanation = "u 对 fx 的偏导数就是 xd；v 不依赖 fx，所以偏导数为0"
    ),
    ContentSection.Text(
        "类似地：\n" +
                "• ∂u/∂fy = 0，∂v/∂fy = yd\n" +
                "• ∂u/∂cx = 1，∂v/∂cx = 0\n" +
                "• ∂u/∂cy = 0，∂v/∂cy = 1\n\n" +
                "对畸变系数的偏导数更复杂（涉及链式法则）。"
    ),
    ContentSection.Important(
        title = "关键理解",
        content = "雅可比矩阵 H_θ 的第 i 行告诉我们：第 i 个像素坐标分量（u 或 v）对8个内参的敏感度。\n\n" +
                "这就是后面滤波器更新的基础！"
    ),
    ContentSection.Example(
        title = "雅可比矩阵示例",
        content = "假设某个点的畸变后归一化坐标是 xd=0.2, yd=0.1，那么：\n\n" +
                "H_θ 的第一行（对u的偏导）：\n" +
                "[0.2, 0, 1, 0, ..., ...]\n" +
                " ↑    ↑  ↑  ↑\n" +
                " fx   fy cx cy\n\n" +
                "含义：如果 fx 增加1，u 增加约0.2像素；如果 cx 增加1，u 增加1像素。"
    )
)

/**
 * 第7章：PnP求解
 */
fun getChapter7Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "PnP（Perspective-n-Point）问题：给定 N 个3D点及其对应的2D像素坐标，以及相机内参，求解相机相对于这些3D点的位姿（R 和 t）。"
    ),
    ContentSection.Formula(
        title = "PnP问题",
        latex = "已知：{P^B_i}, {[ui, vi]}, θ\n求：R, t",
        explanation = "从2D-3D对应关系估计相机位姿"
    ),
    ContentSection.Text(
        "对于标定板，我们知道：\n" +
                "• 3D点：棋盘格角点的坐标（生成的）\n" +
                "• 2D点：检测到的像素坐标\n" +
                "• 内参：当前估计值（初始时是粗略猜测，后面越来越准）\n\n" +
                "需要求的是：这一帧拍摄时，标定板相对相机的位置和姿态。"
    ),
    ContentSection.Note(
        title = "OpenCV的solvePnP",
        content = "OpenCV提供了多种PnP算法，代码使用的是 SOLVEPNP_ITERATIVE（迭代优化方法）。\n\n" +
                "工作原理：\n" +
                "1. 初始化：用DLT或EPnP算法得到初值\n" +
                "2. 迭代优化：最小化重投影误差\n" +
                "3. 收敛判定：当位姿变化小于阈值时停止"
    ),
    ContentSection.Formula(
        title = "重投影误差最小化",
        latex = "min(r,t) Σ ||[ui,vi] - h(P^B_i, r, t, θ)||²",
        explanation = "找到使得投影点与检测点最接近的位姿"
    ),
    ContentSection.Important(
        title = "为什么每次迭代都要重新求PnP",
        content = "在滤波器更新过程中，内参 θ 会迭代优化。每次 θ 变化后，需要重新求解位姿，使位姿和内参的线性化点保持一致。"
    ),
    ContentSection.Note(
        title = "关键理解",
        content = "PnP给出的位姿是临时变量，每帧都不同，不需要存储。我们真正关心的是内参 θ，它是跨帧不变的。"
    ),
    ContentSection.Example(
        title = "PnP求解示例",
        content = "假设检测到48个角点，当前内参估计为 fx=500, fy=500, cx=320, cy=240。\n\n" +
                "solvePnP 求解出：\n" +
                "• rvec = [0.2, -0.1, 0.05]（旋转向量）\n" +
                "• tvec = [0, 0, 0.8]（平移向量）\n\n" +
                "含义：标定板在相机前方80cm处，略微倾斜。"
    )
)

/**
 * 第8章：零空间消元
 */
fun getChapter8Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "这是算法中最精妙的部分！我们要解决一个问题：如何在不增加状态维度的情况下，消除每帧变化的位姿变量？"
    ),
    ContentSection.Formula(
        title = "完整线性化观测方程",
        latex = "r = r0 - H_pose·Δξ - H_θ·Δθ",
        explanation = "残差依赖于位姿增量（6维）和内参增量（8维）"
    ),
    ContentSection.Text(
        "矛盾：\n" +
                "• 我们只想估计8个内参（跨帧不变）\n" +
                "• 但每帧有6个额外的位姿变量\n" +
                "• 如果把位姿也加入状态向量，状态维度会越来越大！"
    ),
    ContentSection.Important(
        title = "核心思想",
        content = "找一个投影矩阵 N，使得：Nᵀ·H_pose = 0\n\n" +
                "然后对整个观测方程左乘 Nᵀ，中间项消失：\n" +
                "Nᵀ·r = Nᵀ·r0 - Nᵀ·H_θ·Δθ\n\n" +
                "现在方程中只有内参 Δθ，位姿 Δξ 被消除了！"
    ),
    ContentSection.Text(
        "如何计算零空间矩阵 N？\n\n" +
                "使用奇异值分解（SVD）："
    ),
    ContentSection.Formula(
        title = "SVD分解",
        latex = "H_pose = U·Σ·Vᵀ",
        explanation = "U 的后 2N-6 列对应零奇异值，它们张成 H_pose 的左零空间"
    ),
    ContentSection.Formula(
        title = "零空间矩阵",
        latex = "N = U_null = U的最后(2N-6)列",
        explanation = "对于48个角点，N 的维度是 96×90"
    ),
    ContentSection.Example(
        title = "数值例子",
        content = "假设有48个角点（6×8），则：\n\n" +
                "• H_pose 维度：96×6\n" +
                "• U 维度：96×96\n" +
                "• 前6列对应6个非零奇异值（位姿的可观测方向）\n" +
                "• 后90列对应零奇异值（零空间）\n" +
                "• N 维度：96×90\n" +
                "• 投影后的残差 Nᵀ·r 维度：90×1\n\n" +
                "损失了6个自由度（位姿），保留了90个独立约束（用于估计8个内参）。"
    ),
    ContentSection.Note(
        title = "为什么这样做",
        content = "优点：\n" +
                "✓ 状态维度固定：永远只有8个内参\n" +
                "✓ 计算效率：协方差矩阵永远是8×8\n" +
                "✓ 数值稳定：避免了高维度矩阵的病态问题\n\n" +
                "代价：\n" +
                "✗ 每帧需要SVD分解（但很快）"
    ),
    ContentSection.Important(
        title = "核心理解",
        content = "零空间投影是一种消元技巧：在线性代数层面消除我们不关心的变量，而不是在状态向量中携带它们。\n\n" +
                "这种思想也用于MSCKF等高级SLAM算法。"
    )
)

/**
 * 第9章：信息滤波器
 */
fun getChapter9Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "现在进入算法的心脏：如何用每帧观测增量式地更新内参估计。"
    ),
    ContentSection.Text(
        "标准的卡尔曼滤波器用协方差矩阵 P 表示不确定性。信息滤波器用信息矩阵 Λ = P⁻¹ 表示。"
    ),
    ContentSection.Formula(
        title = "信息滤波器更新公式",
        latex = "Λ_post = Λ_prior + Hᵀ·R⁻¹·H",
        explanation = "信息矩阵直接相加！不需要求逆大矩阵"
    ),
    ContentSection.Note(
        title = "为什么用信息形式",
        content = "✓ 更新公式更简单（直接相加）\n" +
                "✓ 物理直观（信息累积：观测越多，信息矩阵越"满"）\n" +
                "✓ 数值稳定（避免了协方差矩阵的近奇异问题）"
    ),
    ContentSection.Text(
        "从贝叶斯公式推导：\n\n" +
                "p(θ|z) ∝ p(z|θ)·p(θ)\n\n" +
                "假设高斯分布并线性化，求最大后验估计（MAP），整理得到信息形式的更新公式。"
    ),
    ContentSection.Formula(
        title = "完整更新公式",
        latex = "Λ_post = Λ_prior + (1/σ²)·hᵀ·h\n" +
                "Δθ = Λ_post⁻¹·((1/σ²)·hᵀ·r̃)\n" +
                "P_post = Λ_post⁻¹",
        explanation = "其中 h = Nᵀ·H_θ 是消元后的雅可比，r̃ = Nᵀ·r 是消元后的残差"
    ),
    ContentSection.Important(
        title = "迭代扩展信息滤波（IEIF）",
        content = "由于观测方程是非线性的，线性化点会影响结果。迭代的意思是：在一帧内多次重新线性化，直到收敛。\n\n" +
                "关键细节：每次迭代都从同一个先验出发，这样当前帧不会被重复计入。迭代只是改善线性化点，不是重复融合观测。"
    ),
    ContentSection.Example(
        title = "迭代过程示例",
        content = "第1次迭代：\n" +
                "• 用当前内参 θ̂ 求PnP → 得到位姿\n" +
                "• 计算雅可比和残差\n" +
                "• 信息滤波更新 → 得到 θ_new\n" +
                "• 检查 ||θ_new - θ̂|| < 1e-7？\n\n" +
                "第2次迭代：\n" +
                "• 用 θ_new 重新求PnP\n" +
                "• 重新计算雅可比和残差\n" +
                "• 再次更新\n" +
                "• 收敛！"
    ),
    ContentSection.Text(
        "为什么是"增量式"？\n\n" +
                "每次新来一帧，信息矩阵简单相加：\n" +
                "Λ_新 = Λ_旧 + H_这一帧ᵀ·R⁻¹·H_这一帧"
    ),
    ContentSection.Note(
        title = "信息累积",
        content = "这就像知识累积：\n" +
                "• 第1帧：Λ = Λ0 + H1ᵀ·R⁻¹·H1\n" +
                "• 第2帧：Λ = Λ_第1帧后 + H2ᵀ·R⁻¹·H2\n" +
                "• 第3帧：Λ = Λ_第2帧后 + H3ᵀ·R⁻¹·H3\n\n" +
                "信息矩阵越来越"满"，协方差矩阵越来越"小"，估计越来越准确！"
    )
)

/**
 * 第10章：收敛判定
 */
fun getChapter10Content(): List<ContentSection> = listOf(
    ContentSection.Text(
        "代码中有两个层次的收敛：\n\n" +
                "1. 单帧内迭代收敛：一帧图像内部，重复线性化直到参数不再变化\n" +
                "2. 全局标定收敛：整个标定过程达到目标精度"
    ),
    ContentSection.Formula(
        title = "单帧迭代收敛",
        latex = "||θ_new - θ_old|| < ε_iter",
        explanation = "默认 ε_iter = 1e-7，保证线性化点稳定"
    ),
    ContentSection.Formula(
        title = "全局标定收敛",
        latex = "accepted_views ≥ 20\n且\nmax(std(θ)) ≤ threshold",
        explanation = "接受的视图数量足够，且所有参数的标准差都小于阈值"
    ),
    ContentSection.Text(
        "默认阈值：\n" +
                "• min_accepted_views: 20（至少接受20帧）\n" +
                "• max_focal_std_px: 1.0（焦距标准差 ≤ 1像素）\n" +
                "• max_principal_std_px: 1.0（主点标准差 ≤ 1像素）\n" +
                "• max_distortion_std: 0.02（畸变系数标准差 ≤ 0.02）"
    ),
    ContentSection.Note(
        title = "边际标准差",
        content = "协方差矩阵 P 是8×8的，其对角线元素是边际方差：\n\n" +
                "P = diag(σ²_fx, σ²_fy, σ²_cx, σ²_cy, σ²_k1, σ²_k2, σ²_p1, σ²_p2)\n\n" +
                "边际标准差 σi = √Pii 表示单个参数的不确定性（忽略与其他参数的相关性）。"
    ),
    ContentSection.Example(
        title = "收敛过程示例",
        content = "帧数 → σ(fx) → σ(fy) → σ(cx) → σ(cy) → 收敛？\n\n" +
                "5帧 → 8.5 → 8.3 → 4.2 → 4.1 → ❌（帧数不足）\n" +
                "10帧 → 3.2 → 3.1 → 2.1 → 2.0 → ❌（精度不足）\n" +
                "20帧 → 1.5 → 1.4 → 1.3 → 1.2 → ❌（焦距仍超标）\n" +
                "28帧 → 0.8 → 0.9 → 0.7 → 0.8 → ✅（全部达标！）"
    ),
    ContentSection.Text(
        "信息增益门限：\n\n" +
                "除了最终收敛判定，每一帧还要通过信息增益门限。"
    ),
    ContentSection.Formula(
        title = "信息增益",
        latex = "InfoGain = 0.5·(log|P_prior| - log|P_post|)",
        explanation = "微分熵之差，表示这一帧带来了多少新信息"
    ),
    ContentSection.Text(
        "物理意义：\n" +
                "• InfoGain > 0：后验比先验"更确定"，这一帧有用\n" +
                "• InfoGain ≈ 0：几乎没有新信息，拒绝这一帧\n" +
                "• InfoGain < 0：理论上不应该发生"
    ),
    ContentSection.Note(
        title = "其他质量门限",
        content = "代码还有其他门限保护：\n\n" +
                "• RMS门限：max_reprojection_rms_px = 2.0\n" +
                "  RMS太大说明标定板检测错误或内参估计严重偏离\n\n" +
                "• 位姿秩检查：位姿雅可比秩必须是6（满秩），否则说明姿态退化\n\n" +
                "• 状态合理性：焦距/主点必须在合理范围内，畸变系数不能太大"
    ),
    ContentSection.Important(
        title = "RMS误差的含义",
        content = "RMS = 平均每个像素坐标分量的误差。\n\n" +
                "• RMS = 0.5 像素：非常好\n" +
                "• RMS = 1.0 像素：良好\n" +
                "• RMS = 2.0 像素：可接受（代码的默认门限）\n" +
                "• RMS > 3.0 像素：可能有问题"
    ),
    ContentSection.Text(
        "恭喜！你已经完成了全部理论学习。现在可以进入可视化和实战模块，通过动画和实际操作来加深理解。"
    )
)
