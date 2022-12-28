package com.crownedjester.soft.findoutagebyname.domain.use_cases

import com.crownedjester.soft.findoutagebyname.common.Response
import com.crownedjester.soft.findoutagebyname.data.model.toPersonData
import com.crownedjester.soft.findoutagebyname.domain.model.PersonData
import com.crownedjester.soft.findoutagebyname.domain.repository.AgeByNameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAgeByName @Inject constructor(private val repository: AgeByNameRepository) {

    operator fun invoke(name: String): Flow<Response<PersonData>> = flow {
        try {
            emit(Response.IsLoading())

            delay(1000L)
            emit(
                Response.Success(
                    data = repository.getAgeByName(name).toPersonData()
                )
            )

        } catch (e: HttpException) {
            emit(Response.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Response.Error("Couldn't reach server. Check your internet connection"))
        }
    }

}