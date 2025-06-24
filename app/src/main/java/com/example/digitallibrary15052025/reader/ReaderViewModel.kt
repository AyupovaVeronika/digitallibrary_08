package com.logan.digitallibrary.ui.reader

import android.util.Log
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logan.digitallibrary.data.models.ChapterModel
import com.logan.digitallibrary.data.repository.BookRepository
import com.logan.digitallibrary.epub.parser.EpubParser
import com.logan.digitallibrary.epub.utils.EpubImageData
import com.logan.digitallibrary.ui.theme.Fonts
import com.logan.digitallibrary.ui.theme.ReaderTheme
import com.logan.digitallibrary.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val TAG = "ReaderViewModel"

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: BookRepository,
    private val preferencesManager: PreferencesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookId: String = checkNotNull(savedStateHandle["bookId"])
    private var epubImages: Map<String, ByteArray> = emptyMap()
    private var currentBookId: String = ""
    private var lastSavedPosition: ReadingPosition? = null

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState = _uiState.asStateFlow()

    private val _chapters = MutableStateFlow<List<ChapterModel>>(emptyList())
    val chapters = _chapters.asStateFlow()

    init {
        loadBook()
    }

    fun getImageData(imagePath: String): EpubImageData? {
        return epubImages[imagePath]?.let { 
            EpubImageData(currentBookId, imagePath, it)
        }
    }

    private fun loadBook() {
        viewModelScope.launch {
            try {
                val book = repository.allBooks.first().find { it.id == bookId }
                    ?: throw Exception("Книга не найдена")

                currentBookId = book.id

                // Load the book content
                val epubBook = EpubParser.parse(File(book.filePath).inputStream())
                epubImages = epubBook.images.associate { it.absPath to it.image }

                // Update chapters
                _chapters.value = epubBook.chapters.mapIndexed { index, chapter ->
                    ChapterModel(
                        index = index,
                        title = chapter.title,
                        content = chapter.content
                    )
                }

                // Update UI state with saved position
                _uiState.update { state ->
                    state.copy(
                        title = book.title,
                        currentChapterIndex = book.lastReadChapterIndex,
                        scrollPosition = book.lastReadPosition,
                        fontSize = preferencesManager.fontSize,
                        fontFamily = preferencesManager.fontFamily,
                        theme = preferencesManager.theme,
                        isLoading = false,
                        coverImage = epubBook.coverImage?.let { 
                            EpubImageData(currentBookId, "cover", it.image)
                        }
                    )
                }

                lastSavedPosition = ReadingPosition(
                    chapterIndex = book.lastReadChapterIndex,
                    scrollOffset = book.lastReadPosition
                )

                Log.d(TAG, "Книга загружена с указанием позиции - Главы: ${book.lastReadChapterIndex}, Позиция: ${book.lastReadPosition}")
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось загрузить книгу", e)
                _uiState.update { it.copy(
                    error = e.message ?: "Не удалось загрузить книгу",
                    isLoading = false
                )}
            }
        }
    }

    fun saveReadingProgress(position: ReadingPosition) {
        // Don't save if position hasn't changed
        if (position == lastSavedPosition) return
        
        viewModelScope.launch {
            try {
                repository.updateReadingProgress(
                    bookId = bookId,
                    chapterIndex = position.chapterIndex,
                    position = position.scrollOffset
                )
                
                lastSavedPosition = position
                _uiState.update { it.copy(
                    currentChapterIndex = position.chapterIndex,
                    scrollPosition = position.scrollOffset
                )}
                
                Log.d(TAG, "Сохранен прогресс чтения - Глава: ${position.chapterIndex}, Позиция: ${position.scrollOffset}")
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось сохранить прогресс чтения", e)
            }
        }
    }

    fun updateFontSize(size: Float) {
        preferencesManager.fontSize = size
        _uiState.update { it.copy(fontSize = size) }
    }

    fun updateFont(font: FontFamily) {
        preferencesManager.fontFamily = font
        _uiState.update { it.copy(fontFamily = font) }
    }

    fun updateTheme(theme: ReaderTheme) {
        preferencesManager.theme = theme
        _uiState.update { it.copy(theme = theme) }
    }

    override fun onCleared() {
        super.onCleared()
        // Save final reading position when ViewModel is cleared
        _uiState.value.let { state ->
            viewModelScope.launch {
                try {
                    repository.updateReadingProgress(
                        bookId = bookId,
                        chapterIndex = state.currentChapterIndex,
                        position = state.scrollPosition
                    )
                    Log.d(TAG, "Окончательный прогресс чтения, сохраненный в ViewModel, очищен")
                } catch (e: Exception) {
                    Log.e(TAG, "Не удалось сохранить окончательный прогресс чтения", e)
                }
            }
        }
    }
}

data class ReaderUiState(
    val title: String = "",
    val currentChapterIndex: Int = 0,
    val scrollPosition: Float = 0f,
    val fontSize: Float = 16f,
    val fontFamily: FontFamily = Fonts.Roboto,
    val theme: ReaderTheme = ReaderTheme.LIGHT,
    val isLoading: Boolean = true,
    val error: String? = null,
    val coverImage: EpubImageData? = null
)

data class ReadingPosition(
    val chapterIndex: Int,
    val scrollOffset: Float
)
