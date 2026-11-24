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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: DuaViewModel,
    onNavigateToDetail: (DuaEntity) -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val allDuas by viewModel.allDuas.collectAsState()

    var selectedDetailDua by remember { mutableStateOf<DuaEntity?>(null) }

    val recentSearches = listOf("রিজিক", "rizq", "رزق", "protection", "debt", "forgiveness")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Search Header & Input
        Text(
            text = "Search Duas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search Arabic, Bangla, English, or transliteration...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recent / Quick Search suggestions
        if (query.isBlank()) {
            Text(
                text = "Recent Searches",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentSearches) { term ->
                    AssistChip(
                        onClick = { viewModel.setSearchQuery(term) },
                        label = { Text(term) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Results count or prompt
        val displayList = if (query.isBlank()) allDuas else searchResults

        Text(
            text = if (query.isBlank()) "All Duas (${displayList.size})" else "Search Results (${displayList.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(displayList) { dua ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedDetailDua = dua },
                    shape = RoundedCornerShape(12.dp),
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
                            horizontalArrangement = Arrangement.SpaceBetween
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dua.arabic,
                            fontSize = 18.sp,
                            maxLines = 1,
                            color = IslamicGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dua.englishMeaning,
                            fontSize = 13.sp,
                            maxLines = 2,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
                // Trigger poster creation
            }
        )
    }
}
