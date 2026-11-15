package jp.example.markdowneditor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.example.markdowneditor.data.Document

@Composable
fun PreviewScreen(
    document: Document = Document(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            // TODO: 保存ボタン等
        }
    ) { padding ->
        // TODO: MarkwonによるMarkdownプレビュー
    }
}

@Composable
fun PreviewScreenPreview() {
    PreviewScreen()
}
