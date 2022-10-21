package com.astro.test.irfan.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.astro.test.irfan.SearchTextField
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ScreenSetup(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(uiState = uiState, onQueryChanged = viewModel::updateSearchQuery)
}
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onQueryChanged: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            SearchTextField(onSearchInputChanged = onQueryChanged)
            LazyColumn(modifier = Modifier.fillMaxSize().background(Color.Cyan)) {
                if (uiState is HomeUiState.HasUsers) {
                    Log.d("TAG", "HomeScreen: ${uiState.users}")
                    items(uiState.users) { user ->
                        user.name?.let { Text(text = it) }
                    }
                }
            }
        }
    }
}