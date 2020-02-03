package ie.wit.countdown.main.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountdownModel(var printedcountdown: String = "",
                          var answer: String = "",
                          var score: Int = 0,
                          var id: Long = 0 ): Parcelable