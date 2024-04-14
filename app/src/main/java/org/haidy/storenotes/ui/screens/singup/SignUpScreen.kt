package org.haidy.storenotes.ui.screens.singup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.haidy.storenotes.R
import org.haidy.storenotes.ui.composable.StoreNotesButton
import org.haidy.storenotes.ui.composable.StoreNotesTextField
import org.haidy.storenotes.ui.screens.login.navigateToLogin
import org.haidy.storenotes.ui.theme.Typography
import org.haidy.storenotes.ui.theme.White
import org.haidy.storenotes.ui.utils.EffectHandler

@Composable
fun SignUpScreen() {
    val viewModel: SignUpViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    EffectHandler(effects = viewModel.effect) { effect, navController ->
        when(effect){
            SignUpUiEffect.NavigateToLogin -> navController.navigateToLogin(true)
            is SignUpUiEffect.ShowErrorMessage -> {
                scope.launch {
                    snackbarState.showSnackbar(effect.message)
                }
            }
            SignUpUiEffect.ShowSuccessMessage -> {
                scope.launch {
                    snackbarState.showSnackbar("Sign up success")
                }

            }
        }
    }
    SignUpContent(state, snackbarState, viewModel)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignUpContent(state: SignUpUiState, snackbarState: SnackbarHostState,listener: SignUpInteractionListener) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) {
                Snackbar(
                    snackbarData = it,
                    contentColor = White
                )
            }
        },
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(White), verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Sign Up",
                style = Typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 96.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            StoreNotesTextField(
                onValueChange = { listener.onEmailChanged(it) },
                text = state.email,
                hint = "Email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
            val eyeIcon =
                if (state.isPasswordVisible) R.drawable.disabled_eye_icon else R.drawable.eye_icon
            StoreNotesTextField(
                onValueChange = { listener.onPasswordChanged(it) },
                text = state.password,
                hint = "Password",
                showPassword = state.isPasswordVisible,
                keyboardType = KeyboardType.Password,
                onTrailingIconClick = { listener.onClickEyeIcon() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                trailingPainter = painterResource(id = eyeIcon)
            )

            StoreNotesButton(
                onClick = { listener.onClickSignUp() },
                text = "Sign Up",
                modifier = Modifier.padding(horizontal = 16.dp),
                isLoading = state.isLoading
            )

        }
    }
}