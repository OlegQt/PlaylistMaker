package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.presentation.ui.viewmodel.ActivitySettingsVm

class ActivitySettings : AppCompatActivity() {

    private lateinit var vm: ActivitySettingsVm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.SETTINGS)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val imgShare: ImageView = findViewById(R.id.img_share)
        val imgSupport = findViewById<View>(R.id.ask_support)
        val imgAgreement: ImageView = findViewById(R.id.agreement)
        val switchNightTheme: SwitchCompat = findViewById(R.id.night_theme_switch)

        this.vm = ViewModelProvider(this)[ActivitySettingsVm::class.java]

        // Начальное положение свича определяется загруженной темой
        switchNightTheme.isChecked = vm.theme == 1

        imgBack.setOnClickListener {
            finish()
        }

        // Переключение темы (день/ночь)
        switchNightTheme.setOnClickListener {
            vm.switchTheme()
        }


        // Кнопка поделиться приложением
        imgShare.setOnClickListener { vm.shareApplication() }

        // Кнопка написать в поддержку
        imgSupport.setOnClickListener { vm.sendTechnicalSupportMessage() }

        // Кнопка пользовательское соглашение
        imgAgreement.setOnClickListener { vm.userAgreement() }

    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }

}