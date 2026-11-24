package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaEntity
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicGreen
import com.example.viewmodel.DuaViewModel
import com.example.viewmodel.PosterConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPosterGeneratorScreen(
    viewModel: DuaViewModel,
    allDuas: List<DuaEntity>,
    onApplyToEditor: (DuaEntity, PosterConfig) -> Unit,
    onClose: () -> Unit
) {
    var promptText by remember { mutableStateOf("") }
    var followUpPrompt by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Black & Gold") }
    var selectedSize by remember { mutableStateOf("A4 Portrait") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var matchedDua by remember { mutableStateOf<DuaEntity?>(null) }
    var generatedConfig by remember { mutableStateOf(PosterConfig()) }
    var historyStack by remember { mutableStateOf<List<PosterConfig>>(emptyList()) }

    val styles = listOf("Islamic", "Minimal", "Black & Gold", "Green Islamic", "Ramadan", "Luxury", "Mosque", "Quranic", "Modern", "Elegant")
    val sizes = listOf("A4 Portrait", "A4 Landscape", "1080x1080", "1080x1350", "1080x1920")

    fun generatePosterFromPrompt(inputPrompt: String) {
        if (inputPrompt.isBlank()) return
        isLoading = true
        errorMessage = null

        // 1. Match dua topic from verified database
        val matchingDua = allDuas.firstOrNull { dua ->
            inputPrompt.contains(dua.title, ignoreCase = true) ||
            inputPrompt.contains(dua.category, ignoreCase = true) ||
            inputPrompt.contains(dua.tags, ignoreCase = true) ||
            inputPrompt.contains("debt", ignoreCase = true) && dua.id == "dua_002" ||
            inputPrompt.contains("rizq", ignoreCase = true) && dua.id == "dua_001" ||
            inputPrompt.contains("forgiveness", ignoreCase = true) && dua.id == "dua_006" ||
            inputPrompt.contains("protection", ignoreCase = true) && dua.id == "dua_004" ||
            inputPrompt.contains("daily", ignoreCase = true) && dua.id == "dua_003"
        } ?: allDuas.firstOrNull()

        if (matchingDua == null) {
            errorMessage = "Verified dua not found."
            isLoading = false
            return
        }

        matchedDua = matchingDua

        // 2. Parse style preferences from natural language prompt
        var templateId = 1
        var fontSize = 26f
        var format = selectedSize

        if (inputPrompt.contains("green", ignoreCase = true) || selectedStyle == "Green Islamic") {
            templateId = 3
        } else if (inputPrompt.contains("minimal", ignoreCase = true) || selectedStyle == "Minimal") {
            templateId = 2
        } else if (inputPrompt.contains("luxurious", ignoreCase = true) || inputPrompt.contains("gold", ignoreCase = true)) {
            templateId = 5
        } else if (inputPrompt.contains("ramadan", ignoreCase = true) || selectedStyle == "Ramadan") {
            templateId = 6
        }

        if (inputPrompt.contains("landscape", ignoreCase = true)) {
            format = "A4 Landscape"
        } else if (inputPrompt.contains("square", ignoreCase = true) || inputPrompt.contains("1080x1080", ignoreCase = true)) {
            format = "1080x1080"
        } else if (inputPrompt.contains("story", ignoreCase = true) || inputPrompt.contains("1080x1920", ignoreCase = true)) {
            format = "1080x1920"
        }

        if (inputPrompt.contains("20% larger", ignoreCase = true) || inputPrompt.contains("larger", ignoreCase = true)) {
            fontSize *= 1.2f
        }

        val newConfig = PosterConfig(
            format = format,
            templateId = templateId,
            arabicFontSize = fontSize,
            backgroundColor = "#111111",
            textColor = "#FFFFFF"
        )

        historyStack = historyStack + generatedConfig
        generatedConfig = newConfig
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎨", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "AI Poster Generator", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Prompt Input
            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Describe your poster (e.g. Elegant black and gold A4 poster for Debt Relief)") },
                placeholder = { Text("Create an elegant black and gold A4 poster for the Dua for Debt Relief...") },
                shape = RoundedCornerShape(14.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Style Chips
            Text(text = "Quick Styles", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(styles) { style ->
                    FilterChip(
                        selected = selectedStyle == style,
                        onClick = { selectedStyle = style },
                        label = { Text(style) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Poster Size Selector
            Text(text = "Poster Size", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sizes) { size ->
                    FilterChip(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size },
                        label = { Text(size) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Generate Button
            Button(
                onClick = {
                    val fullPrompt = "$promptText (Style: $selectedStyle, Size: $selectedSize)"
                    generatePosterFromPrompt(fullPrompt)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI is designing your poster...")
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate with AI")
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Preview Section
            if (matchedDua != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "AI Generated Preview", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.DarkGray)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(
                                when (generatedConfig.format) {
                                    "A4 Portrait" -> 1f / 1.414f
                                    "A4 Landscape" -> 1.414f / 1f
                                    "1080x1350" -> 1080f / 1350f
                                    "1080x1920" -> 1080f / 1920f
                                    else -> 1f
                                }
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (generatedConfig.templateId) {
                                3 -> Color(0xFF0B3C29)
                                2 -> Color(0xFFFAFAFA)
                                5 -> Color(0xFF1A150B)
                                else -> Color(0xFF111111)
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                color = GoldPrimary,
                                fontSize = 14.sp
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = matchedDua!!.arabic,
                                    color = if (generatedConfig.templateId == 2) Color.Black else Color.White,
                                    fontSize = generatedConfig.arabicFontSize.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = matchedDua!!.englishMeaning,
                                    color = if (generatedConfig.templateId == 2) Color.DarkGray else Color.LightGray,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = "${matchedDua!!.sourceName} (${matchedDua!!.reference})",
                                color = GoldPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AI Follow-up Prompt Box
                OutlinedTextField(
                    value = followUpPrompt,
                    onValueChange = { followUpPrompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Describe additional changes (e.g. Make Arabic text 20% larger)") },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (followUpPrompt.isNotBlank()) {
                                generatePosterFromPrompt(followUpPrompt)
                                followUpPrompt = ""
                            }
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send")
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Post-Generation Controls: [Apply] [Regenerate] [Change Style] [Edit Manually] [Undo] [Save Template]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onApplyToEditor(matchedDua!!, generatedConfig) },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply")
                    }
                    OutlinedButton(
                        onClick = { generatePosterFromPrompt(promptText) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Regenerate")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (historyStack.isNotEmpty()) {
                                generatedConfig = historyStack.last()
                                historyStack = historyStack.dropLast(1)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = historyStack.isNotEmpty()
                    ) {
                        Text("Undo")
                    }
                    OutlinedButton(
                        onClick = { onApplyToEditor(matchedDua!!, generatedConfig) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit Manually")
                    }
                }
            }
        }
    }
}
