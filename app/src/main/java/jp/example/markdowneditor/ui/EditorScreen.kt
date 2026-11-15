package jp.example.markdowneditor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.example.markdowneditor.data.Document

@Composable
fun EditorScreen(
    document: Document = Document(),
    onDocumentChange: (Document) -> Unit = {},
    onSave: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            // TODO: 保存ボタン等
        }
    ) { padding ->
        // TODO: Markdown TextField
    }
}

@Composable
fun EditorScreenPreview() {
    EditorScreen()
}
