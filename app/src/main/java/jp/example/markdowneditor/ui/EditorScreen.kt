package jp.example.markdowneditor.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.example.markdowneditor.R
import jp.example.markdowneditor.data.Document
import jp.example.markdowneditor.editor.FormatCommand
import jp.example.markdowneditor.editor.MarkdownFormatting

@Composable
fun EditorScreen(
    document: Document,
    onContentChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    inverted: Boolean = false,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember(document.id) {
        mutableStateOf(TextFieldValue(document.content, TextRange(document.content.length)))
    }

    LaunchedEffect(document.content) {
        if (document.content != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = document.content,
                selection = TextRange(document.content.length)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = document.title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.document_title)) },
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FormatCommand.entries.forEach { command ->
                TextButton(
                    onClick = {
                        val result = MarkdownFormatting.apply(
                            content = textFieldValue.text,
                            command = command,
                            selectionStart = textFieldValue.selection.start,
                            selectionEnd = textFieldValue.selection.end
                        )
                        textFieldValue = TextFieldValue(
                            text = result.content,
                            selection = TextRange(result.selectionStart, result.selectionEnd)
                        )
                        onContentChange(result.content)
                    }
                ) {
                    Text(
                        text = when (command) {
                            FormatCommand.Bold -> stringResource(R.string.format_bold)
                            FormatCommand.Italic -> stringResource(R.string.format_italic)
                            FormatCommand.Paragraph -> stringResource(R.string.format_paragraph)
                            FormatCommand.Table -> stringResource(R.string.format_table)
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onContentChange(it.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text(stringResource(R.string.editor_hint)) },
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
    }
}
