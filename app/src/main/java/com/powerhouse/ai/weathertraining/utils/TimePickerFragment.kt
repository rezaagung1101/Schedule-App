package com.powerhouse.ai.weathertraining.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var mListener: DialogTimeListener? = null
    private var fragmentListener: FragmentTimeListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        mListener?.onDialogTimeSet(tag, hour, minute)
        fragmentListener?.onFragmentTimeSet(tag, hour, minute)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as DialogTimeListener?
    }

    override fun onDetach() {
        super.onDetach()
        if (mListener != null) {
            mListener = null
        }
    }

    fun setListener(listener: DialogTimeListener) {
        mListener = listener
    }

    fun setFragmentListener(listener: FragmentTimeListener) {
        fragmentListener = listener
    }

    interface FragmentTimeListener {
        fun onFragmentTimeSet(tag: String?, hour: Int, minute: Int)
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(tag: String?, hour: Int, minute: Int)
    }

}