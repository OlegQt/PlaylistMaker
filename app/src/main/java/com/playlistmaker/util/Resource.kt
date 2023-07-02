package com.playlistmaker.util

sealed class Resource<T>(val data:T?,val errorCode:Int?){
    class Success<T>(data: T):Resource<T>(data,null)
    class Error<T>(errorCode: Int):Resource<T>(null,errorCode)
}
