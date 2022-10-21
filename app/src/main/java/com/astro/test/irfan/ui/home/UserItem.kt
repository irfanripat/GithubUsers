package com.astro.test.irfan.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.astro.test.irfan.data.model.User

@Composable
fun UserItem(modifier: Modifier = Modifier, user: User) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatarUrl),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = user.name.orEmpty().ifEmpty { "No Name" },
                style = MaterialTheme.typography.h6
            )
            Text(
                text = user.login,
                style = MaterialTheme.typography.body2
            )
        }
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