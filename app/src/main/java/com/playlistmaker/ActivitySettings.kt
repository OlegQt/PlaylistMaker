package com.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val imgShare: ImageView = findViewById(R.id.img_share)
        val imgSupport = findViewById<View>(R.id.ask_support)
        val imgAgreement: ImageView = findViewById(R.id.agreement)


        imgBack.setOnClickListener { finish() }
        imgShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_yandex))
            shareIntent.type = getString(R.string.share_type);

            try {
                startActivity(shareIntent)
            } catch (error: Exception) {
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }
        imgSupport.setOnClickListener {
            val sendMail: Intent = Intent(Intent.ACTION_SENDTO)
            sendMail.data = Uri.parse("mailto:")
            //sendMail.type = ("text/plain")
            /* recipient is put as array because you may wanna send email to multiple emails
          so enter comma(,) separated emails, it will be stored in array*/
            sendMail.putExtra(Intent.EXTRA_EMAIL, arrayOf("venOleg@gmail.com"))
            sendMail.putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker»."
            )
            sendMail.putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
            try {
                startActivity(sendMail)
            } catch (error: java.lang.Exception) {
                //get and show exception message
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        }
        imgAgreement.setOnClickListener {
            val address: Uri = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val openLink: Intent = Intent(Intent.ACTION_VIEW, address)
            try {
                startActivity(openLink)
            } catch (error: java.lang.Exception) {
                //get and show exception message
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }

        }
    }
}