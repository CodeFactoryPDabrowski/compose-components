package com.przeman.sample

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.przeman.sample.di.ActivityKey
import com.przeman.sample.di.AppScope
import com.przeman.sample.navigation.ComponentsNavHost
import com.przeman.sample.theme.SampleTheme
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class, boundType = Activity::class)
@ActivityKey(MainActivity::class)
class MainActivity @Inject constructor(
    private val viewModelProviderFactory: ViewModelProvider.Factory,
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SampleTheme {
                val navController = rememberNavController()
                ComponentsNavHost(navController = navController)
            }
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return viewModelProviderFactory
    }
}
