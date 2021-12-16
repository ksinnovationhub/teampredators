package com.sdn.teampredators.polima.data.remote

import com.sdn.teampredators.polima.data.dtos.DummyData
import com.sdn.teampredators.polima.data.dtos.User

interface DummyApi {

    suspend fun getDummyUsers() : DummyData<User>

}