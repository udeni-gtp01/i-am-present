package lk.lnbti.iampresent.data

/**
 * A sealed class representing the result of an operation that can be either
 * a [Success], an [Error], or a [Loading] state.
 *
 * @param T The type of data encapsulated in the result in the case of [Success].
 */
sealed class Result<out T> {

    /**
     * Represents a successful result, containing the data of type [T].
     *
     * @property data The encapsulated data of type [T].
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents an error result, containing an error message.
     *
     * @property message A descriptive error message providing information about the failure.
     */
    data class Error(val message: String) : Result<Nothing>()

    /**
     * Represents a loading state, indicating that the operation is still in progress.
     */
    data object Loading : Result<Nothing>()
}