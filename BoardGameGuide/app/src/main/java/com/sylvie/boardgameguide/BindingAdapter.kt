package com.sylvie.boardgameguide

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.pic_christmas_cookie)
                    .error(R.drawable.pic_graffiti_small)
            )
            .into(imgView)
    }
}

@BindingAdapter("imageLocal")
fun bindLocalImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.pic_graffiti_small)
                    .error(R.drawable.pic_graffiti_small)
            )
            .into(imgView)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("takeOffBracket")
fun bindTakeOffBracket(textView: TextView, typeList: MutableList<String>) {
    typeList.let {
        var typeString = ""
        for (type in it) {
            typeString += "$type | "
        }

        textView.text = typeString.substring(0, typeString.length - 2)
    }
}

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("textTime")
fun bindTextTime(textView: TextView, date: Long) {
    val timeDiff = System.currentTimeMillis() - date
    val day = (1000 * 60 * 60 * 24)
    val hour = (1000 * 60 * 60)
    val minute = (1000 * 60)
    when {
        timeDiff > day -> {
            textView.text = "${(timeDiff / day)}天前"
        }
        timeDiff in (hour + 1) until day -> {
            textView.text = "${(timeDiff / hour)}小時前"
        }
        else -> {
            textView.text = "${(timeDiff / minute)}分鐘前"
        }
    }
}

@SuppressLint("SetTextI18n", "SimpleDateFormat")
@BindingAdapter("textTimeFormat")
fun bindTimeFormat(textView: TextView, time: Long?) {
    time?.let {
        val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(it))
        textView.text = "Time: $dateString"
    }
}