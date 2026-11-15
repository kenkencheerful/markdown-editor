package jp.example.markdowneditor.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.example.markdowneditor.data.Document
import jp.example.markdowneditor.data.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val documentRepository = DocumentRepository.getInstance(application)

    private val _document = MutableStateFlow(Document())
    val document: StateFlow<Document> = _document

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Load saved document
        viewModelScope.launch {
            val document = documentRepository.loadDocumentSuspend()
            if (document != null) {
                _document.value = document
            }
        }
    }

    fun onContentChange(content: String) {
        _document.update { it.copy(content = content) }
    }

    fun onTitleChange(title: String) {
        _document.update { it.copy(title = title) }
    }

    fun applyFormat(
        command: FormatCommand,
        selectionStart: Int,
        selectionEnd: Int
    ) {
        val content = _document.value.content
        val newContent = when (command) {
            is FormatCommand.Bold -> applyBold(content, selectionStart, selectionEnd)
            is FormatCommand.Italic -> applyItalic(content, selectionStart, selectionEnd)
            is FormatCommand.Paragraph -> applyParagraph(content, selectionStart, selectionEnd)
            is FormatCommand.Table -> insertTable(content, selectionStart, selectionEnd)
        }
        _document.update { it.copy(content = newContent) }
    }

    fun save() {
        viewModelScope.launch {
            documentRepository.saveDocument(_document.value)
        }
    }

    fun clear() {
        _document.update { Document() }
    }

    private fun applyBold(content: String, start: Int, end: Int): String {
        if (start >= end) {
            // 選択がない場合は ** を挿入
            return content.substringBeforeEnd("**", "**")
        }
        return content.replaceRange(start, end, "**${content.substring(start, end)}**")
    }

    private fun applyItalic(content: String, start: Int, end: Int): String {
        if (start >= end) {
            return content.substringBeforeEnd("_", "_")
        }
        return content.replaceRange(start, end, "_" + content.substring(start, end) + "_")
    }

    private fun applyParagraph(content: String, start: Int, end: Int): String {
        // 選択範囲を段落（空行）で区切る
        val selectedText = content.substring(start, end)
        return content.replaceRange(start, end, selectedText + "\n\n")
    }

    private fun insertTable(content: String, start: Int, end: Int): String {
        val tableTemplate = """
            | 列1 | 列2 |
            |-----|-----|
            |     |     |
        """.trimIndent()
        return content.replaceRange(start, end, tableTemplate)
    }

    sealed interface FormatCommand {
        data object Bold : FormatCommand
        data object Italic : FormatCommand
        data object Paragraph : FormatCommand
        data object Table : FormatCommand
    }
}

private fun String.substringBeforeEnd(prefix: String, suffix: String): String {
    return prefix + this + suffix
}
