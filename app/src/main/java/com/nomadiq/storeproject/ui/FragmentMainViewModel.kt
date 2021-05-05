package com.nomadiq.storeproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nomadiq.storeproject.ui.character.BreakingBadCharacter
import kotlinx.coroutines.*

enum class ApiStatus { LOADING, ERROR, DONE }

class FragmentMainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of #Movie
    // with new values
    private val _characters = MutableLiveData<List<BreakingBadCharacter>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val characters: LiveData<List<BreakingBadCharacter>>
        get() = _characters

    // The external LiveData interface to the property is immutable, so only this class can modify
    private var _character = MutableLiveData<BreakingBadCharacter>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val character: LiveData<BreakingBadCharacter>
        get() = _character

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedCharacter = MutableLiveData<Int>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedCharacter: LiveData<Int>
        get() = _navigateToSelectedCharacter

    /**
     * Call getMovies() on init so we can display status immediately.
     */
    init {
        getCharacters()
//        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    // Character selection triggered
    fun onCharacterClicked(characterId: Int) {
        _navigateToSelectedCharacter.value = characterId
    }

    // Character selection reset
    fun onCharacterNavigated() {
        _navigateToSelectedCharacter.value = null
    }

    /**
     * Gets movie information from API Retrofit service and updates the [character] [List] and [ApiStatus] [LiveData].
     * The Retrofit service returns a coroutine Deferred, which we await to get the result of the transaction.
     * @param filter the [ApiFilter] that is sent as part of the web server request
     */
    private fun getCharacters() {
        coroutineScope.launch {

            val receivedCharactersList =
                BreakingBadApi.retrofitService.getCharactersAsync()
            try {
                _response.value = "Success: $receivedCharactersList movies retrieved"
                if (receivedCharactersList.isNotEmpty()) {
                    _characters.value = receivedCharactersList
                }
            } catch (t: Throwable) {
                _response.value = "Failure: " + t.message
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


