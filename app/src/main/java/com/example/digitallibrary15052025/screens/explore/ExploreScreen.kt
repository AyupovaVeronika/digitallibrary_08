package com.logan.digitallibrary.ui.screens.explore

import android.R.attr.fontFamily
import android.R.attr.fontWeight
import android.R.attr.text
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.logan.digitallibrary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "Unknown"

            viewModel.addBookFromUri(uri, fileName)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "импорт",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily(Font(R.font.montserrat_bold_bold)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            ))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                ),
                modifier = Modifier.height(56.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Фоновое изображение (используйте R.drawable.your_background)
            Image(
                painter = painterResource(id = R.drawable.background), // Убедитесь что файл background.jpg/png есть в res/drawable
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error?.let { error ->
                    AlertDialog(
                        onDismissRequest = { viewModel.clearMessages() },
                        title = { Text("Error") },
                        text = { Text(error) },
                        confirmButton = {
                            TextButton(onClick = { viewModel.clearMessages() }) {
                                Text("OK")
                            }
                        }
                    )
                }

                uiState.successMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = { viewModel.clearMessages() },
                        title = { Text("Success") },
                        text = { Text(message) },
                        confirmButton = {
                            TextButton(onClick = { viewModel.clearMessages() }) {
                                Text("OK")
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    /*Text(
                        text = "Добавляйте книги в формате EPUB в свою библиотеку",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White // Добавлен цвет текста для лучшей читаемости
                    )

                     */
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = {
                            launcher.launch(arrayOf("application/epub+zip"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                        Text("Выберите файл ")
                    }

                }
            }
        }
    }
}