package org.haidy.storenotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.haidy.storenotes.app.navigation.Destination
import org.haidy.storenotes.ui.theme.StoreNotesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreNotesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()
                val startDestination =
                    if (state.isLoggedIn) Destination.NOTES else Destination.LOGIN
                StoreNotesApp(startDestination = startDestination)
            }
        }
    }
}

