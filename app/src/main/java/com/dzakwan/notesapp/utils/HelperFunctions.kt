package com.dzakwan.notesapp.utils

import android.content.Context
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.dzakwan.notesapp.R
import java.text.FieldPosition

object HelperFunctions {

    fun spinnerListener(context: Context?, priorityIndicator: CardView): AdapterView.OnItemSelectedListener = object: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            context?.let {
                when(position){
                    0 -> {
                        // untuk menentukan warna
                        val red = ContextCompat.getColor(it, R.color.pink)
                        priorityIndicator.setCardBackgroundColor(red)
                    }

                    1 -> {
                        val yellow = ContextCompat.getColor(it, R.color.yellow)
                        priorityIndicator.setCardBackgroundColor(yellow)
                    }

                    2 -> {
                        val green = ContextCompat.getColor(it, R.color.green)
                        priorityIndicator.setCardBackgroundColor(green)
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

    }
}