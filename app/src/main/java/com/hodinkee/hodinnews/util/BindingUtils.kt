package com.hodinkee.hodinnews.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
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
fun setDateYms(view: TextView, date: Date) {
    view.text = DateFormat.ymd(date)
}