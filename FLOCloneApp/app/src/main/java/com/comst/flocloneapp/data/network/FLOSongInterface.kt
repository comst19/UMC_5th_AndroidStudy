package com.comst.flocloneapp.data.network

import com.comst.flocloneapp.model.network.SongResponse
import retrofit2.Response
import retrofit2.http.GET

interface FLOSongInterface {

    @GET("/songs")
    suspend fun getSongs() : Response<SongResponse>
}