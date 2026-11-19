package jp.example.markdowneditor.editor

import android.app.Application
import android.content.Intent
import android.net.Uri
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
import java.nio.charset.Charsets

class EditorViewModel(application: Application) : AndroidViewModel(application) {
    private val documentRepository = DocumentRepository(application)

    private val _document = MutableStateFlow(Document())
    val document: StateFlow<Document> = _document

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private var _selectedFolderUri: String? = null
    val selectedFolderUri: String? get() = _selectedFolderUri

    fun setSelectedFolderUri(uri: String?) {
        if (uri != null) {
            try {
                getApplication<Application>().contentResolver.takePersistableUriPermission(
                    Uri.parse(uri),
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                // ignored
            }
        }
        _selectedFolderUri = uri
    }

    fun saveToSelectedFolder() {
        val uriString = _selectedFolderUri ?: return
        val documentToSave = _document.value
        viewModelScope.launch {
            try {
                val uri = Uri.parse(uriString)
                getApplication<Application>().contentResolver.openOutputStream(uri, "rwt")?.use { output ->
                    output.write(documentToSave.content.toByteArray(Charsets.UTF_8))
                }
                _message.value = "保存先フォルダに保存しました"
            } catch (error: Exception) {
                _message.value = "保存先フォルダへの保存に失敗しました"
            }
        }
    }

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
