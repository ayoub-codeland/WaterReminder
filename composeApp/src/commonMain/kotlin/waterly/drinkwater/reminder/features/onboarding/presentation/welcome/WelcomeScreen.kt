package waterly.drinkwater.reminder.features.onboarding.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import waterly.drinkwater.reminder.core.ui.components.AppScaffold
import org.jetbrains.compose.resources.painterResource
import waterreminderapp.composeapp.generated.resources.Res
import waterreminderapp.composeapp.generated.resources.splash_bg

/**
 * Welcome Screen with proper system insets handling
 *
 * Template showing how to use AppScaffold for screens with bottom CTA
 */
@Composable
fun WelcomeScreen(
    onNavigateToProfileSetup: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        SplashBackground()

        AppScaffold(
            modifier = Modifier.fillMaxSize(), bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                        .padding(bottom = 48.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onNavigateToProfileSetup,
                        modifier = Modifier.fillMaxWidth().height(64.dp).shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Begin Your Journey",
                            style = MaterialTheme.typography.titleMedium.copy(
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }) { _ ->
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Hydration, \n")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Evolved.")
                        }
                    }, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Stop just counting cups. Start building sustainable habits backed by behavioral science and your personal data.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 320.dp)
                )
            }
        }
    }
}

@Composable
fun SplashBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.splash_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.3f))
        )

        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF06B6D4).copy(alpha = 0.1f))
        )

        val gradientColor = Color.White
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    0.0f to Color.Transparent,
                    0.6f to gradientColor.copy(alpha = 0.4f),
                    1.0f to gradientColor
                )
            )
        )
    }
}