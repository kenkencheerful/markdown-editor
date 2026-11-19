package jp.example.markdowneditor.ui

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import jp.example.markdowneditor.R
import jp.example.markdowneditor.data.Document

@Composable
fun PreviewScreen(
    document: Document,
    inverted: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val markwon = remember(context) {
        Markwon.builder(context)
            .usePlugin(TablePlugin.create(context))
            .build()
    }

    if (document.content.isBlank()) {
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.preview_empty))
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { viewContext ->
                    TextView(viewContext).apply {
                        textSize = 18f
                        setTextColor(textColor)
                        setTextIsSelectable(true)
                    }
                },
                update = { textView ->
                    textView.setTextColor(textColor)
                    markwon.setMarkdown(textView, document.content)
                }
            )
        }
    }
}
