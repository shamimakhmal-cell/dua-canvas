package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.DuaEntity
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicGreen

@Composable
fun DuaDetailDialog(
    dua: DuaEntity,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onCreatePoster: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(containerColor = GoldPrimary) {
                        Text(dua.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
                    }
                    Row {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (dua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (dua.isFavorite) androidx.compose.ui.graphics.Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = dua.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Authenticity Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when {
                            dua.authenticity.contains("Quran") || dua.authenticity.contains("Sahih") -> IslamicGreen.copy(alpha = 0.15f)
                            else -> GoldPrimary.copy(alpha = 0.2f)
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = dua.authenticity,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (dua.authenticity.contains("Quran") || dua.authenticity.contains("Sahih")) IslamicGreen else GoldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Arabic Text Box (RTL)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = IslamicGreen.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Arabic",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dua.arabic,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bangla Pronunciation
                    DetailSectionBox(title = "Bangla Pronunciation (উচ্চারণ)", content = dua.banglaPronunciation)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bangla Meaning
                    DetailSectionBox(title = "Bangla Meaning (অর্থ)", content = dua.banglaMeaning)

                    Spacer(modifier = Modifier.height(12.dp))

                    // English Meaning
                    DetailSectionBox(title = "English Meaning", content = dua.englishMeaning)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Source & Reference
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Source: ${dua.sourceName}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Reference: ${dua.reference}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    if (dua.fazilat.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GoldPrimary.copy(alpha = 0.15f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Fazilat / Virtues", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = dua.fazilat, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dua", "${dua.title}\n\n${dua.arabic}\n\n${dua.englishMeaning}\n\nSource: ${dua.sourceName} (${dua.reference})")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy")
                    }

                    Button(
                        onClick = onCreatePoster,
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Poster Maker")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSectionBox(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
