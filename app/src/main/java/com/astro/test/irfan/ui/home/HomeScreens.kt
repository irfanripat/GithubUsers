package com.astro.test.irfan.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astro.test.irfan.SearchTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.astro.test.irfan.R
import com.astro.test.irfan.data.model.User

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
            ) {
                if (uiState is HomeUiState.HasUsers) {
                    items(uiState.users) { user ->
                        UserItem(user = user)
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(modifier: Modifier = Modifier, user: User) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatarUrl),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, Color.Gray, CircleShape)   // add a border (optional)
        )
        Text(
            text = user.name ?: "",
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun UserItemPreview() {
    UserItem(
        modifier = Modifier.fillMaxWidth(),
        user = User(
            name = "Irfan",
            id = 1,
            login = "irfanripat",
            avatarUrl = "https://avatars.githubusercontent.com/u/16873751?v=4"
        )
    )
}