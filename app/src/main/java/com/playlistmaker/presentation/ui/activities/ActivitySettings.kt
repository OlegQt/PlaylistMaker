package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.R
import com.playlistmaker.presentation.ui.viewmodel.ActivitySettingsVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivitySettings : AppCompatActivity() {

    private val vm: ActivitySettingsVm by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val imgShare: ImageView = findViewById(R.id.img_share)
        val imgSupport = findViewById<View>(R.id.ask_support)
        val imgAgreement: ImageView = findViewById(R.id.agreement)
        val switchNightTheme: SwitchCompat = findViewById(R.id.night_theme_switch)

        // Начальное положение свича определяется загруженной темой
        switchNightTheme.isChecked = vm.isNightMode

        imgBack.setOnClickListener {
            finish()
        }

        // Переключение темы (день/ночь)
        switchNightTheme.setOnClickListener {
            vm.switchTheme()
        }

        fun shareApplication() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course))
            shareIntent.type = getString(R.string.share_type);

            try {
                startActivity(shareIntent)
            } catch (error: Exception) {
                Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        fun sendTechnicalSupportMessage() {
            val sendMail: Intent = Intent(Intent.ACTION_SENDTO)
            sendMail.data = Uri.parse("mailto:")
            // recipient is put as array because you may wanna send email to multiple emails
            // so enter comma(,) separated emails, it will be stored in array
            sendMail.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.student_email_address))
            )
            sendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.thanks_msg_theme))
            sendMail.putExtra(Intent.EXTRA_TEXT, getString(R.string.thanks_msg))
            try {
                startActivity(sendMail)
            } catch (error: java.lang.Exception) {
                //get and show exception message
                Toast.makeText(baseContext, error.message, Toast.LENGTH_LONG).show()
            }
        }

        fun userAgreement() {
            val address: Uri = Uri.parse(getString(R.string.android_yandex_offer))
            val openLink: Intent = Intent(Intent.ACTION_VIEW, address)
            try {
                startActivity(openLink)
            } catch (error: java.lang.Exception) {
                //get and show exception message
                Toast.makeText(baseContext, error.message, Toast.LENGTH_LONG).show()
            }
        }


        // Кнопка поделиться приложением
        imgShare.setOnClickListener { shareApplication() }

        // Кнопка написать в поддержку
        imgSupport.setOnClickListener { sendTechnicalSupportMessage() }

        // Кнопка пользовательское соглашение
        imgAgreement.setOnClickListener { userAgreement() }

    }
}