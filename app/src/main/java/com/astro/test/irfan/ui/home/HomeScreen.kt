package com.astro.test.irfan.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astro.test.irfan.R

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            HomeTopBar()
        },
    ) {
        HomeScreenBody(uiState = uiState, onQueryChanged = viewModel::searchUser)
    }
}

@Composable
fun HomeTopBar() {
    TopAppBar {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun HomeScreenBody(
    uiState: HomeUiState,
    onQueryChanged: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            SearchTextField(onSearchInputChanged = onQueryChanged)
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState) {
                    is HomeUiState.HasUsers -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.users) { user ->
                                UserItem(user = user)
                            }
                        }
                    }
                    is HomeUiState.NoUsers -> {
                        Text(
                            text = if (uiState.isLoading) stringResource(R.string.text_loading)
                            else if (uiState.isNoResult) stringResource(R.string.text_no_user_found)
                            else "",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}