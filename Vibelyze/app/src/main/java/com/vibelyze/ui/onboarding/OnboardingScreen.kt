package com.vibelyze.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.vibelyze.R
import kotlinx.coroutines.launch



@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage("🎵 Descubre música según tu estado emocional", R.drawable.ic_launcher_foreground),
        OnboardingPage("⭐ Califica canciones y mejora tus recomendaciones", R.drawable.ic_launcher_foreground),
        OnboardingPage("📂 Crea playlists para cada estado de ánimo", R.drawable.ic_launcher_foreground)
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F2F)) // Fondo azul oscuro
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = Color.White,
            inactiveColor = Color.LightGray
        )

        Button(
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5C7CFA),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = if (pagerState.currentPage == pages.lastIndex) "Comenzar" else "Siguiente")
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(64.dp)
            )

            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp)
            )

            Text(
                text = page.title,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
