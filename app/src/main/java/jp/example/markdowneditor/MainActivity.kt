package jp.example.markdowneditor

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatInvertColor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import jp.example.markdowneditor.editor.EditorViewModel
import jp.example.markdowneditor.ui.EditorScreen
import jp.example.markdowneditor.ui.PreviewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MarkdownEditorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownEditorApp(viewModel: EditorViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val document by viewModel.document.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val editorTab = stringResource(R.string.editor_tab)
    val previewTab = stringResource(R.string.preview_tab)

    var inverted by rememberSaveable { mutableStateOf(false) }
    var selectedFolderUri by rememberSaveable { mutableStateOf<String?>(null) }

    val folderTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { treeUri ->
            selectedFolderUri = treeUri.toString()
            viewModel.setSelectedFolderUri(treeUri.toString())
            // optional: snackbar show
        }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissMessage()
        }
    }

    // Use appropriate theme based on inverted state
    val colorScheme = if (inverted) darkColorScheme() else lightColorScheme()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    actions = {
                        TextButton(onClick = viewModel::save) {
                            Text(stringResource(R.string.save))
                        }
                        TextButton(onClick = { folderTreeLauncher.launch(null) }) {
                            Text(stringResource(R.string.select_folder))
                        }
                        IconButton(onClick = { inverted = !inverted }) {
                            Icon(
                                imageVector = Icons.Default.FormatInvertColor,
                                contentDescription = stringResource(R.string.invert_colors)
                            )
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    listOf(editorTab, previewTab).forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        MaterialTheme(colorScheme = colorScheme) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (selectedTab == 0) {
                    EditorScreen(
                        document = document,
                        onContentChange = viewModel::onContentChange,
                        onTitleChange = viewModel::onTitleChange,
                        inverted = inverted
                    )
                } else {
                    PreviewScreen(document = document, inverted = inverted)
                }
            }
        }
    }
}
