package com.godzuche.recircu.feature.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.godzuche.recircu.core.designsystem.components.GradientBackground
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.godzuche.recircu.core.designsystem.theme.RecircuTheme

@Composable
fun GetStartedRoute(
    navigateToAuthentication: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    GettingStartedScreen(
        modifier = modifier,
        navigateToAuthentication = {
            onboardingViewModel.saveOnboardingState(isOnboardingCompleted = true)
            navigateToAuthentication.invoke()
        }
    )
}

@Composable
fun GettingStartedScreen(
    navigateToAuthentication: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(com.godzuche.recircu.R.drawable.landfill),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Crop
            )

            GradientBackground(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val (msgTxt, btn) = createRefs()
                    val bottomGuideline = createGuidelineFromTop(0.69f)
                    Text(
                        text = "Manage your\n" +
                                "waste properly\n" +
                                "and get paid",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.constrainAs(msgTxt) {
                            centerTo(parent)
                        }
                    )
                    RecircuButton(
                        label = "Get Started",
                        onClick = navigateToAuthentication,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .constrainAs(btn) {
                                top.linkTo(bottomGuideline)
                                centerHorizontallyTo(parent)
                            },
                        containerColor = Color.White,
                        contentColor = Color(0xFF00801C)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecircuTheme {
        GetStartedRoute(navigateToAuthentication = {})
    }
}