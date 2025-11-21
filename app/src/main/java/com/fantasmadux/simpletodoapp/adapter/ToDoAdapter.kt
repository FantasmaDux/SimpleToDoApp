package com.fantasmadux.simpletodoapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.fantasmadux.simpletodoapp.AddNewTask
import com.fantasmadux.simpletodoapp.MainActivity
import com.fantasmadux.simpletodoapp.R
import com.fantasmadux.simpletodoapp.model.ToDoModel
import com.fantasmadux.simpletodoapp.utils.DataBaseHelper

class ToDoAdapter(
    private val mainActivity: MainActivity,
    private val myDB: DataBaseHelper
) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    var modelList: MutableList<ToDoModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val item: ToDoModel = modelList[position]
        holder.checkBox.text = item.task
        holder.checkBox.isChecked = toBoolean(item.status)
        holder.checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                myDB.updateStatus(item.id, 1)
            } else {
                myDB.updateStatus(item.id, 0)
            }
        }
    }

    fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    fun getContext(): Context {
        return mainActivity
    }

    fun setTasks(modelList: MutableList<ToDoModel> ) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int) {
        val item = modelList[position]
        myDB.deleteTask(item.id)

        modelList.removeAt(position)

        notifyItemRemoved(position)
    }

    // bundle -  коллекция пар "ключ-значение", которая используется
    // для передачи данных между компонентами Android (Activity, Fragment, Service и т.д.).
    fun editTask(position: Int) {
        val item = modelList[position]
        val bundle = Bundle();
        bundle.putInt("Id", item.id)
        bundle.putString("task", item.task)

        val task = AddNewTask()
        task.arguments = bundle
        task.show(mainActivity.supportFragmentManager, task.tag)

    }

    override fun getItemCount(): Int {

        return modelList.size;
    }

    class MyViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)

    }

}