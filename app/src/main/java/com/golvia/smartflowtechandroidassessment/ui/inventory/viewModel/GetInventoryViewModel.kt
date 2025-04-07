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
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()



    fun getInventoryItems() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                inventoryRepository.getInventory().collect { inventoryItems ->
                    _uiState.value = UiState.Success(inventoryItems)
                }
            } catch (e: TimeoutException) {
                _uiState.value = UiState.Error("Request timed out: ${e.message}")
            } catch (e: NoInternetException) {
                _uiState.value = UiState.Error("No internet connection. Please try again.")
            } catch (e: ServerException) {
                _uiState.value = UiState.Error("Server error occurred: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun getInventoryDetailItems(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                inventoryRepository.getDetailInventory(id).collect { inventoryItems ->
                    _uiState.value = UiState.SuccessUpdate(inventoryItems)
                }
            } catch (e: TimeoutException) {
                _uiState.value = UiState.Error("Request timed out: ${e.message}")
            } catch (e: NoInternetException) {
                _uiState.value = UiState.Error("No internet connection. Please try again.")
            } catch (e: ServerException) {
                _uiState.value = UiState.Error("Server error occurred: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun searchInventory(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            try {
                inventoryRepository.searchInventoryByName(query).collect { inventoryItems ->
                    _uiState.value = UiState.Success(inventoryItems)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error occurred during search: ${e.message}")
            }
        }
    }
}
