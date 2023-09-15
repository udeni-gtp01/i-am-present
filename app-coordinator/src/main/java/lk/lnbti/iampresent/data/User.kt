package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Role(
    @SerializedName("roleid") val roleid: Int,
    @SerializedName("name") val name: String?=null
) : Parcelable

@Parcelize
data class User(
    @SerializedName("userid") val userid: Int?=null,
    @SerializedName("name") val name: String="",
    @SerializedName("email") val email: String?=null,
    @SerializedName("token") val token: String?=null,
    @SerializedName("roleid") val roleid: Role?=null,
    @SerializedName("isuseravailable") val isuseravailable: Int?=null
) : Parcelable
