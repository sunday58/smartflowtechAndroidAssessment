package com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golvia.smartflowtechandroidassessment.data.InventoryRequest
import com.golvia.smartflowtechandroidassessment.repo.InventoryRepository
import com.golvia.smartflowtechandroidassessment.repo.NoInternetException
import com.golvia.smartflowtechandroidassessment.repo.ServerException
import com.golvia.smartflowtechandroidassessment.repo.TimeoutException
import com.golvia.smartflowtechandroidassessment.ui.inventory.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * davidsunday
 */

@HiltViewModel
class DeleteInventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Default)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _message = MutableStateFlow(false)
    val message: StateFlow<Boolean> = _message.asStateFlow()


    fun deleteInventoryItems(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val deleted = inventoryRepository.deleteInventory(id)
                _uiState.value = UiState.NoBody(deleted ?: false)
                _message.value = true
            } catch (e: TimeoutException) {
                _message.value = false
                _uiState.value = UiState.Error("Request timed out: ${e.message}")
            } catch (e: NoInternetException) {
                _message.value = false
                _uiState.value = UiState.Error("No internet connection. Please try again.")
            } catch (e: ServerException) {
                _message.value = false
                _uiState.value = UiState.Error("Server error occurred: ${e.message}")
            } catch (e: Exception) {
                _message.value = false
                // Fallback for anything else not caught above
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}