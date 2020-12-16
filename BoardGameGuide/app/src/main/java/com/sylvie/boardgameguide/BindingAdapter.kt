package com.sylvie.boardgameguide

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.pic_graffiti_small)
                    .error(R.drawable.pic_graffiti_small))
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
                    .error(R.drawable.pic_graffiti_small))
            .into(imgView)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("takeOffBracket")
fun bindTakeOffBracket(textView: TextView, typeList: MutableList<String>){
    typeList.let {
        var typeString = ""
        for(type in it){
            typeString += "$type | "
        }

        textView.text = typeString.substring(0, typeString.length-2)
//        @string/game_name_chinese +
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("takeOffBracket")
fun bindTakeOffBracket2(textView: TextView, typeList: MutableList<String>){
    typeList.let {
        var typeString = ""
        for(type in it){
            typeString += "$type | "
        }
        textView.text = "遊戲種類: " + typeString.substring(0, typeString.length-2)
    }
}