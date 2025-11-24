package com.fantasmadux.simpletodoapp

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.fantasmadux.simpletodoapp.model.ToDoModel
import com.fantasmadux.simpletodoapp.model.ToDoModel.Companion.createEmpty
import com.fantasmadux.simpletodoapp.utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask(

) : BottomSheetDialogFragment() {

    companion object {
        val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    private var mEditText: EditText? = null
    private var mSaveButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.add_new_task_layout, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEditText = view.findViewById(R.id.editText)
        mSaveButton = view.findViewById(R.id.addBtn) // Теперь этот ID существует

        val myDB = DataBaseHelper(requireActivity())

        var isUpdated = false

        // Чтение данных
        val bundle = arguments
        if (bundle != null) {
            isUpdated = true
            val task = bundle.getString("task")
            mEditText?.setText(task)

            if (task.isNullOrEmpty()) {
                mSaveButton?.isEnabled = false
            }

        }

        mEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Вызывается ДО изменения текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Вызывается во время изменения текста
                if (s.toString().equals("")) {
                    mSaveButton?.isEnabled = false
                    mSaveButton?.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton?.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Вызывается ПОСЛЕ изменения текста
                mSaveButton?.isEnabled = s?.isNotEmpty() == true
            }
        })

        mSaveButton?.setOnClickListener {
            val text = mEditText?.text.toString()

            if (isUpdated) {
                val taskId = arguments?.getInt("Id") ?: -1
                if (taskId != -1) {
                    myDB.updateTask(taskId, text)
                }
            } else {
                val item = ToDoModel.createEmpty().apply {
                    task = text
                }
                myDB.insertTask(item)
            }
            dismiss()
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is OnDialogCloseListener) {
            activity.onDialogClose(dialog)
        }
    }
}