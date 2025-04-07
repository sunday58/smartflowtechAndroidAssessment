package com.golvia.smartflowtechandroidassessment.ui.inventory.states

import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem

/**
 * davidsunday
 */

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<InventoryResponseItem>?) : UiState()
    data class SuccessUpdate(val data: InventoryResponseItem?) : UiState()
    data class Error(val message: String) : UiState()
}