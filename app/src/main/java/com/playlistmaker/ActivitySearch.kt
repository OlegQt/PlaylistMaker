package com.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playlistmaker.Logic.SearchTrackAdapter
import com.playlistmaker.Logic.Track

class ActivitySearch : AppCompatActivity() {
    private val tag: String = "LOGTest"
    private var strSearch: String = String()
    private var txtSearch: EditText? = null
    private var recycleViewTracks: RecyclerView? = null
    private var trackList: ArrayList<Track> = ArrayList()


    // Заполнение списка треков
    private fun fillTrackList(): ArrayList<Track> {
        val musTrack: Track = Track(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        )
        trackList.add(musTrack)
        trackList.add(
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            )
        )
        trackList.add(
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            )
        )
        trackList.add(
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            )
        )
        trackList.add(
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
        trackList.add(
            Track(
                "Very long string Line To check if it ends properly",
                "Very long string Line To check if it ends properly",
                "5:03",
                "100bb.jpg"
            )
        )
        return this.trackList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Переменные для элементов интерфейса
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnCls: ImageView = findViewById(R.id.cls_search)
        txtSearch = findViewById(R.id.txt_search)

        // Создаем адаптер
        recycleViewTracks = findViewById(R.id.search_recycle_view)
        val musLayOut = LinearLayoutManager(this)
        musLayOut.orientation = RecyclerView.VERTICAL
        recycleViewTracks?.layoutManager = musLayOut
        recycleViewTracks?.adapter = SearchTrackAdapter(this.fillTrackList())


        // Анонимный
        val txtSearchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                strSearch = s.toString() // Сохраняем значение введенного текста
                btnCls.isVisible = !s.isNullOrEmpty()

            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        }

        // Вешаем слушателей на элелементы
        btnBack.setOnClickListener { finish() }

        txtSearch?.addTextChangedListener(txtSearchWatcher)

        btnCls.setOnClickListener {
            txtSearch?.setText("") // Очиста текстового поля
            // Убираем клавиатуру
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(txtSearch?.windowToken, 0)
            // Лог для проверки
            Log.d(tag, "Txt Clear")
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTxt", this.strSearch)
        Log.d(tag, "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.strSearch = savedInstanceState.getString("searchTxt").toString()
        Log.d(tag, "onRestoreInstanceState")
        this.txtSearch?.setText(this.strSearch)
    }
}