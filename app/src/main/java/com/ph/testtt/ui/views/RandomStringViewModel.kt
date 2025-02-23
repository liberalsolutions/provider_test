package com.ph.testtt.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ph.testtt.data.provider.RandomStringProvider
import com.ph.testtt.data.repository.RandomStringRepository
import com.ph.testtt.data.room.RandomStringEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomStringViewModel @Inject constructor(
    private val repository: RandomStringRepository,
    private val randomStringProvider: RandomStringProvider
) : ViewModel() {


    val strings: StateFlow<List<RandomStringEntity>> = repository.allStrings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchRandomString(maxLength: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {

                val result = randomStringProvider.fetchRandomString(maxLength)
                result?.let {
                    repository.insert(it)
                    _uiState.value = UiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to fetch string")
            }
        }
    }

    fun deleteString(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
}
