package jp.example.markdowneditor

import android.content.Context
import androidx.test.core.context.AndroidXTestContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.example.markdowneditor.data.Document
import jp.example.markdowneditor.data.DocumentRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDocumentRepository {

    @Test
    fun saveAndLoadDocument() = runTest {
        val context = AndroidXTestContext.createForTest()
        val repository = DocumentRepository.getInstance(context)

        val document = Document(title = "Test", content = "# Hello")
        repository.saveDocument(document)

        val loaded = repository.loadDocumentSuspend()
        assertEquals(document, loaded)
    }

    @Test
    fun deleteDocument() = runTest {
        val context = AndroidXTestContext.createForTest()
        val repository = DocumentRepository.getInstance(context)

        val document = Document(title = "DeleteTest", content = "Content")
        repository.saveDocument(document)

        repository.deleteDocument()
        val loaded = repository.loadDocumentSuspend()
        assertEquals(null, loaded)
    }
}
