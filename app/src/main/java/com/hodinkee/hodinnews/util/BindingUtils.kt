package com.hodinkee.hodinnews.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.util.*

@BindingAdapter("app:visible")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:present")
fun setPresent(view: View, present: Boolean) {
    view.visibility = if (present) View.VISIBLE else View.GONE
}

@BindingAdapter("app:dateYmd")
fun setDateYms(view: TextView, date: Date?) {
    view.text = date?.let { DateFormat.ymd(it) }
}

@BindingAdapter("app:textError")
fun setTextError(view: TextInputLayout, error: String?) {
    if (error.isNullOrEmpty()) {
        view.error = null
        view.isErrorEnabled = false
    } else {
        view.error = error
        view.isErrorEnabled = true
    }
}