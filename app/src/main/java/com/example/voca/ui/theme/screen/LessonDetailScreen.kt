package com.example.voca.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.voca.model.LessonItem

@Composable
fun LessonDetailScreen(lessonId: String, navController: NavController) {
    // Dynamic content based on lesson type
    val lesson = getLessonContent(lessonId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson.title) },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                elevation = 8.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 4.dp,
                backgroundColor = Color(0xFF6200EE).copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF6200EE)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.description,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = 0.0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Text(
                        text = "Points: ${lesson.points}",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            ContentSection(
                title = "Key Concepts",
                content = getKeyConcepts(lessonId),
                backgroundColor = Color(0xFFF3E5F5),
                textColor = Color(0xFF4A148C)
            )

            ContentSection(
                title = "Practice Tips",
                content = getPracticeTips(lessonId),
                backgroundColor = Color(0xFFE8F5E9),
                textColor = Color(0xFF1B5E20)
            )

            ContentSection(
                title = "Examples",
                content = getExamples(lessonId),
                backgroundColor = Color(0xFFFFF9C4),
                textColor = Color(0xFFF57F17)
            )

            ContentSection(
                title = "Resources",
                content = getResources(lessonId),
                backgroundColor = Color(0xFFE3F2FD),
                textColor = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle completion */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
            ) {
                Text(
                    "Start Learning",
                    color = Color.White,
                    style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun ContentSection(
    title: String,
    content: String,
    backgroundColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

// Dynamic content functions
private fun getLessonContent(lessonId: String): LessonItem {
    return when (lessonId) {
        "pronunciation" -> LessonItem(
            title = "Pronunciation Essentials",
            id = "pronunciation",
            points = 100,
            description = "Master English pronunciation with expert guidance and practical exercises."
        )
        "conversation" -> LessonItem(
            title = "Conversational Fluency",
            id = "conversation",
            points = 150,
            description = "Develop natural conversation skills through real-world practice and scenarios."
        )
        "accent" -> LessonItem(
            title = "Accent Reduction",
            id = "accent",
            points = 200,
            description = "Learn techniques to neutralize your accent and speak clearly."
        )
        "idioms" -> LessonItem(
            title = "Idiomatic Expressions",
            id = "idioms",
            points = 250,
            description = "Master common English idioms and sound more natural."
        )
        "public_speaking" -> LessonItem(
            title = "Public Speaking",
            id = "public_speaking",
            points = 300,
            description = "Build confidence and master public speaking techniques."
        )
        else -> LessonItem(
            title = "Unknown Lesson",
            id = lessonId,
            points = 0,
            description = "Lesson details unavailable"
        )
    }
}

private fun getKeyConcepts(lessonId: String): String {
    return when (lessonId) {
        "pronunciation" -> "• Understanding phonetics\n• Vowel and consonant sounds\n• Stress and intonation patterns\n• Common pronunciation challenges"
        "conversation" -> "• Turn-taking in conversations\n• Active listening skills\n• Responding appropriately\n• Natural conversation flow"
        "accent" -> "• Sound placement\n• Rhythm and timing\n• Stress patterns\n• Voice modulation techniques"
        "idioms" -> "• Common English idioms\n• Context and usage\n• Cultural significance\n• Practice scenarios"
        "public_speaking" -> "• Audience engagement\n• Speech structure\n• Body language\n• Voice projection"
        else -> "Key concepts unavailable"
    }
}

private fun getPracticeTips(lessonId: String): String {
    return when (lessonId) {
        "pronunciation" -> "• Practice with tongue twisters\n• Record and listen to yourself\n• Shadow native speakers\n• Use pronunciation apps"
        "conversation" -> "• Join conversation groups\n• Practice with language partners\n• Watch English movies\n• Listen to podcasts"
        "accent" -> "• Mirror practice\n• Recording analysis\n• Slow speech exercises\n• Accent reduction drills"
        "idioms" -> "• Daily idiom practice\n• Context-based learning\n• Real-world usage\n• Writing exercises"
        "public_speaking" -> "• Regular rehearsal\n• Video recording\n• Small group practice\n• Breathing exercises"
        else -> "Practice tips unavailable"
    }
}

private fun getExamples(lessonId: String): String {
    return when (lessonId) {
        "pronunciation" -> "1. 'Think' vs 'Sink' - TH sound practice\n2. Rising/falling intonation in questions\n3. Word stress in compound nouns"
        "conversation" -> "1. Greeting and small talk\n2. Discussing current events\n3. Expressing opinions politely"
        "accent" -> "1. American vs British pronunciations\n2. Connecting words in speech\n3. Reducing strong accents"
        "idioms" -> "1. 'Break a leg' - Good luck\n2. 'Hit the books' - Study hard\n3. 'Piece of cake' - Very easy"
        "public_speaking" -> "1. Introduction techniques\n2. Handling Q&A sessions\n3. Engaging opening lines"
        else -> "Examples unavailable"
    }
}

private fun getResources(lessonId: String): String {
    return when (lessonId) {
        "pronunciation" -> "• IPA Chart\n• Pronunciation apps\n• Online dictionaries\n• Speech therapy tools"
        "conversation" -> "• Language exchange apps\n• Conversation forums\n• Practice groups\n• English podcasts"
        "accent" -> "• Accent reduction software\n• Speech analysis tools\n• Practice recordings\n• Expert tutorials"
        "idioms" -> "• Idiom dictionaries\n• Usage guides\n• Practice exercises\n• Cultural context guides"
        "public_speaking" -> "• Presentation tools\n• Speech writing guides\n• Body language videos\n• Voice training resources"
        else -> "Resources unavailable"
    }
}