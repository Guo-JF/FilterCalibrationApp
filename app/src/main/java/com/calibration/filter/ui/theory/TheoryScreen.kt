package com.calibration.filter.ui.theory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TheoryScreen() {
    var selectedChapter by remember { mutableStateOf<Chapter?>(null) }

    if (selectedChapter != null) {
        ChapterDetailScreen(
            chapter = selectedChapter!!,
            onBack = { selectedChapter = null }
        )
    } else {
        ChapterListScreen(onChapterClick = { selectedChapter = it })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListScreen(onChapterClick: (Chapter) -> Unit) {
    val chapters = listOf(
        Chapter(
            id = 1,
            title = "1. 算法概述",
            icon = Icons.Default.Home,
            description = "相机标定的目的、为什么用滤波器、算法全称",
            content = getChapter1Content()
        ),
        Chapter(
            id = 2,
            title = "2. 坐标系统",
            icon = Icons.Default.ThreeDRotation,
            description = "标定板、相机、像素三个坐标系及其转换",
            content = getChapter2Content()
        ),
        Chapter(
            id = 3,
            title = "3. 标定板角点",
            icon = Icons.Default.GridOn,
            description = "棋盘格生成、3D坐标、2D检测",
            content = getChapter3Content()
        ),
        Chapter(
            id = 4,
            title = "4. 投影模型",
            icon = Icons.Default.CameraAlt,
            description = "针孔相机、透视投影、完整推导",
            content = getChapter4Content()
        ),
        Chapter(
            id = 5,
            title = "5. 畸变模型",
            icon = Icons.Default.Lens,
            description = "径向畸变、切向畸变、完整公式",
            content = getChapter5Content()
        ),
        Chapter(
            id = 6,
            title = "6. 雅可比矩阵",
            icon = Icons.Default.Functions,
            description = "偏导数推导、观测矩阵含义",
            content = getChapter6Content()
        ),
        Chapter(
            id = 7,
            title = "7. PnP求解",
            icon = Icons.Default.Architecture,
            description = "位姿估计、solvePnP算法",
            content = getChapter7Content()
        ),
        Chapter(
            id = 8,
            title = "8. 零空间消元",
            icon = Icons.Default.CleaningServices,
            description = "SVD分解、消除位姿变量",
            content = getChapter8Content()
        ),
        Chapter(
            id = 9,
            title = "9. 信息滤波器",
            icon = Icons.Default.Analytics,
            description = "信息矩阵、增量更新、迭代优化",
            content = getChapter9Content()
        ),
        Chapter(
            id = 10,
            title = "10. 收敛判定",
            icon = Icons.Default.CheckCircle,
            description = "RMS误差、标准差、信息增益",
            content = getChapter10Content()
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "相机标定滤波算法",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "从零开始的完整数学推导",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Chapter List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters) { chapter ->
                ChapterCard(chapter = chapter, onClick = { onChapterClick(chapter) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterCard(chapter: Chapter, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = chapter.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chapter.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "查看详情",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterDetailScreen(chapter: Chapter, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chapter.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(chapter.content) { section ->
                when (section) {
                    is ContentSection.Text -> TextSection(section)
                    is ContentSection.Formula -> FormulaSection(section)
                    is ContentSection.Example -> ExampleSection(section)
                    is ContentSection.Note -> NoteSection(section)
                    is ContentSection.Important -> ImportantSection(section)
                }
            }
        }
    }
}

@Composable
fun TextSection(section: ContentSection.Text) {
    Text(
        text = section.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun FormulaSection(section: ContentSection.Formula) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (section.title.isNotEmpty()) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // 这里应该渲染LaTeX公式，简化版本使用文本
            Text(
                text = section.latex,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth()
            )
            if (section.explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = section.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun ExampleSection(section: ContentSection.Example) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📊 ${section.title}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
fun NoteSection(section: ContentSection.Note) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "💡 ${section.title}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ImportantSection(section: ContentSection.Important) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⚠️ ${section.title}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

data class Chapter(
    val id: Int,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val content: List<ContentSection>
)

sealed class ContentSection {
    data class Text(val content: String) : ContentSection()
    data class Formula(
        val title: String = "",
        val latex: String,
        val explanation: String = ""
    ) : ContentSection()
    data class Example(val title: String, val content: String) : ContentSection()
    data class Note(val title: String, val content: String) : ContentSection()
    data class Important(val title: String, val content: String) : ContentSection()
}
