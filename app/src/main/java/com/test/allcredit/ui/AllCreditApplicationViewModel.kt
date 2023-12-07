package com.test.allcredit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.allcredit.dataStore.FirstStartDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCreditApplicationViewModel @Inject constructor(
    private val firstStartDataStore: FirstStartDataStore
) : ViewModel() {

    val isFirstStart = firstStartDataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun completeFirstStartProcess() {
        viewModelScope.launch {
            firstStartDataStore.updateData { false }
        }
    }
}
