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
fun FavoritesScreen(
    viewModel: DuaViewModel,
    onNavigateToDetail: (DuaEntity) -> Unit
) {
    val favorites by viewModel.favoriteDuas.collectAsState()
    var showAddCustomDialog by remember { mutableStateOf(false) }
    var selectedDetailDua by remember { mutableStateOf<DuaEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favorite Duas & Collections",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                onClick = { showAddCustomDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Custom Dua")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No favorite duas saved yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favorites) { dua ->
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
                                if (dua.isCustom) {
                                    Badge(containerColor = GoldPrimary) {
                                        Text("User Provided", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
                                    }
                                } else {
                                    Badge(containerColor = IslamicGreen.copy(alpha = 0.2f)) {
                                        Text(dua.category, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = IslamicGreen)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = dua.arabic,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = dua.englishMeaning,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddCustomDialog) {
        AddCustomDuaDialog(
            onDismiss = { showAddCustomDialog = false },
            onSave = { title, arabic, pron, bnMean, enMean, ref, source, cat ->
                viewModel.addCustomDua(
                    title = title,
                    arabic = arabic,
                    pronunciation = pron,
                    banglaMeaning = bnMean,
                    englishMeaning = enMean,
                    reference = ref,
                    sourceName = source,
                    category = cat,
                    collectionName = "Personal"
                )
                showAddCustomDialog = false
            }
        )
    }

    selectedDetailDua?.let { dua ->
        DuaDetailDialog(
            dua = dua,
            onDismiss = { selectedDetailDua = null },
            onToggleFavorite = { viewModel.toggleFavorite(dua) },
            onCreatePoster = {
                selectedDetailDua = null
            }
        )
    }
}

@Composable
fun AddCustomDuaDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var arabic by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var banglaMeaning by remember { mutableStateOf("") }
    var englishMeaning by remember { mutableStateOf("") }
    var reference by remember { mutableStateOf("") }
    var sourceName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Dua") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = arabic,
                    onValueChange = { arabic = it },
                    label = { Text("Arabic Text") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = englishMeaning,
                    onValueChange = { englishMeaning = it },
                    label = { Text("English Meaning") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = sourceName,
                    onValueChange = { sourceName = it },
                    label = { Text("Source / Author") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(
                    text = "Note: User-provided content is tagged as '✦ User Provided' to maintain absolute religious integrity.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && arabic.isNotBlank()) {
                        onSave(title, arabic, pronunciation, banglaMeaning, englishMeaning, reference, sourceName, category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen)
            ) {
                Text("Save Dua")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
