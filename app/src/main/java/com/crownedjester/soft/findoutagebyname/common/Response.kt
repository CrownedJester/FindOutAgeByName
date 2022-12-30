package com.crownedjester.soft.findoutagebyname.common

sealed class Response<T>(val data: T? = null, val message: String = "") {

    class IsLoading<T>(data: T? = null) : Response<T>(data)

    class Success<T>(data: T) : Response<T>(data = data)

    class Error<T>(message: String, data: T? = null) : Response<T>(message = message, data = data)

}