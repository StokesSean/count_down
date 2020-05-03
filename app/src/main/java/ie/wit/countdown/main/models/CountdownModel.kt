package ie.wit.countdown.main.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
@IgnoreExtraProperties
@Parcelize
data class CountdownModel(var printedcountdown: String ="",
                          var answer: String = "",
                          var score: Int = 0,
                          var uid: String? ="",
                          var id: Long = 0,
                          var userid: String? ="",
                          var username: String ="",
                          var photo_url: String ="",
                          var user_email: String =""): Parcelable
{
@Exclude
fun toMap(): Map<String, Any?> {
    return mapOf(
        "userid" to userid,
        "printedcountdown" to printedcountdown,
        "score" to score,
        "id" to id,
        "photo_url" to photo_url,
        "username" to username,
        "answer" to answer,
        "user_email" to user_email
    )
}
}