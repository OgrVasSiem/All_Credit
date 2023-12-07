package com.test.allcredit.ui.destination

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.allcredit.application.network.NetworkStatusTracker
import com.test.allcredit.application.rest.ApiService
import com.test.allcredit.application.rest.Country
import com.test.allcredit.application.rest.MicroloansRepository
import com.test.allcredit.application.rest.StoreInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkStatusTracker: NetworkStatusTracker,
    private val microloansRepository: MicroloansRepository,
    private val microloansRequest: ApiService,
) : ViewModel() {

    private val _storeInfo = MutableStateFlow<List<StoreInfo>?>(null)
    val storeInfo: StateFlow<List<StoreInfo>?> = _storeInfo.asStateFlow()

    private val _activeCountryCode = MutableStateFlow<String>("")
    val activeCountryCode: StateFlow<String> = _activeCountryCode.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>?>(null)
    val countries: StateFlow<List<Country>?> = _countries.asStateFlow()


    init {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                getMortgages()
            }
        }

        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest { updateActiveCountryCode() }
        }

        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                loadCountries()
            }
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            val response = microloansRequest.infoGet()
            _countries.value = response.countries
        }
    }

    private fun updateActiveCountryCode() {
        viewModelScope.launch {
            _activeCountryCode.value = microloansRepository.getActiveCountryCode()
        }
    }

    private fun getMortgages() {
        try {
            viewModelScope.launch { _storeInfo.value = microloansRepository.getStoreInfo() }
        } catch (e: Exception) {
            Log.d("mortgages", "error = $e")
        }
    }

    var activeFilterType by mutableStateOf(FilterType.Rating)
        private set

    fun onActiveFilterChange(activeFilter: FilterType) {
        this.activeFilterType = activeFilter
        _storeInfo.value = when (activeFilter) {
            FilterType.Rating -> storeInfo.value?.sortedByDescending { it.rating }
            FilterType.Rate -> storeInfo.value?.sortedBy { it.rateFrom }
            FilterType.Sum -> storeInfo.value?.sortedByDescending { it.sumTo.toFloat() }
            FilterType.Term -> storeInfo.value?.sortedByDescending { it.termTo.toFloat() }
        }
    }
}