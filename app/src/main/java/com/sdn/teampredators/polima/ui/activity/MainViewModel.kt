package com.sdn.teampredators.polima.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdn.teampredators.polima.data.dtos.DummyData
import com.sdn.teampredators.polima.data.dtos.User
import com.sdn.teampredators.polima.domain.repo.MainRepo
import com.sdn.teampredators.polima.ui.utils.DataResult
import com.sdn.teampredators.polima.ui.utils.ListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepo
) : ViewModel() {


    private val userUI = MutableLiveData<ListResult<User>>()

    fun getUsers(): LiveData<ListResult<User>> {
        doGetUsers()
        return userUI
    }

    private fun doGetUsers() {
        viewModelScope.launch {
            repo.getUserData().onStart {
                userUI.postValue(ListResult.Loading)
            }
                .catch {
                    userUI.postValue(ListResult.Error(it.cause))
                }
                .collect {
                    userUI.postValue(ListResult.Success(it.data))
                }
        }
    }

}