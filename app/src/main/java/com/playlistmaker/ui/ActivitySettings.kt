package com.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.playlistmaker.R
import com.playlistmaker.Theme.App

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val imgShare: ImageView = findViewById(R.id.img_share)
        val imgSupport = findViewById<View>(R.id.ask_support)
        val imgAgreement: ImageView = findViewById(R.id.agreement)
        val switchNightTheme:SwitchCompat = findViewById(R.id.night_theme_switch)
        /////////////////////////////////////////////////////////
        // Кнопка назад
        /////////////////////////////////////////////////////////
        imgBack.setOnClickListener { finish() }

        /////////////////////////////////////////////////////////
        // Переключатель темной темы
        /////////////////////////////////////////////////////////

        // Переключаем свитч в положение их сохраненного значения
        switchNightTheme.isChecked = App.instance.darkTheme

        // Вешаем слушатель переключателя темы
        switchNightTheme.setOnCheckedChangeListener { compoundButton, b ->
            //Toast.makeText(this,b.toString(),Toast.LENGTH_SHORT).show()
            App.instance.switchTheme(b)
        }

        /////////////////////////////////////////////////////////
        // Кнопка поделиться приложением
        /////////////////////////////////////////////////////////
        imgShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course))
            shareIntent.type = getString(R.string.share_type);

            try {
                startActivity(shareIntent)
            } catch (error: Exception) {
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        /////////////////////////////////////////////////////////
        // Кнопка написать в поддержку
        /////////////////////////////////////////////////////////
        imgSupport.setOnClickListener {
            val sendMail: Intent = Intent(Intent.ACTION_SENDTO)
            sendMail.data = Uri.parse("mailto:")
            // recipient is put as array because you may wanna send email to multiple emails
            // so enter comma(,) separated emails, it will be stored in array
            sendMail.putExtra(Intent.EXTRA_EMAIL,arrayOf(getString(R.string.student_email_address)))
            sendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.thanks_msg_theme))
            sendMail.putExtra(Intent.EXTRA_TEXT, getString(R.string.thanks_msg))
            try {
                startActivity(sendMail)
            } catch (error: java.lang.Exception) {
                //get and show exception message
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
        }

        /////////////////////////////////////////////////////////
        // Кнопка пользовательское соглашение
        /////////////////////////////////////////////////////////
        imgAgreement.setOnClickListener {
            val address: Uri = Uri.parse(getString(R.string.android_yandex_offer))
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