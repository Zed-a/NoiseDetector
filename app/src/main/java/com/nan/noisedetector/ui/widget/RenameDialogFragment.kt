package com.nan.noisedetector.ui.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.nan.noisedetector.R
import com.nan.noisedetector.util.PreferenceHelper
import com.nan.noisedetector.util.dismissSoftKeyboard
import com.nan.noisedetector.util.isEmpty
import com.nan.noisedetector.util.showSoftKeyboard

class RenameDialogFragment : DialogFragment() {
    private var title: Int = 0
    private var position: Int = 0
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        title = arguments!!.getInt("title")
        position = arguments!!.getInt("position")
        text = arguments!!.getString("text")
    }


    var callback: RenameCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.dlg_edit, container)
        val editText: EditText = view.findViewById(R.id.et_rename)
        if (!isEmpty(text)) {
            editText.setText(text)
            editText.setSelectAllOnFocus(true)
        }
        editText.requestFocus()
        showSoftKeyboard(activity, editText)

        (view.findViewById(R.id.tv_title) as TextView).text = if (title==0) "编辑地点" else "编辑备注"

        (view.findViewById(R.id.btn_ok) as Button).setOnClickListener {
            dismiss()
            dismissSoftKeyboard(activity, editText)
            val list = PreferenceHelper.historyRecord
            if (title == 0)
                list[position].historyData.location = editText.text.toString()
            else
                list[position].historyData.msg = editText.text.toString()
            PreferenceHelper.historyRecord = list
            callback?.rename()
        }

        (view.findViewById(R.id.btn_cancel) as Button).setOnClickListener {
            dismiss()
            dismissSoftKeyboard(activity, editText)
        }
        return view
    }

    companion object
    {
        fun newInstance(title: Int, position: Int, text: String) : RenameDialogFragment
        {
            val f = RenameDialogFragment()

            val args = Bundle()
            args.putInt("title", title)
            args.putInt("position", position)
            args.putString("text", text)
            f.arguments = args

            return f
        }
    }

    interface RenameCallback {
        fun rename()
    }
}