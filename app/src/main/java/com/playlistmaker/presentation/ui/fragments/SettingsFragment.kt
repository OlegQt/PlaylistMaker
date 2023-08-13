package com.playlistmaker.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.playlistmaker.databinding.FragmentSettingsBinding
import com.playlistmaker.presentation.ui.viewmodel.FragmentSettingsVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val vm: FragmentSettingsVm by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Переключение темы (день/ночь)
        binding().nightThemeSwitch.setOnClickListener { vm.switchTheme() }

        // Начальное положение свича определяется загруженной темой
        binding().nightThemeSwitch.isChecked = vm.isNightMode

        // Кнопка поделиться приложением
        binding().imgShare.setOnClickListener { shareApplication() }

        // Кнопка написать в поддержку
        binding().askSupport.setOnClickListener { sendTechnicalSupportMessage() }

        // Кнопка пользовательское соглашение
        binding().agreement.setOnClickListener { userAgreement() }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Начальное положение свича определяется загруженной темой
        binding().nightThemeSwitch.isChecked = vm.isNightMode
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun binding() = this.binding!!

    private fun shareApplication() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course))
        shareIntent.type = getString(R.string.share_type);

        try {
            startActivity(shareIntent)
        } catch (error: Exception) {
            //Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendTechnicalSupportMessage() {
        val sendMail: Intent = Intent(Intent.ACTION_SENDTO)
        sendMail.data = Uri.parse("mailto:")
        // recipient is put as array because you may want to send email to multiple emails
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
            //Toast.makeText(baseContext, error.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun userAgreement() {
        val address: Uri = Uri.parse(getString(R.string.android_yandex_offer))
        val openLink: Intent = Intent(Intent.ACTION_VIEW, address)
        try {
            startActivity(openLink)
        } catch (error: java.lang.Exception) {
            //get and show exception message
            //Toast.makeText(baseContext, error.message, Toast.LENGTH_LONG).show()
        }
    }
}