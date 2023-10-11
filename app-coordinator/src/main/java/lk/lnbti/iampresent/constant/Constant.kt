package lk.lnbti.iampresent.constant

/**
 * Constants used in the application.
 */
object Constant {

    const val BASE_URL = "http://192.168.8.100:8080/api/public/"

    const val ENDPOINT_LECTURE_ALL = "lecture/all"
    const val ENDPOINT_LECTURE_SAVE = "lecture/save"
    const val ENDPOINT_LECTURE_FIND = "lecture/{id}"
    const val ENDPOINT_LECTURE_OPEN_FOR_ATTENDANCE = "lecture/open-for-attendance/{id}"
    const val ENDPOINT_LECTURE_CLOSE_FOR_ATTENDANCE = "lecture/close-for-attendance/{id}"
}