package com.comst.timer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.comst.timer.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private val soundPool = SoundPool.Builder().build()
    private var bellSoundId: Int? = null

    private var isRunning = false
    private var timer: Timer? = null
    private var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = " 타이머 "

        initSound()

        binding.startBtn.setOnClickListener(this)
        binding.resetBtn.setOnClickListener(this)
        binding.timeSetBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.startBtn -> {
                if (isRunning) {
                    pause()
                } else {
                    start()
                }

            }

            R.id.resetBtn -> {
                refresh()
            }

            R.id.timeSetBtn -> {
                time = binding.timeSetEdit.text.toString().toInt() * 100
                val milli_second = time % 100
                val second = (time % 6000) / 100
                val minute = (time / 6000)
                binding.milliTV.text =
                    if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                binding.secTV.text = if (second < 10) ":0${second}" else ":${second}"
                binding.minuteTV.text = "$minute"
            }
        }
    }

    private fun start() {
        if (time == 0) return
        binding.startBtn.text = getString(R.string.btn_pause)
        isRunning = true

        //time = binding.timeSetEdit.text.toString().toInt()*100
        timer = timer(period = 10) {
            // 1000ms = 1s, 0.01ms time 1+
            time--
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = (time / 6000)
            runOnUiThread {
                if (isRunning) {
                    binding.milliTV.text =
                        if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                    binding.secTV.text = if (second < 10) ":0${second}" else ":${second}"
                    binding.minuteTV.text = "$minute"
                }
            }
            if (time == 0) {
                timer?.cancel()
                runOnUiThread {
                    refresh()
                }
                soundPool.autoPause()
                bellSoundId?.let { soundId ->
                    soundPool.play(soundId, 1F, 1F, 0, 5, 1F)
                }

            }
        }
    }

    private fun pause() {
        binding.startBtn.text = getString(R.string.btn_start)
        isRunning = false
        timer?.cancel()
    }

    private fun refresh() {
        timer?.cancel()
        binding.startBtn.text = getString(R.string.btn_start)
        isRunning = false
        time = 0
        binding.milliTV.text = ".00"
        binding.secTV.text = ":00"
        binding.minuteTV.text = "0"
    }

    private fun initSound() {
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}