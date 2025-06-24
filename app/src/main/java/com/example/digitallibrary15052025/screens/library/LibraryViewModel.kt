package com.logan.digitallibrary.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logan.digitallibrary.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "LibraryViewModel"

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    val books = repository.allBooks
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
             initialValue = emptyList()
    )

    suspend fun addBook(fileBytes: ByteArray, fileName: String) {
        try {
            Log.d(TAG, "Добавленая книга: $fileName")
            repository.addBook(fileBytes, fileName)
            Log.d(TAG, "Успешно добавленная книга: $fileName")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при добавлении книги: $fileName", e)
            throw e
        }
    }
}
