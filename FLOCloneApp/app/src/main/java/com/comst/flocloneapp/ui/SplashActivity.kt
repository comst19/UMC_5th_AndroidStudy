package com.comst.flocloneapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.data.network.FLOAuthService
import com.comst.flocloneapp.model.local.AlbumEntity
import com.comst.flocloneapp.model.local.SongEntity
import com.comst.flocloneapp.model.network.Result
import com.comst.flocloneapp.ui.response.AutoLoginView
import com.comst.flocloneapp.ui.response.LoginView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity(), AutoLoginView {

    lateinit var songDB : SongDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        songDB = SongDatabase.getInstance(this)!!
        inputDummySongs()
        inputDummyAlbums()
        autoLogin()
    }

    private fun inputDummySongs(){
        CoroutineScope(Dispatchers.IO).launch {
            val songs = songDB.SongDao().getSongs()

            if (songs.isNotEmpty()) return@launch
            else{
                songDB.SongDao().insert(
                    SongEntity(
                        "Lilac",
                        "아이유 (IU)",
                        0,
                        200,
                        false,
                        "music_lilac",
                        R.drawable.img_album_exp2,
                        false,
                        0
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Flu",
                        "아이유 (IU)",
                        0,
                        200,
                        false,
                        "music_flu",
                        R.drawable.img_album_exp2,
                        false,
                        0
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Butter",
                        "방탄소년단 (BTS)",
                        0,
                        190,
                        false,
                        "music_butter",
                        R.drawable.img_album_exp,
                        false,
                        1
                    )
                )

                songDB.SongDao().insert(
                    SongEntity(
                        "Next Level",
                        "에스파 (AESPA)",
                        0,
                        210,
                        false,
                        "music_next",
                        R.drawable.img_album_exp3,
                        false,
                        2

                    )
                )


                songDB.SongDao().insert(
                    SongEntity(
                        "Boy with Luv",
                        "방탄소년단 (BTS)",
                        0,
                        230,
                        false,
                        "music_lilac",
                        R.drawable.img_album_exp4,
                        false,
                        3
                    )
                )


                songDB.SongDao().insert(
                    SongEntity(
                        "BBoom BBoom",
                        "모모랜드 (MOMOLAND)",
                        0,
                        240,
                        false,
                        "music_bboom",
                        R.drawable.img_album_exp5,
                        false,
                        4
                    )
                )
            }
        }
    }

    private fun inputDummyAlbums() {
        CoroutineScope(Dispatchers.IO).launch{
            val albums = songDB.AlbumDao().getAlbums()

            if (albums.isNotEmpty()) return@launch

            songDB.AlbumDao().insert(
                AlbumEntity(
                    0,
                    "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
                )
            )

            songDB.AlbumDao().insert(
                AlbumEntity(
                    1,
                    "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
                )
            )

            songDB.AlbumDao().insert(
                AlbumEntity(
                    2,
                    "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
                )
            )

            songDB.AlbumDao().insert(
                AlbumEntity(
                    3,
                    "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
                )
            )

            songDB.AlbumDao().insert(
                AlbumEntity(
                    4,
                    "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
                )
            )

        }
    }

    private fun autoLogin(){
        val authService = FLOAuthService()
        authService.setAutoLoginView(this)
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val jwt = spf.getString("currentUserJwt","")!!
        Log.d("jwt", jwt)
        authService.autoLogin(jwt)
    }

    override fun onAutoLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onAutoLoginFailure() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}