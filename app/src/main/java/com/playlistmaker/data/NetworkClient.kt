package com.playlistmaker.data

interface NetworkClient<TRequest,TResponse> {
    fun doRequest(request: TRequest): TResponse
}