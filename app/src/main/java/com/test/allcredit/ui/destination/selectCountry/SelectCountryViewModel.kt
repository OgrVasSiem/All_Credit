package com.test.allcredit.ui.destination.selectCountry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.allcredit.dataStore.CountryDataStore
import com.test.allcredit.dataStore.FirstStartDataStore
import com.test.allcredit.application.rest.ApiService
import com.test.allcredit.application.rest.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCountryViewModel @Inject constructor(
    private val countryDataStore: CountryDataStore,
    private val microloansRequest: ApiService,
    private val firstStartDataStore: FirstStartDataStore
) : ViewModel() {
    private var _country = MutableStateFlow<List<Country>?>(null)
    val country = _country.asStateFlow()

    init {
        loadCountries()
    }

    val activeCountry: StateFlow<String> = countryDataStore.activeCountry.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        "RU"
    )

    fun setActiveCountry(code: String) {
        viewModelScope.launch {
            countryDataStore.setActiveCountry(code)
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            val response = microloansRequest.infoGet()
            _country.value = response.countries
        }
    }

    fun completeFirstStartProcess() {
        viewModelScope.launch {
            firstStartDataStore.updateData { false }
        }
    }
}