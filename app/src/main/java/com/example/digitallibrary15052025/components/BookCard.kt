package com.logan.digitallibrary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.logan.digitallibrary.data.models.BookModel
import java.io.File

@Composable
fun BookCard(
    book: BookModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.Top, // Выравнивание по верху
        horizontalArrangement = Arrangement.Start, // Элементы начинаются слева
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth() // Занимает всю доступную ширину
            .padding(8.dp) // Добавляем отступы вокруг карточки
    ) {
        // Обложка книги (занимает 30% ширины)
        Card(
            modifier = Modifier
                .width(100.dp)
                .aspectRatio(0.7f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.coverPath?.let { File(it) })
                    .crossfade(true)
                    .build(),
                contentDescription = "Cover of ${book.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Текстовая информация (занимает 70% ширины)
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            book.author?.let {
                Text(
                    text = "Автор: $it",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}