package com.calibration.filter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.calibration.filter.ui.calculator.CalculatorScreen
import com.calibration.filter.ui.practice.PracticeScreen
import com.calibration.filter.ui.theory.TheoryScreen
import com.calibration.filter.ui.theme.FilterCalibrationTheme
import com.calibration.filter.ui.visualization.VisualizationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilterCalibrationTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Theory : Screen("theory", "理论", Icons.Default.MenuBook)
    object Visualization : Screen("visualization", "可视化", Icons.Default.Animation)
    object Calculator : Screen("calculator", "计算器", Icons.Default.Calculate)
    object Practice : Screen("practice", "实战", Icons.Default.CameraAlt)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Theory,
        Screen.Visualization,
        Screen.Calculator,
        Screen.Practice
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("滤波标定算法学习") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Theory.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Theory.route) { TheoryScreen() }
            composable(Screen.Visualization.route) { VisualizationScreen() }
            composable(Screen.Calculator.route) { CalculatorScreen() }
            composable(Screen.Practice.route) { PracticeScreen() }
        }
    }
}
