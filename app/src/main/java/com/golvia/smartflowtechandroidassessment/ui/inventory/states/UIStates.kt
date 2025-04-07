package com.golvia.smartflowtechandroidassessment.ui.inventory.states

import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem

/**
 * davidsunday
 */

sealed class UiState {
    object Default : UiState()
    object Loading : UiState()
    data class NoBody(val message: Boolean) : UiState()
    data class Success(val data: List<InventoryResponseItem>?) : UiState()
    data class SuccessUpdate(val data: InventoryResponseItem?) : UiState()
    data class Error(val message: String) : UiState()
}