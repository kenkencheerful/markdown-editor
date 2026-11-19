package jp.example.markdowneditor.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.example.markdowneditor.data.Document
import jp.example.markdowneditor.data.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class EditorViewModel(application: Application) : AndroidViewModel(application) {
    private val documentRepository = DocumentRepository(application)

    private val _document = MutableStateFlow(Document())
    val document: StateFlow<Document> = _document

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        viewModelScope.launch {
            try {
                documentRepository.loadDocument().first()?.let { _document.value = it }
            } catch (error: IOException) {
                _message.value =
                    "文書を読み込めませんでした: ${error.localizedMessage ?: "保存データを確認してください"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onContentChange(content: String) {
        _document.update { it.copyWith(content = content) }
    }

    fun onTitleChange(title: String) {
        _document.update { it.copyWith(title = title) }
    }

    fun save() {
        val documentToSave = _document.value
        viewModelScope.launch {
            try {
                documentRepository.saveDocument(documentToSave)
                _message.value = "保存しました"
            } catch (error: IOException) {
                _message.value =
                    "保存できませんでした: ${error.localizedMessage ?: "ストレージを確認してください"}"
            }
        }
    }

    fun dismissMessage() {
        _message.value = null
    }
}
