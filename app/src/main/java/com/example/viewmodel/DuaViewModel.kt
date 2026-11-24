package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DuaEntity
import com.example.data.DuaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PosterConfig(
    val format: String = "A4 Portrait", // A4 Portrait, A4 Landscape, 1080x1350, 1080x1920, 1080x1080
    val templateId: Int = 1,
    val arabicFont: String = "Amiri",
    val banglaFont: String = "Noto Sans Bengali",
    val englishFont: String = "Noto Sans",
    val arabicFontSize: Float = 24f,
    val banglaFontSize: Float = 16f,
    val englishFontSize: Float = 14f,
    val textColor: String = "#FFFFFF",
    val backgroundColor: String = "#0B3C29",
    val alignment: String = "Center", // Left, Center, Right
    val lineSpacing: Float = 1.5f,
    val margin: Int = 24,
    val hasBorder: Boolean = true,
    val hasIslamicDecorations: Boolean = true,
    val headerText: String = "DuaCanvas",
    val footerText: String = "Verified Islamic Supplication",
    val showSource: Boolean = true
)

class DuaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DuaRepository

    val allDuas: StateFlow<List<DuaEntity>>
    val favoriteDuas: StateFlow<List<DuaEntity>>

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedCollection = MutableStateFlow<String?>(null)
    val selectedCollection: StateFlow<String?> = _selectedCollection.asStateFlow()

    private val _currentDua = MutableStateFlow<DuaEntity?>(null)
    val currentDua: StateFlow<DuaEntity?> = _currentDua.asStateFlow()

    private val _posterConfig = MutableStateFlow(PosterConfig())
    val posterConfig: StateFlow<PosterConfig> = _posterConfig.asStateFlow()

    val searchResults: StateFlow<List<DuaEntity>>
    val categoryDuas: StateFlow<List<DuaEntity>>
    val collectionDuas: StateFlow<List<DuaEntity>>

    init {
        val duaDao = AppDatabase.getDatabase(application, viewModelScope).duaDao()
        repository = DuaRepository(duaDao)

        allDuas = repository.allDuas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        favoriteDuas = repository.favoriteDuas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        searchResults = _searchQuery
            .debounce(200)
            .flatMapLatest { query ->
                if (query.isBlank()) flowOf(emptyList())
                else repository.searchDuas(query)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        categoryDuas = _selectedCategory
            .flatMapLatest { cat ->
                if (cat == null) repository.allDuas
                else repository.getDuasByCategory(cat)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        collectionDuas = _selectedCollection
            .flatMapLatest { col ->
                if (col == null) flowOf(emptyList())
                else repository.getDuasByCollection(col)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSelectedCollection(collection: String?) {
        _selectedCollection.value = collection
    }

    fun setCurrentDua(dua: DuaEntity?) {
        _currentDua.value = dua
    }

    fun toggleFavorite(dua: DuaEntity) {
        viewModelScope.launch {
            repository.updateFavorite(dua.id, !dua.isFavorite)
        }
    }

    fun assignCollection(duaId: String, collectionName: String?) {
        viewModelScope.launch {
            repository.updateCollection(duaId, collectionName)
        }
    }

    fun addCustomDua(
        title: String,
        arabic: String,
        pronunciation: String,
        banglaMeaning: String,
        englishMeaning: String,
        reference: String,
        sourceName: String,
        category: String,
        collectionName: String?
    ) {
        viewModelScope.launch {
            val id = "custom_" + System.currentTimeMillis()
            val customDua = DuaEntity(
                id = id,
                title = title,
                category = category.ifBlank { "Personal" },
                arabic = arabic,
                banglaPronunciation = pronunciation,
                banglaMeaning = banglaMeaning,
                englishMeaning = englishMeaning,
                sourceType = "User Provided",
                sourceName = sourceName.ifBlank { "User Provided Source" },
                reference = reference.ifBlank { "Not provided" },
                authenticity = "✦ User Provided",
                fazilat = "User-authored supplication.",
                tags = "custom, personal, dua",
                isFavorite = true,
                collectionName = collectionName ?: "Personal",
                isCustom = true
            )
            repository.insertDua(customDua)
        }
    }

    fun updatePosterConfig(config: PosterConfig) {
        _posterConfig.value = config
    }
}
