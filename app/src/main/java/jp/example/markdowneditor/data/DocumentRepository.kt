package jp.example.markdowneditor.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "markdown_editor_datastore"
private val DOCUMENT_ID = stringPreferencesKey("document_id")
private val DOCUMENT_TITLE = stringPreferencesKey("document_title")
private val DOCUMENT_CONTENT = stringPreferencesKey("document_content")
private val DOCUMENT_CREATED_AT = longPreferencesKey("document_created_at")
private val DOCUMENT_UPDATED_AT = longPreferencesKey("document_updated_at")

private val Context.markdownEditorDataStore by preferencesDataStore(name = DATASTORE_NAME)

class DocumentRepository(context: Context) {
    private val dataStore = context.applicationContext.markdownEditorDataStore

    suspend fun saveDocument(document: Document) {
        dataStore.edit { preferences ->
            preferences[DOCUMENT_ID] = document.id
            preferences[DOCUMENT_TITLE] = document.title
            preferences[DOCUMENT_CONTENT] = document.content
            preferences[DOCUMENT_CREATED_AT] = document.createdAt
            preferences[DOCUMENT_UPDATED_AT] = document.updatedAt
        }
    }

    fun loadDocument(): Flow<Document?> = dataStore.data.map { preferences ->
        val content = preferences[DOCUMENT_CONTENT] ?: return@map null
        Document(
            id = preferences[DOCUMENT_ID] ?: "",
            title = preferences[DOCUMENT_TITLE] ?: "",
            content = content,
            createdAt = preferences[DOCUMENT_CREATED_AT] ?: 0L,
            updatedAt = preferences[DOCUMENT_UPDATED_AT] ?: 0L
        )
    }

    suspend fun deleteDocument() {
        dataStore.edit { preferences ->
            preferences.remove(DOCUMENT_ID)
            preferences.remove(DOCUMENT_TITLE)
            preferences.remove(DOCUMENT_CONTENT)
            preferences.remove(DOCUMENT_CREATED_AT)
            preferences.remove(DOCUMENT_UPDATED_AT)
        }
    }
}
