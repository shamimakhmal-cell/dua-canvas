package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaEntity
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicGreen
import com.example.viewmodel.DuaViewModel

@Composable
fun DuasScreen(
    viewModel: DuaViewModel,
    onNavigateToDetail: (DuaEntity) -> Unit,
    onNavigateToPoster: (DuaEntity) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryDuas by viewModel.categoryDuas.collectAsState()
    val allDuas by viewModel.allDuas.collectAsState()

    var selectedDetailDua by remember { mutableStateOf<DuaEntity?>(null) }

    val categories = listOf(
        "All",
        "Daily Duas",
        "Morning & Evening",
        "Salah",
        "Rizq & Debt",
        "Protection",
        "Forgiveness",
        "Family",
        "Ramadan",
        "Travel",
        "Sleep",
        "Quranic Duas"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Authentic Duas Library",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = (cat == "All" && selectedCategory == null) || selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (cat == "All") viewModel.setSelectedCategory(null)
                        else viewModel.setSelectedCategory(cat)
                    },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGreen,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val displayList = if (selectedCategory == null) allDuas else categoryDuas

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(displayList) { dua ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDetailDua = dua },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dua.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(containerColor = GoldPrimary) {
                                Text(dua.category, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dua.arabic,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = dua.englishMeaning,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Source: ${dua.sourceName}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { viewModel.toggleFavorite(dua) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (dua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (dua.isFavorite) androidx.compose.ui.graphics.Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onNavigateToPoster(dua) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = "Create Poster",
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
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
