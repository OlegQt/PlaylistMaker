package com.playlistmaker

import android.content.Intent
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
import com.playlistmaker.Logic.SearchTrackAdapter
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen
import com.playlistmaker.itunes.ItunesMusic
import com.playlistmaker.itunes.ItunesTrack
import com.playlistmaker.searchhistory.History

class ActivitySearch : AppCompatActivity() {
    private var stubLayout: View? = null
    private var strSearch: String = String()
    private var txtSearch: EditText? = null
    private var recycleViewTracks: RecyclerView? = null
    private var btnReload: Button? = null

    // Элементы Ui для истории просмотра треков
    private lateinit var btnClearHistory: Button
    private lateinit var loutHistory: LinearLayout
    private lateinit var recyclerHistory: RecyclerView


    // Переменная trackList хранит список треков, найденных по запросу в iTunesMedia
    private var trackList: ArrayList<ItunesTrack> = ArrayList()
    private var itunesMusic = ItunesMusic()

    // В переменной searchHistory осуществляется доступ к истории 10 просмотренных трекам
    // а так же к их удалению, добавлению
    private var searchHistory: History = History()

    private lateinit var musTrackAdapter: SearchTrackAdapter
    private lateinit var searchHistoryTrackAdapter: SearchTrackAdapter

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
            Log.d(App.TAG_LOG, "Some error occurred")
            this.showStubConnectionTroubles()
        }
    }

    private fun startPlayerActivity() {
        startActivity(Intent(App.instance, ActivityPlayer::class.java))
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
            this.trackList.add(trackJSON)
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
        Log.d(App.TAG_LOG, "Keyboard ok button")
        itunesMusic.search(songName, this.doAfterSearch)
    }

    private fun deploySearchHistoryUi() {
        btnClearHistory = findViewById(R.id.btn_clear_history)
        loutHistory = findViewById(R.id.history_layout)
        recyclerHistory = findViewById(R.id.history_search_recycle_view)

        // Подгружаем историю поиска
        searchHistory.loadHistory()

        // Создаем адаптер для показа истории поиска музыкальных треков
        // Добавляем туда отдельный листенер на случай описания отдельного поведения для данного recycler
        searchHistoryTrackAdapter = SearchTrackAdapter(searchHistory.trackHistoryList,
            object : SearchTrackAdapter.OnTrackClickListener {
                override fun onTrackClick(position: Int) {
                    Toast.makeText(baseContext, "Click", Toast.LENGTH_LONG).show()
                }
            })
        val musLayOut = LinearLayoutManager(this)
        musLayOut.orientation = RecyclerView.VERTICAL
        recyclerHistory?.layoutManager = musLayOut
        recyclerHistory?.adapter = searchHistoryTrackAdapter

        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            showSearchHistory(true) // Дополнительная перерисовка
        }
    }

    fun showSearchHistory(visibility: Boolean) {
        // Показываем список просмотров только если он не пуст
        if (visibility and searchHistory.trackHistoryList.isNotEmpty()) {
            // Load tracks to historyRecycler
            searchHistoryTrackAdapter.notifyDataSetChanged()
            loutHistory.visibility = View.VISIBLE
        } else loutHistory.visibility = View.GONE
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


        // Создаем recyclerView
        val pL = object : SearchTrackAdapter.OnTrackClickListener {
            override fun onTrackClick(position: Int) {
                // Добавляем трек в историю просмотров
                searchHistory.addToSearchHistory(trackList[position])
                // Запускаем плеер
                startPlayerActivity()
            }
        }
        this.musTrackAdapter = SearchTrackAdapter(this.trackList, pL)

        val musLayOut = LinearLayoutManager(this)
        musLayOut.orientation = RecyclerView.VERTICAL
        recycleViewTracks?.layoutManager = musLayOut
        recycleViewTracks?.adapter = musTrackAdapter

        // Анонимный
        val txtSearchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                strSearch = s.toString() // Сохраняем значение введенного текста
                btnCls.isVisible = !s.isNullOrEmpty()
                showSearchHistory(s.isNullOrEmpty())

                if (s.isNullOrEmpty()) {
                    clearTrackList()
                    showSearchHistory(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        // Вешаем слушателей на элелементы
        btnBack.setOnClickListener {
            App.instance.saveCurrentScreen(Screen.MAIN) // Сохраняем экран
            finish()
        }

        btnReload?.setOnClickListener {
            if (!txtSearch?.text.isNullOrEmpty()) showSearchResults(txtSearch?.text.toString())
        }

        txtSearch?.addTextChangedListener(txtSearchWatcher)

        // Обработка фокуса у строки поиска треков
        // Если появился фокус в пустом окне - показать историю поиска
        txtSearch?.setOnFocusChangeListener { view, hasFocus ->
            showSearchHistory(hasFocus)
            //searchHistory.setVisibility(hasFocus)
            //searchHistory.showAllSearchHistory()
        }

        // Нажатие на конпку ОК на клавиатуре
        txtSearch?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                showSearchResults(textView.text.toString())
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

        deploySearchHistoryUi()

        // Сохраняем текущий экран в sharedPrefs
        App.instance.saveCurrentScreen(Screen.SEARCH)

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

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }
}