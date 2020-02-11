package ie.wit.countdown.main.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountdownModel(var printedcountdown: String ="",
                          var answer: String = "",
                          var score: Int = 0,
                          var uid: String ="",
                          var id: Long = 0,
                          var userid: String ="",
                          var username: String ="",
                          var photo_url: String ="",
                          var usere_email: String =""): Parcelable