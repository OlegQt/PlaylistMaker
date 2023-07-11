package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.util.Creator

class ActivitySettingsVm(private val application: Application) : AndroidViewModel(application) {
    private val app = application
    private var safeLastScreenUseCase = Creator.getCreator().provideSafeLastScreenUseCase(app.baseContext)
    var theme: Int = App.instance.getCurrentTheme()

    init {
        //safeLastScreenUseCase.execute(Screen.SETTINGS.screenName)
    }

    fun setTheme() {
    }

    fun switchTheme() {
        when (theme) {
            1 -> theme = 0
            0 -> theme = 1
        }
        App.instance.applyTheme(theme)
    }

    fun shareApplication() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_TEXT, app.getString(R.string.android_developer_course))
        shareIntent.type = app.getString(R.string.share_type);

        try {
            app.startActivity(shareIntent)
        } catch (error: Exception) {
            Toast.makeText(app.baseContext, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun sendTechnicalSupportMessage() {
        val sendMail: Intent = Intent(Intent.ACTION_SENDTO)
        sendMail.data = Uri.parse("mailto:")
        sendMail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // recipient is put as array because you may wanna send email to multiple emails
        // so enter comma(,) separated emails, it will be stored in array
        sendMail.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(app.getString(R.string.student_email_address))
        )
        sendMail.putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.thanks_msg_theme))
        sendMail.putExtra(Intent.EXTRA_TEXT, app.getString(R.string.thanks_msg))
        try {
            app.startActivity(sendMail)
        } catch (error: java.lang.Exception) {
            //get and show exception message
            Toast.makeText(app.baseContext, error.message, Toast.LENGTH_LONG).show()
        }
    }

    fun userAgreement() {
        val address: Uri = Uri.parse(app.getString(R.string.android_yandex_offer))
        val openLink: Intent = Intent(Intent.ACTION_VIEW, address)
        openLink.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            app.startActivity(openLink)
        } catch (error: java.lang.Exception) {
            //get and show exception message
            Toast.makeText(app.baseContext, error.message, Toast.LENGTH_LONG).show()
        }
    }
}