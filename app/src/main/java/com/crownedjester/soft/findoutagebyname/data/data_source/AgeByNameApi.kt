package com.crownedjester.soft.findoutagebyname.data.data_source

import com.crownedjester.soft.findoutagebyname.data.model.PersonDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AgeByNameApi {

    companion object {

        const val BASE_URL = "https://api.agify.io"

    }

    @GET("/")
    suspend fun getAgeByName(@Query("name") name: String): PersonDataDto

}