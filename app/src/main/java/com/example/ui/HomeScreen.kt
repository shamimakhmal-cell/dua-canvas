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
import com.example.ui.theme.DeepEmeraldGreen
import com.example.ui.theme.MutedIslamicGold
import com.example.ui.theme.WarmIvory
import com.example.viewmodel.DuaViewModel

data class HomeCategory(val name: String, val icon: String, val queryCategory: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DuaViewModel,
    onNavigateToDuas: () -> Unit,
    onNavigateToDetail: (DuaEntity) -> Unit,
    onNavigateToPoster: (DuaEntity) -> Unit
) {
    val allDuas by viewModel.allDuas.collectAsState()
    var selectedDetailDua by remember { mutableStateOf<DuaEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        HomeCategory("Daily", "🤲", "Daily Duas"),
        HomeCategory("Morning", "🌅", "Morning & Evening"),
        HomeCategory("Evening", "🌙", "Morning & Evening"),
        HomeCategory("Protection", "🛡️", "Protection"),
        HomeCategory("Rizq", "💰", "Rizq & Debt"),
        HomeCategory("Family", "👨‍👩‍👧", "Family"),
        HomeCategory("Salah", "🕌", "Salah"),
        HomeCategory("Quranic", "📖", "Quranic Duas")
    )

    val dailyDua = allDuas.firstOrNull { it.id == "dua_003" } ?: allDuas.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Top Section: Basmala, Greeting, Headline
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DeepEmeraldGreen,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    color = MutedIslamicGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Assalamu Alaikum",
                            color = WarmIvory.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Your Daily Duas",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MutedIslamicGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MutedIslamicGold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search duas in Arabic, Bangla or English", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MutedIslamicGold) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MutedIslamicGold,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        // Quick Category Cards
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Quick Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { cat ->
                    Card(
                        modifier = Modifier
                            .width(96.dp)
                            .height(90.dp)
                            .clickable {
                                viewModel.setSelectedCategory(cat.queryCategory)
                                onNavigateToDuas()
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = cat.icon, fontSize = 26.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = cat.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Dua of the Day Editorial Card
        if (dailyDua != null) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Dua of the Day",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            DeepEmeraldGreen.copy(alpha = 0.05f),
                                            MutedIslamicGold.copy(alpha = 0.1f)
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
                                    containerColor = MutedIslamicGold,
                                    contentColor = Color.Black
                                ) {
                                    Text(dailyDua.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold)
                                }
                                IconButton(onClick = { viewModel.toggleFavorite(dailyDua) }) {
                                    Icon(
                                        imageVector = if (dailyDua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (dailyDua.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = dailyDua.arabic,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 24.sp,
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
                                OutlinedButton(
                                    onClick = { selectedDetailDua = dailyDua },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Read Dua")
                                }
                                Button(
                                    onClick = { onNavigateToPoster(dailyDua) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Create Poster")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

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
