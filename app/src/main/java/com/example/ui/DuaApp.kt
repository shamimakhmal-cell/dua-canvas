package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.DuaViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val outlinedIcon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Search : Screen("search", "Search", Icons.Filled.Search, Icons.Outlined.Search)
    object Duas : Screen("duas", "Duas", Icons.Filled.Book, Icons.Outlined.Book)
    object Favorites : Screen("favorites", "Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object PosterStudio : Screen("poster", "Poster", Icons.Filled.Palette, Icons.Outlined.Palette)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaApp(viewModel: DuaViewModel = viewModel()) {
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Duas,
        Screen.Favorites,
        Screen.PosterStudio
    )

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEach { screen ->
                    val selected = currentScreen == screen
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.icon else screen.outlinedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = { currentScreen = screen }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDuas = { currentScreen = Screen.Duas },
                    onNavigateToDetail = { dua ->
                        viewModel.setCurrentDua(dua)
                        // Or navigate to detail dialog/screen
                    },
                    onNavigateToPoster = { dua ->
                        viewModel.setCurrentDua(dua)
                        currentScreen = Screen.PosterStudio
                    }
                )
                is Screen.Search -> SearchScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { dua ->
                        viewModel.setCurrentDua(dua)
                    }
                )
                is Screen.Duas -> DuasScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { dua ->
                        viewModel.setCurrentDua(dua)
                    },
                    onNavigateToPoster = { dua ->
                        viewModel.setCurrentDua(dua)
                        currentScreen = Screen.PosterStudio
                    }
                )
                is Screen.Favorites -> FavoritesScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { dua ->
                        viewModel.setCurrentDua(dua)
                    }
                )
                is Screen.PosterStudio -> PosterStudioScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
