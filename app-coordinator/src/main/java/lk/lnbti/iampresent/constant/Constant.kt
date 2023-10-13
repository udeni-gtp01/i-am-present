package lk.lnbti.iampresent.constant

/**
 * Constants used in the application.
 */
object Constant {
    const val PERMISSION_WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val BASE_URL = "http://192.168.8.101:8080/api/public/"

    const val ENDPOINT_LECTURE_ALL = "lecture/all"
    const val ENDPOINT_LECTURE_ALL_TODAY = "lecture/today"
    const val ENDPOINT_LECTURE_SAVE = "lecture/save"
    const val ENDPOINT_LECTURE_FIND = "lecture/{id}"
    const val ENDPOINT_LECTURE_DELETE = "lecture/{id}"
    const val ENDPOINT_LECTURE_OPEN_FOR_ATTENDANCE = "lecture/open-for-attendance/{id}"
    const val ENDPOINT_LECTURE_CLOSE_FOR_ATTENDANCE = "lecture/close-for-attendance/{id}"

    const val ENDPOINT_ATTENDANCE_ALL = "attendance/all"
    const val ENDPOINT_ATTENDANCE_SAVE = "attendance/save"
}