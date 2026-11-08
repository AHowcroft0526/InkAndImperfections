package com.inkandimperfections.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject

data class JournalEntry(val id: Long, val title: String, val body: String, val date: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { InkApp(this) }
    }
}

private val paper = Color(0xFFF5EFE6)
private val ink = Color(0xFF302A2A)
private val plum = Color(0xFF70536B)
private val sage = Color(0xFF82917A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InkApp(context: Context) {
    var entries by remember { mutableStateOf(loadEntries(context)) }
    var showEditor by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<JournalEntry?>(null) }
    var selectedPreset by remember { mutableStateOf("Bohemian") }

    fun save(updated: List<JournalEntry>) {
        entries = updated
        persistEntries(context, updated)
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = plum,
            secondary = sage,
            background = paper,
            surface = Color(0xFFFFFCF7),
            onBackground = ink,
            onSurface = ink
        )
    ) {
        Scaffold(
            containerColor = paper,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Ink & Imperfections", fontWeight = FontWeight.Bold, fontSize = 21.sp)
                            Text("a little space to be wonderfully you", fontSize = 11.sp, fontStyle = FontStyle.Italic)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = paper)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { editing = null; showEditor = true },
                    containerColor = plum,
                    contentColor = Color.White
                ) { Icon(Icons.Default.Add, contentDescription = "New journal entry") }
            }
        ) { padding ->
            Column(Modifier.padding(padding).fillMaxSize().padding(horizontal = 18.dp)) {
                Spacer(Modifier.height(8.dp))
                Text("YOUR JOURNAL", color = plum, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("Every page can be a fresh start.", color = ink, fontSize = 14.sp)
                Spacer(Modifier.height(14.dp))
                Text("Choose a moodboard", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf("Bohemian", "Gothic", "Celestial", "Cottagecore").forEach { preset ->
                        FilterChip(
                            selected = selectedPreset == preset,
                            onClick = { selectedPreset = preset },
                            label = { Text(preset, fontSize = 10.sp) }
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                if (entries.isEmpty()) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✦", color = plum, fontSize = 38.sp)
                            Text("Your story starts here", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Tap + to write your first entry.", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 90.dp)
                    ) {
                        items(entries, key = { it.id }) { entry ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable { editing = entry; showEditor = true },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFCF7)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(entry.title.ifBlank { "Untitled page" }, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                        Text(entry.date, color = plum, fontSize = 11.sp)
                                        Spacer(Modifier.height(5.dp))
                                        Text(entry.body, maxLines = 2, color = Color.DarkGray, fontSize = 13.sp)
                                    }
                                    IconButton(onClick = { save(entries.filterNot { it.id == entry.id }) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showEditor) {
            var title by remember(editing?.id) { mutableStateOf(editing?.title ?: "") }
            var body by remember(editing?.id) { mutableStateOf(editing?.body ?: "") }
            AlertDialog(
                onDismissRequest = { showEditor = false },
                title = { Text(if (editing == null) "A new page" else "Edit your page") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Page title") }, singleLine = true)
                        OutlinedTextField(value = body, onValueChange = { body = it }, label = { Text("Let it out...") }, minLines = 5)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val date = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                        val item = JournalEntry(editing?.id ?: System.currentTimeMillis(), title, body, date)
                        val updated = if (editing == null) listOf(item) + entries else entries.map { if (it.id == item.id) item else it }
                        save(updated)
                        showEditor = false
                    }) { Text("Save page") }
                },
                dismissButton = { TextButton(onClick = { showEditor = false }) { Text("Cancel") } }
            )
        }
    }
}

private fun loadEntries(context: Context): List<JournalEntry> {
    return try {
        val raw = context.getSharedPreferences("journal", Context.MODE_PRIVATE).getString("entries", "[]") ?: "[]"
        val array = JSONArray(raw)
        (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            JournalEntry(o.getLong("id"), o.getString("title"), o.getString("body"), o.getString("date"))
        }
    } catch (_: Exception) { emptyList() }
}

private fun persistEntries(context: Context, entries: List<JournalEntry>) {
    val array = JSONArray()
    entries.forEach {
        array.put(JSONObject().put("id", it.id).put("title", it.title).put("body", it.body).put("date", it.date))
    }
    context.getSharedPreferences("journal", Context.MODE_PRIVATE).edit().putString("entries", array.toString()).apply()
}
