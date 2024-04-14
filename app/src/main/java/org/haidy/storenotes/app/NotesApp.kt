package org.haidy.storenotes.app

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.HiltAndroidApp
import org.haidy.storenotes.app.navigation.AppNavGraph

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }
@Composable
fun StoreNotesApp(startDestination: String){
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController){
        AppNavGraph(startDestination)
    }
}
@HiltAndroidApp
class NotesApp: Application()