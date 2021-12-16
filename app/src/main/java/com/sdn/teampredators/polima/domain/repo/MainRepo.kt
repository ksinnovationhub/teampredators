package com.sdn.teampredators.polima.domain.repo

import com.sdn.teampredators.polima.data.remote.DummyApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val dummyApi: DummyApi
) {
    fun getUserData() = flow {
        val data = dummyApi.getDummyUsers()
        emit(data)
    }
}