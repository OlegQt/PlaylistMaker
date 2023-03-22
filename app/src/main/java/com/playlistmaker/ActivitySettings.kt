package com.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val imgShare:ImageView = findViewById(R.id.img_share)


        imgBack.setOnClickListener{finish()}
        imgShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_yandex))
            shareIntent.type = getString(R.string.share_type);
            startActivity(shareIntent)
            //Toast.makeText(this.baseContext,"xt",Toast.LENGTH_LONG).show()
        }

    }
}