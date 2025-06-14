package net.senosoft.rekamdatagps

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import net.senosoft.rekamdatagps.data.DatabaseHelper
import net.senosoft.rekamdatagps.adapter.DataAdapter

class ListDataActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_data)

        val recyclerView = findViewById<RecyclerView>(R.id.rvListData)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        val dataList = dbHelper.getAllData() // pastikan pakai tabel 'data_lapangan'
        val adapter = DataAdapter(dataList)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        recyclerView.adapter = adapter
    }
}
