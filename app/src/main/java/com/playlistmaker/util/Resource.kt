package com.playlistmaker.util

import com.playlistmaker.domain.models.ErrorList

sealed class Resource<T>(val data:T?,val error:ErrorList?){
    class Success<T>(data: T):Resource<T>(data,null)
    class Error<T>(errorCode: ErrorList):Resource<T>(null,errorCode)
}
