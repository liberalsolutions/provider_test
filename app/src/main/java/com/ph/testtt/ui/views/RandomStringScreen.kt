package com.ph.testtt.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RandomStringScreen(modifier: Modifier) {
    val viewModel: RandomStringViewModel = hiltViewModel()
    val strings by viewModel.strings.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var maxLength by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val isLoading = remember {
        mutableStateOf(true)
    }
    Box(modifier = modifier.padding(16.dp)){
        if (uiState is UiState.Loading) {
            Box (
                Modifier.fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }  // Show loader when fetching
        }

        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = maxLength,
                onValueChange = {
                    maxLength = it
                    isError = it.isBlank() || it.toIntOrNull() == null || it.toInt() <= 0
                    errorMessage = when {
                        it.isBlank() -> "Please enter a number"
                        it.toIntOrNull() == null -> "Only numbers are allowed"
                        it.toInt() <= 0 -> "Number must be greater than 0"
                        else -> ""
                    }
                },
                isError = isError,
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } // Hide keyboard on Done action
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Max Length") },
                supportingText = { if (isError) Text(errorMessage, color = Color.Red) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            Button(
                onClick = {
                    if (!isError && maxLength.isNotEmpty()) {
                        //                    viewModel.fetchRandomString(maxLength.toInt())
                        viewModel.fetchRandomString(maxLength.toIntOrNull() ?: 1)
                    } else {
                        isError = true
                        errorMessage = "Please enter a number"
                        focusRequester.requestFocus()

                    }

                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Generate Random String")
            }

            if (strings.isNotEmpty()) {
                Button(
                    onClick = { viewModel.deleteAll() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Delete All")
                }
            }

            when (uiState) {
                is UiState.Loading -> {
                    isLoading.value = true
                }

                is UiState.Error -> {
                    Text("Error: ${(uiState as UiState.Error).message}")
                    isLoading.value = false
                }

                else -> {
                    isLoading.value = true
                }
            }

            if (uiState !is UiState.Loading) LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(strings.size) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { viewModel.deleteString(strings[item].id) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "randomText: ${strings[item].value}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "Length: ${strings[item].length}")
                            Text(text = "Created: ${strings[item].created}")
                        }
                    }
                }
            }


        }


    }
}

