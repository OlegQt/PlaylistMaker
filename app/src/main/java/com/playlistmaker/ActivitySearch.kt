package com.playlistmaker

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.Logic.SearchTrackAdapter
import com.playlistmaker.Logic.Track
import com.playlistmaker.itunes.ItunesMusic
import com.playlistmaker.searchhistory.History

class ActivitySearch : AppCompatActivity() {
    private val TAG: String = "DEBUG"

    private var stubLayout: View? = null
    private var strSearch: String = String()
    private var txtSearch: EditText? = null
    private var recycleViewTracks: RecyclerView? = null
    private var btnReload: Button? = null

    private var trackList: ArrayList<Track> = ArrayList()
    private var itunesMusic = ItunesMusic()
    private var searchHistory: History = History()

    init {
    }

    private val recyclerListener = object : SearchTrackAdapter.OnTrackClickListener {
        override fun onTrackClick(position: Int) {
            Snackbar.make(
                recycleViewTracks!!,
                "Добавлен ${trackList[position].trackName}",
                Snackbar.LENGTH_SHORT
            ).show()
            searchHistory.addToSearchHistory(trackList[position])
        }
    }

    private var musTrackAdapter = SearchTrackAdapter(this.trackList, recyclerListener)

    // Функция вызывается внутри call.enqueue
    private var doAfterSearch: (Msgcode) -> Unit = {
        if (it == Msgcode.OK) {
            // Если ответ получен, но трэклист пуст. На всякий случай добавил проверку на null,
            // хотя там его и не может быть
            if (itunesMusic.trackLst.isNullOrEmpty()) this.showStubNothingFound()
            else {
                // Если и ответ от сервера есть и список треков не пустой
                this.modifyTrackList()
            }
        } else if (it == Msgcode.Failure) {
            Log.d(TAG, "Some error occurred")
            this.showStubConnectionTroubles()
        }
    }

    private fun showStubNothingFound() {
        this.recycleViewTracks?.visibility = View.GONE // Hide RecyclerView
        this.stubLayout?.visibility = View.VISIBLE // Show Layout with our Stub


        val txtMainError: TextView = findViewById(R.id.txt_stub_main_error)
        val img: ImageView = findViewById(R.id.img_stub) // Find img resource
        img.setImageResource(R.drawable.nothing_found) // Set Proper image from drawable
        txtMainError.text = getString(R.string.nothing_found)
        btnReload?.visibility = View.GONE
    }

    private fun showStubConnectionTroubles() {
        this.recycleViewTracks?.visibility = View.GONE // Hide RecyclerView
        this.stubLayout?.visibility = View.VISIBLE // Show Layout with our Stub

        val txtMainError: TextView = findViewById(R.id.txt_stub_main_error)
        val img: ImageView = findViewById(R.id.img_stub) // Find img resource
        img.setImageResource(R.drawable.connection_troubles) // Set Proper image from drawable
        txtMainError.text = getString(R.string.connection_troubles)
            .plus("\n")
            .plus(getString(R.string.loading_fail))
        btnReload?.visibility = View.VISIBLE
    }

    private fun modifyTrackList() {
        this.trackList.clear() // Очищаем трэк лист от предыдущего запроса
        // Ниже переводим формат в читабельный для View Holder и Адаптера и заполняем трэк лист
        this.itunesMusic.trackLst?.forEach { trackJSON ->
            this.trackList.add(trackJSON.toTrack())
        }
        this.musTrackAdapter.notifyDataSetChanged() // Уведомляем адаптер о необходимости перерисовки
        this.recycleViewTracks?.visibility = View.VISIBLE // Show RecyclerView
        this.stubLayout?.visibility = View.GONE // Hide Layout with our Stub
    }

    private fun clearTrackList() {
        // Очищаем трэкЛист и RecyclerView
        this.trackList.clear()
        this.musTrackAdapter.notifyDataSetChanged()
    }

    private fun showSearchResults(songName: String) {
        // Сохраняю эту функцию на случай, если придется отказаться от лямбды
        // Или добавить какой-то доп. функционал
        Log.d(TAG, "Keyboard ok button")
        itunesMusic.search(songName, this.doAfterSearch)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Переменные для элементов интерфейса
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnCls: ImageView = findViewById(R.id.cls_search)
        txtSearch = findViewById(R.id.txt_search)
        stubLayout = findViewById(R.id.stub_layout)
        recycleViewTracks = findViewById(R.id.search_recycle_view)
        btnReload = findViewById(R.id.btn_reload)

        searchHistory.deployExtraUi(this)


        // Создаем адаптер
        val musLayOut = LinearLayoutManager(this)
        musLayOut.orientation = RecyclerView.VERTICAL
        recycleViewTracks?.layoutManager = musLayOut
        recycleViewTracks?.adapter = this.musTrackAdapter

        // Анонимный
        val txtSearchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                strSearch = s.toString() // Сохраняем значение введенного текста
                btnCls.isVisible = !s.isNullOrEmpty()
                searchHistory.setVisibility(s.isNullOrEmpty())

                if (s.isNullOrEmpty()) {
                    clearTrackList()
                    searchHistory.showAllSearchHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        // Вешаем слушателей на элелементы
        btnBack.setOnClickListener { finish() }

        btnReload?.setOnClickListener {
            if (!txtSearch?.text.isNullOrEmpty()) showSearchResults(txtSearch?.text.toString())
        }

        txtSearch?.addTextChangedListener(txtSearchWatcher)

        txtSearch?.setOnFocusChangeListener { view, hasFocus ->
            searchHistory.setVisibility(hasFocus)
            searchHistory.showAllSearchHistory()

        }

        // Нажатие на конпку ОК на клавиатуре
        txtSearch?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                showSearchResults(textView.text.toString())
                true
            }
            false
        }

        btnCls.setOnClickListener {
            txtSearch?.setText("") // Очиста текстового поля
            // Убираем клавиатуру
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(txtSearch?.windowToken, 0)

            clearTrackList()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTxt", this.strSearch)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.strSearch = savedInstanceState.getString("searchTxt").toString()
        this.txtSearch?.setText(this.strSearch)
    }

}