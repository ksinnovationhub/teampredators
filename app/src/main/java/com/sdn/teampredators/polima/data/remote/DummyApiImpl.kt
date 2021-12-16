package com.sdn.teampredators.polima.data.remote

import com.sdn.teampredators.polima.data.dtos.DummyData
import com.sdn.teampredators.polima.data.dtos.User
import com.sdn.teampredators.polima.ui.utils.Constants
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class DummyApiImpl @Inject constructor(
    private val client: HttpClient
): DummyApi {
    override suspend fun getDummyUsers(): DummyData<User> {
        return client.request(Constants.BASE_URL){
            method = HttpMethod.Get
        }
    }
}