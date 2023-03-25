package com.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ActivitySearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnCls: ImageView = findViewById(R.id.cls_search)
        val txtSearch: EditText = findViewById(R.id.txt_search)

        val txtSearchWatcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) btnCls.visibility=View.GONE
                else btnCls.visibility=View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        }

        btnBack.setOnClickListener { finish() }
        txtSearch.addTextChangedListener(txtSearchWatcher)
        btnCls.setOnClickListener{
            txtSearch.setText("")
        }


    }
}