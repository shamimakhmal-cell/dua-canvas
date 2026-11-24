package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaEntity
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.GoldPrimary
import com.example.viewmodel.DuaViewModel

data class CategoryItem(val name: String, val icon: String)

@Composable
fun HomeScreen(
    viewModel: DuaViewModel,
    onNavigateToDuas: () -> Unit,
    onNavigateToDetail: (DuaEntity) -> Unit,
    onNavigateToPoster: (DuaEntity) -> Unit
) {
    val allDuas by viewModel.allDuas.collectAsState()
    val favorites by viewModel.favoriteDuas.collectAsState()
    var selectedDetailDua by remember { mutableStateOf<DuaEntity?>(null) }

    val categories = listOf(
        CategoryItem("Daily Duas", "🤲"),
        CategoryItem("Morning & Evening", "🌅"),
        CategoryItem("Salah", "🕌"),
        CategoryItem("Rizq & Debt", "💰"),
        CategoryItem("Protection", "🛡️"),
        CategoryItem("Forgiveness", "🤲"),
        CategoryItem("Family", "👨‍👩‍👧"),
        CategoryItem("Ramadan", "🌙"),
        CategoryItem("Travel", "✈️"),
        CategoryItem("Sleep", "💤"),
        CategoryItem("Quranic Duas", "📖")
    )

    val dailyDua = allDuas.firstOrNull { it.id == "dua_003" } ?: allDuas.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingBottom(24.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(IslamicGreen, MaterialTheme.colorScheme.surface)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                color = GoldPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "DuaCanvas",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { /* Settings / Info */ },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Dua of the Day Card
        if (dailyDua != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { selectedDetailDua = dailyDua },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        IslamicGreen.copy(alpha = 0.08f),
                                        GoldPrimary.copy(alpha = 0.12f)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Badge(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ) {
                                Text("Dua of the Day", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                            IconButton(
                                onClick = { viewModel.toggleFavorite(dailyDua) }
                            ) {
                                Icon(
                                    imageVector = if (dailyDua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (dailyDua.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = dailyDua.arabic,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = dailyDua.englishMeaning,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { onNavigateToPoster(dailyDua) },
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create Poster")
                            }
                            OutlinedButton(
                                onClick = { selectedDetailDua = dailyDua },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Read Details")
                            }
                        }
                    }
                }
            }
        }

        // Categories Header
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigateToDuas) {
                    Text("View All", color = IslamicGreen)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Categories Grid/List
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .width(110.dp)
                            .height(100.dp)
                            .clickable {
                                viewModel.setSelectedCategory(category.name)
                                onNavigateToDuas()
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = category.icon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Popular Duas Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Duas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(allDuas.take(5)) { dua ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { selectedDetailDua = dua },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = dua.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dua.category,
                            fontSize = 12.sp,
                            color = GoldPrimary
                        )
                    }
                    IconButton(onClick = { viewModel.toggleFavorite(dua) }) {
                        Icon(
                            imageVector = if (dua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (dua.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Dua Detail Dialog when clicked from Home
    selectedDetailDua?.let { dua ->
        DuaDetailDialog(
            dua = dua,
            onDismiss = { selectedDetailDua = null },
            onToggleFavorite = { viewModel.toggleFavorite(dua) },
            onCreatePoster = {
                selectedDetailDua = null
                onNavigateToPoster(dua)
            }
        )
    }
}

fun PaddingBottom(dp: androidx.compose.ui.unit.Dp): PaddingValues = PaddingValues(bottom = dp)
