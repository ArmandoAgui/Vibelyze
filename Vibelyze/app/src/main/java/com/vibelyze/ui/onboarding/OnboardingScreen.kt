package com.vibelyze.ui.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import com.vibelyze.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage("🎵 Descubre música según tu estado emocional", R.drawable.logo),
        OnboardingPage("⭐ Califica canciones y mejora tus recomendaciones", R.drawable.logo),
        OnboardingPage("📂 Crea playlists para cada estado de ánimo", R.drawable.logo)
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    // Animación de pulso
    val transition = rememberInfiniteTransition()
    val noteScale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo degradado
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1B1F3B), Color(0xFF0B0F2F))
                    )
                )
        )

        // Notas musicales rodeando el mensaje
        val noteSize = 100.dp
        val offsets = listOf(
            Pair((-120).dp, (-140).dp),
            Pair(120.dp, (-140).dp),
            Pair((-160).dp, 0.dp),
            Pair(160.dp, 0.dp),
            Pair(0.dp, 160.dp)
        )
        offsets.forEach { (dx, dy) ->
            Image(
                painter = painterResource(R.drawable.nota1),
                contentDescription = null,
                modifier = Modifier
                    .size(noteSize)
                    .scale(noteScale)
                    .align(Alignment.Center)
                    .offset(x = dx, y = dy)
                    .zIndex(0f)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .zIndex(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo central con fondo y borde
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .scale(noteScale)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF5C7CFA).copy(alpha = 0.4f), Color.Transparent),
                                    center = Offset(100f, 100f),
                                    radius = 100f
                                ),
                                CircleShape
                            )
                            .border(4.dp, Color(0xFF5C7CFA), CircleShape)
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(pages[page].imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = pages[page].title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            HorizontalPagerIndicator(
                pagerState     = pagerState,
                activeColor    = Color.White,
                inactiveColor  = Color.Gray,
                indicatorWidth = 12.dp,
                spacing        = 8.dp,
                modifier       = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Button(
                onClick = {
                    if (pagerState.currentPage == pages.lastIndex) onFinish()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, CircleShape),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5C7CFA),
                    contentColor   = Color.White
                )
            ) {
                Icon(
                    imageVector = if (pagerState.currentPage == pages.lastIndex)
                        Icons.Default.Check else Icons.Default.ArrowForward,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (pagerState.currentPage == pages.lastIndex) "Comenzar" else "Siguiente"
                )
            }
        }
    }
}
