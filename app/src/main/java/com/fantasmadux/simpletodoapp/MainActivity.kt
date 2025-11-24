package com.fantasmadux.simpletodoapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fantasmadux.simpletodoapp.adapter.ToDoAdapter
import com.fantasmadux.simpletodoapp.model.ToDoModel
import com.fantasmadux.simpletodoapp.utils.DataBaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity() : AppCompatActivity(), OnDialogCloseListener {


    private lateinit var myDB: DataBaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton

    private var mList: List<ToDoModel> = emptyList()
    private var adapter: ToDoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addBtn)
        myDB = DataBaseHelper(this)

        adapter = ToDoAdapter(this, myDB)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        mList = myDB.getAllTasks().reversed()
        adapter?.setTasks(mList.toMutableList())
        adapter?.notifyDataSetChanged()

        addButton.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }

        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter!!))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDialogClose(dialogInterface: DialogInterface) {
        mList = myDB.getAllTasks().reversed()
        adapter?.setTasks(mList.toMutableList())
        adapter?.notifyDataSetChanged()
    }

}