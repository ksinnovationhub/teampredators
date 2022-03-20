package com.sdn.teampredators.polima.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.Constants.POLITICIAN_COLLECTION
import com.sdn.teampredators.polima.utils.DummyPoliticalData
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _aspirantData = MutableLiveData<HomeStates>()
    val aspirantData: LiveData<HomeStates> = _aspirantData

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    private val politicianResponse = mutableListOf<Politician>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _aspirantData.postValue(HomeStates.Error(throwable))
        Timber.e(throwable)
    }

    init {
        repeat(30) {
            // setData() // todo: uncomment to populate the database with random data
        }
        getData()
    }

    private fun setData() {
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                val politician = DummyPoliticalData.getPolitician()
                db.collection(POLITICIAN_COLLECTION).document(politician.id).set(politician).await()
            }
                .onSuccess { Timber.d("Successfully added dummy data") }
                .onFailure { Timber.e(it) }
        }
    }

    private fun getData() {
        viewModelScope.launch(exceptionHandler) {
            _aspirantData.postValue(HomeStates.Loading)
            kotlin.runCatching {
                db.collection(POLITICIAN_COLLECTION).get().await()
            }.fold(onSuccess = {
                val politicianData = it.toObjects(Politician::class.java)
                politicianResponse.clear()
                politicianResponse.addAll(politicianData)
                _aspirantData.postValue(HomeStates.NonEmpty(politicianData))
            }, onFailure = {
                _aspirantData.postValue(
                    HomeStates.Error(it)
                )
            })
        }
    }

    fun toAspirantTask(item: Politician) {
        _action.value = GenericActions.Navigate(HomeFragmentDirections.toAspirantFragment(item))
    }

    fun searchPolitician(query: String) {
        viewModelScope.launch(exceptionHandler) {
            val searchResult = politicianResponse.filter {
                it.fullName.contains(query, ignoreCase = true)
            }
            if (searchResult.isNotEmpty()) {
                _aspirantData.postValue(HomeStates.NonEmpty(content = searchResult))
            } else {
                _aspirantData.postValue(HomeStates.Empty)
            }
        }
    }
}

sealed class HomeStates {
    data class NonEmpty(val content: List<Politician>) : HomeStates()
    object Empty : HomeStates()
    data class Error(val error: Throwable) : HomeStates()
    object Loading : HomeStates()
}
