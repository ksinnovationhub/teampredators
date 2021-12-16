package com.sdn.teampredators.polima.domain.di

import com.sdn.teampredators.polima.ui.utils.Constants
import com.sdn.teampredators.polima.ui.utils.Constants.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorEngineModule {

    @Singleton
    @Provides
    fun provideLogger(): Logger {
        return object : Logger {
            override fun log(message: String) {
                Timber.v("Logger Ktor =>", message)
            }
        }
    }

    @Singleton
    @Provides
    fun provideKotlinxSerializer(): KotlinxSerializer {
        return KotlinxSerializer(kotlinx.serialization.json.Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }

    @Singleton
    @Provides
    fun provideKtorEngine(httpLogger: Logger, kotlinxSerializer: KotlinxSerializer): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = kotlinxSerializer

                engine {
                    connectTimeout = NETWORK_TIMEOUT
                    socketTimeout = NETWORK_TIMEOUT
                }
            }

            install(Logging) {
                level = LogLevel.ALL
            }
            install(ResponseObserver) {
                onResponse { response ->
                    Timber.d("HTTP status:", "${response.status.value}")
                }
            }
            install(DefaultRequest) {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                    append(HttpHeaders.Authorization, "token")
                    append("app-id", "617e6dd2b84b267da7cbd397")
                }

//                header(HttpHeaders.ContentType, ContentType.Application.Json)
//                header(HttpHeaders.Authorization, String)
//                header("app-id", "617e6dd2b84b267da7cbd397")
            }
        }
    }
}