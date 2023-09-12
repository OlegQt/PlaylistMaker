package com.playlistmaker.data

interface NetworkClient<TRequest,TResponse> {
    fun doRequest(request: TRequest): TResponse

    suspend fun doSuspendRequest(request: TRequest):TResponse
}