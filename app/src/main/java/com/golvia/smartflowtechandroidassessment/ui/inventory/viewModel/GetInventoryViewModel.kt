package com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class GetInventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    fun getInventoryItems() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val inventory = inventoryRepository.getInventory()
                _uiState.value = UiState.Success(inventory)
            } catch (e: TimeoutException) {
                _uiState.value = UiState.Error("Request timed out: ${e.message}")
            } catch (e: NoInternetException) {
                _uiState.value = UiState.Error("No internet connection. Please try again.")
            } catch (e: ServerException) {
                _uiState.value = UiState.Error("Server error occurred: ${e.message}")
            } catch (e: Exception) {
                // Fallback for anything else not caught above
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}