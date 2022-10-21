package com.astro.test.irfan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.astro.test.irfan.ui.home.HomeScreen
import com.astro.test.irfan.ui.theme.GithubUsersTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubUsersTheme {
                HomeScreen()
            }
        }
    }
}