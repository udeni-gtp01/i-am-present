package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a user role.
 *
 * @property roleid The unique identifier for the role.
 * @property name The name of the role.
 */
@Parcelize
data class Role(
    @SerializedName("roleid") val roleid: Int,
    @SerializedName("name") val name: String? = null
) : Parcelable

/**
 * Data class representing a user.
 *
 * @property userid The unique identifier for the user.
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property roleid The role assigned to the user.
 * @property isuseravailable An indicator of user availability(active/deactivate).
 */
@Parcelize
data class User(
    @SerializedName("userid") val userid: Int? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String,
    @SerializedName("roleid") val roleid: Role? = null,
    @SerializedName("isuseravailable") val isuseravailable: Int? = null
) : Parcelable