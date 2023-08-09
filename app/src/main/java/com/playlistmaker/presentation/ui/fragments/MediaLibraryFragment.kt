package com.playlistmaker.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentMedialibraryBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.fragments.medialibrary.FavouriteTracksFragment
import com.playlistmaker.presentation.ui.fragments.medialibrary.PlayListsFragment

class MediaLibraryFragment : Fragment() ,AlertMessaging{
    private lateinit var binding: FragmentMedialibraryBinding
    private lateinit var mediator: TabLayoutMediator

    // Хранение фрагментов внутри коллекции fragmentMap
    private val fragmentMap = mutableMapOf<String, Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedialibraryBinding.inflate(layoutInflater)

        // Заполняем коллекцию нужными фрагментами
        fragmentMap[getString(R.string.favouriteTracks)] = FavouriteTracksFragment.newInstance("")
        fragmentMap[getString(R.string.Playlists)] = PlayListsFragment.newInstance()

        // Динамически создаем TabItems
        fragmentMap.keys.forEach {
            binding.mediaStorageTab.addTab(binding.mediaStorageTab.newTab().setText(it))
        }

        // Инициализация адаптера для
        binding.mediaStorageFragmentPlaceholder.adapter = MediaPager(requireActivity(),fragmentMap)
        mediator = TabLayoutMediator(binding.mediaStorageTab,binding.mediaStorageFragmentPlaceholder){
            tab,pos ->
            tab.text = fragmentMap.keys.elementAt(pos)
        }
        mediator.attach()

        // Возврат назад через кнопку "назад"
        //binding.backToMainActivity.setOnClickListener { finish() }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mediator.detach()
    }

    // Есть ли необходимость выносить данный класс в отдельный файл? По сути он используется только
    // в момент подключения адаптера к viewpager2
    class MediaPager(activityFr: FragmentActivity, private val fragmentMap: Map<String, Fragment>) :
        FragmentStateAdapter(activityFr) {
        override fun getItemCount() = fragmentMap.size
        override fun createFragment(position: Int) = fragmentMap.values.elementAt(position)
    }

    override fun showAlertDialog(alertMessage: String) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Alert")
            .setMessage(alertMessage)
            .setPositiveButton("OK",null)
            .show()
    }

}

