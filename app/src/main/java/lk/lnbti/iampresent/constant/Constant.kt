package lk.lnbti.iampresent.constant

/**
 * Constants used in the application.
 */
object Constant {
    /**
     * Camera permission required for the application.
     */
    const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA

    /**
     * Base URL for the API endpoints.
     */
    const val BASE_URL = "http://iampresent.us-east-1.elasticbeanstalk.com/api/public/"

    /**
     * Endpoint for retrieving all lectures.
     */
    const val ENDPOINT_LECTURE_ALL = "lecture/all"

    /**
     * Endpoint for retrieving all lectures scheduled for today.
     */
    const val ENDPOINT_LECTURE_ALL_TODAY = "lecture/today"

    /**
     * Endpoint for saving a new lecture.
     */
    const val ENDPOINT_LECTURE_SAVE = "lecture/save"

    /**
     * Endpoint for retrieving a specific lecture by ID.
     */
    const val ENDPOINT_LECTURE_FIND = "lecture/{id}"

    /**
     * Endpoint for deleting a specific lecture by ID.
     */
    const val ENDPOINT_LECTURE_DELETE = "lecture/{id}"

    /**
     * Endpoint for opening a lecture for attendance by ID.
     */
    const val ENDPOINT_LECTURE_OPEN_FOR_ATTENDANCE = "lecture/open-for-attendance/{id}"

    /**
     * Endpoint for closing a lecture for attendance by ID.
     */
    const val ENDPOINT_LECTURE_CLOSE_FOR_ATTENDANCE = "lecture/close-for-attendance/{id}"

    /**
     * Endpoint for retrieving all attendance records.
     */
    const val ENDPOINT_ATTENDANCE_ALL = "attendance/all"

    /**
     * Endpoint for saving attendance records.
     */
    const val ENDPOINT_ATTENDANCE_SAVE = "attendance/save"

    /**
     * Key used for QR code generation and scanning.
     */
    const val QR_KEY = "BYthSpKmqbDytmFU"
}