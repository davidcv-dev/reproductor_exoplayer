package com.developerstark.reproductorexoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.ConfigurationInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class WatchActivity : AppCompatActivity() {
    companion object{
        var isFullScreen=false
        var isLock=false
    }
    lateinit var simpleExoPlayer:SimpleExoPlayer
    lateinit var bt_fullscreem: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        val playerView = findViewById<PlayerView>(R.id.player)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
         bt_fullscreem = findViewById<ImageView>(R.id.bt_fullscreem)
        val bt_lockscreem = findViewById<ImageView>(R.id.exo_lock)

        bt_fullscreem.setOnClickListener {
            if(!isFullScreen){
                bt_fullscreem.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen_exit))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }else{
                bt_fullscreem.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen= !isFullScreen

        }
        bt_lockscreem.setOnClickListener {
            if(!isLock){
                bt_lockscreem.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock))
            }else{
                bt_lockscreem.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock_open))
            }
            isLock=!isLock
            lockScreen(isLock)
        }
        simpleExoPlayer=SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player=simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.visibility= View.VISIBLE
                }else if(playbackState==Player.STATE_READY){
                    progressBar.visibility=View.GONE
                }
            }
        })
        val videoSource= Uri.parse("https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    private fun lockScreen(clock: Boolean) {
        val sec_mid= findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom= findViewById<LinearLayout>(R.id.sec_controlvid2)
        if(clock){
            sec_mid.visibility= View.INVISIBLE
            sec_bottom.visibility= View.INVISIBLE
        }else{
            sec_mid.visibility= View.VISIBLE
            sec_bottom.visibility= View.VISIBLE
            }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            bt_fullscreem.performClick()
        }
        else super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }
}