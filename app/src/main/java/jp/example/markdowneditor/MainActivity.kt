package jp.example.markdowneditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.example.markdowneditor.editor.EditorViewModel
import jp.example.markdowneditor.ui.EditorScreen
import jp.example.markdowneditor.ui.PreviewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarkdownEditorApp()
        }
    }
}

@Composable
fun MarkdownEditorApp(
    viewModel: EditorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("編集", "プレビュー")

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> EditorScreen(
                document = viewModel.document.value,
                onDocumentChange = { viewModel.onContentChange(it.content) },
                onSave = { viewModel.save() }
            )
            1 -> PreviewScreen(
                document = viewModel.document.value
            )
        }
    }
}

@Preview
@Composable
fun MarkdownEditorAppPreview() {
    MarkdownEditorApp()
}
