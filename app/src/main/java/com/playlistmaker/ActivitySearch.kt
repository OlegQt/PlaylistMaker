package com.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ActivitySearch : AppCompatActivity() {
    private val tag: String = "LOGTest"
    private var strSearch: String = String()
    private var txtSearch: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Переменные для элементов интерфейса
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnCls: ImageView = findViewById(R.id.cls_search)
        txtSearch = findViewById(R.id.txt_search)
        // Анонимный
        val txtSearchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) btnCls.visibility = View.GONE
                else {
                    btnCls.visibility = View.VISIBLE // Делаем кнопку очистки видимой
                    strSearch = s.toString() // Сохраняем значение введенного текста
                }
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
            // Очистка строки, иначе даже при стертом значении текстого поля, после поворота экрана, значение восстановиться
            this.strSearch = ""
            // Убираем клавиатуру
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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