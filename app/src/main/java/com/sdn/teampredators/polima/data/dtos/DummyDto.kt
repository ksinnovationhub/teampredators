package com.sdn.teampredators.polima.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DummyData<T>(
   @SerialName("data")
   val data : List<T>? = null
)

@Serializable
data class User(
   @SerialName("id")
   val id : String? = null,

   @SerialName("title")
   val title: String? = null,

   @SerialName("firstName")
   val firstName: String? = null,

   @SerialName("lastName")
   val lastName: String? = null
)
