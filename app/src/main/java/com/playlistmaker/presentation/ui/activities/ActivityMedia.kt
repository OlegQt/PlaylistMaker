package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.playlistmaker.databinding.ActivityMediaBinding
import com.playlistmaker.presentation.ui.fragments.FavouriteTracksFragment
import com.playlistmaker.presentation.ui.fragments.PlayListsFragment

class ActivityMedia : AppCompatActivity() {
    lateinit var binding: ActivityMediaBinding
    private val fragmentMap = mutableMapOf<String, Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentMap["Избранные треки"] = FavouriteTracksFragment.newInstance("")
        fragmentMap["Плейлисты"] = PlayListsFragment.newInstance()

        fragmentMap.keys.forEach {
            binding.mediaStorageTab.addTab(binding.mediaStorageTab.newTab().setText(it))
        }

        binding.mediaStorageFragmentPlaceholder.adapter = MediaPager(this,fragmentMap)
        TabLayoutMediator(binding.mediaStorageTab,binding.mediaStorageFragmentPlaceholder){
            tab,pos ->
            tab.text = fragmentMap.keys.elementAt(pos)
        }.attach()

        binding.backToMainActivity.setOnClickListener { finish() }

    }
}

class MediaPager(activityFr: FragmentActivity, private val fragmentMap: Map<String, Fragment>) :
    FragmentStateAdapter(activityFr) {
    override fun getItemCount() = fragmentMap.size
    override fun createFragment(position: Int) = fragmentMap.values.elementAt(position)
}