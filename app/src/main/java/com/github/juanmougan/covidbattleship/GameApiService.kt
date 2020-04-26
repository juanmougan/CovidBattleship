package com.github.juanmougan.covidbattleship

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*
import java.util.*

interface GameApiService {

    companion object {
        //        val BASE_URL = "http://my-json-server.typicode.com/juanmougan/mock_battleship_api/";
        val BASE_URL = "http://10.0.2.2:3000/"

        fun create(): GameApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ApiWorker.gsonConverter)
                .client(ApiWorker.client)
                .build()

            return retrofit.create(GameApiService::class.java)
        }
    }

    @POST("games")
    @Headers("Content-Type: application/json")
    fun createGame(@Body gameRequest: GameRequest): Observable<GameResponse>

    @GET("games/{id}/status")
    @Headers("Content-Type: application/json")
    fun getGameStatus(@Path(value = "id") id: UUID): Observable<GameStatusResponse>

    @PATCH("games/{id}")
    @Headers("Content-Type: application/json")
    fun joinGame(@Path(value = "id") id: UUID, @Body gameRequest: GameRequest): Observable<GameResponse>
}
