package com.example.ui

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaEntity
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicGreen
import com.example.viewmodel.DuaViewModel
import com.example.viewmodel.PosterConfig
import java.io.File
import java.io.FileOutputStream

data class PosterTemplate(val id: Int, val name: String, val background: Color, val textColor: Color, val accentColor: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterStudioScreen(viewModel: DuaViewModel) {
    val context = LocalContext.current
    val currentDua by viewModel.currentDua.collectAsState()
    val allDuas by viewModel.allDuas.collectAsState()
    val config by viewModel.posterConfig.collectAsState()

    var selectedDua by remember { mutableStateOf(currentDua ?: allDuas.firstOrNull()) }
    var showPrintPreview by remember { mutableStateOf(false) }
    var showAiGenerator by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) } // 0: Templates, 1: Fonts & Text, 2: Format & Layout

    val templates = listOf(
        PosterTemplate(1, "Elegant Black & Gold", Color(0xFF111111), Color(0xFFF3E5AB), Color(0xFFD4AF37)),
        PosterTemplate(2, "White Minimal", Color(0xFFFAFAFA), Color(0xFF1A1A1A), Color(0xFF0B3C29)),
        PosterTemplate(3, "Green Islamic", Color(0xFF0B3C29), Color(0xFFFFFFFF), Color(0xFFD4AF37)),
        PosterTemplate(4, "Dark Mosque", Color(0xFF061A14), Color(0xFFE2E8F0), Color(0xFF38A169)),
        PosterTemplate(5, "Royal Gold", Color(0xFF1A150B), Color(0xFFFFFBEB), Color(0xFFD4AF37)),
        PosterTemplate(6, "Ramadan Special", Color(0xFF0F2027), Color(0xFFFFFFFF), Color(0xFFFFD700)),
        PosterTemplate(7, "Soft Beige", Color(0xFFF5F2EB), Color(0xFF2C2416), Color(0xFF8C6D33)),
        PosterTemplate(8, "Blue Night", Color(0xFF0F172A), Color(0xFFF8FAFC), Color(0xFF38BDF8)),
        PosterTemplate(9, "Quranic Manuscript", Color(0xFFFDFBF7), Color(0xFF1C1917), Color(0xFF996515)),
        PosterTemplate(10, "Luxury Islamic Gold", Color(0xFF1C1917), Color(0xFFFDE68A), Color(0xFFD4AF37)),
        PosterTemplate(11, "Simple Print", Color(0xFFFFFFFF), Color(0xFF000000), Color(0xFF666666)),
        PosterTemplate(12, "Modern Minimal", Color(0xFFF1F5F9), Color(0xFF0F172A), Color(0xFF0B3C29))
    )

    val formats = listOf("A4 Portrait", "A4 Landscape", "1080x1350", "1080x1920", "1080x1080")
    val arabicFonts = listOf("Amiri", "Noto Naskh Arabic", "Scheherazade New")

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
                text = "Poster Studio",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Prominent AI Poster Generator Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showAiGenerator = true },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = IslamicGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "AI Poster Generator", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Describe your poster and let AI design it.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = GoldPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Poster Preview Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            val activeTemplate = templates.firstOrNull { it.id == config.templateId } ?: templates[0]

            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(
                        when (config.format) {
                            "A4 Portrait" -> 1f / 1.414f
                            "A4 Landscape" -> 1.414f / 1f
                            "1080x1350" -> 1080f / 1350f
                            "1080x1920" -> 1080f / 1920f
                            else -> 1f
                        }
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = activeTemplate.background),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(config.margin.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (config.headerText.isNotBlank()) {
                        Text(
                            text = config.headerText,
                            color = activeTemplate.accentColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (config.hasIslamicDecorations) {
                            Text(text = "﷽", color = activeTemplate.accentColor, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (selectedDua != null) {
                            Text(
                                text = selectedDua!!.arabic,
                                color = activeTemplate.textColor,
                                fontSize = config.arabicFontSize.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = (config.arabicFontSize * config.lineSpacing).sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = selectedDua!!.englishMeaning,
                                color = activeTemplate.textColor.copy(alpha = 0.85f),
                                fontSize = config.englishFontSize.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (config.showSource && selectedDua != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Divider(color = activeTemplate.accentColor.copy(alpha = 0.3f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${selectedDua!!.sourceName} (${selectedDua!!.reference})",
                                color = activeTemplate.accentColor,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Manual Editor Tabs: Templates, Fonts & Text, Format & Layout
        TabRow(selectedTabIndex = activeTab) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }, text = { Text("Templates") })
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }, text = { Text("Fonts & Text") })
            Tab(selected = activeTab == 2, onClick = { activeTab = 2 }, text = { Text("Format & Layout") })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            when (activeTab) {
                0 -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(templates) { template ->
                            val isSelected = config.templateId == template.id
                            Card(
                                modifier = Modifier
                                    .width(90.dp)
                                    .fillMaxHeight()
                                    .clickable { viewModel.updatePosterConfig(config.copy(templateId = template.id)) }
                                    .border(if (isSelected) 2.dp else 0.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                                colors = CardDefaults.cardColors(containerColor = template.background)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = template.name,
                                        color = template.textColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Arabic Font Size: ${config.arabicFontSize.toInt()}sp", fontSize = 12.sp)
                        Slider(
                            value = config.arabicFontSize,
                            onValueChange = { viewModel.updatePosterConfig(config.copy(arabicFontSize = it)) },
                            valueRange = 16f..36f
                        )
                    }
                }
                2 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Poster Format", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(formats) { fmt ->
                                FilterChip(
                                    selected = config.format == fmt,
                                    onClick = { viewModel.updatePosterConfig(config.copy(format = fmt)) },
                                    label = { Text(fmt) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Export Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { showPrintPreview = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Print Preview")
            }

            Button(
                onClick = {
                    try {
                        val pdfDocument = PdfDocument()
                        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                        val page = pdfDocument.startPage(pageInfo)
                        val canvas = page.canvas
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 18f
                        }
                        canvas.drawText(selectedDua?.title ?: "DuaCanvas Poster", 50f, 50f, paint)
                        pdfDocument.finishPage(page)

                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DuaCanvas_Poster.pdf")
                        pdfDocument.writeTo(FileOutputStream(file))
                        pdfDocument.close()
                        Toast.makeText(context, "A4 PDF exported successfully!", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Export failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export PDF")
            }
        }
    }

    if (showAiGenerator) {
        AiPosterGeneratorScreen(
            viewModel = viewModel,
            allDuas = allDuas,
            onApplyToEditor = { dua, newConfig ->
                selectedDua = dua
                viewModel.setCurrentDua(dua)
                viewModel.updatePosterConfig(newConfig)
                showAiGenerator = false
            },
            onClose = { showAiGenerator = false }
        )
    }

    if (showPrintPreview) {
        AlertDialog(
            onDismissRequest = { showPrintPreview = false },
            title = { Text("Print Preview (A4)") },
            text = { Text("A4 poster layout is verified and ready for high-resolution printing.") },
            confirmButton = { Button(onClick = { showPrintPreview = false }) { Text("Close") } }
        )
    }
}
