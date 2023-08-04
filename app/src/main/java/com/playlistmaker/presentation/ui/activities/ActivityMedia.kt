package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.playlistmaker.databinding.ActivityMediaBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.fragments.FavouriteTracksFragment
import com.playlistmaker.presentation.ui.fragments.PlayListsFragment

class ActivityMedia : AppCompatActivity() ,AlertMessaging{
    private lateinit var binding: ActivityMediaBinding

    // Хранение фрагментов внутри коллекции fragmentMap
    private val fragmentMap = mutableMapOf<String, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Заполняем коллекцию нужными фрагментами
        fragmentMap["Избранные треки"] = FavouriteTracksFragment.newInstance("")
        fragmentMap["Плейлисты"] = PlayListsFragment.newInstance()

        // Динамически создаем TabItems
        fragmentMap.keys.forEach {
            binding.mediaStorageTab.addTab(binding.mediaStorageTab.newTab().setText(it))
        }

        // Инициализация адаптера для
        binding.mediaStorageFragmentPlaceholder.adapter = MediaPager(this,fragmentMap)
        TabLayoutMediator(binding.mediaStorageTab,binding.mediaStorageFragmentPlaceholder){
            tab,pos ->
            tab.text = fragmentMap.keys.elementAt(pos)
        }.attach()

        // Возврат назад через кнопку "назад"
        binding.backToMainActivity.setOnClickListener { finish() }
    }

    // Есть ли необходимость выносить данный класс в отдельный файл? По сути он используется только
    // в момент подключения адаптера к viewpager2
    class MediaPager(activityFr: FragmentActivity, private val fragmentMap: Map<String, Fragment>) :
        FragmentStateAdapter(activityFr) {
        override fun getItemCount() = fragmentMap.size
        override fun createFragment(position: Int) = fragmentMap.values.elementAt(position)
    }

    override fun showAlertDialog(alertMessage: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage(alertMessage)
            .setPositiveButton("OK",null)
            .show()
    }

}

