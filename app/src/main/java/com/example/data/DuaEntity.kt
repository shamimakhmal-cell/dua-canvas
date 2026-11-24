package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class DuaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val arabic: String,
    val banglaPronunciation: String,
    val banglaMeaning: String,
    val englishMeaning: String,
    val sourceType: String, // "Quran", "Hadith", "Other", "User Provided"
    val sourceName: String,
    val reference: String,
    val authenticity: String, // "✓ Quran", "✓ Sahih Hadith", "✓ Hasan Hadith", "⚠ Reference Requires Verification", "✦ User Provided"
    val fazilat: String,
    val tags: String,
    val isFavorite: Boolean = false,
    val collectionName: String? = null,
    val isCustom: Boolean = false
)
