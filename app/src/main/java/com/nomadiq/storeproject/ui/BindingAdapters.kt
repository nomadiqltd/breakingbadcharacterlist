package com.nomadiq.storeproject.ui

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nomadiq.storeproject.ui.character.CharactersAdapter
import com.nomadiq.storeproject.ui.character.BreakingBadCharacter
import com.bumptech.glide.Glide


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<BreakingBadCharacter>?) {
    val adapter = recyclerView.adapter as CharactersAdapter
    adapter.addHeaderAndSubmitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    // Let method
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()

        Log.d(imgUri.path, "binding adapter")
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("imgMovie")
fun setImagePoster(imgView: ImageView, item: BreakingBadCharacter) {
    //  Log.d(imgUri.path, "binding adapter")
    Glide.with(imgView.context)
        .load(item.imgUrl)
        .into(imgView)
}


@BindingAdapter("myMovieName")
fun TextView.setMovieName(item: BreakingBadCharacter) {
    item?.let {
        text = item.name
    }
}
