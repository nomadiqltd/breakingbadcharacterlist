package com.nomadiq.storeproject.ui.character

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Gets movie information from the API Retrofit service and updates the
 * [Breaking Bad BreakingBadCharacter] and [ApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [ApiFilter] that is sent as part of the web server request
 */
@Parcelize
data class BreakingBadCharacter(
    @Json(name = "char_id") val charId: Long,
    @Json(name = "name") val name: String,
    @Json(name = "birthday") val birthday: String,
    @Json(name = "img") val imgUrl: String
) : Parcelable
