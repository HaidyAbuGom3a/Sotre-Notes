package org.haidy.storenotes.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.haidy.storenotes.app.LocalNavController

@Composable
fun <Effect> EffectHandler(
    effects: Flow<Effect>,
    onEffect: (Effect, NavController) -> Unit
) {
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    LaunchedEffect(key1 = effects) {
        scope.launch {
            effects.collectLatest { effect ->
                onEffect(effect, navController)
            }
        }
    }
}